package game.combat.model;

import game.entities.Enemy;
import game.entities.player.Player;
import game.maingame.model.GameModel;

public class CombatModel
{
    private final GameModel gameModel;

    private final Player player;
    private Enemy currentEnemy;

    public CombatModel(GameModel gModel)
    {
        gameModel = gModel;
        player = gModel.getPlayer();
    }

    public void startCombat(Enemy newEnemy)
    {
        currentEnemy = newEnemy;
    }

    public String playerAttack()
    {
        float damage = player.attack();
        float armourPen = player.getArmourPenetration();
        currentEnemy.takeDamage(damage, armourPen);

        String resultText = "Player inflicted " + Math.round(damage) + " to the enemy.\n" +
                "Enemy has " + Math.round(currentEnemy.getHealth()) + " health.";

        //TODO If player health hits zero then return the correct string and game is over.

        return resultText;
    }

    public String enemyAttack()
    {
        float enemyDamage = currentEnemy.attack();
        player.takeDamage(enemyDamage, 0);

        String resultText = "Enemy inflicted " + Math.round(enemyDamage) + " to the player.\n" +
                "Player has " + Math.round(player.getHealth()) + " health.";

        //TODO If enemy health hits zero then return the correct string and set the room to complete

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
}
