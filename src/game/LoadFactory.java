package game;

import game.entities.Player;

public class LoadFactory
{
    public LoadFactory()
    {
    
    }
    
    public Object create(String name, String[] data)
    {
        switch(name)
        {
            case("Player"):
                return new Player(data);
            default:
                return null;
        }
    }
}
