package core.clients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.models.Booking;
import core.models.BookingDates;
import core.models.PartBooking;
import core.models.User;
import core.settings.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class APIClient {

    private final String baseUrl;

    public APIClient() {
        this.baseUrl = determineBaseUrl();
    }

    // Определение базового URL на основе файла конфигурации
    private String determineBaseUrl() {
        String environment = System.getProperty("env", "test");
        String configFileName = "application-" + environment + ".properties";//переменная с именем файла

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalStateException("Configuration file not found: " + configFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load cofiguration file: " + configFileName, e);
        }

        return properties.getProperty("baseUrl");
    }

    //Настройка базовых HTTP запросов
    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .header("Content-type", "application/json")
                .header("Accept", "application/json")
                .log().all(); // Логирование всех запросов
    }

    //Get запрос на эндпоинт ping
    public Response ping() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.PING.getPath()) // используем Enum для эндпоинта/ping
                .then()
                .statusCode(201) //ожидаемый статус код
                .extract()
                .response();
    }

    //Get запрос на эндпоинт getBooking
    public Response getBooking() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOkING.getPath()) // используем Enum для эндпоинта/ping
                .then()
                .log()
                .all()
                .statusCode(200) //ожидаемый статус код
                .extract()
                .response();
    }


    public Response delete(int bookingid, String token) {
        return getRequestSpec()
                .header("Cookie", "token=" + token)
                .when()
                .log()
                .all()
                .pathParam("id", bookingid)
                .delete(ApiEndpoints.BOOkING.getPath() + "/{id}")
                .then()
                .log()
                .all()
                .statusCode(201)
                .extract()
                .response();
    }

    public Response getId(int bookingid) {
        return getRequestSpec()
                .when()
                .pathParam("id", bookingid)
                .get(ApiEndpoints.BOOkING.getPath() + "/{id}")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .response();
    }

    public Response createToken() throws JsonProcessingException {
        User user = new User("admin", "password123");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(user);

        return getRequestSpec()
                .body(requestBody)
                .when()
                .post(ApiEndpoints.AUTH.getPath())
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .response();
    }

    public Response createPerson() throws JsonProcessingException {
        BookingDates bookingdates = new BookingDates("2018-01-01", "2019-01-01");
        Booking booking = new Booking("John", "Vinokurov", 123, true, bookingdates, "nope");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(booking);

        return getRequestSpec()
                .body(requestBody)
                .when()
                .post(ApiEndpoints.BOOkING.getPath())
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .response();
    }

    public Response updatePerson(int bookingid, String token) throws JsonProcessingException {
        BookingDates bookingdates = new BookingDates("2019-01-01", "2020-01-01");
        Booking booking = new Booking(bookingid,"van", "vanov", 2500, true,  bookingdates, "no");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(booking);

        return getRequestSpec()
                .body(requestBody)
                .header("Cookie", "token=" + token)
                .log()
                .all()
                .when()
                .pathParam("id", bookingid)
                .put(ApiEndpoints.BOOkING.getPath() + "/{id}")
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    public Response partUpdatePerson(int bookingid, String token) throws JsonProcessingException {
        PartBooking partbooking = new PartBooking("Sergey");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(partbooking);

        return getRequestSpec()
                .body(requestBody)
                .header("Cookie", "token=" + token)
                .when()
                .pathParam("id", bookingid)
                .patch(ApiEndpoints.BOOkING.getPath() + "/{id}")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .response();
    }

    public Response getIdAfterDelete(int bookingid) {
        return getRequestSpec()
                .when()
                .pathParam("id", bookingid)
                .get(ApiEndpoints.BOOkING.getPath() + "/{id}")
                .then()
                .log()
                .all()
                .statusCode(404)
                .extract()
                .response();
    }
    public Response getById(int bookingid) {
        return getRequestSpec()
                .when()
                .pathParam("id", bookingid)
                .get(ApiEndpoints.BOOkING.getPath() + "/{id}")
                .then()
                .log()
                .all()
                .statusCode(404)
                .extract()
                .response();
    }
}
