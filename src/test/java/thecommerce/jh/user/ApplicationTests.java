package thecommerce.jh.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("통합 테스트")
class ApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void beforeAll() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/data/user.test.sql"));
        }
    }

    @Nested
    @DisplayName("POST /api/user/join")
    class signup {
        HttpHeaders headers;
        JSONObject body;

        @BeforeEach
        void beforeEach() {
            headers = new HttpHeaders();
            body = new JSONObject();
        }

        @Test
        @DisplayName("정상 요청에 대해 201 응답")
        void idealRequest () throws JSONException {
            String url = "/api/user/join";
            headers.setContentType(MediaType.APPLICATION_JSON);
            body.put("userId", "test");
            body.put("password", "test");
            body.put("passwordConfirm", "test");
            body.put("name", "test");
            HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

            ResponseEntity responseEntity = restTemplate.postForEntity(url, request, String.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }

        @Test
        @DisplayName("일치하지 않는 패스워드에 대해 400 응답")
        void unmatchedPassword () throws JSONException {
            String url = "/api/user/join";
            headers.setContentType(MediaType.APPLICATION_JSON);
            body.put("userId", "test");
            body.put("password", "test");
            body.put("passwordConfirm", "wrong password");
            body.put("name", "name");
            HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

            ResponseEntity responseEntity = restTemplate.postForEntity(url, request, String.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("userId 누락에 대해 400 응답")
        void missingUserId() throws JSONException {
            String url = "/api/user/join";
            headers.setContentType(MediaType.APPLICATION_JSON);
            body.put("password", "test");
            body.put("passwordConfirm", "test");
            body.put("name", "test");
            HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

            ResponseEntity responseEntity = restTemplate.postForEntity(url, request, String.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("name 누락에 대해 400 응답")
        void missingName () throws JSONException {
            String url = "/api/user/join";
            headers.setContentType(MediaType.APPLICATION_JSON);
            body.put("userId", "test");
            body.put("password", "test");
            body.put("passwordConfirm", "test");
            HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

            ResponseEntity responseEntity = restTemplate.postForEntity(url, request, String.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("GET /api/user/list")
    class retrieveUsers {

        @Test
        @DisplayName("정상 요청에 대해 page, total, users 데이터와 함께 200 응답")
        void idealRequest() {
            String page = "1";
            String pageSize = "5";
            String url = String.format("/api/user/list?page=%s&pageSize=%s", page, pageSize);

            ResponseEntity responseEntity = restTemplate.getForEntity(url, String.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(responseEntity.getBody()).isNotNull();
        }

        @Test
        @DisplayName("잘못된 page 값에 대해 400 응답")
        void invalidPage() {
            String page = "0";
            String pageSize = "5";
            String url = String.format("/api/user/list?page=%s&pageSize=%s", page, pageSize);

            ResponseEntity responseEntity = restTemplate.getForEntity(url, String.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }


        @Test
        @DisplayName("잘못된 pageSize 값에 대해 400 응답")
        void invalidPageSize() {
            String page = "1";
            String pageSize = "0";
            String url = String.format("/api/user/list?page=%s&pageSize=%s", page, pageSize);

            ResponseEntity responseEntity = restTemplate.getForEntity(url, String.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("PATCH /api/user/{userId}")
    class updateUserData{

        HttpHeaders headers;
        JSONObject body;

        @BeforeEach
        void beforeEach() {
            headers = new HttpHeaders();
            body = new JSONObject();
        }

        @Test
        @DisplayName("정상 요청에 대해 변경된 데이터와 200 응답")
        void idealRequest() throws JSONException {
            String url = "/api/user/userForUpdateTest";
            headers.setContentType(MediaType.APPLICATION_JSON);
            body.put("name", "update");
            body.put("nickname", "update");
            body.put("phoneNumber", "update");
            body.put("email", "update");
            HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

            ResponseEntity responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(responseEntity.getBody()).isNotNull();
        }

        @Test
        @DisplayName("존재하지 않는 유저에 대해 404 응답")
        void nonexistedUser() throws JSONException {
            String url = "/api/user/non-existed-user";
            headers.setContentType(MediaType.APPLICATION_JSON);
            body.put("name", "update");
            body.put("nickname", "update");
            body.put("phoneNumber", "update");
            body.put("email", "update");
            HttpEntity<String> request = new HttpEntity<String>(body.toString(), headers);

            ResponseEntity responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
