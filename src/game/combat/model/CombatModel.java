package game.combat.model;

import game.combat.ActionResult;
import game.combat.CombatOutcome;
import game.entities.Enemy;
import game.entities.player.Player;
import game.maingame.model.GameModel;

import static game.combat.CombatOutcome.*;

public class CombatModel
{
    private final GameModel gameModel;

    private Enemy currentEnemy;

    public CombatModel(GameModel gModel)
    {
        gameModel = gModel;
    }

    public void startCombat(Enemy newEnemy)
    {
        currentEnemy = newEnemy;
    }

    public boolean notInCombat()
    {
        return currentEnemy == null;
    }

    private void endCombat()
    {
        currentEnemy =  null;
    }

    public ActionResult attack()
    {
        Player player = gameModel.getPlayer();

        float damage = player.attack();
        float armourPen = player.getArmourPenetration();
        float oldEnemyHealth = currentEnemy.getHealth();
        currentEnemy.takeDamage(damage, armourPen);

        String playerAttackResult = "Player inflicted " + Math.round(oldEnemyHealth - currentEnemy.getHealth()) + " to the enemy.\n" +
                "Enemy has " + Math.round(Math.max(0, currentEnemy.getHealth())) + " health.\n";

        if(currentEnemy.getHealth() <= 0)
        {
            endCombat();
            return new ActionResult(WIN, playerAttackResult + "The enemy has been defeated!");
        }

        String enemyAttackResult = enemyAttack(player);
        if(player.getHealth() <= 0)
        {
            endCombat();
            return new ActionResult(LOSE, playerAttackResult + "\n" + enemyAttackResult + "You have been defeated!");
        }

        return new ActionResult(ONGOING, playerAttackResult + "\n" + enemyAttackResult);

    }

    private String enemyAttack(Player player)
    {
        float oldPlayerHealth = player.getHealth();
        float enemyDamage = currentEnemy.attack();
        player.takeDamage(enemyDamage, 0);

        String resultText = "Enemy inflicted " + Math.round(oldPlayerHealth - player.getHealth()) + " to the player.\n" +
                "Player has " + Math.round(player.getHealth()) + " health.\n";

        return resultText;
    }

    public String heal()
    {
//        int healingAmount = potion.use();
//        player.healing(healingAmount);
//
//        combatViewAction.updateCombatTextPane(String.format("Healing %d points", healingAmount));

        return "";
    }

    public ActionResult run()
    {
        endCombat();
        return new ActionResult(FLEE, "You ran away.");
    }
}