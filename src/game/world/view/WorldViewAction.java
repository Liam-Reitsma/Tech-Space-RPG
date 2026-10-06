package game.world.view;

import game.world.model.Direction;
import game.world.model.room.RoomState;

public interface WorldViewAction
{
    void updateWorldMapPanel(RoomState[][] roomState);
    void updateAvailableMovementsPanel(Direction[] directions);
}