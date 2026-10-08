package game.combat.model;

public class ActionResult
{
    private final CombatOutcome outcome;
    private final String message;
    
    public ActionResult(CombatOutcome combatOutcome, String stringMessage)
    {
        outcome = combatOutcome;
        message = stringMessage;
    }
    
    public String getMessage() { return message; }
    
    public CombatOutcome getOutcome() { return outcome; }
}