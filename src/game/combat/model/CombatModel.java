package game.combat.model;

import game.entities.Enemy;
import game.entities.player.Player;
import game.items.Potion;
import game.items.PotionType;
import game.maingame.model.GameModel;

import java.util.ArrayList;
import java.util.List;

import static game.combat.model.CombatOutcome.*;

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

    public void resetCombat()
    {
        endCombat();
    }

    private void endCombat()
    {
        currentEnemy =  null;
    }

    public PotionType[] getAvailablePotions()
    {
        Player player = gameModel.getPlayer();
        Potion[] potionsFromInventory = player.getInventory().getAllItemTypeInInventory(Potion.class);
        List<PotionType> potionTypes = new ArrayList<>();
        for(Potion potion : potionsFromInventory)
        {
            if(player.getInventory().getItemCount(potion) > 0)
                potionTypes.add(potion.getPotionType());
        }
        return potionTypes.toArray(new PotionType[0]);
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

        return enemyTurn(player, playerAttackResult);
    }

    private ActionResult enemyTurn(Player player, String playerResult)
    {
        float oldPlayerHealth = player.getHealth();
        float enemyDamage = currentEnemy.attack();
        player.takeDamage(enemyDamage, 0);

        String resultText = "Enemy inflicted " + Math.round(oldPlayerHealth - player.getHealth()) + " to the player.\n" +
                "Player has " + Math.round(player.getHealth()) + " health.\n";

        if(player.getHealth() <= 0)
        {
            endCombat();
            return new ActionResult(LOSE, playerResult + "\n" + resultText + "You have been defeated!");
        }
        return new ActionResult(ONGOING, playerResult + "\n" + resultText);
    }

    public ActionResult heal(PotionType potionType)
    {
        Player player = gameModel.getPlayer();
        float oldPlayerHealth = player.getHealth();
        Potion potion = new Potion(potionType);
        int potionCount = player.getInventory().getItemCount(potion);

        if(potionCount == 0) return new ActionResult(ONGOING, "No potions " + potionType + " in inventory!");

        player.healing(potion.use());
        player.removeFromInventory(potion, false);

        String playerHealString =  "Player has healed for " + Math.round(player.getHealth() - oldPlayerHealth) + " hp.\n" +
                "Player has " + Math.round(player.getHealth()) + " health.\n";

        return enemyTurn(player, playerHealString);
    }

    public ActionResult run()
    {
        endCombat();
        return new ActionResult(FLEE, "You ran away.");
    }
}