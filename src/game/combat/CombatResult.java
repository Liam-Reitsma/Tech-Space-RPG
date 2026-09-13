package game.combat;

import game.Result;

/**CombatResult extends {@link Result}. It takes {@link CombatErrors} and {@link CombatOutcome} as parameters and has a getter method for the {@link CombatOutcome}. The purpose of the class is to have the ability to return the error the task gave and the outcome of the combat.*/
public class CombatResult extends Result<CombatErrors>
{
    private final CombatOutcome combatOutcome;

    public CombatResult(CombatErrors e, CombatOutcome co)
    {
        error = e;
        combatOutcome = co;
    }

    public CombatOutcome getCombatOutcome() { return combatOutcome; }
}