package game.world;

import game.entities.Persistable;

public class RuntimeRoom implements Persistable
{
    private boolean completed;
    private Point point;
    private RoomType roomType;
    
    public RuntimeRoom(Point roomPoint, RoomType type)
    {
        point = roomPoint;
        roomType = type;
        completed = false;
    }
    
    public RuntimeRoom(String[] data)
    {
        loadSaveInfo(data);
    }
    
    public boolean isCompleted() { return completed; }
    
    public Point getPoint() { return point; }
    
    public RoomType getRoomType() { return roomType; }
    
    public void setCompleted()
    {
        completed = true;
        roomType = RoomType.EMPTY;
    }
    
    public void setRoomType(RoomType newType) { roomType = newType; }
}