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
import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.common.exception.CustomException;
import thecommerce.jh.user.model.User;
import thecommerce.jh.user.service.UserService;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Nested
    class List {

        User firstUser = User.builder().id(1L).build();
        User secondUser = User.builder().id(2L).build();

        @Test
        @DisplayName("정상 요청")
        void idealRequest() throws Exception {

            when(userService.retrieveUsers(anyInt(), anyInt(), any(SortBy.class), anyBoolean()))
                    .thenReturn(Arrays.asList(firstUser, secondUser));

            mvc.perform(MockMvcRequestBuilders
                            .get("/api/user/list")
                            .param("page", "1")
                            .param("pageSize", "2")
                            .param("sort", "name"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(".page").value(1))
                    .andExpect(jsonPath(".total").value(2))
                    .andExpect(jsonPath(".users.[0].id").isNotEmpty())
                    .andExpect(jsonPath(".users.[0].name").isNotEmpty())
                    .andExpect(jsonPath(".users.[0].nickname").isNotEmpty())
                    .andExpect(jsonPath(".users.[0].phoneNumber").isNotEmpty())
                    .andExpect(jsonPath(".users.[0].email").isNotEmpty())
                    .andExpect(jsonPath(".users.[0].createdAt").isNotEmpty())
                    .andExpect(jsonPath(".users.[0].userId").isEmpty())
                    .andExpect(jsonPath(".users.[0].password").isEmpty());
        }

        @Test
        @DisplayName("page가 0 이하인 요청의 경우 Bad Request 오류 발생")
        void pageUnderZero() throws Exception {
            mvc.perform(MockMvcRequestBuilders
                            .get("/api/user/list")
                            .param("page", "0")
                            .param("pageSize", "10")
                            .param("sort", "name"))
                    .andExpect(status().isBadRequest());
        }


        @Test
        @DisplayName("pageSize가 0 이하인 요청의 경우 Bad Request 오류 발생")
        void pageSizeUnderZero() throws Exception {
            mvc.perform(MockMvcRequestBuilders
                            .get("/api/user/list")
                            .param("page", "1")
                            .param("pageSize", "0")
                            .param("sort", "name"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class modify {

        User updatedUser = User.builder().id(1L).userId("test").name("test").nickname("test").phoneNumber("test").email("test").build();

        @Test
        @DisplayName("정상 요청")
        void Modify() throws Exception {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("name", "test");

            when(userService.updateUser(any(User.class))).thenReturn(updatedUser);

            mvc.perform(MockMvcRequestBuilders.patch("/api/user/test")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.valueOf(new JSONObject(requestBody))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(".id").isNotEmpty())
                    .andExpect(jsonPath(".name").isNotEmpty())
                    .andExpect(jsonPath(".nickname").isNotEmpty())
                    .andExpect(jsonPath(".phoneNumber").isNotEmpty())
                    .andExpect(jsonPath(".email").isNotEmpty())
                    .andExpect(jsonPath(".createdAt").isNotEmpty())
                    .andExpect(jsonPath(".userId").isEmpty())
                    .andExpect(jsonPath(".password").isEmpty());
        }
    }
}
