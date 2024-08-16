package example.jwtstart.model.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
public record ApiResponse<T>(
    int code,
    String message,
    T data
) {
    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status) {
        return ResponseEntity.status(status)
            .body(ApiResponse.<T>builder()
                .code(status.value())
                .build()
            );
    }
    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status, T data) {
        return ResponseEntity.status(status)
            .body(ApiResponse.<T>builder()
                .code(status.value())
                .message(status.getReasonPhrase())
                .data(data)
                .build()
            );
    }

    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status)
            .body(ApiResponse.<T>builder()
                .code(status.value())
                .message(message)
                .data(data)
                .build()
            );
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return of(HttpStatus.OK, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
            .body(ApiResponse.<T>builder()
                .code(status.value())
                .message(message)
                .build()
            );
    }

}
