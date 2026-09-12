package game.combat;

import game.entities.Enemy;
import game.entities.player.Player;

public interface Actionable
{
    public ActionResult action(Player player, Enemy enemy);
}