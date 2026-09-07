package game.combat;

import game.Result;

public class CombatResult extends Result<CombatErrors>
{
    public CombatResult(CombatErrors e)
    {
        error = e;
    }

}
