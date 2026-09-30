package game.world;

import static game.world.room.RoomType.*;
import static game.world.generation.WorldGenConfig.MAX_RANGE;

import game.world.generation.GenerationRoom;
import game.world.generation.WorldGenerator;
import game.world.room.RoomState;
import game.world.room.RoomTileState;
import game.world.room.RoomType;
import game.world.room.RuntimeRoom;

import java.util.*;

public class WorldModel
{
    private static final int VIEWABLE_RANGE = 5;
    
    private final Random random;
    private final WorldGenerator worldGenerator;
    
    public WorldModel()
    {
        random = new Random();
        worldGenerator = new WorldGenerator();
    }
    
    public Map<Point, RuntimeRoom> generateWorld()
    {
        Map<Point, GenerationRoom> generatedMap = worldGenerator.generate();
        Map<Point, RuntimeRoom> world = new HashMap<>();
        if(generatedMap.isEmpty()) return world;
        
        for(Point point : generatedMap.keySet())
        {
            int chance = random.nextInt(100);
            if(point.getX() == 0 && point.getY() == 0) world.put(point, new RuntimeRoom(point, new RoomState(EMPTY)));
            else if(chance < 5  && checkRoomToBuild(world, point, MINIBOSS)) world.put(point, new RuntimeRoom(point, new RoomState(MINIBOSS)));
            else if(chance < 25 && checkRoomToBuild(world, point, CHEST)) world.put(point, new RuntimeRoom(point, new RoomState(CHEST)));
            else if(chance < 40 && checkRoomToBuild(world, point, EMPTY)) world.put(point, new RuntimeRoom(point, new RoomState(EMPTY)));
            else world.put(point, new RuntimeRoom(point, new RoomState(COMBAT)));
        }
        
        return world;
    }
    
    private boolean checkRoomToBuild(Map<Point, RuntimeRoom> world, Point point, RoomType roomType)
    {
        RuntimeRoom testRoom;
        
        for(int i = -1; i < 2; i += 2)
        {
            testRoom = world.get(new Point(point.getX() + i, point.getY()));
            if(testRoom != null)
            {
                if(testRoom.getRoomState().getRoomType() == roomType) return false;
            }
            
            testRoom = world.get(new Point(point.getX(), point.getY() + i));
            if(testRoom != null)
            {
                if(testRoom.getRoomState().getRoomType() == roomType) return false;
            }
        }
        
        return true;
    }
    
    public Direction[] getPossibleDirections(Map<Point, RuntimeRoom> world, Point playerLocation)
    {
        List<Direction> directions = new ArrayList<>();
        
        for(Point orthogonalPoint : Point.getOrthogonalPoints(playerLocation))
        {
            if(world.containsKey(orthogonalPoint)) directions.add(orthogonalPoint.returnRelativeDirection(playerLocation));
        }
        
        return directions.toArray(new Direction[0]);
    }
    
    public RoomState[][] getSurroundingRoomStates(Map<Point, RuntimeRoom> world, Point playerPoint)
    {
        RoomState[][] roomStateMap = new RoomState[VIEWABLE_RANGE][VIEWABLE_RANGE];
        
        Point newPoint = getTopLeftPoint(playerPoint);
        
        for(int i = 0; i < roomStateMap.length; i++)
        {
            for(int j = 0; j < roomStateMap[0].length; j++)
            {
                if(isPointOutOfBounds(newPoint) || !world.containsKey(newPoint)) roomStateMap[i][j] = new RoomState(RoomTileState.WALL);
                else roomStateMap[i][j] = world.get(newPoint).getRoomState();
                
                newPoint = getNextPoint(newPoint, true);
            }
            
            newPoint = getNextPoint(newPoint, false);
        }
        
        return roomStateMap;
    }
    
    private Point getTopLeftPoint(Point point)
    {
        int x = point.getX();
        int y = point.getY();
        
        return new Point(x - VIEWABLE_RANGE / 2, y + VIEWABLE_RANGE / 2);
    }
    
    private Point getNextPoint(Point currentPoint, boolean moveX)
    {
        int x = currentPoint.getX();
        int y = currentPoint.getY();
        
        if(moveX) return new Point(x + 1, y);
        else return new Point(x - VIEWABLE_RANGE, y - 1);
    }
    
    private boolean isPointOutOfBounds(Point point)
    {
        if(point.getX() < -MAX_RANGE || point.getX() > MAX_RANGE) return true;
        if(point.getY() < -MAX_RANGE || point.getY() > MAX_RANGE) return true;
        
        return false;
    }
    
    public RuntimeRoom getNextRoom(Map<Point, RuntimeRoom> world,Point playerLocation, Direction direction)
    {
        return world.get(playerLocation.calculateNewPoint(direction));
    }
}