package tests;

import core.clients.APIClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Epic("Работа с данными пользователя")
@Feature("Получает информацию о пользователях")
public class GetBookingIdTest  {

    private APIClient apiclient;

    @BeforeEach
    public void setup(){
        apiclient = new APIClient();
    }

    @Story("Получение информации списка пользователей")
    @Severity(SeverityLevel.MINOR)

    @Test
    public void TestId(){
        step("Получение Get");
        Response response = apiclient.getId(1);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }
}
