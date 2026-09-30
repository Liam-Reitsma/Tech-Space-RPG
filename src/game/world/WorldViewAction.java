package game.world;

public interface WorldViewAction
{
    void updateWorldMapPanel(RoomState[][] roomState);
    void updateAvailableMovementsPanel(Direction[] directions);
}