package game.world.model.room;

import static game.world.model.room.RoomType.*;

import game.entities.Enemy;
import game.entities.EnemyType;
import game.player.model.Player;
import game.items.Item;
import game.items.armour.Armour;
import game.items.armour.ArmourMaterial;
import game.items.armour.ArmourSlot;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponType;

public class RoomModel
{
    private static final int ENEMY_LEVEL_RANGE = 7;
    private static final int ITEM_LEVEL_RANGE = 7;
    
    public RoomModel()
    {
    
    }

    /**
     * Creates an enemy or chest item based on the room type and player's level.
     *
     * @param roomType The type of room being entered.
     * @param player The player whose level determines the generated content's level.
     * @return The generated enemy or item, or null for empty and wall rooms.
     */
    public RoomContent createRoomContent(RoomType roomType, Player player)
    {
        return switch(roomType)
        {
            case EMPTY, WALL-> null;
            case COMBAT     -> createEnemy(COMBAT, player.getLevel());
            case CHEST      -> createChest(player.getLevel());
            case MINIBOSS   -> createEnemy(MINIBOSS, player.getLevel());
            case BOSS       -> createEnemy(BOSS, player.getLevel());
        };
    }
    
    private Item createChest(int level)
    {
        int newLevel = Math.max(1, level + (int)Math.round((ITEM_LEVEL_RANGE * (Math.random() - 0.5))));
        
        if(Math.random() < 0.5)
            return new Armour(ArmourMaterial.getRandomMaterial(newLevel), ArmourSlot.getRandomSlot());
        else
            return new Weapon(WeaponType.getRandomWeaponType(), newLevel);
    }
    
    private Enemy createEnemy(RoomType roomType, int level)
    {
        EnemyType type = EnemyType.getRandomEnemyType(roomType);
        
        return new Enemy(type, Math.max(1, level + (int)Math.round((ENEMY_LEVEL_RANGE * (Math.random() - 0.5)))));
    }
}