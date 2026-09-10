package game.persistence;

import game.entities.Persistable;
import game.entities.Player;
import game.items.Armour;
import game.items.Potion;
import game.items.Weapon;
import game.world.RuntimeRoom;

public class LoadFactory
{
    public LoadFactory()
    {
    
    }
    
    public Persistable create(String name, String[] data)
    {
        return switch(name)
        {
            case ("Player")      -> new Player(data);
            case ("Weapon")      -> new Weapon(data);
            case ("Armour")      -> new Armour(data);
            case ("Potion")      -> new Potion(data);
            case ("RuntimeRoom") -> new RuntimeRoom(data);
            default -> null;
        };
    }
}
