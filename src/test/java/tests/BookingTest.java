package tests;

import core.clients.APIClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Epic("Проверка полноценного сценария с частичным обновление")
@Feature("Использование нескольких методов")
public class BookingTest {

    private static APIClient apiClient;
    private static int bookingId;
    private static String token;

    @BeforeAll
    public static void setUp() throws Exception {
        apiClient = new APIClient();

        // Создание токена (необходимо для авторизации)
        Response tokenResponse = apiClient.createToken();
        token = tokenResponse.jsonPath().getString("token");
    }

    @Test
    @Story("Создать, обновить, частично обновить ")
    @Description("Тест, который создаёт букинг, обновляет его полностью, частично обновляет.")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUpdatePartUpdateBooking() throws Exception {

        // 1. Создание бронирования
        Response createResponse = apiClient.createPerson();
        Assertions.assertEquals(200, createResponse.getStatusCode(), "Ошибка при создании бронирования");
        createResponse.then().log().all(); // Логирование ответа

        // Получение ID созданного бронирования
        bookingId = createResponse.jsonPath().getInt("bookingid");

        // Проверка, что бронирование создано
        Response getBookingResponse = apiClient.getId(bookingId);
        Assertions.assertEquals(200, getBookingResponse.getStatusCode(), "Ошибка при получении созданного бронирования");
        getBookingResponse.then().log().all(); // Логирование ответа

        // 2. Обновление созданного бронирования
        Response updateResponse = apiClient.updatePerson(bookingId, token);
        Assertions.assertEquals(200, updateResponse.getStatusCode(), "Ошибка при обновлении бронирования");
        updateResponse.then().log().all(); // Логирование ответа

        // Проверка, что бронирование обновлено
        Response updatedBookingResponse = apiClient.getId(bookingId);
        Assertions.assertEquals(200, updatedBookingResponse.getStatusCode(), "Ошибка при получении обновленного бронирования");
        updatedBookingResponse.then().log().all(); // Логирование ответа

        // 3. Частичное обновление созданного бронирования
        Response partUpdateResponse = apiClient.partUpdatePerson(bookingId, token);
        Assertions.assertEquals(200, partUpdateResponse.getStatusCode(), "Ошибка при частичном обновлении бронирования");
        partUpdateResponse.then().log().all(); // Логирование ответа

        // Проверка, что бронирование частично обновлено
        Response partUpdatedBookingResponse = apiClient.getId(bookingId);
        Assertions.assertEquals(200, partUpdatedBookingResponse.getStatusCode(), "Ошибка при получении частично обновленного бронирования");
        partUpdatedBookingResponse.then().log().all(); // Логирование ответа
    }
}