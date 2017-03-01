# One-to-ManyBidirectional JoinTable

The prerequisite is that you need the DataNucleus Maven2 plugin installed. You can download this plugin from the DataNucleus downloads area.

* Set the database configuration in "src/main/resources/META-INF/persistence.xml"
* Make sure you have the JDBC driver jar specified in the "pom.xml" file
* Run the command: "mvn clean compile". This builds everything, and enhances the classes
* Run the command: "mvn datanucleus:schema-create". This creates the schema
* ... run some persistence code ...
* Run the command: "mvn datanucleus:schema-delete". This deletes the schema



# Guide

This guide demonstrates a 1-N collection relationship between 2 classes. In this sample we
have *Pack* and *Card* such that each *Pack* can contain many *Card*s. In addition
each *Card* has a *Pack* that it belongs to. We demonstrate the classes themselves,
and the MetaData necessary to persist them to the datastore in the way that we require. In this
case we are going to persist the relation to an RDBMS using a Join Table.

* Design your Java classes to represent what you want to model in your system. Persistence doesn't have much of an impact on this stage, 
but we'll analyse the very minor influence it does have.
* Decide how the identities of your objects of these classes will be defined. Do you want JDO to give them id's or will you do it yourself.
* Define how your objects of these classes will be persisted.
** New Database Schema - you have a clean sheet of paper and can have them persisted with no constraints.
** Existing Database Schema - you have existing tables that you need the objects persisted to.


## The Classes

Let's look at our initial classes for the example. We want to represent a pack of cards.

```
package org.datanucleus.samples.packofcards.normal;

public class Pack
{
    String name=null;
    String description=null;

    Set    cards=new HashSet();

    public Pack(String name, String desc)
    {
        this.name = name;
        this.description = desc;
    }

    public void addCard(Card card)
    {
        cards.add(card);
    }

    public void removeCard(Card card)
    {
        cards.remove(card);
    }

    public Set getCards()
    {
        return cards;
    }

    public int getNumberOfCards()
    {
        return cards.size();
    }
}

public class Card
{
    String suit=null;
    String number=null;

    public Card(String suit,String number)
    {
        this.suit = suit;
        this.number = number;
    }

    public String getSuit()
    {
        return suit;
    }

    public String getNumber()
    {
        return number;
    }

    public String toString()
    {
        return "The " + number + " of " + suit;
    }
}
```

The first thing that we need to do is add a default constructor. This is a requirement of JDO. This can be private if we wish, so we add

```
public class Pack
{
    private Pack()
    {
    }

    ...
}
public class Card
{
    private Card()
    {
    }

    ...
}
```

## Object Identity

The next thing to do is decide if we want to allow DataNucleus to generate the identities of our objects, or whether we want to do it ourselves. 
In our case we will allow DataNucleus to create the identities for our *Pack*s, but since we know which *Card*s are in a pack we want to give them an 
identity based on the _suit_ and the _number_. This allows us to give examples of both *datastore identity* and *application identity*.

In the case of *Pack* there is nothing more to code since DataNucleus will handle the identities.
With *Card* however we now need to define a definition of the primary key. With JDO we must 
define a _primary key class_. In our case we want our primary key to be a composite of _suit_ and _number_, so we define a primary key as follows

```
    public static class CardKey
    {
        public String suit;
        public String number;

        /**
         *  Default constructor (mandatory for JDO).
         */
        public CardKey()
        {
        }

        /**
         * String constructor (mandatory for JDO).
         **/
        public CardKey(String str) 
        {
            StringTokenizer toke = new StringTokenizer (str, "::");
            str = toke.nextToken ();
            this.suit = str;
            str = toke.nextToken ();
            this.number = str;
        }

        /**
         * Implementation of equals method (JDO requirement).
         **/
        public boolean equals(Object obj)
        {
            if (obj == this)
            {
                return true;
            }
            if (!(obj instanceof CardKey))
            {
                return false;
            }
            CardKey c=(CardKey)obj;

            return suit.equals(c.suit) &amp;&amp; number.equals(c.number);
        }

        /**
         * Implementation of hashCode (JDO requirement)
         */
        public int hashCode ()
        {
            return this.suit.hashCode() ^ this.number.hashCode();
        }

        /**
         * Implementation of toString that outputs this object id's PK values.
         * (JDO requirement).
         **/
        public String toString ()
        {
            return this.suit + "::" + this.number;
        }
    }
```

We decide that we don't want to use this class as an external object so we make it an inner 
class of our *Card* class. This is optional. You could have it as a class used elsewhere 
if you wish.

## MetaData for New Schema

Now that we've decided on our classes and how we want to define their identities we can decide 
on the precise persistence definition. In this section we'll describe how to persist these objects 
to a new database schema where we can create new tables and don't need to write to some 
existing table.


Some JDO tools provide an IDE to generate Meta-Data files, but DataNucleus doesn't currently. Either 
way it is a good idea to become familiar with the structure of these files since they define 
how your classes are persisted. Lets start with the header area. You add a block like this to 
define that the file is JDO Meta-Data

```
<?xml version="1.0"?>
<!DOCTYPE jdo PUBLIC 
    "-//Sun Microsystems, Inc.//DTD Java Data Objects Metadata 2.0//EN" 
    "http://java.sun.com/dtd/jdo_2_0.dtd">
<jdo>
```

Now lets define the persistence for our *Pack* class. We are going to use 
*datastore identity* here, meaning that DataNucleus will assign id's to each *Pack* object 
persisted and store them in a surrogate column in the datastore. We define it as follows

```
    <package name="org.datanucleus.samples.packofcards.normal">
        <class name="Pack" identity-type="datastore">
            <field name="name" persistence-modifier="persistent">
                <column length="100" jdbc-type="VARCHAR"/>
            </field>
            <field name="description" persistence-modifier="persistent">
                <column length="255" jdbc-type="VARCHAR"/>
            </field>
            <field name="cards" persistence-modifier="persistent">
                <collection element-type="org.datanucleus.samples.packofcards.normal.Card"/>
                <join/>
            </field>
        </class>
```

Here we've defined that our _name_ field will be persisted to a VARCHAR(100) column, 
our _description_ field will be persisted to a VARCHAR(255) column, and that our 
_cards_ field is a Collection containing *org.datanucleus.samples.packofcards.normal.Card* 
objects. This final information is to inform DataNucleus to link the table for this class (via a 
foreign key) to the table for *Card* class. This is what is termed a **Join Table** 
relationship. Please refer to the [Relationships Guide](http://www.datanucleus.org/products/accessplatform/jdo/mapping.html#one_many_relations)
for more details on this. We'll discuss **ForeignKey** relationships in a different example.


Now lets define the persistence for our *Card* class. We are going to use 
*application identity* here, meaning that we store the id's for any object of type 
*Card* in a nominated field/fields, and that we can set the value if we so wish. 
We define it as follows

```
        <class name="Card" identity-type="application"
           objectid-class="org.datanucleus.samples.packofcards.normal.Card$CardKey">
            <field name="suit" primary-key="true">
                <column length="10" jdbc-type="VARCHAR"/>
            </field>
            <field name="number" primary-key="true">
                <column length="20" jdbc-type="VARCHAR"/>
            </field>
        </class>
    </package>
```

Here we've defined that our class uses *application identity* and that its primary key for 
this is *org.datanucleus.samples.packofcards.normal.Card$CardKey* (since its an inner class, we 
use the $ notation to define that). We've also defined that the 2 fields in this class are part 
of the primary key. You *must* define which fields are in the primary key.



We finally terminate the Meta-Data file with the closing tag

```
</jdo>
```

## MetaData for Existing Schema

Now that we've decided on our classes and how we want to define their identities we can decide 
on the precise persistence definition. In this section we'll describe how to persist these 
objects to an existing database schema where we already have some database tables from a 
previous persistence mechanism and we want to use those tables (because they have data in them). 
Our existing tables are shown below.

![Schema](docs/one_to_many_bi_join_existingschema.gif)

We will take the Meta-Data that was described in the previous section (New Schema) and continue 
from there. To recap, here is what we arrived at

```
<?xml version="1.0"?>
<!DOCTYPE jdo PUBLIC 
    "-//Sun Microsystems, Inc.//DTD Java Data Objects Metadata 2.0//EN" 
    "http://java.sun.com/dtd/jdo_2_0.dtd">
<jdo>
    <package name="org.datanucleus.samples.packofcards.normal">
        <class name="Pack" identity-type="datastore">
            <field name="name" persistence-modifier="persistent">
                <column length="100" jdbc-type="VARCHAR"/>
            </field>
            <field name="description" persistence-modifier="persistent">
                <column length="255" jdbc-type="VARCHAR"/>
            </field>
            <field name="cards" persistence-modifier="persistent">
                <collection element-type="org.datanucleus.samples.packofcards.normal.Card"/>
                <join/>
            </field>
        </class>
        <class name="Card" identity-type="application"
           objectid-class="org.datanucleus.samples.packofcards.normal.Card$CardKey">
            <field name="suit" primary-key="true">
                <column length="10" jdbc-type="VARCHAR"/>
            </field>
            <field name="number" primary-key="true">
                <column length="20" jdbc-type="VARCHAR"/>
            </field>
        </class>
    </package>
</jdo>
```

The first thing we need to do is map the *Pack* class to the table that we have in 
our database. It needs to be mapped to a table called "DECK", with columns "IDENTIFIERNAME"
and "DETAILS", and the identity column that DataNucleus uses needs to be called IDENTIFIER_ID. 
We do this by changing the Meta-Data to be

```
        <class name="Pack" identity-type="datastore" table="DECK">
            <datastore-identity>
                <column name="IDENTIFIER_ID"/>
            </datastore-identity>
            <field name="name" persistence-modifier="persistent">
                <column name="IDENTIFIERNAME" length="100" jdbc-type="VARCHAR"/>
            </field>
            <field name="description" persistence-modifier="persistent">
                <column name="DETAILS" length="255" jdbc-type="VARCHAR"/>
            </field>
            <field name="cards" persistence-modifier="persistent">
                <collection element-type="org.datanucleus.samples.packofcards.normal.Card"/>
                <join/>
            </field>
        </class>
```

So we made use of the attributes _table_ (of element *class*) and _name_ (of 
element *column*) to align to the table that is there. In addition we made use of the 
_datastore-identity_ element to map the identity column name. Lets now do the same for 
the class *Card*. In our database we want this to map to a table called "PLAYINGCARD",
with columns "SET" and "VALUE". So we do the same thing to its Meta-Data

```
        <class name="Card" identity-type="application" table="PLAYINGCARD"
           objectid-class="org.datanucleus.samples.packofcards.normal.Card$CardKey">
            <field name="suit" primary-key="true">
                <column name="SET" length="10" jdbc-type="VARCHAR"/>
            </field>
            <field name="number" primary-key="true">
                <column name="VALUE" length="20" jdbc-type="VARCHAR"/>
            </field>
        </class>
```

OK, so we've now mapped our 2 classes to their tables. The only remaining thing is that to form 
our relationship with the "Join Table Relationship" we have a _join table_ and in our database 
this is called "DECKOFCARDS" with columns "SET_ID", "VALUE_ID" and "DECK_ID". To map to these we 
need to further modify the Meta-Data for *Pack* as follows

```
        <class name="Pack" identity-type="datastore" table="DECK">
            <datastore-identity>
            	<column name="IDENTIFIER_ID"/>
            </datastore-identity>
            <field name="name" persistence-modifier="persistent">
                <column name="IDENTIFIERNAME" length="100" jdbc-type="VARCHAR"/>
            </field>
            <field name="description" persistence-modifier="persistent">
                <column name="DETAILS" length="255" jdbc-type="VARCHAR"/>
            </field>
            <field name="cards" persistence-modifier="persistent" table="DECKOFCARDS">
                <collection element-type="org.datanucleus.samples.packofcards.normal.Card"/>
                <join>
                    <column name="DECK_ID"/>
                </join>
                <element>
                    <column name="SET_ID" target="SET"/>
                    <column name="VALUE_ID" target="VALUE"/>
                </element>
            </field>
        </class>
```

So we've now updated the _cards_ field to use the new JDO 2 attributes 
_field_:_table_, _field_:_join_:_column_, and _field_:_element_:_column_ 
to have the mapping to the existing table. Please note the use of multiple columns where we have 
to map to the composite primary key on Card. This completes our job. The only other aspect that 
is likely to be met is where a column in the database is of a particular type, but we'll cover 
that in a different example.


                    One thing worth mentioning is the difference if our Collection class was a List, ArrayList, 
                    Vector, etc. In this case we need to specify the ordering column for maintaining the order within 
                    the List. In our case we want to specify this column to be called "IDX", so we do it like this.

```
        <class name="Pack" identity-type="datastore" table="DECK">
            <datastore-identity>
            	<column name="IDENTIFIER_ID"/>
            </datastore-identity>
            <field name="name" persistence-modifier="persistent">
                <column name="IDENTIFIERNAME" length="100" jdbc-type="VARCHAR"/>
            </field>
            <field name="description" persistence-modifier="persistent">
                <column name="DETAILS" length="255" jdbc-type="VARCHAR"/>
            </field>
            <field name="cards" persistence-modifier="persistent" table="DECKOFCARDS">
                <collection element-type="org.datanucleus.samples.packofcards.normal.Card"/>
                <join>
                    <column name="DECK_ID"/>
                </join>
                <element>
                    <column name="SET_ID" target="SET"/>
                    <column name="VALUE_ID" target="VALUE"/>
                </element>
                <order column="IDX"/>
            </field>
        </class>
```
