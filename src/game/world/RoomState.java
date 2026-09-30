package game.world;

import static game.world.RoomTileState.*;

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
        roomTileState = UNVISITED;
        roomType = type;
    }
    
    public RoomState(RoomTileState state, RoomType type)
    {
        roomTileState = state;
        roomType = type;
    }
    
    public void setCompleted() { roomTileState = COMPLETED; }
    
    public boolean isCompleted() { return roomTileState == COMPLETED; }
    
    public RoomTileState getRoomTileState() { return roomTileState; }
    
    public RoomType getRoomType() { return roomType; }
    
    @Override
    public String toString()
    {
        if(roomTileState == WALL || roomTileState == UNVISITED) return roomTileState.toString();
        return roomTileState.toString() + " " + roomType.toString();
    }
}