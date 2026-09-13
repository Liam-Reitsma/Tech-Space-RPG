package game.combat;

import static game.combat.CombatOutcome.*;

import game.entities.player.Player;
import game.entities.player.PlayerErrors;
import game.entities.player.PlayerResult;
import game.items.*;
import game.entities.*;

import java.util.*;

public enum CombatAction implements Actionable
{
    ATTACK
        {
            @Override
            public ActionResult doAction(Player player, Enemy enemy)
            {
                StringBuilder message = new StringBuilder();
                CombatOutcome outcome = NOTHING;
                String equippedWeaponName = player.getEquippedWeapon() == null ? "fists" : player.getEquippedWeapon().getName();
                float damage = player.attack();
                enemy.takeDamage(damage);
                message.append("You used ").append(equippedWeaponName).append(" to attack ").append(enemy.getName()).append("!\n");
                message.append("You dealt ").append(damage).append("!\n");
                if(enemy.getHealth() <= 0) outcome = WIN;
                
                return new ActionResult(outcome, message.toString());
            }
        },
    RUN
        {
            @Override
            public ActionResult doAction(Player player, Enemy enemy)
            {
                return new ActionResult(FLEE, "You ran away!");
            }
        },
    HEAL
        {
            @Override
            public ActionResult doAction(Player player, Enemy enemy)
            {
                PlayerResult<Potion> result = player.getSelectedPotion();
                if(result.getError() != PlayerErrors.SUCCESS) return new ActionResult(USER_WENT_BACK, "No potion was selected!");
                Potion potionUsed = result.getResultArray()[0];
                if(player.removeFromInventory(potionUsed) != PlayerErrors.SUCCESS) return new ActionResult(USER_WENT_BACK, "Something went wrong!");
                player.healing(potionUsed.use());
                player.setSelectedPotion(null);
                return new ActionResult(NOTHING, "You healed for " + potionUsed.use() + "hp!");
            }
        };
    
    @Override
    public String toString() { return name().charAt(0) + name().substring(1).toLowerCase(); }
    
    public static String optionsToString()
    {
        StringBuilder result = new StringBuilder();
        int i = 1;
        for(CombatAction action : CombatAction.values())
        {
            result.append(i++).append(". ").append(action.toString()).append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}