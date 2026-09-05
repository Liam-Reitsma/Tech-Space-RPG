package game.items;

public abstract class Items
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
