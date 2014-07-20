javac -classpath $HADOOP_HOME/share/hadoop/common/hadoop-common-2.4.1.jar:$HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.4.1.jar:$HADOOP_HOME/share/hadoop/tools/lib/commons-cli-1.2.jar -d huedistribution huedistribution/ac/ahmet/analysis/HueDistribution.java
jar -cvf ./huedistribution.jar -C huedistribution/ .

