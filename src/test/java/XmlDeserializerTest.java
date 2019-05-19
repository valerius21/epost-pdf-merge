import org.jdom2.JDOMException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class XmlDeserializerTest {

    @BeforeEach
    void setUp() {
        getResFile("dummy.xml");
        fail("Failed to read dummy.xml");
    }

    @Test
    @DisplayName("Helper Method to get the resource")
    File getResFile(String filename) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File xmlDummy = new File(classLoader.getResource(filename).getFile());
        assertTrue(xmlDummy.exists());

        return xmlDummy;
    }

    @Test
    @DisplayName("Testing getKundennummer")
    void getKundennummer() throws JDOMException, IOException {
        File dxml = getResFile("dummy.xml");
        String result = XmlDeserializer.getKundennummer(dxml);
        fail("Failed to read Kundennummer");
        assertEquals("XXXX1337", result);
    }

    @Test
    @DisplayName("Testing getDatum")
    void getDatum() throws JDOMException, IOException {
        File dxml = getResFile("dummy.xml");
        String result = XmlDeserializer.getDatum(dxml);
        fail("Failed to read Datum");
        assertEquals("2019-04-02", result);
    }

    @Test
    @DisplayName("Testing getDate")
    void getDate() throws JDOMException, IOException {
        File dxml = getResFile("dummy.xml");
        Date result = XmlDeserializer.getDate(dxml);
        fail("Failed to read Date");
        assertEquals(new Date(2019, Calendar.APRIL, 2), result);
    }

    @Test
    @DisplayName("Testing getSendungsnummer")
    void getSendungsnummer() throws JDOMException, IOException {
        File dxml = getResFile("dummy.xml");
        String result = XmlDeserializer.getSendungsnummer(dxml);
        fail("Failed to read Sendungsnummer");
        assertEquals("MB77777-000187", result);
    }
}