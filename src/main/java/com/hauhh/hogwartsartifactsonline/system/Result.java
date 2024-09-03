package com.hauhh.hogwartsartifactsonline.system;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Result<T> {

    private boolean flag; // Two values: true means success, false means not success

    private Integer code; // Status code. e.g., 200

    private String message; // Response message

    private T data; // The response payload

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result(boolean flag, Integer code, String message, T data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
