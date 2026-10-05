package game.combat;

import static game.combat.CombatOutcome.*;

import game.entities.player.Player;
import game.entities.player.PlayerErrors;
import game.entities.player.PlayerResult;
import game.items.*;
import game.entities.*;

import java.util.*;

/**
 * Actions that {@link Player} can perform when in battle.
 */
public enum CombatAction implements Actionable
{
    /** Gets the {@link Player} weapon to display, then uses player.attack() to calculate and apply damage to the {@link Enemy}.
     * When the {@link Enemy} health reaches 0, it will return a WIN outcome and ends this encounter.*/
    ATTACK
        {
            @Override
            public ActionResult doAction(Player player, Enemy enemy)
            {
                StringBuilder message = new StringBuilder();
                CombatOutcome outcome = NOTHING;
//                String equippedWeaponName = player.getEquippedWeapon() == null ? "fists" : player.getEquippedWeapon().getName();
                float damage = player.attack();
                enemy.takeDamage(damage, player.getArmourPenetration());
//                message.append("You used ").append(equippedWeaponName).append(" to attack ").append(enemy.getName()).append("!\n");
                message.append("You dealt ").append(damage).append("!\n");
                if(enemy.getHealth() <= 0) outcome = WIN;
                
                return new ActionResult(outcome, message.toString());
            }
        },
    /** Ends the encounter by returning the {@link CombatOutcome} FLEE and does nothing else.*/
    RUN
        {
            @Override
            public ActionResult doAction(Player player, Enemy enemy)
            {
                return new ActionResult(FLEE, "You ran away!");
            }
        },
    /** Checks the {@link Player} for potion and if the {@link Player} does not have a potion selected, then it returns the outcome of USER_WENT_BACK with the message "No potion was selected!". When an error happens when removing the potion from the inventory, it returns USER_WENT_BACK with the message "Something went wrong!".
     * Otherwise, apply the heal amount to the {@link Player} and return the outcome NOTHING with message "You healed for (potion used) hp!".*/
    HEAL
        {
            @Override
            public ActionResult doAction(Player player, Enemy enemy)
            {
//                PlayerResult<Potion> result = player.getSelectedPotion();
//                if(result.getError() != PlayerErrors.SUCCESS) return new ActionResult(USER_WENT_BACK, "No potion was selected!");
//                Potion potionUsed = result.getResultArray()[0];
//                if(player.removeFromInventory(potionUsed) != PlayerErrors.SUCCESS) return new ActionResult(USER_WENT_BACK, "Something went wrong!");
//                player.healing(potionUsed.use());
//                player.setSelectedPotion(null);
                return new ActionResult(NOTHING, "You healed for " + 10 + "hp!");
            }
        };
    /** Returns enum values as first letter capitalised and lowercase for rest by overriding the default toString().*/
    @Override
    public String toString() { return name().charAt(0) + name().substring(1).toLowerCase(); }

    /** Builds a string that contains a list of the enums with number in front for the {@link Player} to pick from separated by a newline.*/
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