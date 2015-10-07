package mydomain.model;

import javax.jdo.annotations.*;

@PersistenceCapable(detachable="true")
public class Person
{
    @PrimaryKey
    Long id;

    String name;

    /** Persist this as either 1 or 0. */
    @Persistent(converter=Boolean10Converter.class)
    Boolean myBool1;

    /** Persist this as either Y or N. */
    @Persistent(converter=BooleanYNConverter.class)
    Boolean myBool2;

    public Person(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Long getId()
    {
        return id;
    }

    public void setMyBool1(Boolean bool)
    {
        this.myBool1 = bool;
    }
    public Boolean getMyBool1()
    {
        return myBool1;
    }
    public void setMyBool2(Boolean bool)
    {
        this.myBool2 = bool;
    }
    public Boolean getMyBool2()
    {
        return myBool2;
    }
}
