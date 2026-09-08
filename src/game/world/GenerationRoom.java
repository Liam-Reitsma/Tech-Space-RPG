package game.world;

public class GenerationRoom
{
    private final GenerationRoom parent;
    
    private final int depth;
    private final int streak;
    private final int branch;
    
    private final Point point;
    private final Direction direction;
    
    public GenerationRoom(GenerationRoom parentRoom, int depthCount, int streakCount, int branchCount, Point pointNew, Direction directionNew)
    {
        parent = parentRoom;
        
        depth = depthCount;
        streak = streakCount;
        branch = branchCount;
        
        point = pointNew;
        direction = directionNew;
    }
    
    public GenerationRoom()
    {
        parent = null;
        
        depth = 0;
        streak = 0;
        branch = 2;
        
        point = new Point(0, 0);
        direction = null;
    }
    
    public GenerationRoom getParent() { return parent; }
    
    public int getDepth() { return depth; }
    
    public int getStreak() { return streak; }
    
    public int getBranch() { return branch; }
    
    public Point getPoint() { return point; }
    
    public Direction getDirection() { return direction; }
    
}
