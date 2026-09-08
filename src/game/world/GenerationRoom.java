package game.world;

public class GenerationRoom
{
    private final GenerationRoom parent;
    
    private final int depth;
    private final int streak;
    private final int branch;
    
    private final Point point;
    private final Direction currentDir;
    
    public GenerationRoom(GenerationRoom parentRoom, int depthCount, int streakCount, int branchCount, Direction childDirection)
    {
        parent = parentRoom;
        
        depth = depthCount;
        streak = streakCount;
        branch = branchCount;
        
        point = switch(childDirection)
        {
            case NORTH -> new Point(parent.point.getX(), (parent.point.getY() + 1));
            case SOUTH -> new Point(parent.point.getX(), (parent.point.getY() - 1));
            case EAST  -> new Point((parent.point.getX() + 1), parent.point.getY());
            case WEST  -> new Point((parent.point.getX() - 1), parent.point.getY());
        };
        
        currentDir = childDirection;
    }
    
    public GenerationRoom()
    {
        parent = null;
        
        depth = 0;
        streak = 0;
        branch = 2;
        
        point = new Point(0, 0);
        currentDir = null;
    }
    
    public GenerationRoom getParent() { return parent; }
    
    public int getDepth() { return depth; }
    
    public int getStreak() { return streak; }
    
    public int getBranch() { return branch; }
    
    public Point getPoint() { return point; }
    
    public Direction getDirection() { return currentDir; }
    
}
