package mydomain.model;

import javax.jdo.annotations.*;

@PersistenceCapable(detachable="true")
public class Person
{
    @PrimaryKey
    Long id;

    String name;

    @Column(name="PERMISSIONS", jdbcType="ARRAY", sqlType="INT ARRAY")
    int[] permissions;

    public Person(long id, String name, int[] perms)
    {
        this.id = id;
        this.name = name;
        this.permissions = perms;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int[] getPermissions()
    {
        return permissions;
    }
}
