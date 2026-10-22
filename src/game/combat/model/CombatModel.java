package game.combat.model;

import game.entities.Enemy;
import game.player.model.Player;
import game.items.Potion;
import game.items.PotionType;
import game.maingame.model.GameModel;

import java.util.ArrayList;
import java.util.List;

import static game.combat.model.CombatOutcome.*;

public class CombatModel
{
    private boolean combatActive;
    private String enemyName;
    private Enemy currentEnemy;
    
    private final GameModel gameModel;
    
    public CombatModel(GameModel gModel)
    {
        gameModel = gModel;
    }

    public boolean startCombat(Enemy enemy)
    {
        if(enemy == null || combatActive || enemy.getHealth() <= 0)
            return false;
        
        currentEnemy = enemy;
        enemyName = enemy.getName();
        combatActive = true;
        
        return true;
    }
    
    public void resetCombat()
    {
        endCombat();
        enemyName = null;
    }
    
    private void endCombat()
    {
        combatActive = false;
        currentEnemy = null;
    }

    /**
     * Attacks the enemy, ending combat if the enemy's health reaches zero.
     *
     * @return An ActionResult containing actual damage dealt and WIN or ONGOING.
     */
    public ActionResult attack()
    {
        Player player = gameModel.getPlayer();

        float damage = player.calculateAttackDamage();
        float armourPen = player.getArmourPenetration();
        float oldEnemyHealth = currentEnemy.getHealth();
        currentEnemy.applyDamage(damage, armourPen);
        
        float damageDealt = oldEnemyHealth - currentEnemy.getHealth();

        if(currentEnemy.getHealth() <= 0)
        {
            endCombat();
            return new ActionResult(WIN, damageDealt);
        }
        
        return new ActionResult(ONGOING, damageDealt);
    }

    /**
     * Attacks the player, ending combat if the player's health reaches zero.
     *
     * @return An ActionResult containing actual damage dealt and LOSE or ONGOING.
     */
    public ActionResult enemyAttack()
    {
        Player player = gameModel.getPlayer();
        
        float oldPlayerHealth = player.getHealth();
        float enemyDamage = currentEnemy.calculateAttackDamage();
        player.applyDamage(enemyDamage, 0);
        
        float damageTaken = oldPlayerHealth - player.getHealth();

        if(player.getHealth() <= 0)
        {
            endCombat();
            return new ActionResult(LOSE, damageTaken);
        }
        
        return new ActionResult(ONGOING, damageTaken);
    }

    /**
     * Uses a potion if one is available.
     *
     * @param potionType The type of potion to use.
     * @return An ActionResult containing ONGOING and the actual health restored, or zero if unavailable.
     */
    public ActionResult heal(PotionType potionType)
    {
        Player player = gameModel.getPlayer();
        Potion potion = new Potion(potionType);
        
        float oldPlayerHealth = player.getHealth();
        int potionCount = player.getInventory().getItemCount(potion);

        if(potionCount == 0)
            return new ActionResult(ONGOING, 0f);

        player.applyHealing(potion.use());
        player.removeFromInventory(potion, false);
        
        return new ActionResult(ONGOING, player.getHealth() - oldPlayerHealth);
    }

    public ActionResult run()
    {
        endCombat();
        return new ActionResult(FLEE, 0f);
    }
    
    public PotionType[] getAvailablePotions()
    {
        Player player = gameModel.getPlayer();
        Potion[] potionsFromInventory = player.getInventory().getAllItemTypeInInventory(Potion.class);
        List<PotionType> potionTypes = new ArrayList<>();
        
        for(Potion potion : potionsFromInventory)
            potionTypes.add(potion.getPotionType());
        
        return potionTypes.toArray(new PotionType[0]);
    }
    
    public boolean isInCombat() { return combatActive; }
    
    public String getEnemyName() { return enemyName; }
}