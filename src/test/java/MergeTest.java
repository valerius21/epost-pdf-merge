import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO test remaining methods

class MergeTest {
    private File sp = new File("src/test/resources/merge");
    private Merge m = new Merge(sp.listFiles());

    @BeforeEach
    void setUp() {
        assert (sp.exists());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void equals1() {
        Merge x = new Merge(sp.listFiles());
        assertEquals(m, x);
    }

    @Test
    void hashCode1() { // TODO refactor
        int hc = m.hashCode();
        for (int i = 0; i < 1000; i++) {
            assertEquals(hc, m.hashCode());
        }
    }

    @Test
    void mergePack() throws IOException {
        m.mergePack("20190402_MB77777-000187");
        assertTrue(new File("src/test/resources/merge/merged_20190402_MB77777-000187.pdf").exists());
    }
}