package org.example.day09_totalproject.member.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String password;
    private Boolean enabled;
    private String role;
    private String profileImage;
    private String createdAt;
    private String updatedAt;

    public MemberSignupRes toMemberSignupRes(){
        return MemberSignupRes.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();
    }

    public void setEnabled() {
        this.enabled = true;
    }
}
