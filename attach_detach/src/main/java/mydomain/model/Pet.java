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

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
@FetchGroups({@FetchGroup(name="pet_owner", members=@Persistent(name="owner")),
    @FetchGroup(name="pet_vet", members=@Persistent(name="vet"))})
public class Pet
{
    @PrimaryKey
    private Long id;

    private Vet vet;

    private String name;

    private Owner owner;

    public Pet(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public long getId()
    {
        return id;
    }

    public Vet getVet()
    {
        return vet;
    }
    public void setVet(Vet vet)
    {
        this.vet = vet;
    }

    public Owner getOwner()
    {
        return owner;
    }
    public void setOwner(Owner owner)
    {
        this.owner = owner;
    }
}
