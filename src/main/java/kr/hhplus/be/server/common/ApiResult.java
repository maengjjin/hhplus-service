package kr.hhplus.be.server.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiResult<T> {

    private final int code;

    private final String message;

    private final T data;


    // 성공 응답 (데이터)
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "성공", data);
    }


    // 실패 응답
    public static <T> ApiResult<T> fail(int code, String message) {
        return new ApiResult<>(code, message, null);
    }
}
