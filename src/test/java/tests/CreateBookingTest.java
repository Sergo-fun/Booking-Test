package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    public void TestCreation() throws JsonProcessingException {

        Response response = apiClient.createPerson();
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        Booking bookingResponse = objectMapper.readValue(responseBody, Booking.class);
    }
}



