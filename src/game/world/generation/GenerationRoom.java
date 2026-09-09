package game.world.generation;

import game.world.Direction;
import game.world.Point;

public class GenerationRoom
{
    private final GenerationRoom parent;
    
    private final int depth;
    private final int streak;
    private final int branch;
    
    private final Direction direction;
    private final Point point;
    
    public GenerationRoom(GenerationRoom parentRoom, int depthCount, int streakCount, int branchCount, Direction directionNew, Point pointNew)
    {
        parent = parentRoom;
        
        depth = depthCount;
        streak = streakCount;
        branch = branchCount;
        
        direction = directionNew;
        point = pointNew;
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
