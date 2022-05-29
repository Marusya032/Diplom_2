import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    User user;
    StellarburgersClient stellarburgersClient;
    String autorization;
    String email;



    @Before
    public void setUp() {
        stellarburgersClient = new StellarburgersClient();
        user  = user.getRandom();
    }

    @After
    public void TearDown() {
        if (autorization != null) {


          stellarburgersClient.deleteUser(autorization);
    }}

    @Test
    @DisplayName("Test user can be created")
    public void userCanBeCreated(){

        ValidatableResponse createResponse = stellarburgersClient.createNewUser(user);

        int statusCode = createResponse.extract().statusCode();
        boolean errorStatus = createResponse.extract().body().path("success");
        autorization = createResponse.extract().body().path("accessToken");
        assertThat("Неверный код ответа", statusCode, equalTo(SC_OK));
        assertThat("Неверный статус ответа", errorStatus, equalTo(true));

    }

    @Test
    @DisplayName("Cannot create an existing user")
    public void cannotCreateAnExitingUser(){

        ValidatableResponse createResponse = stellarburgersClient.createNewUser(user);
        email = createResponse.extract().body().path("user.email");

        ValidatableResponse createResponse2 = stellarburgersClient.createNewUser(user);
        int statusCode = createResponse2.extract().statusCode();
        boolean errorStatus = createResponse2.extract().body().path("success");
        String message = createResponse2.extract().body().path("message");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Неверный статус ответа", errorStatus, equalTo(false));
        assertThat("Неверный текст сообщения", message, equalTo("User already exists"));


    }

    @Test
    @DisplayName("Create user without required field Email")
    public void createUserWithoutRequiredFieldEmail(){
        user.setEmail("");

        ValidatableResponse createResponse = stellarburgersClient.createNewUser(user);

        int statusCode = createResponse.extract().statusCode();
        boolean errorStatus = createResponse.extract().body().path("success");
        String message = createResponse.extract().body().path("message");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Неверный статус ответа", errorStatus, equalTo(false));
        assertThat("Неверный текст сообщения", message, equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Create user without required field Password")
    public void createUserWithoutRequiredFieldPassword(){
        user.setPassword("");

        ValidatableResponse createResponse = stellarburgersClient.createNewUser(user);

        int statusCode = createResponse.extract().statusCode();
        boolean errorStatus = createResponse.extract().body().path("success");
        String message = createResponse.extract().body().path("message");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Неверный статус ответа", errorStatus, equalTo(false));
        assertThat("Неверный текст сообщения", message, equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Create user without required field Name")
    public void createUserWithoutRequiredFieldName(){
        user.setName("");

        ValidatableResponse createResponse = stellarburgersClient.createNewUser(user);

        int statusCode = createResponse.extract().statusCode();
        boolean errorStatus = createResponse.extract().body().path("success");
        String message = createResponse.extract().body().path("message");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Неверный статус ответа", errorStatus, equalTo(false));
        assertThat("Неверный текст сообщения", message, equalTo("Email, password and name are required fields"));

    }

}
