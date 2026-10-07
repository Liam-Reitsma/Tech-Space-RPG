package game.combat.controller;

import game.Updatable;
import game.combat.model.CombatModel;
import game.combat.view.CombatViewAction;
import game.items.Potion;

public class CombatController implements CombatAction, Updatable
{
    private final CombatModel combatModel;

    private CombatViewAction combatViewAction;

    public CombatController(CombatModel cModel)
    {
        combatModel = cModel;
    }

    public void setCombatViewAction(CombatViewAction cvAction)
    {
        combatViewAction = cvAction;
    }

    public void requestAttack()
    {
        String combatResult = combatModel.playerAttack();

        combatViewAction.displayMessage(combatResult);
    }

    public void requestHeal()
    {
        combatModel.heal();
    }

    public void requestUsePotion(Potion potion)
    {
        combatModel.heal();
    }

    public void requestRun()
    {
        combatViewAction.displayMessage("Player has ran away.");
    }

    @Override
    public void requestUpdate()
    {

    }
}