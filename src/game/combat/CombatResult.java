package game.combat;

import game.Result;

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