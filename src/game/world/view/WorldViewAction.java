package game.world.view;

import game.world.model.Direction;
import game.world.model.room.RoomState;

public interface WorldViewAction
{
    void updateWorldMapPanel(RoomState[][] roomStates);
    void updateAvailableMovementsPanel(Direction[] directions);
}