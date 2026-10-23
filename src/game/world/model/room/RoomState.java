package game.world.model.room;

import static game.world.model.room.RoomTileState.*;

public class RoomState
{
    private RoomTileState roomTileState;
    private final RoomType roomType;
    private boolean isCurrentRoom;
    
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

    /**
     * Transitions UNSEEN to UNVISITED, leaving COMPLETED, FLED and other states unchanged.
     */
    public void setSeen()
    {
        if(roomTileState == UNSEEN)
            roomTileState = UNVISITED;
    }
    
    public RoomTileState getRoomTileState() { return roomTileState; }
    
    public void setCurrent() { isCurrentRoom = true; }

    public boolean isCurrent() { return isCurrentRoom; }

    public void clearCurrent() { isCurrentRoom = false; }

    public boolean isFled() { return roomTileState == FLED; }

    public void setFled() { roomTileState = FLED; }
    
    public RoomType getRoomType() { return roomType; }
    
    @Override
    public String toString() { return roomTileState.toString() + " " + roomType.toString(); }
}