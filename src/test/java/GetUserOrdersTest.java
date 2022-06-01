import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetUserOrdersTest {
    User user ;
    StellarburgersClient stellarburgersClient;
    String authorization;

    @Before
    public void setUp() {
        stellarburgersClient = new StellarburgersClient();
    }

    @After
    public void tearDown() {
        if (authorization != null) {
            stellarburgersClient.deleteUser(authorization);
        }
    }

    @Test
    public void getOrdersAuthorizedUser(){
        user  = user.getRandom();
        ValidatableResponse createResponse = stellarburgersClient.createNewUser(user);

        authorization = createResponse.extract().body().path("accessToken");

        ValidatableResponse getOrdersResponse = stellarburgersClient.getOrdersAuthorizedUser(user, authorization);

        int statusCode = getOrdersResponse.extract().statusCode();

        assertThat("Неверный код ответа", statusCode, equalTo(SC_OK));

    }

    @Test
    public void getOrdersUnauthorizedUser(){
        ValidatableResponse getOrdersResponse = stellarburgersClient.getOrdersUnauthorizedUser();

        int statusCode = getOrdersResponse.extract().statusCode();
        String message = getOrdersResponse.extract().body().path("message");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Неверный текст сообщения", message, equalTo("You should be authorised"));
    }
}
