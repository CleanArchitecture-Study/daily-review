package dev.us00ng.week1_login.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String password;
    private String role;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String profileImage;

    private Boolean enabled;

    public void setEnabled(boolean value) {
        enabled = value;
    }
    public void setPassword(String password) {this.password = password;}
}