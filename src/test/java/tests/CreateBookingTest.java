package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingCreate;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Epic("Проверка создания букинга")
@Feature("Создать одного человека")
public class CreateBookingTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;

    // Инициализация API клиента перед каждым тестом
    @BeforeEach
    public void setup(){
        apiClient = new APIClient();
         objectMapper = new ObjectMapper();
    }

    @Test
    @Story("Создать person ")
    @Description("Тест, который создаёт букинг.")
    @Severity(SeverityLevel.NORMAL)
    public void TestCreation() throws JsonProcessingException {

        Response response = apiClient.createPerson();
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        BookingCreate bookingCreateResponse = objectMapper.readValue(responseBody, BookingCreate.class);
    }
}



