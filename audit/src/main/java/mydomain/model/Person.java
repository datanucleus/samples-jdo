package mydomain.model;

import org.datanucleus.api.jdo.annotations.*;
import java.sql.Timestamp;
import javax.jdo.annotations.*;

@PersistenceCapable(detachable="true")
public class Person
{
    @PrimaryKey
    Long id;

    String name;

    @CreateTimestamp
    Timestamp createTimestamp;

    @CreateUser
    String createUser;

    @UpdateTimestamp
    Timestamp updateTimestamp;

    @UpdateUser
    String updateUser;

    public Person(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getCreateUser()
    {
        return createUser;
    }

    public Timestamp getCreateTimestamp()
    {
        return createTimestamp;
    }

    public String getUpdateUser()
    {
        return updateUser;
    }

    public Timestamp getUpdateTimestamp()
    {
        return updateTimestamp;
    }
}
