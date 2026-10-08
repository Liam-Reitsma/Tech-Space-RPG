package game.combat.controller;

import game.combat.model.CombatOutcome;

public interface CombatListener
{
    void onCombatEnded(CombatOutcome combatOutcome);
}