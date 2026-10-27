package edu.psu.se411;

import org.junit.jupiter.api.Test;
import edu.psu.se411.exceptions.InvalidAgeException;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void rejectsAgesBelow18() {
        assertThrows(InvalidAgeException.class, () -> App.validateAge(17));
        assertThrows(InvalidAgeException.class, () -> App.validateAge(-1));
    }

    @Test
    void accepts18AndOlder() {
        assertDoesNotThrow(() -> App.validateAge(18));
        assertDoesNotThrow(() -> App.validateAge(65));
    }
}
