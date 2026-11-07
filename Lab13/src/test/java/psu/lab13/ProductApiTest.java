package psu.lab13;
import java.net.URI;
import java.net.http.*;
import java.math.BigDecimal;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import psu.lab13.model.Product;
import psu.lab13.repositories.ProductRepository;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductApiTest {
    @LocalServerPort int port;
    @Autowired ProductRepository repository;
    @Autowired Validator validator;
    HttpResponse<String> get(String suffix) throws Exception {
        return HttpClient.newHttpClient().send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/v1/products" + suffix)).GET().build(), HttpResponse.BodyHandlers.ofString());
    }
    @Test void listsProducts() throws Exception {
        var r = get("");
        assertEquals(200, r.statusCode());
        assertTrue(r.headers().firstValue("content-type").orElse("").contains("application/json"));
        assertTrue(r.body().contains("3d printer"));
        assertTrue(r.body().contains("PC 08"));
        assertEquals(17, repository.count());
    }
    @Test void findsExactPrice() throws Exception {
        assertEquals(200, get("/10").statusCode());
        assertTrue(get("/10").body().contains("1840.25"));
        assertEquals(new BigDecimal("1840.25"), repository.findById(10).orElseThrow().price());
    }
    @Test void missingReturns404() throws Exception { assertEquals(404, get("/999").statusCode()); }
    @Test void invalidIdsReturn400() throws Exception {
        for (String id : new String[]{"-1", "0", "abc", "2147483648"}) assertEquals(400, get("/" + id).statusCode(), id);
    }
    @Test void validatesFields() {
        assertTrue(validator.validate(new Product(10, "Printer", "Description", new BigDecimal("10.25"))).isEmpty());
        assertFalse(validator.validate(new Product(-1, " ", "x".repeat(101), new BigDecimal("-0.001"))).isEmpty());
        assertFalse(validator.validate(new Product(1, "Printer", null, null)).isEmpty());
    }
}
