package game.combat.controller;

import static game.combat.model.CombatOutcome.ONGOING;

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
    
    private CombatListener combatListener;
    private CombatViewAction combatViewAction;

    public CombatController(CombatModel cModel)
    {
        combatModel = cModel;
    }
    
    public void setCombatListener(CombatListener cListener) { combatListener = cListener;}
    
    public void setCombatViewAction(CombatViewAction cvAction) { combatViewAction = cvAction;}
    
    @Override
    public void requestStartCombat(Enemy enemy)
    {
        if(combatModel.startCombat(enemy))
            combatViewAction.displayMessage(enemy.getName() + " has appeared!");
        requestUpdate();
    }

    @Override
    public void requestResetCombat()
    {
        combatModel.resetCombat();
        combatViewAction.clearMessage();
        requestUpdate();
    }

    @Override
    public void requestAttack()
    {
        if(combatModel.isInCombat())
        {
            ActionResult result = combatModel.attack();
            String message = "You attacked dealing " + Math.round(result.resultValue()) + " damage to " + combatModel.getEnemyName() + "!";
            showResult(result.combatOutcome(), message);
            
            requestEnemyTurn();
        }
        
        requestUpdate();
    }

    @Override
    public void requestHeal(PotionType potionType)
    {
        if(potionType == null)
            return;
        
        if(combatModel.isInCombat())
        {
            ActionResult result = combatModel.heal(potionType);
            String message = "You drank a " + potionType.toString() + "! You healed for " + Math.round(result.resultValue())+ ".";
            showResult(result.combatOutcome(), message);
            
            requestEnemyTurn();
        }
        
        requestUpdate();
    }
    
    private void requestEnemyTurn()
    {
        if(combatModel.isInCombat())
        {
            ActionResult result = combatModel.enemyAttack();
            String message = combatModel.getEnemyName() + " attacked you! He dealt " + Math.round(result.resultValue()) + " damage to you!";
            showResult(result.combatOutcome(),  message);
        }
    }

    @Override
    public void requestRun()
    {
        if(combatModel.isInCombat())
        {
            ActionResult result = combatModel.run();
            String message = "You ran away from " + combatModel.getEnemyName() + "!";
            showResult(result.combatOutcome(), message);
        }
        
        requestUpdate();
    }

    @Override
    public void requestDeathMessage()
    {
        combatViewAction.displayMessage
                ("""
                Player has been defeated!
                It is time for a new timeline.
                Load another save or start a new game.
                """);
    }

    private void showResult(CombatOutcome combatOutcome, String message)
    {
        combatViewAction.displayMessage(message);
        if(combatOutcome != ONGOING && combatListener != null)
            combatListener.onCombatEnded(combatOutcome);
    }

    @Override
    public void requestUpdate()
    {
        combatViewAction.showAvailablePotions(combatModel.getAvailablePotions());
        combatViewAction.setActionEnabled(combatModel.isInCombat());
    }
}