/**********************************************************************
Copyright (c) 2017 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
     ...
 **********************************************************************/
package mydomain.model;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Owner of animals.
 */
@PersistenceCapable(detachable = "true")
@FetchGroup(name="owner_pets", members=@Persistent(name="pets"))
public class Owner
{
    @PrimaryKey
    private Long id;

    private String firstName;
    private String lastName;

    @Element(mappedBy="owner")
    @Join
    private Set<Pet> pets = new HashSet<>();

    public Owner(long id, String first, String last)
    {
        this.id = id;
        this.firstName = first;
        this.lastName = last;
    }
    
    public long getId()
    {
        return id;
    }
    public String getFirstName()
    {
        return this.firstName;
    }
    public String getLastName()
    {
        return this.lastName;
    }

    public Set<Pet> getPets()
    {
        return pets;
    }
    public void addPet(Pet pet)
    {
        pets.add(pet);
    }
}
