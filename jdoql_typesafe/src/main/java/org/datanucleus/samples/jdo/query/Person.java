package org.datanucleus.samples.jdo.query;

import javax.jdo.annotations.*;

@PersistenceCapable
public class Person
{
    @PrimaryKey
    long id;

    String name;

    Person bestFriend;

    public Person(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public void setBestFriend(Person per)
    {
        this.bestFriend = per;
    }
}
