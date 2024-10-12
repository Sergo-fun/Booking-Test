package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingDates;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateUpdateTest {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private int bookingId;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test // Создать букинг -> Обновить букинг
    public void CreatePutTest() throws JsonProcessingException {

        Response responseCreate = apiClient.createToken();
        String token = responseCreate.jsonPath().getString("token");
        assertNotNull(token, "Токен не должен быть null");

        Response responseCreatePerson = apiClient.createPerson();
        assertThat(responseCreatePerson.getStatusCode()).isEqualTo(200);

        bookingId = responseCreatePerson.jsonPath().getInt("bookingid");

        Response responseUpdate = apiClient.updatePerson(bookingId, token);
        Assertions.assertThat(responseUpdate.getStatusCode()).isEqualTo(200);
        assertThat(responseUpdate.getBody().asString()).doesNotContain("error", "Ответ не должен содержать ошибок");

        Response response = apiClient.getId(bookingId);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        Map<String, Object> bookingMap= objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>(){});
        Assertions.assertThat(bookingMap).isNotEmpty();

        assertThat(bookingMap.get("firstname")).isEqualTo("van");
        assertThat(bookingMap.get("lastname")).isEqualTo("vanov");
        assertThat(bookingMap.get("totalprice")).isEqualTo(2500);
        assertThat(bookingMap.get("additionalneeds")).isEqualTo("no");


        Object bookingDatesObject = bookingMap.get("bookingdates");
        // Приведение к Map и извлечение значений
        Map<String, String> bookingDates = (Map<String, String>) bookingDatesObject;

        // Проверка обновленных дат
        assertThat(bookingDates.get("checkin")).isEqualTo("2019-01-01");
        assertThat(bookingDates.get("checkout")).isEqualTo("2020-01-01");
    }
}
