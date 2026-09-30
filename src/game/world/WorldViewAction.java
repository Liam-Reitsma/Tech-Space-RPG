package game.world;

import game.world.room.RoomState;

public interface WorldViewAction
{
    void updateWorldMapPanel(RoomState[][] roomState);
    void updateAvailableMovementsPanel(Direction[] directions);
}