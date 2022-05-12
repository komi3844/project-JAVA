package com.mmtr.finance.parser.repository;

import com.mmtr.finance.parser.model.entity.RecoveryPassword;
import com.mmtr.finance.parser.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecoveryPasswordRepository extends JpaRepository<RecoveryPassword, Long> {
    Optional<RecoveryPassword> findByToken(String token);

    List<RecoveryPassword> findByUser(User user);
}
