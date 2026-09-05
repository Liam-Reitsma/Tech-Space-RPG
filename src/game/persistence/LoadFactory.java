package game.persistence;

import game.entities.Persistable;
import game.entities.Player;

public class LoadFactory
{
    public LoadFactory()
    {
    
    }
    
    public Persistable create(String name, String[] data)
    {
        switch(name)
        {
            case("Player"):
                return new Player(data);
            default:
                throw new IllegalArgumentException("Failed to create: " + name);
        }
    }
}
