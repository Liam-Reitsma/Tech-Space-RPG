package game.combat;

import game.entities.Enemy;
import game.entities.player.Player;

public interface Actionable
{
    public ActionResult doAction(Player player, Enemy enemy);
}