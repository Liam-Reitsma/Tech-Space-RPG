package game.entities;

public abstract class Entities
{
    protected String name;
    protected int level;
    protected float health;
    
    public abstract float attack();
    public abstract void takeDamage(float damage);
    
    public String getName() { return name; }
    
    public int getLevel() { return level; }
    
    public float getHealth() { return health; }
}