package game.items;

public enum ArmourType
{
    CHEST(0.15),
    HELMET(0.10),
    BOOTS(0.08);

    public final double protectionValue;

    ArmourType(double value)
    {
        protectionValue = value;
    }

    public double getProtectionValue()
    {
        return protectionValue;
    }
}
