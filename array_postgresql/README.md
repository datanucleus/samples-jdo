JDO Array using Postgresql array column
=======================================

Sample showing how to define an array to be stored into a PostgreSQL "INT ARRAY" column

In this example we have a class [Person](https://github.com/datanucleus/samples-jdo/blob/master/array_postgresql/src/main/java/mydomain/model/Person.java) with an _int[]_ field. 

We annotate the column to use _jdbcType_ of "ARRAY", and _sqlType_ of "INT ARRAY".

The table is then created by DataNucleus using this DDL

`CREATE TABLE person
(
    id int8 NOT NULL,
    "name" varchar(255) NULL,
    permissions int array NULL,
    CONSTRAINT person_pk PRIMARY KEY (id)
)`

A `Person` object is inserted using this SQL

_INSERT INTO person ("name",permissions,id) VALUES (<'First'>,<{"1","2","0"}>,<1>)_

and is retrieved using this SQL

_SELECT a0.permissions FROM person a0 WHERE a0.id = <1>_


NOTE: In DN up to v5.1.7 you would have to annotate the _element_ column, like
this, since it did not support direct annotation of the column for an array

> @Element(columns={@Column(name="PERMISSIONS", jdbcType="ARRAY", sqlType="INT ARRAY")})
> int[] permissions;

