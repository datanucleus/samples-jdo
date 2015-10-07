package mydomain.model;

import javax.jdo.*;

public class BooleanYNConverter implements AttributeConverter<Boolean, Character>
{
    public Character convertToDatastore(Boolean attribute)
    {
        if (attribute == null)
        {
            return null;
        }
        return (attribute == Boolean.TRUE ? Character.valueOf('Y') : Character.valueOf('N'));
    }
    public Boolean convertToAttribute(Character dbData)
    {
        if (dbData == null)
        {
            return null;
        }

        if (dbData.equals(Character.valueOf('Y')))
        {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
