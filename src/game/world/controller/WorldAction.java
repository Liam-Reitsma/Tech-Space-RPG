package game.world.controller;

import game.world.model.Direction;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.Map;

public interface WorldAction
{
    Map<Point, RuntimeRoom> requestGenerateWorld();
    void requestMoveNextRoom(Direction direction);
    void requestSetMovementLocked(boolean locked);
}