package game.entities;

import game.world.model.room.RoomContent;

public class Enemy extends Entities implements RoomContent
{
    private final int gold;
    private final int xp;
    private final float damage;
    private final float armour;

    public Enemy(EnemyType type, int playerLevel)
    {
        level = Math.max(playerLevel, 1);
        name = type.toString();
        health = (type.getHealthGrowth() * level);
        
        gold = (int)(type.getGoldGrowth() * level);
        xp = (int)(type.getXPGrowth() * level);
        damage = Math.max(type.getDamageGrowth() * level, 0);
        armour = Math.clamp(type.getArmourGrowth() * level, 0, 0.7f);
    }

    @Override
    public float calculateAttackDamage()
    {
        return damage;
    }

    @Override
    public void applyDamage(float damage, float armourPen)
    {
        health -= Math.max(damage, 0) * Math.clamp((1 - armour + armourPen), 0, 1);
        health = Math.max(health, 0);
    }

    public int getGold() { return gold; }
    
    public int getXP() { return xp; }
}