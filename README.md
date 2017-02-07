## hive
https://cwiki.apache.org/confluence/display/Hive/AvroSerDe
https://cwiki.apache.org/confluence/display/Hive/LanguageManual+DML

## boot profile?
you can set a environment variable to fix profile.
powershell: setx spring.profiles.active dev

## jar?
Get-Item env:java_home
jar cf mrjars\xx.jar hello\hadoopwc\mrcode\*.class
jar cf .\mrjars\wc.jar .\bin\hello\hadoopwc\mrcode\*.class
cd .\bin

jar cf ..\mrjars\wc.jar .\hello\hadoopwc\mrcode\*.class

## java.lang.UnsatisfiedLinkError: org.apache.hadoop.io.nativeio.NativeIO$Windows.access0(Ljava/lang/String;I)Z
run winutilcopy.ps1
setx PATH "%PATH%;E:\configuratedHadoopFolder\hadoop-2.7.3\bin"

## hadoop env?
search Cluster class to debug.
JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,HADOOP_YARN_HOME

## hdfs permission
/user/admin
/tmp/hadoop-yarn/staging/admin
/tmp/hadoop-yarn/staging/history/done_intermediate