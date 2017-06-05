drop table user_info;
drop table movie_info;
drop table rating_info;
drop table predict_info;

create external table user_info(key string, id int) stored by "org.apache.hadoop.hive.hbase.HBaseStorageHandler" with serdeproperties ("hbase.columns.mapping"=":key,info:id") tblproperties("hbase.table.name"="user","hbase.mapred.out.outputtable"="user");

create external table movie_info(key string, id int, name string, genres string) stored by "org.apache.hadoop.hive.hbase.HBaseStorageHandler" with serdeproperties ("hbase.columns.mapping"=":key,info:id,info:name,info:genres") tblproperties("hbase.table.name"="movie","hbase.mapred.out.outputtable"="movie");

create external table rating_info(key string, rating double) stored by "org.apache.hadoop.hive.hbase.HBaseStorageHandler" with serdeproperties("hbase.columns.mapping"=":key,train:rating") tblproperties("hbase.table.name"="srs","hbase.mapred.out.outputtable"="srs");

create external table predict_info(key string, predicts string) stored by "org.apache.hadoop.hive.hbase.HBaseStorageHandler" with serdeproperties("hbase.columns.mapping"=":key,result:rating") tblproperties("hbase.table.name"="srs","hbase.mapred.out.outputtable"="srs");

--------------------------------------------------------------------------------------------------------------------------------------------

create external table rating_info2(key struct<u:string,m:string>, rating double) row format delimited COLLECTION ITEMS TERMINATED BY ':' stored by "org.apache.hadoop.hive.hbase.HBaseStorageHandler" with serdeproperties("hbase.columns.mapping"=":key,train:rating") tblproperties("hbase.table.name"="srs","hbase.mapred.out.outputtable"="srs");

create external table predict_info2(key struct<u:string>, predicts string) row format delimited COLLECTION ITEMS TERMINATED BY ':' stored by "org.apache.hadoop.hive.hbase.HBaseStorageHandler" with serdeproperties("hbase.columns.mapping"=":key,result:rating") tblproperties("hbase.table.name"="srs","hbase.mapred.out.outputtable"="srs");

查询平均评分最高的电影
select key.m as movie, sum(rating)/count(*) as result from rating_info2 group by key.m order by result desc;