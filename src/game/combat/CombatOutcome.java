package game.combat;

public enum CombatOutcome
{
    WIN("You Win."),
    LOSE("You Lose."),
    FLEE("You Ran Away."),
    NOTHING("");

    private final String message;

    CombatOutcome(String s)
    {
        message = s;
    }

    public String getMessage() { return message; }
}