package com.mmtr.finance.parser.service;

import com.mmtr.finance.parser.model.dto.UserDto;
import com.mmtr.finance.parser.model.dto.requests.ChangePasswordRequest;
import com.mmtr.finance.parser.model.dto.requests.EmailRequest;
import com.mmtr.finance.parser.model.dto.requests.UserDataRequest;
import com.mmtr.finance.parser.model.dto.responses.MessageResponse;
import com.mmtr.finance.parser.model.dto.responses.PagingResponse;
import com.mmtr.finance.parser.model.dto.responses.UserResponse;
import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.utils.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(long id);

    Optional<User> findByLogin(String login);

    UserResponse getUser(long userId);

    PagingResponse<UserResponse> getAllUsers(int page, int size);

    List<User> findAllUsers(Role role);

    MessageResponse createUser(UserDto userDto);

    MessageResponse recoveryPassword(EmailRequest request);

    MessageResponse changePassword(ChangePasswordRequest request);

    MessageResponse updateUser(UserDataRequest request, String login);
}
