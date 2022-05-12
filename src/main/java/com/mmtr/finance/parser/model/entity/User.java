package com.mmtr.finance.parser.model.entity;

import com.mmtr.finance.parser.model.dto.responses.UserResponse;
import com.mmtr.finance.parser.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "login", nullable = false, length = 100)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "registration_date", nullable = false)
    private Date registrationDate;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Portfolio> userToCompanies;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<RecoveryPassword> recoveryPasswords;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserPortfolioSummaries> userPortfolioSummaries;

    public UserResponse getUserResponse() {
        return new UserResponse(id,
                email,
                login,
                registrationDate,
                role);
    }
}
