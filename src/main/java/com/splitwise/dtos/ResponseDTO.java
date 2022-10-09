package com.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ResponseDTO {
    private Long requestId;
    private Status status;
    private String message;
}
