package game.world;

import game.world.room.RuntimeRoom;

import java.util.Map;

public interface WorldAction
{
    Map<Point, RuntimeRoom> requestGenerateWorld();
    void requestMoveNextRoom(Direction direction);
}