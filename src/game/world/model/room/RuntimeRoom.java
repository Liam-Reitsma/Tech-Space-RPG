package game.world.model.room;

import game.entities.Persistable;
import game.world.model.Point;

public class RuntimeRoom implements Persistable
{
    private final Point point;
    private final RoomState roomState;
    private boolean isSeen;
    
    public RuntimeRoom(Point roomPoint, RoomState state)
    {
        point = roomPoint;
        roomState = state;
        isSeen = false;
    }
    
    public RuntimeRoom(Object[] data)
    {
        point = new Point((int)data[0], (int)data[1]);
        roomState = new RoomState(RoomTileState.valueOf((String)data[3]), RoomType.valueOf((String)data[2]));
        //TODO Add unseen to database
    }
    
    public boolean isSeen() { return isSeen; }
    
    public void setSeen() { isSeen = true; }
    
    public boolean isCompleted() { return roomState.isCompleted(); }
    
    public void setCompleted() { roomState.setCompleted(); }
    
    public RoomState getRoomState() { return roomState; }
    
    public void setCurrent() { roomState.setCurrent(); }
    
    public Point getPoint() { return point; }
}