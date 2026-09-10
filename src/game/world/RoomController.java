package game.world;

import static game.world.WorldErrors.*;
import static game.world.RoomType.*;

import game.ConsoleHandler;
import game.Loopable;
import game.entities.Enemy;
import game.entities.EnemyType;
import game.entities.Persistable;
import game.items.*;

import java.util.Random;

public class RoomController implements Loopable<RuntimeRoom, Void, RoomResult>
{
    private static RuntimeRoom currentRoom;
    
    private final Random random;
    private final ConsoleHandler consoleH;
    
    public RoomController(ConsoleHandler consoleHandler)
    {
        random = new Random();
        consoleH = consoleHandler;
    }
    
    @Override
    public RoomResult loop(RuntimeRoom room, Void input2)
    {
        currentRoom = room;
        
        return switch(room.getRoomType())
        {
            case EMPTY    -> new RoomResult(SUCCESS, null, EMPTY);
            case COMBAT   -> new RoomResult(SUCCESS, createEnemy(COMBAT), COMBAT);
            case CHEST    -> new RoomResult(SUCCESS, createChest(), CHEST);
            case MINIBOSS -> new RoomResult(SUCCESS, createEnemy(MINIBOSS), MINIBOSS);
            case BOSS     -> new RoomResult(SUCCESS, createEnemy(BOSS), BOSS);
        };
    }
    
    private Persistable createChest()
    {
        return switch(random.nextInt(10))
        {
            case 0 -> new Armour("Combat Visor", "A reinforced helmet fitted with basic targeting and environmental systems.", ArmourType.HELMET);
            case 1 -> new Armour("Armour", "A protective chest piece designed to reduce damage from weapons and impacts.", ArmourType.CHEST);
            case 2 -> new Armour("Tactical Boots", "Reinforced boots built for stability, protection, and movement across harsh terrain.", ArmourType.BOOTS);
            case 3 -> new Weapon("Energy Blade", "A compact weapon that projects a concentrated blade of energy capable of cutting through most materials.", WeaponType.LIGHTSABER);
            case 4 -> new Weapon("Void Blade", "A rare energy weapon that produces a dense, darkened blade with unusually high destructive power.", WeaponType.DARKSABER);
            case 5 -> new Weapon("Pulse Pistol", "A ranged weapon that fires concentrated bursts of energy at hostile targets.", WeaponType.LASERGUN);
            case 6 -> new Weapon("Psionic Power", "A mysterious energy that allows certain individuals to influence matter, movement, and living beings through thought.", WeaponType.FORCE);
            default -> new Weapon("Energy Blade", "A compact weapon that projects a concentrated blade of energy capable of cutting through most materials.", WeaponType.LIGHTSABER);
        };
    }
    
    private Persistable createEnemy(RoomType roomType)
    {
        return switch(roomType)
        {
            case COMBAT -> new Enemy("Timeweaver", EnemyType.CHRONOMANCER);
            case MINIBOSS -> new Enemy("Carapace", EnemyType.CHITIN);
            case BOSS -> new Enemy("Warborn", EnemyType.KRORK);
            default -> new Enemy("Failure", EnemyType.ATOM);
        };
    }
    
    public RoomResult loop(RuntimeRoom room)
    {
        return loop(room, null);
    }
    
    public RoomResult loop()
    {
        currentRoom.setCompleted();
        
        return new RoomResult(SUCCESS, null, null);
    }
}
