package com.mmtr.finance.parser.service;

import com.mmtr.finance.parser.exception.EmptyFile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface FileService {
    ResponseEntity getFileUserCompaniesIndicator(String login) throws IOException,
            EmptyFile,
            NoSuchFieldException,
            IllegalAccessException;
}
