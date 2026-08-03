package game.entities;

public class StatChange
{
    public int change;
    public StatType statType;
    
    public StatChange(int delta, StatType stat)
    {
        change = delta;
        statType = stat;
    }
}
