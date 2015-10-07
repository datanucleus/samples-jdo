JDO AttributeConverter
======================

Sample showing how to use a JDO AttributeConverter to store a Boolean in different ways.

In this example we have a class [Person](https://github.com/datanucleus/samples-jdo/blob/master/attribute_converter/src/main/java/mydomain/model/Person.java) with 2 Boolean fields. 
One is annotated to be persisted using [Boolean10Converter](https://github.com/datanucleus/samples-jdo/blob/master/attribute_converter/src/main/java/mydomain/model/Boolean10Converter.java)
meaning that it will persist either NULL, or 0 or 1 depending on the boolean value.
The other is annotated to be persisted using [BooleanYNConverter](https://github.com/datanucleus/samples-jdo/blob/master/attribute_converter/src/main/java/mydomain/model/BooleanYNConverter.java)
meaning that it will persist either NULL, or "N" or "Y" depending on the boolean value.

