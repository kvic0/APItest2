package POJO;

import lombok.Data;
import lombok.Builder;
import lombok.Data;

public class LombokUserData {

    @Data
    @Builder
    public static class UserRequest {
        private String userName;
        private String password;
    }
}
