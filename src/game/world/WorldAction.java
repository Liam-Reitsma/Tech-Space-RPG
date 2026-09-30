package game.world;

import java.util.Map;

public interface WorldAction
{
    Map<Point, RuntimeRoom> requestGenerateWorld();
    void requestMoveNextRoom(Direction direction);
}