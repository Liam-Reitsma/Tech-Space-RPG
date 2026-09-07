package game.items;

public enum PotionType
{
    SMALL_HEALTH(20),
    MEDIUM_HEALTH(40),
    LARGE_HEALTH(60);

    private final int healGain;

    PotionType(int value)
    {
        healGain = value;
    }

    public int getHealAmount() { return healGain; }
}
