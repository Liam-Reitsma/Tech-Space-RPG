package game.combat.controller;

import static game.combat.model.CombatOutcome.ONGOING;

import game.Updatable;
import game.combat.model.ActionResult;
import game.combat.model.CombatModel;
import game.combat.model.CombatOutcome;
import game.combat.view.CombatViewAction;
import game.entities.Enemy;
import game.items.PotionType;
import game.maingame.controller.GameAction;

public class CombatController implements CombatAction, Updatable
{
    private final CombatModel combatModel;
    
    private GameAction gameAction;
    
    private CombatListener combatListener;
    private CombatViewAction combatViewAction;
    
    public CombatController(CombatModel cModel)
    {
        combatModel = cModel;
    }
    
    public void setGameAction(GameAction gAction) { gameAction = gAction;}
    
    public void setCombatListener(CombatListener cListener) { combatListener = cListener;}
    
    public void setCombatViewAction(CombatViewAction cvAction) { combatViewAction = cvAction;}
    
    @Override
    public void requestStartCombat(Enemy enemy)
    {
        if(combatModel.startCombat(enemy))
            combatViewAction.displayMessage(enemy.getName() + " has appeared!");
        gameAction.requestGameUpdate();
    }

    @Override
    public void requestResetCombat()
    {
        combatModel.resetCombat();
        combatViewAction.clearMessage();
        gameAction.requestGameUpdate();
    }

    /**
     * Requests a player attack and automatically triggers the enemy's turn if the combat remains active.
     */
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
        
        gameAction.requestGameUpdate();
    }

    /**
     * Requests heal and automatically triggers the enemy's turn if the combat remains active.
     *
     * @param potionType Type of potion that is being used.
     */
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
        
        gameAction.requestGameUpdate();
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
        
        gameAction.requestGameUpdate();
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
    
    @Override
    public void requestDisplayMessage(String message)
    {
        combatViewAction.displayMessage(message);
    }
    
    /**
     * Displays the combat message and notifies the listener if combat has ended.
     *
     * @param combatOutcome The outcome of the combat.
     * @param message The message that is going to be displayed.
     */
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