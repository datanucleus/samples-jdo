package mydomain.model;

import javax.jdo.annotations.*;
import org.datanucleus.api.jdo.annotations.*;

@PersistenceCapable(detachable="true")
@MultiTenant(column="TENANT") // Provide column name for multitenant discrim
public class Person
{
    @PrimaryKey
    Long id;

    String name;

    public Person(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public void setName(String name)
    {
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
}
