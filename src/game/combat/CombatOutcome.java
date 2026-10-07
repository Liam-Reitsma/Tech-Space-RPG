package game.combat;

/** Enum of CombatOutcomes. NOTHING is used to say that there are no outcomes yet allowing for the combat to continue.*/
public enum CombatOutcome
{
    WIN("You Win."),
    LOSE("You Lose."),
    FLEE("You Ran Away."),
    ONGOING("");

    private final String message;

    CombatOutcome(String s)
    {
        message = s;
    }

    public String getMessage() { return message; }
}