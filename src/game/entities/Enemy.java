package game.entities;

public class Enemy extends Entities implements Persistable
{
    private int gold;
    private int xp;
    
    private float damage;
    private float armour;
    
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

    public Enemy(String[] data) { loadSaveInfo(data); }

    @Override
    public float attack() { return damage; }

    @Override
    public void takeDamage(float damage, float armorPen)
    {
        health -= Math.round(damage * Math.clamp((1 - armour + armorPen), 0, 1));
    }

    public int getGold() { return gold; }
    
    public int getXP() { return xp; }
}