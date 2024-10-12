package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.clients.APIClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTest {
    private APIClient apiClient;
    private int bookingId;

    // Инициализация API клиента перед каждым тестом
    @BeforeEach
    public void setup(){
        apiClient = new APIClient();
    }
    @Test
    public void testDeleteBooking() throws JsonProcessingException {

        Response response = apiClient.createToken();
        String token = response.jsonPath().getString("token");
        System.out.println(token);

        Response createResponse = apiClient.createPerson();
        Assertions.assertEquals(200, createResponse.getStatusCode(), "Ошибка при создании бронирования");
        createResponse.then().log().all();

        bookingId = createResponse.jsonPath().getInt("bookingid");

        Response responseDel = apiClient.delete(bookingId, token);
        assertThat(responseDel.getStatusCode()).isEqualTo(201);
    }
}
