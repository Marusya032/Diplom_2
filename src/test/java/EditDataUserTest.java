import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.EventListener;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class EditDataUserTest {
    User user;
    UserCredentials userCredentials;
    StellarburgersClient stellarburgersClient;
    String authorization;
    String email;
    ValidatableResponse createResponse;

    @Before
    public void setUp() {
        stellarburgersClient = new StellarburgersClient();
        user  = user.getRandom();
        ValidatableResponse createResponse = stellarburgersClient.createNewUser(user);
        authorization = createResponse.extract().body().path("accessToken");
    }

    @After
    public void TearDown() {
        if (authorization != null) {


            stellarburgersClient.deleteUser(authorization);
        }}

    @Test
    @DisplayName("Edit field Name With Authorization")
    public void editFieldNameWithAuthorization(){
        user.setName(RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse editResponse = stellarburgersClient.editUserWithAuthorization(user, authorization);

        int statusCode = editResponse.extract().statusCode();
        boolean errorStatus = editResponse.extract().body().path("success");
        String newName = editResponse.extract().body().path("user.name");
      //  String message = editResponse.extract().body().path("message");
       // authorization = editResponse.extract().body().path("accessToken");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_OK));
        assertThat("Неверный статус ответа", errorStatus, equalTo(true));
      //  assertThat("Неверное новое имя пользователя", newName, equalTo((user.getName().toLowerCase())));
        assertThat("Неверное новое имя пользователя", newName, equalTo(user.getName()));

        // assertThat("Неверный текст сообщения", message, equalTo("email or password are incorrect"));

    }

    @Test
    @DisplayName("Edit field Email With Authorization")
    public void editFieldEmailWithAuthorization(){
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.ru");
        ValidatableResponse editResponse = stellarburgersClient.editUserWithAuthorization(user, authorization);

        int statusCode = editResponse.extract().statusCode();
        boolean errorStatus = editResponse.extract().body().path("success");
        String newEmail = editResponse.extract().body().path("user.email");
        //  String message = editResponse.extract().body().path("message");
        // authorization = editResponse.extract().body().path("accessToken");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_OK));
        assertThat("Неверный статус ответа", errorStatus, equalTo(true));
          assertThat("Неверное новое имя пользователя", newEmail, equalTo((user.getEmail().toLowerCase())));
       // assertThat("Неверное новое имя пользователя", newEmail, equalTo(user.getEmail()));

        // assertThat("Неверный текст сообщения", message, equalTo("email or password are incorrect"));

    }

    @Test
    @DisplayName("Edit field Password With Authorization")
    public void editFieldPasswordWithAuthorization(){
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse editResponse = stellarburgersClient.editUserWithAuthorization(user, authorization);

        int statusCode = editResponse.extract().statusCode();
        boolean errorStatus = editResponse.extract().body().path("success");
    //    String newPassword = editResponse.extract().body().path("user.password");
        //  String message = editResponse.extract().body().path("message");
        // authorization = editResponse.extract().body().path("accessToken");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_OK));
        assertThat("Неверный статус ответа", errorStatus, equalTo(true));
      //  assertThat("Неверное новое имя пользователя", newPassword, equalTo(user.getPassword()));
        // assertThat("Неверное новое имя пользователя", newEmail, equalTo(user.getEmail()));

        // assertThat("Неверный текст сообщения", message, equalTo("email or password are incorrect"));

    }

    @Test
    @DisplayName("Edit field Email an Exiting Email")
    public void editFieldEmailAnExitingEmail(){
        stellarburgersClient = new StellarburgersClient();
        user  = user.getRandom();
        ValidatableResponse createResponse = stellarburgersClient.createNewUser(user);
        String authorization = createResponse.extract().body().path("accessToken");


     //   String email = createResponse.extract().body().path("user.email");
    //    System.out.println(email);
        User user2   = user.getRandom();
        ValidatableResponse createResponse2 = stellarburgersClient.createNewUser(user2);
      //  String email2 = createResponse2.extract().body().path("user.email");

     //   System.out.println(email2);

        ValidatableResponse editResponse = stellarburgersClient.editUserWithAuthorization(user2, authorization);

        int statusCode = editResponse.extract().statusCode();
        boolean errorStatus = editResponse.extract().body().path("success");

        String message = editResponse.extract().body().path("message");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Неверный статус ответа", errorStatus, equalTo(false));


        assertThat("Неверный текст сообщения", message, equalTo("User with such email already exists"));

    }

    @Test
    @DisplayName("Edit field Name Without Authorization")
    public void editFieldNameWithoutAuthorization(){
        user.setName(RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse editResponse = stellarburgersClient.editUserWithoutAuthorization(user);

        int statusCode = editResponse.extract().statusCode();
        boolean errorStatus = editResponse.extract().body().path("success");
        //String newEmail = editResponse.extract().body().path("user.email");
          String message = editResponse.extract().body().path("message");
        // authorization = editResponse.extract().body().path("accessToken");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Неверный статус ответа", errorStatus, equalTo(false));
      //  assertThat("Неверное новое имя пользователя", newEmail, equalTo((user.getEmail().toLowerCase())));
        // assertThat("Неверное новое имя пользователя", newEmail, equalTo(user.getEmail()));

         assertThat("Неверный текст сообщения", message, equalTo("You should be authorised"));

    }

    @Test
    @DisplayName("Edit field Email Without Authorization")
    public void editFieldEmailWithoutAuthorization(){
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.ru");
        ValidatableResponse editResponse = stellarburgersClient.editUserWithoutAuthorization(user);

        int statusCode = editResponse.extract().statusCode();
        boolean errorStatus = editResponse.extract().body().path("success");
        //String newEmail = editResponse.extract().body().path("user.email");
        String message = editResponse.extract().body().path("message");
        // authorization = editResponse.extract().body().path("accessToken");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Неверный статус ответа", errorStatus, equalTo(false));
        //  assertThat("Неверное новое имя пользователя", newEmail, equalTo((user.getEmail().toLowerCase())));
        // assertThat("Неверное новое имя пользователя", newEmail, equalTo(user.getEmail()));

        assertThat("Неверный текст сообщения", message, equalTo("You should be authorised"));
    }


    @Test
    @DisplayName("Edit field Password Without Authorization")
    public void editFieldPasswordWithoutAuthorization(){
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse editResponse = stellarburgersClient.editUserWithoutAuthorization(user);

        int statusCode = editResponse.extract().statusCode();
        boolean errorStatus = editResponse.extract().body().path("success");
        //String newEmail = editResponse.extract().body().path("user.email");
        String message = editResponse.extract().body().path("message");
        // authorization = editResponse.extract().body().path("accessToken");

        assertThat("Неверный код ответа", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Неверный статус ответа", errorStatus, equalTo(false));
        //  assertThat("Неверное новое имя пользователя", newEmail, equalTo((user.getEmail().toLowerCase())));
        // assertThat("Неверное новое имя пользователя", newEmail, equalTo(user.getEmail()));

        assertThat("Неверный текст сообщения", message, equalTo("You should be authorised"));
    }


}
