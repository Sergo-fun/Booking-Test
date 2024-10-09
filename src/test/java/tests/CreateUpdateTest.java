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
    public void testEndToEnd4() throws JsonProcessingException {

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
    }
}
