import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {

	@BeforeAll
	static void setUp() {
		RestAssured.baseURI = "http://localhost:4004";
	}

	@Test
	public void shouldReturnOkWithValidToken() {
		//1. Arrange - set up anything this test need to work
		//2. act
		//3. assert

		String loginPayload = """
					{
								"email": "testuser@test.com",
								"password": "password123"
					}
					""";

		Response response = given()
			  .contentType("application/json")
			  .body(loginPayload)
			  .when() //act
			  .post("/api/v1/auths/login")
			  .then()
			  .statusCode(200)
			  .body("token", notNullValue()) //assert
			  .extract().response();

		System.out.println("-------token generated------- " + response.jsonPath().getString("token"));
	}
}
