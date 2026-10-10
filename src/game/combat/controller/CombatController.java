package game.combat.controller;

import game.Updatable;
import game.combat.model.ActionResult;
import game.combat.model.CombatModel;
import game.combat.model.CombatOutcome;
import game.combat.view.CombatViewAction;
import game.entities.Enemy;
import game.items.PotionType;


public class CombatController implements CombatAction, Updatable
{
    private final CombatModel combatModel;

    private CombatViewAction combatViewAction;
    private CombatListener combatListener;

    public CombatController(CombatModel cModel)
    {
        combatModel = cModel;
    }

    public void setCombatViewAction(CombatViewAction cvAction) { combatViewAction = cvAction;}

    public void setCombatListener(CombatListener cListener) { combatListener = cListener;}

    @Override
    public void startCombat(Enemy enemy)
    {
        combatModel.startCombat(enemy);
        combatViewAction.displayMessage(enemy.getName() + " has appeared!");
        requestUpdate();
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
        combatViewAction.displayMessage(combatResult.getMessage());
        if(combatResult.getOutcome() != CombatOutcome.ONGOING && combatListener != null)
            combatListener.onCombatEnded(combatResult.getOutcome());

        requestUpdate();
    }

    @Override
    public void requestUpdate()
    {
        combatViewAction.showAvailablePotions(combatModel.getAvailablePotions());
        combatViewAction.setActionEnabled(!combatModel.notInCombat());
    }
}