package game.items;

import java.util.Objects;

public class Potion extends Item
{
    private final PotionType potionType;

    public Potion(PotionType type)
    {
        name = type.toString();
        potionType = type;
    }
    
    public int getHealAmount()
    {
        return potionType.getHealAmount();
    }
    
    public PotionType getPotionType() { return potionType; }
    
    
    @Override
    public String getImageURL()
    {
        return "resources/images/items/" + potionType.toFileName() + ".png";
    }
    
    @Override
    public Potion copy()
    {
        return new Potion(potionType);
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
        if(this == object)
            return true;
        if(!(object instanceof Potion potion))
            return false;
        
        return potionType == potion.potionType;
    }
}