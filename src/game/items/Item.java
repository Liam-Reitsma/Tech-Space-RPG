package game.items;

import game.entities.Persistable;

public abstract class Item implements Persistable
{
    protected String title;
    protected String description;

    public String getName()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }
}
