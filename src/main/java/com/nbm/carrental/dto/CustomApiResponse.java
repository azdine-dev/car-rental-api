package com.nbm.carrental.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CustomApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public CustomApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }


}
