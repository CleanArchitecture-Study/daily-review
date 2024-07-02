package org.example.day09_totalproject.member.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostLoginReq {

    private String email;
    private String password;


}
