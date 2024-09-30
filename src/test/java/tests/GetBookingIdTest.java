package tests;

import core.clients.APIClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class GetBookingIdTest  {

    private APIClient apiclient;

    @BeforeEach
    public void setup(){
        apiclient = new APIClient();
    }

    @Test
    public void TestId(){
        Response response = apiclient.getId(1);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }



}
