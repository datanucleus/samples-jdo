user-type
=========

This project shows how to provide your own TypeConverter to allow persistence
of your own custom classes as a String (can also be persisted as Long using
the same ideas). It also shows how to provide your own JavaTypeMapping for
persisting that type to RDBMS datastores (it would work with the above
TypeConverter, but allows more control when done using a JavaTypeMapping). The
important files here are

* <a href="https://github.com/datanucleus/samples-jdo/blob/master/user_type/src/main/resources/META-INF/MANIFEST.MF">src/main/resources/META-INF/MANIFEST.MF</a>   The MANIFEST file for the plugin jar, defining the plugin imports/exports.
* <a href="https://github.com/datanucleus/samples-jdo/blob/master/user_type/src/main/resources/plugin.xml">src/main/resources/plugin.xml</a>   The plugin.xml file for the plugin jar, defining what features are being provided by this plugin.


The project also provides basic testing of the TypeConverter/JavaTypeMapping
