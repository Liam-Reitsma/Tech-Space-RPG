package game.items;

import game.entities.Persistable;

public abstract class Item implements Persistable
{
    protected String name;

    public String getName()
    {
        return name;
    }
}
