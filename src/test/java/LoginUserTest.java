import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    User user;
 //   UserCredentials userCredentials;
    StellarBurgersClient stellarBurgersClient;
    String authorization;
 //   String email;


    @Before
    public void setUp() {
        stellarBurgersClient = new StellarBurgersClient();
        user  = user.getRandom();
        stellarBurgersClient.createNewUser(user);
    }

    @After
    public void TearDown() {
        if (authorization != null) {
            stellarBurgersClient.deleteUser(authorization);
        }}

    @Test
    @DisplayName("Test user can be login")
    public void userCanBeLogin(){
     //   ValidatableResponse loginResponse = stellarBurgersClient.loginUser(new UserCredentials(user.getEmail(), user.getPassword()));
        ValidatableResponse loginResponse = stellarBurgersClient.loginUser(user);
        int statusCode = loginResponse.extract().statusCode();
        boolean errorStatus = loginResponse.extract().body().path("success");
        authorization = loginResponse.extract().body().path("accessToken");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_OK));
        assertThat("Неверный статус ответа", errorStatus, equalTo(true));
    }

    @Test
    @DisplayName("Login with incorrect email")
    public void loginWithIncorrectEmail(){
    //    ValidatableResponse loginResponse = stellarBurgersClient.loginUser(new UserCredentials("test_qajava5@mail.ru", user.getPassword()));
        user.setEmail("test_qajava5@mail.ru");
        ValidatableResponse loginResponse = stellarBurgersClient.loginUser(user);
        int statusCode = loginResponse.extract().statusCode();
        boolean errorStatus = loginResponse.extract().body().path("success");
        String message = loginResponse.extract().body().path("message");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Неверный статус ответа", errorStatus, equalTo(false));
        assertThat("Неверный текст сообщения", message, equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login with incorrect password")
    public void loginWithIncorrectPassword(){
      //  ValidatableResponse loginResponse = stellarBurgersClient.loginUser(new UserCredentials(user.getEmail(), "555555"));
        user.setPassword("555555");
        ValidatableResponse loginResponse = stellarBurgersClient.loginUser(user);
        int statusCode = loginResponse.extract().statusCode();
        boolean errorStatus = loginResponse.extract().body().path("success");
        String message = loginResponse.extract().body().path("message");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Неверный статус ответа", errorStatus, equalTo(false));
        assertThat("Неверный текст сообщения", message, equalTo("email or password are incorrect"));
    }
}
