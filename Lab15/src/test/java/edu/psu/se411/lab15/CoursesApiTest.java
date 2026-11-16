package edu.psu.se411.lab15;
import java.net.URI;
import java.net.http.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoursesApiTest {
    @LocalServerPort int port;
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpResponse<String> request(String method, String path, String body) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/v1/courses" + path))
            .header("Content-Type", "application/json")
            .method(method, body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body))
            .build(), HttpResponse.BodyHandlers.ofString());
    }
    @Test void allFiveCrudOperations() throws Exception {
        var list = request("GET", "", null);
        assertEquals(200, list.statusCode());
        assertTrue(list.body().contains("SE201"));
        var created = request("POST", "", """
            {"code":"SE411","name":"Software Construction","description":"Lab 15","credits":3}
            """);
        assertEquals(201, created.statusCode());
        String location = created.headers().firstValue("Location").orElseThrow();
        assertTrue(location.contains("/api/v1/courses/"));
        String path = location.substring(location.lastIndexOf('/'));
        var found = request("GET", path, null);
        assertEquals(200, found.statusCode());
        assertTrue(found.body().contains("Software Construction"));
        assertEquals(204, request("PUT", path, """
            {"code":"SE411","name":"Updated Course","description":"Updated","credits":4}
            """).statusCode());
        assertTrue(request("GET", path, null).body().contains("Updated Course"));
        assertEquals(204, request("DELETE", path, null).statusCode());
        assertEquals(404, request("GET", path, null).statusCode());
        assertEquals(404, request("DELETE", path, null).statusCode());
        assertEquals(404, request("PUT", path, """
            {"code":"SE411","name":"Missing Course","credits":3}
            """).statusCode());
    }
    @Test void rejectsInvalidInputs() throws Exception {
        assertEquals(400, request("POST", "", "{}").statusCode());
        assertEquals(400, request("POST", "", """
            {"id":99,"code":"SE411","name":"Invalid ID","credits":3}
            """).statusCode());
        assertEquals(400, request("POST", "", """
            {"code":"SE411","name":"Invalid Credits","credits":11}
            """).statusCode());
    }
}
