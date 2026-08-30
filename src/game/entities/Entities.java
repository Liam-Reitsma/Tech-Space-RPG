package game.entities;

import game.SaveExclude;

public abstract class Entities
{
    @SaveExclude
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