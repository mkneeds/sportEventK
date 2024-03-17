package bsuir.kraevskij.sportevent.model;

public class AuthenticationResponse {
    private String token;
    private String message;
    private final int errorCode;
    public AuthenticationResponse(String token, String message,Integer errorCode) {
        this.token = token;
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
