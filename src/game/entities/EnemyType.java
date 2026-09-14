package game.entities;

import static game.world.RoomType.*;

import game.world.RoomType;

/** Enum with all the Enemy types that the Player can encounter*/
public enum EnemyType
{
    /** Bruiser has 5 strength, 3 dexterity, and 3 intelligence. This enemy will only be encountered in rooms categorised as Combat.*/
    BRUISER(5, 3, 3, COMBAT),
    /** Juggernaut has 8 strength, 4 dexterity, and 4 intelligence. This enemy will only be encountered in rooms categorised as Miniboss.*/
    JUGGERNAUT(8, 4, 4, MINIBOSS),
    /** Colossus has 10 strength, 7 dexterity, and 7 intelligence. This enemy will only be encountered in rooms categorised as Boss.*/
    COLOSSUS(10, 7, 7, BOSS),
    /** Striker has 3 strength, 5 dexterity, and 3 intelligence. This enemy will only be encountered in rooms categorised as Combat.*/
    STRIKER(3, 5, 3, COMBAT),
    /** Hunter has 4 strength, 8 dexterity, and 4 intelligence. This enemy will only be encountered in rooms categorised as Miniboss.*/
    HUNTER(4, 8, 4, MINIBOSS),
    /** Phantom has 7 strength, 10 dexterity, and 7 intelligence. This enemy will only be encountered in rooms categorised as Boss.*/
    PHANTOM(7, 10, 7, BOSS),
    /** Caster has 3 strength, 3 dexterity, and 5 intelligence. This enemy will only be encountered in rooms categorised as Combat.*/
    CASTER(3, 3, 5, COMBAT),
    /** Channeler has 4 strength, 4 dexterity, and 8 intelligence. This enemy will only be encountered in rooms categorised as Miniboss.*/
    CHANNELER(4, 4, 8, MINIBOSS),
    /** Ascendant has 7 strength, 7 dexterity, and 10 intelligence. This enemy will only be encountered in rooms categorised as Boss.*/
    ASCENDANT(7, 7, 10, BOSS),
    /** Vanguard has 4 strength, 4 dexterity, and 3 intelligence. This enemy will only be encountered in rooms categorised as Combat.*/
    VANGUARD(4, 4, 3, COMBAT),
    /** Ravager has 6 strength, 6 dexterity, and 4 intelligence. This enemy will only be encountered in rooms categorised as Miniboss.*/
    RAVAGER(6, 6, 4, MINIBOSS),
    /** Warlord has 9 strength, 9 dexterity, and 6 intelligence. This enemy will only be encountered in rooms categorised as Boss.*/
    WARLORD(9, 9, 6, BOSS),
    /** Operative has 3 strength, 4 dexterity, and 4 intelligence. This enemy will only be encountered in rooms categorised as Combat.*/
    OPERATIVE(3, 4, 4, COMBAT),
    /** Assassin has 4 strength, 6 dexterity, and 6 intelligence. This enemy will only be encountered in rooms categorised as Miniboss.*/
    ASSASSIN(4, 6, 6, MINIBOSS),
    /** Spectre has 6 strength, 9 dexterity, and 9 intelligence. This enemy will only be encountered in rooms categorised as Boss.*/
    SPECTRE(6, 9, 9, BOSS),
    /** Sentinel has 4 strength, 3 dexterity, and 4 intelligence. This enemy will only be encountered in rooms categorised as Combat.*/
    SENTINEL(4, 3, 4, COMBAT),
    /** Warcaster has 6 strength, 4 dexterity, and 6 intelligence. This enemy will only be encountered in rooms categorised as Miniboss.*/
    WARCASTER(6, 4, 6, MINIBOSS),
    /** Dreadlord has 9 strength, 6 dexterity, and 9 intelligence. This enemy will only be encountered in rooms categorised as Boss.*/
    DREADLORD(9, 6, 9, BOSS);

    private final float healthGrowth;
    private final float damageGrowth;
    private final float armourGrowth;
    private final float goldGrowth;
    private final float xpGrowth;
    private final RoomType roomType;

    /** Used to create Enemy.
     * Health growth of the Enemy scales with the Enemy's strength and dexterity stat
     * The maximum damage growth an Enemy could have is the largest number between the dexterity and intelligence stat added, then multiplied by 3 and divided by 9, and the multiple of the strength stat and 4 divided by 9.
     * Armour growth is the multiple of intelligence and 4, then add dexterity stat. Then it is divided by 1500.
     * Gold growth depends on health growth, damage growth, and armour growth to get its value which is used to determine the Enemy's gold drop.
     * XP growth depends on health growth, damage growth, and armour growth to get its value which is used to determine the Enemy's XP drop.*/
    EnemyType(int statStr, int statDex, int statInt, RoomType type)
    {
        healthGrowth = ((5 * statStr + statDex) / 3f);
        damageGrowth = (Math.max(4 * statStr, (3 * (statDex + statInt))) / 9f);
        armourGrowth = ((4 * statInt + statDex) / 1500f);
        goldGrowth = 0.2f * (damageGrowth + (healthGrowth / (1 - armourGrowth)));
        xpGrowth   = 0.6f * (damageGrowth + (healthGrowth / (1 - armourGrowth)));
        roomType = type;
    }

    public float getHealthGrowth() { return healthGrowth; }
    
    public float getDamageGrowth() { return damageGrowth; }
    
    public float getArmourGrowth() { return armourGrowth; }

    public float getGoldGrowth() { return goldGrowth; }
    
    public float getXPGrowth() { return xpGrowth; }
    
    public RoomType getRoomType() { return roomType; }

    /** Returns enum values as first letter capitalised and lowercase for rest by overriding the default toString().*/
    @Override
    public String toString() { return this.name().charAt(0) + this.name().substring(1).toLowerCase(); }

    /** When no match is found it returns null.*/
    public static EnemyType parseType(String typeToCheck)
    {
        for(EnemyType type : EnemyType.values())
        {
            if(type.toString().equals(typeToCheck)) return type;
        }

        return null;
    }

    /** Creates a EnemyType array that is set length of 6. It adds the EnemyTypes that apply to the RoomType and return a random EnemyType by getting a random index in the array.*/
    public static EnemyType getRandomEnemyType(RoomType room)
    {
        EnemyType[] enemyOption = new EnemyType[6];
        int i = 0;
        for(EnemyType type : EnemyType.values())
        {
            if(type.roomType == room) enemyOption[i++] = type;
        }
        
        return enemyOption[((int)(Math.random() * enemyOption.length))];
    }
}