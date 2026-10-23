package game.items;

import game.world.model.room.RoomContent;

public abstract class Item implements RoomContent
{
    protected String name;

    public String getName() { return name; }
    public abstract String getImageURL();
    public abstract Item copy();
}