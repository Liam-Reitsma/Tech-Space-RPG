package game.combat.controller;

import game.Updatable;
import game.combat.ActionResult;
import game.combat.model.CombatModel;
import game.combat.view.CombatViewAction;
import game.entities.Enemy;
import game.items.PotionType;

import static game.combat.CombatOutcome.ONGOING;

public class CombatController implements CombatAction, Updatable
{
    private final CombatModel combatModel;

    private CombatViewAction combatViewAction;

    public CombatController(CombatModel cModel)
    {
        combatModel = cModel;
    }

    public void setCombatViewAction(CombatViewAction cvAction) { combatViewAction = cvAction;}

    public void startCombat(Enemy enemy)
    {
        combatModel.startCombat(enemy);
        combatViewAction.setActionEnabled(true);
        combatViewAction.displayMessage(enemy.getName() + " has appeared!");
    }

    @Override
    public void requestAttack()
    {
        if(combatModel.notInCombat()) return;
        showResult(combatModel.attack());
    }

    @Override
    public void requestHeal(PotionType potionType)
    {
        if(combatModel.notInCombat()) return;
        showResult(combatModel.heal(potionType));
    }

    @Override
    public void requestRun()
    {
        if(combatModel.notInCombat()) return;
        showResult(combatModel.run());
    }

    private void showResult(ActionResult combatResult)
    {
        if(combatResult.getOutcome() != ONGOING) combatViewAction.setActionEnabled(false);
        combatViewAction.displayMessage(combatResult.getMessage());
    }

    @Override
    public void requestUpdate()
    {

    }
}