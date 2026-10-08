package game.world.model.room;

import static game.world.model.room.RoomTileState.*;

public class RoomState
{
    private RoomTileState roomTileState;
    private final RoomType roomType;
    
    public RoomState(RoomTileState state)
    {
        roomTileState = state;
        roomType = RoomType.EMPTY;
    }
    
    public RoomState(RoomType type)
    {
        roomTileState = UNSEEN;
        roomType = type;
    }
    
    public RoomState(RoomTileState state, RoomType type)
    {
        roomTileState = state;
        roomType = type;
    }
    
    public boolean isCompleted() { return roomTileState == COMPLETED; }
    
    public void setCompleted() { roomTileState = COMPLETED; }
    
    public boolean isSeen() { return roomTileState != UNSEEN; }
    
    public void setSeen() { roomTileState = UNVISITED; }
    
    public RoomTileState getRoomTileState() { return roomTileState; }
    
    public void setCurrent() { roomTileState = CURRENT; }
    
    public RoomType getRoomType() { return roomType; }
    
    @Override
    public String toString() { return roomTileState.toString() + " " + roomType.toString(); }
}