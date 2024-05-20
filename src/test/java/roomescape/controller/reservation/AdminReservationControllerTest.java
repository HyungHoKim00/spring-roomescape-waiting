package roomescape.controller.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.controller.member.dto.MemberLoginRequest;
import roomescape.controller.reservation.dto.AdminCreateReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/fixture.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class AdminReservationControllerTest {

    @LocalServerPort
    int port;

    String accessToken;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;

        accessToken = RestAssured
                .given().log().all()
                .body(new MemberLoginRequest("redddy@gmail.com", "0000"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().cookies().extract().cookie("token");
    }

    @Test
    @DisplayName("ADMIN이 관리자 메인 페이지로 이동")
    void moveToAdminMainPage() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("ADMIN이 예약 관리 페이지로 이동")
    void moveToAdminReservationPage() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("ADMIN이 시간 관리 페이지로 이동")
    void moveToAdminTimePage() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("ADMIN이 테마 관리 페이지로 이동")
    void moveToAdminThemePage() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("ADMIN이 예약 생성")
    void createReservationAdmin() {
        final AdminCreateReservationRequest request = new AdminCreateReservationRequest(1L,
                1L, LocalDate.now().plusDays(1), 1L);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }
}
