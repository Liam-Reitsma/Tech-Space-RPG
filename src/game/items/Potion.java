package game.items;

import java.util.Objects;
import java.util.Random;

public class Potion extends Item
{
    private PotionType potionType;

    public Potion(PotionType type)
    {
        name = type.toString();
        potionType = type;
    }

    public Potion(String[] data)
    {
        loadSaveInfo(data);
    }

    public PotionType getPotionType() { return potionType; }

    public PotionType getRandomPotionType()
    {
        int random = new Random().nextInt(PotionType.values().length);
        return PotionType.values()[random];
    }

    public int use()
    {
        return potionType.getHealAmount();
    }

    @Override
    public String toString()
    {
        return potionType.toString();
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(potionType);
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(this == object) return true;
        if(!(object instanceof Potion potion)) return false;
        
        return potionType == potion.potionType;
    }
    
    @Override
    public String getImageURL()
    {
        return "resources/images/items" + potionType.toString() + ".png";
    }
}