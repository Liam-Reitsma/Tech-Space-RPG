package game.combat;

import game.entities.Enemy;
import game.entities.player.Player;

public interface Actionable
{
    /**
     * Performs a player action dependent on {@link CombatAction}.
     * @param player the {@link Player} object.
     * @param enemy the {@link Enemy} object.
     * @return {@link ActionResult}
     */
    public ActionResult doAction(Player player, Enemy enemy);
}