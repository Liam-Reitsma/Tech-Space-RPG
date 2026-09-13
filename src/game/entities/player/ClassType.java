package game.entities.player;

import static game.items.weapon.WeaponType.*;

import game.items.weapon.WeaponType;

/** Enum of class types that a player can pick.*/
public enum ClassType
{
    /** Guardian class starts with 5 strength, 3 dexterity, and 3 intelligence. It starts with an Energy Sword.*/
    GUARDIAN(5, 3, 3, ENERGY_SWORD),
    /** Scoundrel class starts with 3 strength, 5 dexterity, and 3 intelligence. It starts with a Pulse Pistol.*/
    SCOUNDREL(3, 5, 3, PULSE_PISTOL),
    /** Sage class starts with 3 strength, 3 dexterity, and 5 intelligence. It starts with a Gravity Conduit.*/
    SAGE(3, 3, 5, GRAVITY_CONDUIT),
    /** Mercenary class starts with 4 strength, 4 dexterity, and 3 intelligence. It starts with a Shock Dagger.*/
    MERCENARY(4, 4, 3, SHOCK_DAGGER),
    /** Shadow class starts with 3 strength, 4 dexterity, and 4 intelligence. It starts with a Shock Dagger.*/
    SHADOW(3, 4, 4, SHOCK_DAGGER),
    /** Powertech class starts with 4 strength, 3 dexterity, and 4 intelligence. It starts with an Energy Sword.*/
    POWERTECH(4, 3, 4, ENERGY_SWORD),
    /** Scavenger is the bare-bone class with 2 strength, 2 dexterity, and 2 intelligence. It starts with a Rusted Knife.*/
    SCAVENGER(2, 2, 2, RUSTED_KNIFE),
    /** Unselected is a placeholder/default.*/
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

    /** Returns enum values as first letter capitalised and lowercase for rest by overriding the default toString().*/
    @Override
    public String toString() { return this.name().charAt(0) + this.name().substring(1).toLowerCase(); }

    /** Builds a string that contains a list of the enums with number in front for the {@link Player} to pick from separated by a newline. The Unselected class will be skipped as it is non-playable.*/
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