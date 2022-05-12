package com.mmtr.finance.parser.service.impl;

import com.mmtr.finance.parser.exception.EmptyFile;
import com.mmtr.finance.parser.model.entity.Portfolio;
import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.service.CompanyService;
import com.mmtr.finance.parser.service.ExcelService;
import com.mmtr.finance.parser.service.FileService;
import com.mmtr.finance.parser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private final ExcelService excelService;
    private final UserService userService;
    private final CompanyService companyService;

    @Autowired
    public FileServiceImpl(ExcelService excelService,
                           UserService userService,
                           CompanyService companyService) {
        this.excelService = excelService;
        this.userService = userService;
        this.companyService = companyService;
    }

    /**
     * Obtaining an Excel download on the user's portfolio, taking into account all the company's indicators, current prices
     *
     * @param login
     * @return
     * @throws IOException
     * @throws EmptyFile
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Override
    public ResponseEntity getFileUserCompaniesIndicator(String login) throws IOException,
            EmptyFile,
            NoSuchFieldException,
            IllegalAccessException {
        User user = userService.findByLogin(login).orElse(null);
        if (user == null) throw new EmptyFile();

        List<Portfolio> userToCompanies = companyService.findByUser(user);
        if (userToCompanies.isEmpty()) throw new EmptyFile();

        File file = excelService.getExcelFileUserCompaniesIndicator(userToCompanies);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Portfolio.xls");
        return new ResponseEntity<>(Files.readAllBytes(file.toPath()),
                header, HttpStatus.CREATED);
    }
}
