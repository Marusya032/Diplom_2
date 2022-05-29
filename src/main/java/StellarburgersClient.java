import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class StellarburgersClient extends StellarburgersRestClient{

    private static final String CREATE_USER_PATH = "api/auth/register";
    private static final String ORDER_PATH = "api/orders";
    private static final String INGREDIENTS_LIST = "api/ingredients";
    private static final String LOGIN_USER_PATH = "api/auth/login/";
    private static final String EDIT_USER_PATH = "api/auth/user/";


    @Step("Create new user")
    public ValidatableResponse createNewUser(User user) {

        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(CREATE_USER_PATH)
                .then();
    }


    @Step("Delete user")
    public ValidatableResponse deleteUser(String authorization){

        return given()
                .spec(getBaseSpec())
                .header("Authorization", authorization)
                .when()
                .delete(EDIT_USER_PATH)
                .then();
    }

    }




