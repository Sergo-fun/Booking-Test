package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateDeleteTest {
    private APIClient apiClient;
    private static int bookingId;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test   // Создать букинг -> Удалить букинг
    public void testEndToEnd2() throws JsonProcessingException {

        Response responseCreate = apiClient.createToken();
        String token = responseCreate.jsonPath().getString("token");
        assertNotNull(token, "Токен не должен быть null");

        Response responseCreatePerson = apiClient.createPerson();
        assertEquals(200, responseCreatePerson.getStatusCode(), "Ошибка при создании букинга");

        bookingId = responseCreatePerson.jsonPath().getInt("bookingid");
        assertThat(bookingId).isGreaterThan(0);

        Response getBookingCreate = apiClient.getId(bookingId);
        assertEquals(200, getBookingCreate.getStatusCode(), "Ошибка при получении букинга");

        String responseBody = getBookingCreate.getBody().asString();
        Map<String, Object> bookingMap= objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>(){});
        Assertions.assertThat(bookingMap).isNotEmpty();

        // Удаление бронирования
        Response responseDelete = apiClient.delete(bookingId, token);
        assertEquals(201, responseDelete.getStatusCode(), "Ошибка при удалении бронирования");
    }
}
