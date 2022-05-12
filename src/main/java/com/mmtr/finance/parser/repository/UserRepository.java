package com.mmtr.finance.parser.repository;

import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.utils.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);

    List<User> findByLoginIgnoreCaseOrEmailIgnoreCase(String login, String email);

    Optional<User> findByLoginIgnoreCase(String login);

    Optional<User> findByEmailIgnoreCase(String email);

    Page<User> findByRoleOrderByIdDesc(Pageable pageable, Role role);

    List<User> findByRole(Role role);
}
