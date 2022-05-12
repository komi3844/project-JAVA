package com.mmtr.finance.parser.controller;

import com.mmtr.finance.parser.exception.EmptyFile;
import com.mmtr.finance.parser.message.MessageProvider;
import com.mmtr.finance.parser.model.dto.responses.MessageResponse;
import com.mmtr.finance.parser.security.model.JwtUser;
import com.mmtr.finance.parser.service.FileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/files")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(description = "Действия с файлами", name = "files-controller")
@Log4j2
public class FilesController {

    private final FileService fileService;
    private final MessageProvider messageProvider;

    @Autowired
    public FilesController(FileService fileService,
                           MessageProvider messageProvider) {
        this.fileService = fileService;
        this.messageProvider = messageProvider;
    }

    @ApiOperation(value = "Получение excel файла по портфелю пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение операции, если любая из ошибок, " +
                    "в том числе и непредвиденная - сообщение об ошибке"),
            @ApiResponse(code = 401, message = "Необходимо авторизоваться"),
            @ApiResponse(code = 403, message = "Доступ запрещен (запрос только для пользователей системы)")}
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/excel-companies-indicator", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity getFileUserCompaniesIndicator() {
        try {
            Object principal = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();

            return fileService.getFileUserCompaniesIndicator(
                    ((JwtUser) principal).getUsername());
        } catch (EmptyFile ex) {
            return ResponseEntity.ok(
                    new MessageResponse(
                            messageProvider.getMessage("exception.emptyFile")));
        } catch (Exception ex) {
            log.error("Unexpected error with download excel table", ex);
            return ResponseEntity.ok(
                    new MessageResponse(
                            messageProvider.getMessage("reply.unexpectedError")));
        }
    }
}

