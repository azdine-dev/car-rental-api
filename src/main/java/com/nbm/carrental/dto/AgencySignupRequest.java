package com.nbm.carrental.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencySignupRequest {
    String email;
    String password;
}
