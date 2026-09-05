package game.entities;

public abstract class Entities
{
    protected  String name;
    protected int level;
    protected float health;
    
    protected float baseMana;
    protected float baseDamage;
    protected float baseArmor;
    
    abstract void attack();
    abstract void useAbility();
    abstract void dealDamage();
    abstract void takeDamage();
}