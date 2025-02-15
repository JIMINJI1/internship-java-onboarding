package demo.onboarding.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private int status;
    private String message;

    public ErrorResponseDto(int status, String errorMessage) {
        this.status = status;
        this.message = errorMessage;
    }
}
