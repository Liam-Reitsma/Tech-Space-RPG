package game.world;

import game.entities.Persistable;

public class RuntimeRoom implements Persistable
{
    private final Point point;
    private final RoomState roomState;
    
    public RuntimeRoom(Point roomPoint, RoomState state)
    {
        point = roomPoint;
        roomState = state;
    }
    
    public RuntimeRoom(Object[] data)
    {
        point = new Point((int)data[0], (int)data[1]);
        roomState = new RoomState(RoomTileState.valueOf((String)data[3]), RoomType.valueOf((String)data[2]));
    }
    
    public boolean isCompleted() { return roomState.isCompleted(); }
    
    public Point getPoint() { return point; }
    
    public RoomState getRoomState() { return roomState; }
    
    public void setCompleted() { roomState.setCompleted(); }
}