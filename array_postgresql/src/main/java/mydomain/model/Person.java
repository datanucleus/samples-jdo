package mydomain.model;

import javax.jdo.annotations.*;

@PersistenceCapable(detachable="true")
public class Person
{
    @PrimaryKey
    Long id;

    String name;

    // Note that in DN up to 5.1.7 you had to specify the column in the ELEMENT, like this (commented out)
    //@Element(columns={@Column(name="PERMISSIONS", jdbcType="ARRAY", sqlType="INT ARRAY")})

    // In DN > 5.1.7 you can do this
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
