package edu.psu.se411.lab14;

import java.net.URI;
import java.net.http.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.*;

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
    private String course(long id, String code, String name, int credits) {
        return "{\"id\":" + id + ",\"code\":\"" + code + "\",\"name\":\"" + name
                + "\",\"description\":\"Test course\",\"credits\":" + credits + "}";
    }
    @Test void crudLifecycle() throws Exception {
        assertEquals(200, request("GET", "", null).statusCode());
        assertTrue(request("GET", "/1", null).body().contains("SE411"));
        String body = course(0, "TEST", "Testing", 1);
        var created = request("POST", "", body);
        assertEquals(201, created.statusCode());
        assertTrue(created.headers().firstValue("Location").orElseThrow().endsWith("/api/v1/courses/0"));
        assertEquals(200, request("GET", "/0", null).statusCode());
        assertEquals(422, request("POST", "", body).statusCode());
        var updated = request("PUT", "/0", course(0, "TEST", "Updated", 10));
        assertEquals(204, updated.statusCode());
        assertEquals("", updated.body());
        assertTrue(request("GET", "/0", null).body().contains("Updated"));
        assertEquals(400, request("PUT", "/0", course(99, "TEST", "Mismatch", 3)).statusCode());
        assertEquals(204, request("DELETE", "/0", null).statusCode());
        assertEquals(404, request("GET", "/0", null).statusCode());
        assertEquals(404, request("DELETE", "/0", null).statusCode());
        assertEquals(404, request("PUT", "/0", body).statusCode());
    }
    @Test void rejectsInvalidInputs() throws Exception {
        String[] invalid = {
            course(-1, "TEST", "Testing", 3), course(500, "", "Testing", 3),
            course(500, "   ", "Testing", 3), course(500, "TEST", "A", 3),
            course(500, "TEST", "Testing", 0), course(500, "TEST", "Testing", 11),
            "{}", "{", "{\"id\":500,\"code\":\"TEST\",\"name\":\"Testing\",\"credits\":null}"
        };
        for (String body : invalid) assertEquals(400, request("POST", "", body).statusCode(), body);
        assertEquals(404, request("GET", "/500", null).statusCode());
    }
}
