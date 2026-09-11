package game.entities;

public abstract class Entities
{
    protected String name;
    protected int level;
    protected int health;
    
    abstract float attack();
    abstract void takeDamage(float damage);
}