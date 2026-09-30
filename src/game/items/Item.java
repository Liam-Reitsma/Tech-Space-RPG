package game.items;

import game.entities.Persistable;
import game.world.RoomContent;

public abstract class Item implements Persistable, RoomContent
{
    protected String name;

    public String getName()
    {
        return name;
    }
}