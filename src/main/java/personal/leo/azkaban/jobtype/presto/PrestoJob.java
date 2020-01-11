package personal.leo.azkaban.jobtype.presto;

import azkaban.jobExecutor.AbstractProcessJob;
import azkaban.jobExecutor.Job;
import azkaban.utils.Props;
import io.prestosql.jdbc.PrestoDriver;
import lombok.Cleanup;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.*;


public class PrestoJob extends AbstractProcessJob implements Job {

    public static final String KEY_PRESTO_JDBC_URL = "presto_jdbc_url";
    public static final String KEY_PRESTO_JDBC_USER = "presto_jdbc_user";
    public static final String KEY_PRESTO_JDBC_PASSWORD = "presto_jdbc_password";
    public static final String KEY_SQL_NAME = "sql_name";

    public PrestoJob(String jobId, Props sysProps, Props jobProps, Logger log) {
        super(jobId, sysProps, jobProps, log);
    }


    public void run() throws Exception {
        getLog().info("PrestoJob run");
        Class.forName(PrestoDriver.class.getName());

        String url = getAllProps().getString(KEY_PRESTO_JDBC_URL);
        String user = getAllProps().getString(KEY_PRESTO_JDBC_USER);
        getLog().info("get sql content begin ");
        String sqlContent = GitHttpUtil.getRaw(getAllProps());
        getLog().info("get sql Content end: " + sqlContent);
        String[] sqls = sqlContent.split(";");
        try (
                Connection connection = DriverManager.getConnection(url, user, null);
        ) {
            for (String sql : sqls) {
                if (StringUtils.isNotBlank(sql)) {
                    @Cleanup
                    Statement statement = connection.createStatement();
                    getLog().info("execute sql: " + sql);
                    boolean isQuery = statement.execute(sql);
                    getLog().info("isQuery: " + isQuery);
                    if (isQuery) {
                        printResultSet(statement);
                    } else {
                        long largeUpdateCount = statement.getLargeUpdateCount();
                        getLog().info("is update query,update count is :" + largeUpdateCount);
                    }
                }
            }
        }

    }

    private void printResultSet(Statement statement) throws SQLException {
        getLog().info("is query =======begin======");

        ResultSet resultSet = statement.getResultSet();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        getLog().info("column count: " + columnCount);
//TODO 没有打印出结果
        printRowHead(metaData, columnCount);
        printRowData(resultSet, columnCount);

        getLog().info("is query,=======end======");
    }

    private void printRowData(ResultSet resultSet, int columnCount) throws SQLException {
        int rowCount = 0;
        while (resultSet.next()) {
            StringBuilder rowContent = new StringBuilder();
            for (int columnIndex = 1; columnIndex < columnCount; columnIndex++) {
                Object columnValue = resultSet.getObject(columnIndex);
                rowContent.append(columnValue)
                        .append(",");
            }
            rowCount++;
            getLog().info("row " + rowCount + " : " + rowContent);
        }

    }

    private void printRowHead(ResultSetMetaData metaData, int columnCount) throws SQLException {
        StringBuilder rowHead = new StringBuilder();
        for (int columnIndex = 1; columnIndex < columnCount; columnIndex++) {
            String columnName = metaData.getColumnName(columnIndex);
            rowHead.append(columnName)
                    .append(",");
        }
        getLog().info("row head: " + rowHead);
    }

    public void cancel() {

    }

    public double getProgress() {
        return 0;
    }


    public boolean isCanceled() {
        return false;
    }
}
