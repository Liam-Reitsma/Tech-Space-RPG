package game.world;

import game.entities.Persistable;

public class RuntimeRoom implements Persistable
{
    private Point point;
    private RoomType roomType;
    
    public RuntimeRoom(Point roomPoint, RoomType type)
    {
        point = roomPoint;
        roomType = type;
    }
    
    public RuntimeRoom(String[] data)
    {
        loadSaveInfo(data);
    }
    
    public Point getPoint() { return point; }
    
    public RoomType getRoomType() { return roomType; }
    
    @Override
    public String getSaveInfo()
    {
        return this.getClass().getSimpleName() + Persistable.CLASS_SEPARATOR + point.toString() + Persistable.DATA_SEPARATOR + roomType.toString();
    }
    
    @Override
    public void loadSaveInfo(String[] data)
    {
        for(String datum : data)
        {
            if(datum.startsWith("Point"))
            {
                point = Point.parsePoint(datum);
            }
            else
            {
                roomType = RoomType.parseRoom(datum);
            }
        }
    }
}