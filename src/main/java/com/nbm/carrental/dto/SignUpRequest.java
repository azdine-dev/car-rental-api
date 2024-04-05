package com.nbm.carrental.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {
    String firstName;
    String lastName;
    String email;
    String password;
}
