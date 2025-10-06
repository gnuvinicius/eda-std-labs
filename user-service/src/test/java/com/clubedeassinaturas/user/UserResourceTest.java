package com.clubedeassinaturas.user;

import com.clubedeassinaturas.resource.CreateUserRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class UserResourceTest {
    @Test
    void testCreateUserEndpoint() {
        var request = new CreateUserRequest("user", "email@domain.com");

        given()
          .when().body(request)
             .contentType("application/json")
             .post("/users")
          .then()
             .statusCode(201);
    }

}