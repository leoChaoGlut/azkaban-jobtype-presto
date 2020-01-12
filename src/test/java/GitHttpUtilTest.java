import org.junit.Test;
import personal.leo.azkaban.jobtype.presto.GitHttpUtil;

import java.io.IOException;

public class GitHttpUtilTest {
    @Test
    public void testGetRaw() throws IOException {
        String host = "gitlab.com";
        String projectId = "389";
        String filePath = "dataworkflow-sql/test/a.sql";
        String branch = "feature_arch";
        String token = "ysf2sNhjkhxSbrYSRUEm";
        String raw = GitHttpUtil.getRaw(host, projectId, filePath, branch, token);
        System.out.println(raw);
    }
}
