package org.datanucleus.samples.jdo.one_many_unidir;

import java.util.Set;
import java.util.HashSet;

public class Pack
{
    String name=null;
    String description=null;

    Set<Card> cards = new HashSet<Card>();

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

    public Set<Card> getCards()
    {
        return cards;
    }

    public int getNumberOfCards()
    {
        return cards.size();
    }
}
