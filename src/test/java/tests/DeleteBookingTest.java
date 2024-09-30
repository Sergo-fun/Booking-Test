package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.clients.APIClient;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTest {
    private APIClient apiClient;

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

        Response srat = apiClient.delete(20, token);
        assertThat(srat.getStatusCode()).isEqualTo(200);




        // Выполняем DELETE запрос через APIClient
       // Response response = apiClient.delete(1);


        //assertThat(response.getStatusCode()).isEqualTo(201);



    }

}
