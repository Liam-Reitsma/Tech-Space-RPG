package game.entities;

import game.items.WeaponType;

import static game.items.WeaponType.*;

public enum ClassType
{
    GUARDIAN(5, 3, 3, ENERGY_SWORD),
    SCOUNDREL(3, 5, 3, PULSE_PISTOL),
    SAGE(3, 3, 5, GRAVITY_CONDUIT),
    MERCENARY(4, 4, 3, SHOCK_DAGGER),
    SHADOW(3, 4, 4, SHOCK_DAGGER),
    POWERTECH(4, 3, 4, ENERGY_SWORD),
    SCAVENGER(2, 2, 2, RUSTED_KNIFE),
    UNSELECTED(0, 0 ,0, null);
    
    private final int statStrength;
    private final int statDexterity;
    private final int statIntelligence;
    private final WeaponType weapon;
    
    ClassType(int sStr, int sDex, int sInt, WeaponType startingWeapon)
    {
        statStrength = sStr;
        statDexterity = sDex;
        statIntelligence = sInt;
        weapon = startingWeapon;
    }
    
    public int getStatStrength() { return statStrength; }
    
    public int getStatDexterity() { return statDexterity; }
    
    public int getStatIntelligence() { return statIntelligence; }

    public WeaponType getWeapon() { return weapon; }
    
    @Override
    public String toString() { return this.name().charAt(0) + this.name().substring(1).toLowerCase(); }
    
    public static ClassType parseType(String typeToCheck)
    {
        for(ClassType type : ClassType.values())
        {
            if(type.toString().equalsIgnoreCase(typeToCheck)) return type;
        }
        
        return null;
    }
    
    public static String optionsToString()
    {
        StringBuilder result = new StringBuilder();
        for(ClassType type : ClassType.values())
        {
            if(type == UNSELECTED) continue;
            result.append(type.toString()).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }
}