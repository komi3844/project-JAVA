package com.mmtr.finance.parser.model.dto.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserIdRequest {
    @NotNull
    private long userId;
}
