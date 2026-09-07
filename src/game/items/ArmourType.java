package game.items;

public enum ArmourType
{
    CHEST(50),
    HELMET(10),
    BOOTS(30);

    public final int protectionValue;

    ArmourType(int value)
    {
        protectionValue = value;
    }

    public int getProtectionValue()
    {
        return protectionValue;
    }
}
