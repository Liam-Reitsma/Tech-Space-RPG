package game.world;

import static game.world.WorldErrors.*;
import static game.world.RoomType.*;

import game.ConsoleHandler;
import game.Loopable;
import game.entities.Enemy;
import game.entities.EnemyType;
import game.entities.Persistable;
import game.entities.player.Player;
import game.items.armour.Armour;
import game.items.armour.ArmourMaterial;
import game.items.armour.ArmourSlot;

public class RoomController implements Loopable<RuntimeRoom, Player, RoomResult>
{
    private static final int ENEMY_LEVEL_RANGE = 7;
    
    private static RuntimeRoom currentRoom;
    
    private final ConsoleHandler consoleH;
    
    public RoomController(ConsoleHandler consoleHandler)
    {
        consoleH = consoleHandler;
    }
    
    @Override
    public RoomResult loop(RuntimeRoom room, Player player)
    {
        currentRoom = room;
        
        return switch(room.getRoomType())
        {
            case EMPTY    -> new RoomResult(SUCCESS, null, EMPTY);
            case COMBAT   -> new RoomResult(SUCCESS, createEnemy(COMBAT, player.getLevel()), COMBAT);
            case CHEST    -> new RoomResult(SUCCESS, createChest(player.getLevel()), CHEST);
            case MINIBOSS -> new RoomResult(SUCCESS, createEnemy(MINIBOSS, player.getLevel()), MINIBOSS);
            case BOSS     -> new RoomResult(SUCCESS, createEnemy(BOSS, player.getLevel()), BOSS);
        };
    }
    
    private Persistable createChest(int level)
    {
        ArmourSlot slot = ArmourSlot.getRandomSlot();
        ArmourMaterial mat = ArmourMaterial.getRandomMaterial(level);
        
        return new Armour(mat, slot);
    }
    
    private Persistable createEnemy(RoomType roomType, int level)
    {
        EnemyType type = EnemyType.getRandomEnemyType(roomType);
        
        return new Enemy(type, Math.max((level + (int)Math.round((ENEMY_LEVEL_RANGE * (Math.random() - 0.5)))), 1));
    }
    
    public RoomResult loop()
    {
        currentRoom.setCompleted();
        
        return new RoomResult(SUCCESS, null, null);
    }
}