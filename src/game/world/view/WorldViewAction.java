package game.world.view;

import game.world.model.Direction;
import game.world.model.room.RuntimeRoom;

public interface WorldViewAction
{
    void updateWorldMapPanel(RuntimeRoom[][] rooms);
    void updateAvailableMovementsPanel(Direction[] directions);
}