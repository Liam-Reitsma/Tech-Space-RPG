package game.persistence;

import game.player.model.Player;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.Map;

public record SavedData(Player player, Map<Point, RuntimeRoom> world) {}