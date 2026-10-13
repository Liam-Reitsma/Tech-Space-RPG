package game.world.controller;

import game.world.model.room.RuntimeRoom;

public interface RoomListener
{
    void onRoomEntered(RuntimeRoom room);
}
