package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Проверка создания и удаления букинга")
@Feature("Создать один букинг, а после удалить и снова его получить")
public class CreateDeleteGetTest {

    private APIClient apiClient;
    private static int bookingId;
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    @Story("Создать букинг -> Удалить букинг -> Получить букинг ")
    @Description("Тест, который проверяет корректность удаления.")
    @Severity(SeverityLevel.NORMAL)
    public void CreateDelGetTest() throws JsonProcessingException {

        Response responseCreate = apiClient.createToken();
        String token = responseCreate.jsonPath().getString("token");

        Response responseCreatePerson = apiClient.createPerson();
        assertThat(responseCreatePerson.getStatusCode()).isEqualTo(200);

        bookingId= responseCreatePerson.jsonPath().getInt("bookingid");

        assertThat(bookingId).isGreaterThan(0);

        Response responseGetPerson = apiClient.getId(bookingId);
        assertEquals(200,responseGetPerson.getStatusCode(),"Букинг не получен");

        String responseBody = responseGetPerson.getBody().asString();
        Map<String, Object> bookingMap = objectMapper.readValue(responseBody,new TypeReference<Map<String, Object>>(){});
        assertThat(bookingMap).isNotEmpty();

        Response responseDelete = apiClient.delete(bookingId, token);
        assertThat(responseDelete.getStatusCode()).isEqualTo(201);
        Response responseGetDel = apiClient.getIdAfterDelete(bookingId);
        assertEquals(404,responseGetDel.getStatusCode(),"Букинг не удален");

        String responseAfterDeleteBody = responseGetDel.getBody().asString();
        assertThat(responseAfterDeleteBody).contains("Not Found");
    }
}
