package game.entities.player;

import static game.items.weapon.WeaponType.*;

import game.items.weapon.WeaponType;

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
    private final WeaponType startingWeapon;
    
    ClassType(int sStr, int sDex, int sInt, WeaponType weapon)
    {
        statStrength = sStr;
        statDexterity = sDex;
        statIntelligence = sInt;
        startingWeapon = weapon;
    }
    
    public int getStatStrength() { return statStrength; }
    
    public int getStatDexterity() { return statDexterity; }
    
    public int getStatIntelligence() { return statIntelligence; }

    public WeaponType getStartingWeapon() { return startingWeapon; }
    
    @Override
    public String toString() { return this.name().charAt(0) + this.name().substring(1).toLowerCase(); }
    
    public static String optionsToString()
    {
        StringBuilder result = new StringBuilder();
        int i = 1;
        for(ClassType type : ClassType.values())
        {
            if(type == UNSELECTED) continue;
            result.append(i++).append(". ").append(type.toString()).append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}