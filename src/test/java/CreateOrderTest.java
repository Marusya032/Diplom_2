import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    User user ;
    Order order;
    StellarBurgersClient stellarBurgersClient;
    String authorization;

    @Before
    public void setUp() {
        stellarBurgersClient = new StellarBurgersClient();
        order = order.generateOrder();
    }

    @After
    public void tearDown() {
        if (authorization != null) {
            stellarBurgersClient.deleteUser(authorization);
        }
    }
    
    @Test
    public void createOrderWithAuthorization(){
        user  =  user.getRandom();
        ValidatableResponse orderResponse = stellarBurgersClient.createNewUser(user);
        authorization = orderResponse.extract().body().path("accessToken");
        ValidatableResponse createOrder  = stellarBurgersClient.createOrderWithAuthorization(order, authorization);
        int statusCode = orderResponse.extract().statusCode();
        boolean errorStatus = orderResponse.extract().body().path("success");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_OK));
        assertThat("Неверный статус ответа", errorStatus, equalTo(true));
    }

    @Test
    public void createOrderWithoutAuthorization(){
        ValidatableResponse orderResponse  = stellarBurgersClient.createOrderWithoutAuthorization(order);
        int statusCode = orderResponse.extract().statusCode();
        boolean errorStatus = orderResponse.extract().body().path("success");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_OK));
        assertThat("Неверный статус ответа", errorStatus, equalTo(true));
    }

    @Test
    public void createOrderWithoutIngredients(){
        ValidatableResponse orderResponse  = stellarBurgersClient.createOrderWithoutIngredients(order);
        int statusCode = orderResponse.extract().statusCode();
        boolean errorStatus = orderResponse.extract().body().path("success");
        String message = orderResponse.extract().body().path("message");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Неверный статус ответа", errorStatus, equalTo(false));
        assertThat("Неверный текст сообщения", message, equalTo("Ingredient ids must be provided"));
    }


    @Test
    public void createOrderWithIncorrectHash(){
        order.setBun("111");
        ValidatableResponse orderResponse  = stellarBurgersClient.createOrderWithoutAuthorization(order);
        int statusCode = orderResponse.extract().statusCode();

        assertThat("Неверный код ответа", statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));
    }
}
