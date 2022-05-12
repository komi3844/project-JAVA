package com.mmtr.finance.parser.service.impl;

import com.mmtr.finance.parser.exception.ExpiredRecoveryToken;
import com.mmtr.finance.parser.exception.NotFoundRecoveryToken;
import com.mmtr.finance.parser.exception.UserAlreadyExists;
import com.mmtr.finance.parser.exception.UserAlreadyExistsWithSpecifiedData;
import com.mmtr.finance.parser.message.MessageProvider;
import com.mmtr.finance.parser.model.dto.UserDto;
import com.mmtr.finance.parser.model.dto.requests.ChangePasswordRequest;
import com.mmtr.finance.parser.model.dto.requests.EmailRequest;
import com.mmtr.finance.parser.model.dto.requests.UserDataRequest;
import com.mmtr.finance.parser.model.dto.responses.MessageResponse;
import com.mmtr.finance.parser.model.dto.responses.PagingResponse;
import com.mmtr.finance.parser.model.dto.responses.UserResponse;
import com.mmtr.finance.parser.model.entity.RecoveryPassword;
import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.model.entity.UserPortfolioSummaries;
import com.mmtr.finance.parser.repository.UserRepository;
import com.mmtr.finance.parser.service.EmailService;
import com.mmtr.finance.parser.service.RecoveryPasswordService;
import com.mmtr.finance.parser.service.UserPortfolioSummariesService;
import com.mmtr.finance.parser.service.UserService;
import com.mmtr.finance.parser.utils.Role;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserPortfolioSummariesService userPortfolioSummariesService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RecoveryPasswordService recoveryPasswordService;
    private final EmailService emailService;
    private final MessageProvider messageProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserPortfolioSummariesService userPortfolioSummariesService,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           RecoveryPasswordService recoveryPasswordService,
                           EmailService emailService,
                           MessageProvider messageProvider) {
        this.userRepository = userRepository;
        this.userPortfolioSummariesService = userPortfolioSummariesService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.recoveryPasswordService = recoveryPasswordService;
        this.emailService = emailService;
        this.messageProvider = messageProvider;
    }

    /**
     * Searching for a user by user ID
     *
     * @param id
     * @return
     */
    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    /**
     * Search for a user by login
     *
     * @param login
     * @return
     */
    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }

    /**
     * Retrieving a specific user's data
     *
     * @param userId
     * @return
     */
    @Override
    public UserResponse getUser(long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserResponse userResponse = user.getUserResponse();
        userPortfolioSummariesService.findTopByUserOrderByIdDesc(userId)
                .ifPresent(userPortfolioSummaries -> userResponse
                        .setUserPortfolioSummaries(userPortfolioSummaries
                                .getPortfolioSummaryResponse()));
        return userResponse;
    }

    /**
     * Getting all users with pageable (command for administrator only)
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PagingResponse<UserResponse> getAllUsers(int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<User> userPage = findAllUsers(paging);
        List<UserResponse> userResponseList = userPage.getContent()
                .stream()
                .map(User::getUserResponse)
                .collect(Collectors.toList());

        return new PagingResponse<>(
                userResponseList,
                userPage.getNumber(),
                userPage.getTotalElements(),
                userPage.getTotalPages());
    }

    /**
     * Search for all users, excluding system administrators
     *
     * @param role
     * @return
     */
    @Override
    public List<User> findAllUsers(Role role) {
        return userRepository.findByRole(role);
    }

    /**
     * New user registration
     *
     * @param userDto
     * @return
     */
    @Override
    public MessageResponse createUser(UserDto userDto) {
        MessageResponse messageResponse = new MessageResponse("");
        try {
            boolean userAlreadyExists = !userRepository
                    .findByLoginIgnoreCaseOrEmailIgnoreCase(userDto.getLogin(),
                            userDto.getEmail())
                    .isEmpty();
            if (userAlreadyExists) throw new UserAlreadyExists();

            User user = userDto.fromUserDtoToUser();
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            User savedUser = userRepository.save(user);
            userPortfolioSummariesService
                    .save(new UserPortfolioSummaries(savedUser));

            log.info("Success user registration with email " + userDto.getEmail());
        } catch (UserAlreadyExists ex) {
            messageResponse.setErrorMessage(
                    messageProvider.getMessage("exception.userAlreadyExists"));
        } catch (Exception ex) {
            log.error("Failed user registration", ex);
            messageResponse.setErrorMessage(
                    messageProvider.getMessage("reply.unexpectedError"));
        }
        return messageResponse;
    }

    /**
     * Initializing the user password recovery procedure
     *
     * @param request
     * @return
     */
    @Override
    public MessageResponse recoveryPassword(EmailRequest request) {
        MessageResponse messageResponse = new MessageResponse(
                messageProvider.getMessage("reply.userNotFound"));

        try {
            userRepository
                    .findByEmailIgnoreCase(request.getEmail())
                    .ifPresent(user -> {
                        messageResponse.setErrorMessage("");
                        recoveryPasswordService.deletePrevRecoveryPasswords(user);

                        String recoveryToken = UUID.randomUUID().toString();
                        recoveryPasswordService.createRecoveryPassword(user, recoveryToken);
                        emailService.sendRecoveryPassword(user.getEmail(), recoveryToken);

                        log.info(String.format("User %d init recovery password",
                                user.getId()));
                    });
        } catch (Exception ex) {
            messageResponse.setErrorMessage(
                    messageProvider.getMessage("reply.unexpectedError"));
            log.error("Unexpected error with recovery password", ex);
        }

        return messageResponse;
    }

    /**
     * Changing the user's password after the password recovery procedure
     *
     * @param request
     * @return
     */
    @Override
    public MessageResponse changePassword(ChangePasswordRequest request) {
        MessageResponse messageResponse = new MessageResponse("");
        try {
            RecoveryPassword recoveryPassword = recoveryPasswordService
                    .findByTokenAndCheckDate(request.getToken());

            User user = recoveryPassword.getUser();
            user.setPassword(encryptPassword(request.getPassword()));
            userRepository.save(user);
            recoveryPasswordService.deleteRecoveryPassword(recoveryPassword);

            log.info(String.format("User %d successful change password",
                    user.getId()));
        } catch (NotFoundRecoveryToken ex) {
            messageResponse.setErrorMessage(
                    messageProvider.getMessage("exception.notFoundRecoveryToken"));
        } catch (ExpiredRecoveryToken ex) {
            messageResponse.setErrorMessage(
                    messageProvider.getMessage("exception.expiredRecoveryToken"));
        } catch (Exception ex) {
            messageResponse.setErrorMessage(
                    messageProvider.getMessage("reply.unexpectedError"));
            log.error("Unexpected error with change password", ex);
        }
        return messageResponse;
    }

    /**
     * Updating user data
     *
     * @param request
     * @param login
     * @return
     */
    @Override
    public MessageResponse updateUser(UserDataRequest request, String login) {
        MessageResponse messageResponse = new MessageResponse("");
        try {
            User user = userRepository
                    .findByLoginIgnoreCase(login)
                    .orElseThrow(Exception::new);
            String userDtoEmail = request.getEmail().toLowerCase();
            boolean changedEmail = !userDtoEmail.equals(user.getEmail().toLowerCase());

            if (changedEmail) {
                User userWithUpdatesData = userRepository
                        .findByEmailIgnoreCase(userDtoEmail)
                        .orElse(null);

                if (userWithUpdatesData != null && !user.equals(userWithUpdatesData)) {
                    throw new UserAlreadyExistsWithSpecifiedData();
                }
                user.setEmail(userDtoEmail);
            }
            String password = request.getPassword();
            if (password != null && !password.equals("")) {
                user.setPassword(encryptPassword(password));
            }
            userRepository.save(user);
        } catch (UserAlreadyExistsWithSpecifiedData ex) {
            messageResponse.setErrorMessage(
                    messageProvider.getMessage("exception.userAlreadyExistsWithTheSpecifiedData"));
        } catch (Exception ex) {
            messageResponse.setErrorMessage(
                    messageProvider.getMessage("reply.unexpectedError"));
            log.error("Unexpected error with updates user data", ex);
        }
        return messageResponse;
    }

    private String encryptPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     * Search for all users with pageable
     *
     * @param pageable
     * @return
     */
    private Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findByRoleOrderByIdDesc(pageable, Role.ROLE_USER);
    }
}
