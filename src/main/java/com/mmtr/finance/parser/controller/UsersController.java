package com.mmtr.finance.parser.controller;

import com.mmtr.finance.parser.message.MessageProvider;
import com.mmtr.finance.parser.model.dto.UserDto;
import com.mmtr.finance.parser.model.dto.requests.ChangePasswordRequest;
import com.mmtr.finance.parser.model.dto.requests.EmailRequest;
import com.mmtr.finance.parser.model.dto.requests.SignInRequest;
import com.mmtr.finance.parser.model.dto.requests.UserDataRequest;
import com.mmtr.finance.parser.model.dto.responses.MessageResponse;
import com.mmtr.finance.parser.model.dto.responses.PagingResponse;
import com.mmtr.finance.parser.model.dto.responses.SignInResponse;
import com.mmtr.finance.parser.model.dto.responses.UserResponse;
import com.mmtr.finance.parser.security.model.JwtUser;
import com.mmtr.finance.parser.security.service.TokenProvider;
import com.mmtr.finance.parser.service.UserService;
import com.mmtr.finance.parser.utils.Role;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(description = "Операции с пользователями", name = "users-controller")
@Log4j2
public class UsersController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MessageProvider messageProvider;

    @Autowired
    public UsersController(UserService userService,
                           TokenProvider tokenProvider,
                           AuthenticationManager authenticationManager,
                           MessageProvider messageProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.messageProvider = messageProvider;
    }

    @ApiOperation(value = "Получение данных пользователя",
            authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение данных"),
            @ApiResponse(code = 401, message = "Необходимо авторизоваться"),
            @ApiResponse(code = 404, message = "Запрашиваемые данные не были обнаружены"),
            @ApiResponse(code = 403, message = "Доступ запрещен"),
            @ApiResponse(code = 500, message = "Непредвиденная ошибка в ходе обработки")}
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        try {
            return ResponseEntity.ok(userService.getUser(userId));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Получение всех пользователей системы (только для администратора)",
            authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение данных"),
            @ApiResponse(code = 401, message = "Необходимо авторизоваться"),
            @ApiResponse(code = 403, message = "Доступ запрещен (запрос только для администратора системы)"),
            @ApiResponse(code = 500, message = "Непредвиденная ошибка в ходе обработки")}
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<PagingResponse<UserResponse>> getAllUsers(@RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @ApiOperation(value = "Авторизация")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешная авторизация, если логин/пароль неверный - сообщение об ошибке")}
    )
    @RequestMapping(value = "/sign-in", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<SignInResponse> authenticate(@Valid @RequestBody SignInRequest request) throws AuthenticationException {

        String login = request.getLogin();
        String password = request.getPassword();

        SignInResponse signInResponse = new SignInResponse();
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(login, password);
            authenticationManager.authenticate(authToken);

            userService.findByLogin(login).ifPresent(user -> {
                Role role = user.getRole();

                signInResponse.setAccessToken(tokenProvider
                        .createToken(login, role));
                signInResponse.setId(user.getId());
                signInResponse.setErrorMessage("");
                signInResponse.setRole(role.toString());

                log.info("Success login user " + user.getId());
            });

        } catch (BadCredentialsException | UsernameNotFoundException exception) {
            signInResponse.setErrorMessage(messageProvider
                    .getMessage("reply.notCorrectDataForAuth"));
        } catch (Exception ex) {
            log.error("Failed user login", ex);
            signInResponse.setErrorMessage(messageProvider
                    .getMessage("reply.unexpectedError"));
        }
        return ResponseEntity.ok(signInResponse);
    }

    @ApiOperation(value = "Регистрация")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение операции, если любая из ошибок, " +
                    "в том числе и непредвиденная - сообщение об ошибке")}
    )
    @RequestMapping(value = "/sign-up", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<MessageResponse> createUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @ApiOperation(value = "Отправка письма с восстановлением пароля по почте")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение операции, если любая из ошибок, " +
                    "в том числе и непредвиденная - сообщение об ошибке")}
    )
    @RequestMapping(value = "/recovery-password", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<MessageResponse> recoveryPassword(@Valid @RequestBody EmailRequest request) {
        return ResponseEntity.ok(userService.recoveryPassword(request));
    }

    @ApiOperation(value = "Смена пароля (после перехода по ссылке с письма)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение операции, если любая из ошибок, " +
                    "в том числе и непредвиденная - сообщение об ошибке")}
    )
    @RequestMapping(value = "/change-password", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(request));
    }

    @ApiOperation(value = "Изменение данных пользователя",
            authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение операции, если любая из ошибок, " +
                    "в том числе и непредвиденная - сообщение об ошибке"),
            @ApiResponse(code = 401, message = "Необходимо авторизоваться"),
            @ApiResponse(code = 403, message = "Доступ запрещен")}
    )
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/change-user-data", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<MessageResponse> updateUserData(@Valid @RequestBody UserDataRequest request) {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(userService
                .updateUser(request,
                        ((JwtUser) principal).getUsername()));
    }
}
