package game.entities;

public abstract class Entities
{
    protected String name;
    protected int level;
    protected int health;
    
    public abstract float attack();
    public abstract void takeDamage(float damage);
}