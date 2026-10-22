package game.entities;

import static game.world.model.room.RoomType.*;

import game.world.model.room.RoomType;

public enum EnemyType
{
    BRUISER(5, 3, 3, COMBAT),
    JUGGERNAUT(8, 4, 4, MINIBOSS),
    COLOSSUS(10, 7, 7, BOSS),
    STRIKER(3, 5, 3, COMBAT),
    HUNTER(4, 8, 4, MINIBOSS),
    PHANTOM(7, 10, 7, BOSS),
    CASTER(3, 3, 5, COMBAT),
    CHANNELER(4, 4, 8, MINIBOSS),
    ASCENDANT(7, 7, 10, BOSS),
    VANGUARD(4, 4, 3, COMBAT),
    RAVAGER(6, 6, 4, MINIBOSS),
    WARLORD(9, 9, 6, BOSS),
    OPERATIVE(3, 4, 4, COMBAT),
    ASSASSIN(4, 6, 6, MINIBOSS),
    SPECTRE(6, 9, 9, BOSS),
    SENTINEL(4, 3, 4, COMBAT),
    WARCASTER(6, 4, 6, MINIBOSS),
    DREADLORD(9, 6, 9, BOSS);

    private final float healthGrowth;
    private final float damageGrowth;
    private final float armourGrowth;
    private final float goldGrowth;
    private final float xpGrowth;
    
    private final RoomType roomType;

    EnemyType(int statStr, int statDex, int statInt, RoomType type)
    {
        healthGrowth = ((5 * statStr + statDex) / 3f);
        damageGrowth = (Math.max(4 * statStr, (3 * (statDex + statInt))) / 9f);
        armourGrowth = ((4 * statInt + statDex) / 1500f);
        goldGrowth = 0.2f * (damageGrowth + (healthGrowth / (1 - armourGrowth)));
        xpGrowth = 0.6f * (damageGrowth + (healthGrowth / (1 - armourGrowth)));
        roomType = type;
    }

    public float getHealthGrowth() { return healthGrowth; }
    
    public float getDamageGrowth() { return damageGrowth; }
    
    public float getArmourGrowth() { return armourGrowth; }

    public float getGoldGrowth() { return goldGrowth; }
    
    public float getXPGrowth() { return xpGrowth; }

    @Override
    public String toString() { return this.name().charAt(0) + this.name().substring(1).toLowerCase(); }

    /**
     * Randomly selects an enemy type associated with the room type.
     *
     * @param roomType The room type to filter eligible enemies.
     * @return A randomly selected matching enemy type, or null if none exists.
     */
    public static EnemyType getRandomEnemyType(RoomType roomType)
    {
        int length = 0;
        for(EnemyType enemyType : EnemyType.values())
            if(enemyType.roomType == roomType)
                length++;
        if(length == 0)
            return null;
        
        EnemyType[] enemyOption = new EnemyType[length];
        
        int i = 0;
        for(EnemyType enemyType : EnemyType.values())
            if(enemyType.roomType == roomType)
                enemyOption[i++] = enemyType;
        
        return enemyOption[((int)(Math.random() * enemyOption.length))];
    }
}