package game.world.model.generation;

import game.world.model.Direction;
import game.world.model.Point;

import static game.world.model.Direction.*;
import static game.world.model.generation.WorldGenConfig.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldGenerator
{
    private final Map<Point, GenerationRoom> rooms;
    private final Random rng;
    
    public WorldGenerator()
    {
        rooms = new HashMap<>();
        rng = new Random();
    }
    
    public Map<Point, GenerationRoom> generate()
    {
        if(!rooms.isEmpty()) return rooms;
        
        GenerationRoom origin = new GenerationRoom();
        rooms.put(origin.getPoint(), origin);
        GenerationRoom firstNorth = new GenerationRoom(origin, origin.getDepth() + 1, origin.getStreak() + 1, 1, NORTH, new Point((origin.getPoint().getX()), origin.getPoint().getY() + 1));
        rooms.put(firstNorth.getPoint(), firstNorth);
        GenerationRoom firstEast  = new GenerationRoom(origin, origin.getDepth() + 1, origin.getStreak() + 1, 1, EAST, new Point((origin.getPoint().getX() + 1), origin.getPoint().getY()));
        rooms.put(firstEast.getPoint(), firstEast);
        GenerationRoom firstSouth = new GenerationRoom(origin, origin.getDepth() + 1, origin.getStreak() + 1, 1, SOUTH, new Point((origin.getPoint().getX()), origin.getPoint().getY() - 1));
        rooms.put(firstSouth.getPoint(), firstSouth);
        GenerationRoom firstWest  = new GenerationRoom(origin, origin.getDepth() + 1, origin.getStreak() + 1, 1, WEST, new Point((origin.getPoint().getX() - 1), origin.getPoint().getY()));
        rooms.put(firstWest.getPoint(), firstWest);
        
        buildFrom(firstNorth);
        buildFrom(firstEast);
        buildFrom(firstSouth);
        buildFrom(firstWest);
        
        return rooms;
    }
    
    private void buildFrom(GenerationRoom parent)
    {
        if(terminate(parent)) return;
        
        for(int i = 0; i < parent.getBranch(); i++)
        {
            Direction direction;
            Point point;
            int retryCount = 0;
            
            do
            {
                direction = calculateDirection(parent);
                point = calculatePoint(parent, direction);
                retryCount++;
            } while((Math.abs(point.getX()) > MAX_RANGE || Math.abs(point.getY()) > MAX_RANGE || tooClumped(point)) && retryCount < 5);
            
            if(!rooms.containsKey(point) && retryCount < 5)
            {
                int depth = parent.getDepth() + 1;
                int streak = parent.getBranch() == 1 ? parent.getStreak() + 1 : 0;
                int branch = calculateBranch(parent);
                
                GenerationRoom room = new GenerationRoom(parent, depth, streak, branch, direction, point);
                rooms.put(room.getPoint(), room);
                buildFrom(room);
            }
        }
    }
    
    private int calculateBranch(GenerationRoom parent)
    {
        int threshold = rng.nextInt(100);
        if(parent.getStreak() >= TRIPLE_ROOM_THRESHOLD && threshold < (BASE_CHANCE + (parent.getStreak() - TRIPLE_ROOM_THRESHOLD) * GAIN_CHANCE)) return 3;
        if(parent.getStreak() >= DOUBLE_ROOM_THRESHOLD && threshold < (BASE_CHANCE + (parent.getStreak() - DOUBLE_ROOM_THRESHOLD) * GAIN_CHANCE)) return 2;
        return 1;
    }
    
    private Direction calculateDirection(GenerationRoom parent)
    {
        if(parent.getDepth() <= 1) return parent.getDirection();
        
        int chance = rng.nextInt(100);
        if(parent.getDirection() == parent.getParent().getDirection())
        {
            if(chance < 60) return parent.getDirection();
            if(chance < 80) return parent.getDirection().left();
            return parent.getDirection().right();
        }
        else
        {
            if(chance < 60) return parent.getDirection();
            if(chance < 90) return parent.getParent().getDirection();
            return parent.getParent().getDirection().opposite();
        }
    }
    
    private Point calculatePoint(GenerationRoom parent, Direction newDirection)
    {
        return switch(newDirection)
        {
            case NORTH -> new Point(parent.getPoint().getX(), parent.getPoint().getY() + 1);
            case EAST  -> new Point(parent.getPoint().getX() + 1, parent.getPoint().getY());
            case SOUTH -> new Point(parent.getPoint().getX(), parent.getPoint().getY() - 1);
            case WEST  -> new Point(parent.getPoint().getX() - 1, parent.getPoint().getY());
        };
    }
    
    private boolean tooClumped(Point parentPoint)
    {
        int counter = 0;
        
        for(int i = -1; i < 2; i++)
        {
            for(int j = -1; j < 2; j++)
            {
                Point pointToCheck = new Point(parentPoint.getX() + i, parentPoint.getY() + j);
                
                if(rooms.containsKey(pointToCheck)) counter++;
                if(counter > MAX_CLUMPING) return true;
            }
        }
        
        return false;
    }
    
    private boolean terminate(GenerationRoom parent)
    {
        if(rooms.size() < MIN_ROOM_COUNT) return false;
        if(Math.abs(parent.getPoint().getX()) < MIN_RANGE && Math.abs(parent.getPoint().getY()) < MIN_RANGE) return false;
        if(rng.nextInt(100) > TERMINATION_CHANCE) return false;
        
        return true;
    }
}