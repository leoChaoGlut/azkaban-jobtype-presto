# azkaban-jobtype-presto

# 插件部署步骤
- mvn package
- 把jobtype-presto-jar-with-dependencies.jar改名为jobtype-presto.jar
- 进入${azkabanExecutorHome}/plugins/jobtypes/,创建presto目录
- 把jobtype-presto.jar放到${azkabanExecutorHome}/plugins/jobtypes/presto/lib
- 把private.properties放到${azkabanExecutorHome}/plugins/jobtypes/presto
- 重启executor或 curl http://localhost:EXEC_SERVER_PORT/executor?action=reloadJobTypePlugins
