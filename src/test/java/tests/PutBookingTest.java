package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.clients.APIClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Проверка полного обновления")
@Feature("Выводится подробная информация с полностью обновленными полями")

public class PutBookingTest {
    private APIClient apiClient;
    @BeforeEach
    public void setup(){
        apiClient = new APIClient();
    }

    @Test
    @Story("Необходимо частично обновить букинг")
    @Description("Тест, который проверяет частичную обновленность полей пользователя")
    @Severity(SeverityLevel.NORMAL)
    public void updateTest() throws JsonProcessingException {

        Response responseCreate = apiClient.createToken();
        String token = responseCreate.jsonPath().getString("token");
        System.out.println(token);

        Response responseUpdate = apiClient.updatePerson(31, token);
        assertThat(responseUpdate.getStatusCode()).isEqualTo(200);
    }
}
