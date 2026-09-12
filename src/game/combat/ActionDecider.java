package game.combat;

import java.util.Optional;

public class ActionDecider
{
    private final String message;
    private final Optional<CombatOutcome> endReason;
    
    ActionDecider(String stringMessage, CombatOutcome outcome)
    {
        message = stringMessage;
        endReason = Optional.ofNullable(outcome);
    }
    
    public String getMessage() { return message; }
    
    public Optional<CombatOutcome> getEndReason() { return endReason; }
}