package game.world;

public class RuntimeRoom
{
    private final Point point;
    private final RoomType roomType;
    
    public RuntimeRoom(Point roomPoint, RoomType type)
    {
        point = roomPoint;
        roomType = type;
    }
    
    public Point getPoint() { return point; }
    
    public RoomType getRoomType() { return roomType; }
}