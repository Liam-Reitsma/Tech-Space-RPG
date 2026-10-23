package game.world.controller;

import game.world.model.Direction;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.Map;
import java.util.Set;

public interface WorldAction
{
    Map<Point, RuntimeRoom> requestGenerateWorld();
    Set<RuntimeRoom> requestGetUnsavedDiscoveredRooms();
    void requestClearUnsavedDiscoveredRooms();
    void requestMarkDiscoveredRoomSaved(RuntimeRoom room);
    void requestMoveNextRoom(Direction direction);
    void requestReturnPreviousRoom();
    void requestSetMovementLocked(boolean locked);
}