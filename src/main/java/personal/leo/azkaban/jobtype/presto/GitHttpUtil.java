package personal.leo.azkaban.jobtype.presto;

import azkaban.utils.Props;
import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GitHttpUtil {
    private static final String URL_TMPL = "https://%s/api/v4/projects/%s/repository/files/%s/raw?ref=%s";

    public static final String KEY_GIT_HOST = "git_host";
    public static final String KEY_GIT_PROJECT_ID = "git_project_id";
    public static final String KEY_GIT_FILE_PATH = "git_file_path";
    public static final String KEY_GIT_BRANCH = "git_branch";
    public static final String KEY_GIT_TOKEN = "git_token";


    public static String getRaw(Props props) throws IOException {
        String gitHost = props.getString(KEY_GIT_HOST);
        String gitProjectId = props.getString(KEY_GIT_PROJECT_ID);
        String gitFilePath = props.getString(KEY_GIT_FILE_PATH);
        String gitBranch = props.getString(KEY_GIT_BRANCH);
        String gitToken = props.getString(KEY_GIT_TOKEN);

        return getRaw(gitHost, gitProjectId, gitFilePath, gitBranch, gitToken);
    }

    public static String getRaw(String host, String projectId, String filePath, String branch, String token) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = String.format(
                URL_TMPL,
                host,
                projectId,
                URLEncoder.encode(filePath, StandardCharsets.UTF_8.name()),
                branch
        );
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("PRIVATE-TOKEN", token);
        @Cleanup
        CloseableHttpResponse response = httpclient.execute(httpGet);
        InputStream content = response.getEntity().getContent();
        String contentStr = IOUtils.toString(content, StandardCharsets.UTF_8);
        return contentStr;
    }

}
