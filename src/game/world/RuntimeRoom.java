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
    
    public void setCompleted() { completed = true; }
    
    public void setRoomType(RoomType newType) { roomType = newType; }
    
    @Override
    public String getSaveInfo()
    {
        StringBuilder saveInfo = new StringBuilder();
        saveInfo.append(this.getClass().getSimpleName()).append(Persistable.CLASS_SEPARATOR);
        saveInfo.append("completed").append(FIELD_SEPARATOR).append(completed).append(DATA_SEPARATOR);
        saveInfo.append(point.toString()).append(DATA_SEPARATOR);
        saveInfo.append(roomType.toString());
        return saveInfo.toString();
    }
    
    @Override
    public void loadSaveInfo(String[] data)
    {
        for(String datum : data)
        {
            if(datum.toLowerCase().startsWith("completed"))
            {
                completed = Boolean.parseBoolean(datum.substring(datum.indexOf(String.valueOf(FIELD_SEPARATOR)) + 1));
            }
            else if(datum.toLowerCase().startsWith("point"))
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