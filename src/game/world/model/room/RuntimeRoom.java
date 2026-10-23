package game.world.model.room;

import game.world.model.Point;

public class RuntimeRoom
{
    private final Point point;
    private final RoomState roomState;
    
    public RuntimeRoom(Point roomPoint, RoomState state)
    {
        point = roomPoint;
        roomState = state;
    }

    /**
     * Creates a room from an array containing its coordinates and state.
     * Indices 0 and 1 contain integer coordinates, while indices 2 and 3
     * contain the room type and tile state enum names as strings.
     *
     * @param data The room coordinates, type and tile state in that order.
     */
    public RuntimeRoom(Object[] data)
    {
        point = new Point((int)data[0], (int)data[1]);
        roomState = new RoomState(RoomTileState.valueOf((String)data[3]), RoomType.valueOf((String)data[2]));
    }
    
    public boolean isSeen() { return roomState.isSeen(); }
    
    public void setSeen() { roomState.setSeen(); }
    
    public boolean isCompleted() { return roomState.isCompleted(); }
    
    public void setCompleted() { roomState.setCompleted(); }
    
    public RoomState getRoomState() { return roomState; }
    
    public void setCurrent() { roomState.setCurrent(); }

    public boolean isCurrent() { return roomState.isCurrent(); }

    public void clearCurrent() { roomState.clearCurrent(); }

    public boolean isFled() { return roomState.isFled(); }

    public void setFled() { roomState.setFled(); }
    
    public Point getPoint() { return point; }
}