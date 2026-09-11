package game.entities;

public class Enemy extends Entities implements Persistable
{
    private int gold;
    private int xp;
    
    private float damage;
    private float armour;
    
    public Enemy(EnemyType type, int level)
    {
        name = type.toString();
        health = (int)(type.getHealthGrowth() * level);
        gold = (int)(type.getGoldGrowth() * level);
        xp = (int)(type.getXPGrowth() * level);
        
        damage = type.getDamageGrowth() * level;
        armour = Math.min(0.7f, type.getArmourGrowth() * level);
    }

    public Enemy(String[] data) { loadSaveInfo(data); }

    @Override
    public float attack() { return damage; }

    @Override
    public void takeDamage(float damage)  { health -= Math.round((1-armour) * damage); }

    public float getHealth() { return health; }
    
    public int getGold() { return gold; }
    
    public int getXP() { return xp; }
}