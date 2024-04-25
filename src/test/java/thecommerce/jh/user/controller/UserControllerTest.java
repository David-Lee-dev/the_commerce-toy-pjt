package thecommerce.jh.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.parser.JSONParser;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import thecommerce.jh.user.common.enums.ErrorCode;
import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.common.exception.CustomException;
import thecommerce.jh.user.dto.UserDto;
import thecommerce.jh.user.model.User;
import thecommerce.jh.user.service.UserService;

import java.sql.Array;
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

            when(userService.retrieveUsers(anyInt(), anyInt(), any(SortBy.class), anyBoolean())).thenReturn(Arrays.asList(firstUser, secondUser));

            mvc.perform(MockMvcRequestBuilders
                            .get("/api/user/list")
                            .param("page", "1")
                            .param("pageSize", "10")
                            .param("sort", "name"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(".[0].id").isNotEmpty())
                    .andExpect(jsonPath(".[0].name").isNotEmpty())
                    .andExpect(jsonPath(".[0].nickname").isNotEmpty())
                    .andExpect(jsonPath(".[0].phoneNumber").isNotEmpty())
                    .andExpect(jsonPath(".[0].email").isNotEmpty())
                    .andExpect(jsonPath(".[0].createdAt").isNotEmpty())
                    .andExpect(jsonPath(".[0].userId").isEmpty())
                    .andExpect(jsonPath(".[0].password").isEmpty());
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
}
