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
    
    @Override
    public String getSaveInfo()
    {
        StringBuilder saveInfo = new StringBuilder();
        saveInfo.append(this.getClass().getName()).append(CLASS_SEPARATOR);
        saveInfo.append("completed").append(FIELD_SEPARATOR).append(completed);
        if(completed) saveInfo.append(" ");
        saveInfo.append(DATA_SEPARATOR);
        saveInfo.append(point.toString()).append(DATA_SEPARATOR);
        saveInfo.append(roomType.toString()).repeat(" ", RoomType.maxToStringLength() - roomType.toString().length());
        return saveInfo.toString();
    }
    
    @Override
    public void loadSaveInfo(String[] data)
    {
        for(String datum : data)
        {
            if(datum.toLowerCase().startsWith("completed"))
            {
                completed = Boolean.parseBoolean(datum.trim().substring(datum.indexOf(String.valueOf(FIELD_SEPARATOR)) + 1));
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