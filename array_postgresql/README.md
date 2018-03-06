JDO Array using Postgresql array column
=======================================

Sample showing how to define an array to be stored into a PostgreSQL "INT ARRAY" column

In this example we have a class [Person](https://github.com/datanucleus/samples-jdo/blob/master/array_postgresql/src/main/java/mydomain/model/Person.java) with an _int[]_ field. 

We annotate the column to use _jdbcType_ of "ARRAY", and _sqlType_ of "INT
ARRAY".
