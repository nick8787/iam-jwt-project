package spring.security.jwt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
    private String message;
    private Object data;
    private Date timestamp;
    private Boolean success;

    public ApiResponse(Boolean success, String message, Object data) {
        this.message = message;
        this.data = data;
        this.timestamp = new Date();
        this.success = success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    // Static factory method for creating a successful response
    public static ApiResponse createSuccessful(Object data) {
        return new ApiResponse(true, "Request processed successfully", data);
    }

    // Static factory method for creating a failure response
    public static ApiResponse createFailure(String errorMessage) {
        ApiResponse response = new ApiResponse(false, errorMessage, null);
        response.setTimestamp(new Date()); // Устанавливаем текущее время
        return response;
    }
}
