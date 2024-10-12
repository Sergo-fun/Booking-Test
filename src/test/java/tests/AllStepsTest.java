package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllStepsTest {
    private APIClient apiClient;
    private static int bookingId;
    private static String token;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        apiClient = new APIClient();
        Response responseCreate = apiClient.createToken();
        token = responseCreate.jsonPath().getString("token");
        objectMapper = new ObjectMapper();
    }

    @Test // Создать букинг -> Частично обновить -> Полностью обновить -> Удалить
    public void createPartUpdateFullUpdateDeleteTest() throws JsonProcessingException {
        // создание букинга
        Response responseCreatePerson = apiClient.createPerson();
        assertEquals(200, responseCreatePerson.getStatusCode(), "Ошибка при создании");

        bookingId = responseCreatePerson.jsonPath().getInt("bookingid");

        assertThat(bookingId).isGreaterThan(0);

        // Проверка получения букинга
        Response getBookingResponse = apiClient.getId(bookingId);
        assertEquals(200, getBookingResponse.getStatusCode(), "Ошибка при получении созданного букинга");

        // Десериализуем тело ответа в список объектов bookingMap, чтобы проверить верный набор полей
        String responseBody = getBookingResponse.getBody().asString();
        Map<String, Object> bookingMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
        // Проверяем, что тело ответа содержит объекты bookingMap
        assertThat(bookingMap).isNotEmpty();

        // Частичное обновление букинга
        Response responseUpdate = apiClient.partUpdatePerson(bookingId, token);
        assertEquals(200, responseUpdate.getStatusCode(), "Ошибка при частичном обновлении");

        // Проверка букинга с частичным обновлением
        Response getBookingPartUpdate = apiClient.getId(bookingId);
        assertEquals(200, getBookingPartUpdate.getStatusCode(), "Ошибка при получении букинга с частичным обновлением");

        // Десериализуем тело ответа в список объектов updatedBookingMap, чтобы проверить верный набор полей
        String updatedResponseBody = getBookingPartUpdate.getBody().asString();
        Map<String, Object> updatedBookingMap = objectMapper.readValue(updatedResponseBody, new TypeReference<Map<String, Object>>() {});
        assertThat(updatedBookingMap).isNotEmpty();

        // Полное обновление букинга
        Response responseGetUpdate = apiClient.updatePerson(bookingId, token);
        assertEquals(200, responseGetUpdate.getStatusCode(), "Ошибка при обновлении букинга");

        // Проверка на получение букинга с полным обновлением
        Response getBookingUpdate = apiClient.getId(bookingId);
        assertEquals(200, getBookingUpdate.getStatusCode(), "Ошибка при получении букинга с полным обновлением");

        // Десериализуем тело ответа в список объектов fullUpdatedBookingMap, чтобы проверить верный набор полей
        String fullUpdatedResponseBody = getBookingUpdate.getBody().asString();
        Map<String, Object> fullUpdatedBookingMap = objectMapper.readValue(fullUpdatedResponseBody, new TypeReference<Map<String, Object>>() {});
        assertThat(fullUpdatedBookingMap).isNotEmpty();

        // Удаление букинга
        Response responseDelete = apiClient.delete(bookingId, token);
        assertEquals(201, responseDelete.getStatusCode(), "Ошибка при удалении");

        // Проверка на удаление букинга
        Response getDeletedBookingResponse = apiClient.getIdAfterDelete(bookingId);
        assertEquals(404, getDeletedBookingResponse.getStatusCode(), "Ошибка: бронирование должно быть удалено и не существовать");
    }
}
