import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class XmlDeserializerTest {

    @DisplayName("Setting up test; loading dummy.xml")
    @BeforeEach
    void setUp() {
//        ClassLoader classLoader = getClass().getClassLoader();
//        try (InputStream inputStream = classLoader.getResourceAsStream("dummy.xml")) {
//            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//            System.out.println(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @DisplayName("Testing Kundennummer...")
    @Test
    void getKundennummber() {
    }

    @DisplayName("Testing Datum...")
    @Test
    void getDatum() {
    }

    @DisplayName("Testing Sendungsnummer...")
    @Test
    void getSendungsnummer() {
    }
}