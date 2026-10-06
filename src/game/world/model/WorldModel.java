package game.world.model;

import static game.world.model.room.RoomType.*;
import static game.world.model.generation.WorldGenConfig.MAX_RANGE;

import game.world.model.generation.GenerationRoom;
import game.world.model.generation.WorldGenerator;
import game.world.model.room.RoomState;
import game.world.model.room.RoomTileState;
import game.world.model.room.RoomType;
import game.world.model.room.RuntimeRoom;

import java.util.*;

public class WorldModel
{
    public static final int VIEWABLE_RANGE = 5;
    
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
    
    public RuntimeRoom[][] updateWorldInfo(Map<Point, RuntimeRoom> world, Point playerPoint)
    {
        RuntimeRoom[][] roomMap = new RuntimeRoom[MAX_RANGE * 2 + 1][MAX_RANGE * 2 + 1];
        
        for(int y = 0; y < MAX_RANGE * 2 + 1; y++)
        {
            for(int x = 0; x < MAX_RANGE * 2 + 1; x++)
            {
                Point point = new Point(x - MAX_RANGE, y - MAX_RANGE);
                if(world.containsKey(point))
                    roomMap[y][x] = world.get(point);
                else
                    roomMap[y][x] = new RuntimeRoom(point, new RoomState(RoomTileState.WALL, EMPTY));
            }
        }
        
        int viewingYLowRange = playerPoint.getY() + MAX_RANGE - (VIEWABLE_RANGE / 2);
        int viewingYTopRange = playerPoint.getY() + MAX_RANGE + 1 + (VIEWABLE_RANGE / 2);
        
        int viewingXLowRange = playerPoint.getX() + MAX_RANGE - (VIEWABLE_RANGE / 2);
        int viewingXTopRange = playerPoint.getX() + MAX_RANGE + 1 + (VIEWABLE_RANGE / 2);
        
        for(int y = viewingYLowRange; y < viewingYTopRange; y++)
        {
            for(int x = viewingXLowRange; x < viewingXTopRange; x++)
            {
                if(y < 0 || y >= MAX_RANGE * 2 + 1)
                    continue;
                if(x < 0 || x >= MAX_RANGE * 2 + 1)
                    continue;

                roomMap[y][x].setSeen();
            }
        }
        
        roomMap[playerPoint.getY() + MAX_RANGE][playerPoint.getX() + MAX_RANGE].setCurrent();
        
        return roomMap;
    }
    
    public RuntimeRoom moveNextRoom(Map<Point, RuntimeRoom> world, Point playerLocation, Direction direction)
    {
        RuntimeRoom room = world.get(playerLocation.calculateNewPoint(direction));
        room.setCurrent();
        return room;
    }
}