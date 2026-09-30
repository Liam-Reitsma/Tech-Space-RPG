package game.entities;

import game.world.RoomContent;

/** Enemy extends Entities and implements Persistable. It holds the logic that applies to the {@link Enemy}.
 * It is also important to note that the {@link Enemy} scales to the Player's level but the armour is capped at 0.7.*/
public class Enemy extends Entities implements Persistable, RoomContent
{
    private int gold;
    private int xp;
    
    private float damage;
    private float armour;

    /** Used when creating new Enemy.*/
    public Enemy(EnemyType type, int playerLevel)
    {
        level = playerLevel;
        
        name = type.toString();
        health = (type.getHealthGrowth() * level);
        gold = (int)(type.getGoldGrowth() * level);
        xp = (int)(type.getXPGrowth() * level);
        
        damage = type.getDamageGrowth() * level;
        armour = Math.min(0.7f, type.getArmourGrowth() * level);
    }

    /** Used when loading the Enemy.*/
    public Enemy(String[] data) { loadSaveInfo(data); }

    /** Gets the damage stat of the Enemy and rounds it to 4 decimal places.*/
    @Override
    public float attack() { return Math.round(damage * 10000f) / 10000f; }

    /** Gets the armour stat of the Enemy and subtracts it from 1, which then has the Player armour penetration stat added to it. The result of this is set to be between 0 and 1.
     * It is then multiplied by the Player's damage output and subtracted from Enemy health.*/
    @Override
    public void takeDamage(float damage, float armourPen)
    {
        health -= Math.round(damage * Math.clamp((1 - armour + armourPen), 0, 1));
    }

    public int getGold() { return gold; }
    
    public int getXP() { return xp; }
}