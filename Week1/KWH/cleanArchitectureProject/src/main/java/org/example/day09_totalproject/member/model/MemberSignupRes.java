package org.example.day09_totalproject.member.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class MemberSignupRes {
    private Long id;
    private String name;
    private String email;

}
