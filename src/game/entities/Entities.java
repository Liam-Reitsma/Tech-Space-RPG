package game.entities;

public abstract class Entities
{
    protected String name;
    protected int level;
    protected float health;
    protected float mana;
    protected float damage;
    protected float armor;
    
    abstract void attack();
    abstract void useAbility();
    abstract void dealDamage();
    abstract void takeDamage();
}