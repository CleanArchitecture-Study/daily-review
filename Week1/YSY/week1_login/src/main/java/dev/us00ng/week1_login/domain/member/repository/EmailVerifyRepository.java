package dev.us00ng.week1_login.domain.member.repository;


import dev.us00ng.week1_login.domain.member.entity.EmailVerify;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerifyRepository extends JpaRepository<EmailVerify, Long> {
    Optional<EmailVerify> findByEmail(String email);
}