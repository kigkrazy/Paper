import org.junit.Test;

import java.util.List;

import io.paperdb.Paper;

public class TestDb {
    @Test
    public void test() {
        Paper.book().write("test_string", "xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        Paper.book().write("test_string1", "xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        Paper.book().write("test_string2", "xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        Paper.book().write("test_1113", "xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        Paper.book().write("test_222", "xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        String cc = Paper.book().read("test_string");//read the information
        Paper.book().delete("test_string11");//read the information
        List<String> keys = Paper.book().getAllKeys();//read the information

        Paper.bookOn("d:\\tmp").write("lo_string", "xxffffggg");
        String x2 = Paper.bookOn("d:\\tmp").read("lo_string");
    }
}
