package game.entities;

/** Class used in Enemy and Player*/
public abstract class Entities
{
    protected String name;
    protected int level;
    protected float health;

    /** Returns a float of the total damage value that an entity can deal.*/
    public abstract float attack();
    /** Reduces the entity's health calculated by considering damage and armourPen.*/
    public abstract void takeDamage(float damage, float armourPen);
    
    public String getName() { return name; }
    
    public int getLevel() { return level; }
    
    public float getHealth() { return health; }
}