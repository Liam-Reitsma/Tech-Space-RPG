package game.combat;

public enum CombatOutcome
{
    WIN("You Win."),
    LOSE("You Lose."),
    FLEE("You Ran Away."),;

    private final String message;

    CombatOutcome(String s)
    {
        message = s;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
