package com.linkedin.backend.exception;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class WebResponse<T> {
    private boolean success;
    private T data;
    private String message;
}
