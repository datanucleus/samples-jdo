spatial
=======
This requires the DataNucleus Maven2 plugin. You can download this plugin from the DataNucleus downloads area.

1. Set the database configuration in the "src/main/resources/META-INF/persistence.xml" file.

2. Make sure you have the JDBC driver jar specified in the "pom.xml" file

3. Run the command: "mvn clean compile"
   This builds everything, and enhances the classes

4. Run the command: "mvn datanucleus:schema-create"
   This creates the schema for this sample.

5. Run the command: "mvn exec:java"
   This runs the tutorial

6. Run the command: "mvn datanucleus:schema-delete"
   This deletes the schema for this sample.



(c) Copyright 2008-2013 DataNucleus
