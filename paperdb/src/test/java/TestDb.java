import org.junit.Test;

import io.paperdb.Paper;

public class TestDb {
    @Test
    public void test() {
        Paper.book().write("test_string", "xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        String cc = Paper.book().read("test_string");
    }
}
