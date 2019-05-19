import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class XmlDeserializer {

    /**
     * Deserialize Kundenummer from a given XML file
     * @param xmlFile xml file to get Kundenummer from
     * @return Kundenummer in string
     * */
    public static String getKundennummer(File xmlFile) throws JDOMException, IOException {
        Document doc = new SAXBuilder().build(xmlFile);
        Element metadata = doc.getRootElement();
        for (Element e :
                metadata.getChildren()) {
            if (e.getName().equals("Kundennummer")) return e.getText();
        }

        return "-1";
    }

    /**
     * Deserialize date from a given XML file
     * @param xmlFile xml file to get date from
     * @return date in XML-given string format
     * */
    public static String getDatum(File xmlFile) throws JDOMException, IOException {
        Document doc = new SAXBuilder().build(xmlFile);
        Element metadata = doc.getRootElement();
        for (Element e : metadata.getChildren())
            if (e.getName().equals("Scandatum"))
                return e.getText();

        return "1970-01-01";
    }

    /**
     * Deserialize date from a given XML file
     * @param xmlFile xml file to get date from
     * @return Date object with xml given date
     * */
    public static Date getDate(File xmlFile) throws JDOMException, IOException {
        Document doc = new SAXBuilder().build(xmlFile);
        Element metadata = doc.getRootElement();
        for (Element e :
                metadata.getChildren()) {
            if (e.getName().equals("Scandatum")) {
                String[] parts = e.getText().split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);

                return new Date(year, month, day);
            }
        }

        return new Date(1970, Calendar.JANUARY, 1);
    }

    /**
     * Deserialize delivery-number from a given XML file
     * @param xmlFile xml file to get delivery-number from
     * @return String object with xml given delivery-number
     * */
    public static String getSendungsnummer(File xmlFile) throws JDOMException, IOException {
        Document doc = new SAXBuilder().build(xmlFile);
        Element metadata = doc.getRootElement();
        for (Element e :
                metadata.getChildren()) {
            if (e.getName().equals("Sendungsnummer")) return e.getText();
        }

        return "-1";
    }
}
