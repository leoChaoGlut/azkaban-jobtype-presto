import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CommonTest {
    @Test
    public void test() throws UnsupportedEncodingException {
        String str = "dataworkflow-sql/test/test.sql";
        System.out.println(URLEncoder.encode(str, StandardCharsets.UTF_8.name()));
    }

    @Test
    public void test1() throws UnsupportedEncodingException {
        DateTimeZone dateTimeZone = DateTimeZone.forID("Asia/Shanghai");
        System.out.println(dateTimeZone);
    }

    @Test
    public void test2() throws UnsupportedEncodingException {
        String sqlContent = "create table if not exists t3(id int);\n" +
                "\n" +
                "delete from t3;\n" +
                "\n" +
                "insert into t3 \n" +
                "select * from t1";
        String[] split = sqlContent.split(";");
        for (String s : split) {
            System.out.println(s);
            System.out.println("==========");
        }
    }


}
