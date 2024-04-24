package thecommerce.jh.user.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import thecommerce.jh.user.common.enums.ErrorCode;
import thecommerce.jh.user.common.exception.CustomException;
import thecommerce.jh.user.model.User;
import thecommerce.jh.user.service.UserService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @Nested
    class Join {

        @Test
        @DisplayName("정상 요청")
        void idealRequest() throws Exception {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("password", "test");
            requestBody.put("passwordConfirm", "test");

            when(userService.createUser(any(User.class))).thenReturn(null);

            mvc.perform(MockMvcRequestBuilders
                            .post("/api/user/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.valueOf(new JSONObject(requestBody))))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않는 경우 Bad Request 응답")
        void unmatchedPassword() throws Exception {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("password", "test");
            requestBody.put("passwordConfirm", "unmatched password");

            mvc.perform(MockMvcRequestBuilders
                            .post("/api/user/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.valueOf(new JSONObject(requestBody))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("userId 중복일 경우 Bad Request 응답")
        void duplicatedUserId() throws Exception {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("password", "test");
            requestBody.put("passwordConfirm", "test");

            when(userService.createUser(any(User.class))).thenThrow(new CustomException(ErrorCode.DUPLICATED_USER_ID));

            mvc.perform(MockMvcRequestBuilders
                            .post("/api/user/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.valueOf(new JSONObject(requestBody))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("nickname 중복일 경우 Bad Request 응답")
        void duplicatedNickname() throws Exception {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("password", "test");
            requestBody.put("passwordConfirm", "test");

            when(userService.createUser(any(User.class))).thenThrow(new CustomException(ErrorCode.DUPLICATED_NICKNAME));

            mvc.perform(MockMvcRequestBuilders
                            .post("/api/user/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.valueOf(new JSONObject(requestBody))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("phone number 중복일 경우 Bad Request 응답")
        void duplicatedPhoneNumber() throws Exception {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("password", "test");
            requestBody.put("passwordConfirm", "test");

            when(userService.createUser(any(User.class))).thenThrow(new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER));

            mvc.perform(MockMvcRequestBuilders
                            .post("/api/user/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.valueOf(new JSONObject(requestBody))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("email 중복일 경우 Bad Request 응답")
        void duplicatedEmail() throws Exception {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("password", "test");
            requestBody.put("passwordConfirm", "test");

            when(userService.createUser(any(User.class))).thenThrow(new CustomException(ErrorCode.DUPLICATED_EMAIL));

            mvc.perform(MockMvcRequestBuilders
                            .post("/api/user/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.valueOf(new JSONObject(requestBody))))
                    .andExpect(status().isBadRequest());
        }
    }
}
