package game.world;

import static game.world.RoomType.*;
import static game.world.WorldErrors.*;

import game.ConsoleHandler;
import game.Loopable;
import game.Startable;
import game.mainmenu.MainMenuStates;
import game.world.generation.GenerationRoom;
import game.world.generation.WorldGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;

public class WorldController implements Startable<MainMenuStates, Map<Point, RuntimeRoom>, WorldResult>, Loopable<RuntimeRoom, Void, WorldResult>
{
    private final Random random;
    private final WorldGenerator worldGenerator;
    private final Map<Point, RuntimeRoom> world;
    private final ConsoleHandler consoleH;
    
    private Point playerLocation;
    
    public WorldController(ConsoleHandler consoleHandler)
    {
        consoleH = consoleHandler;
        
        random = new Random();
        worldGenerator = new WorldGenerator();
        world = new HashMap<>();
        playerLocation = new Point(0, 0);
    }
    
    @Override
    public WorldResult start(MainMenuStates state, Map<Point, RuntimeRoom> loadedWorld)
    {
        if(state == MainMenuStates.NEW_GAME) return newMap();
        if(loadedWorld == null)
        {
            consoleH.printLine(LOADING_WORLD_ERROR.getMessage());
            return new WorldResult(LOADING_WORLD_ERROR, null);
        }
        else return loadMapFromSave(loadedWorld);
    }
    
    private WorldResult newMap()
    {
        Map<Point, GenerationRoom> generatedMap = worldGenerator.generate();
        
        if(generatedMap.isEmpty()) return new WorldResult(WorldErrors.GENERATING_ERROR, null);
        
        for(Point point : generatedMap.keySet())
        {
            int chance = random.nextInt(100);
            if(point.getX() == 0 && point.getY() == 0) world.put(point, new RuntimeRoom(point, EMPTY));
            else if(chance < 5  && checkRoomToBuild(point, MINIBOSS)) world.put(point, new RuntimeRoom(point, MINIBOSS));
            else if(chance < 25 && checkRoomToBuild(point, CHEST)) world.put(point, new RuntimeRoom(point, CHEST));
            else if(chance < 40 && checkRoomToBuild(point, EMPTY)) world.put(point, new RuntimeRoom(point, EMPTY));
            else world.put(point, new RuntimeRoom(point, COMBAT));
        }
        
        return new WorldResult(WorldErrors.SUCCESS, null);
    }
    
    private WorldResult loadMapFromSave(Map<Point, RuntimeRoom> loadedWorld)
    {
        for(Map.Entry<Point, RuntimeRoom> room : loadedWorld.entrySet())
        {
            if(room.getKey() == null) return new WorldResult(WorldErrors.LOADING_WORLD_ERROR, null);
            world.put(room.getKey(), room.getValue());
        }
        
        return new WorldResult(WorldErrors.SUCCESS, null);
    }
    
    private boolean checkRoomToBuild(Point point, RoomType roomType)
    {
        RuntimeRoom testRoom;
        
        for(int i = -1; i < 2; i += 2)
        {
            testRoom = world.get(new Point(point.getX() + i, point.getY()));
            if(testRoom != null)
            {
                if(testRoom.getRoomType() == roomType) return false;
            }
            
            testRoom = world.get(new Point(point.getX(), point.getY() + i));
            if(testRoom != null)
            {
                if(testRoom.getRoomType() == roomType) return false;
            }
        }
        
        return true;
    }
    
    @Override
    public WorldResult loop(RuntimeRoom currentRoom, Void input2)
    {
        if(currentRoom != null) playerLocation = currentRoom.getPoint();
        
        int i = 0;
        for(Entry<Point, RuntimeRoom> room : world.entrySet())
        {
            if(!room.getValue().isCompleted()) i++;
            if(i > 1) break;
        }
        
        consoleH.printLine(movementOptions());
        Optional<Integer> result;
        RuntimeRoom room;
        
        do
        {
            result  = consoleH.nextInt().getInput();
            if(result.isEmpty()) consoleH.printLine("You can not use 'go back' here.");
            else if(result.get() > possibleOptions() || result.get() <= 0) consoleH.printLine("Please select a valid option.");
            else if(getNextRoom(result.get()) != null) { room = getNextRoom(result.get()); break; }
            else if(getNextRoom(result.get()) == null) return new WorldResult(WorldErrors.IMPOSSIBLE_ROOM_ERROR, null);
        } while(true);
        
        if(i == 1 && room != null) room.setRoomType(BOSS);
        
        return new WorldResult(WorldErrors.SUCCESS, room);
    }
    
    private int possibleOptions()
    {
        int i = 0;
        
        for(Point orthogonalPoint : Point.getOrthogonalPoints(playerLocation))
        {
            if(world.containsKey(orthogonalPoint)) i++;
        }
        
        return i;
    }
    
    private RuntimeRoom getNextRoom(int input)
    {
        int i = 0;
        
        for(Point orthogonalPoint : Point.getOrthogonalPoints(playerLocation))
        {
            if(world.containsKey(orthogonalPoint)) i++;
            if(world.containsKey(orthogonalPoint) && i == input) return world.get(orthogonalPoint);
        }
        
        return null;
    }
    
    private String movementOptions()
    {
        StringBuilder stringToReturn = new StringBuilder();
        stringToReturn.append("Where would you like to move? Your options are:\n");
        int i = 1;
        
        for(Point orthogonalPoint : Point.getOrthogonalPoints(playerLocation))
        {
            if(world.containsKey(orthogonalPoint))
            {
                
                stringToReturn.append(i).append(". ").append("Move ").append(playerLocation.returnRelativeDirection(orthogonalPoint));
                if(world.get(orthogonalPoint).isCompleted()) stringToReturn.append(" (Completed)");
                stringToReturn.append('\n');
                i++;
            }
        }
        
        stringToReturn.deleteCharAt(stringToReturn.length() - 1);
        
        return stringToReturn.toString();
    }
    
    public Map<Point, RuntimeRoom> getWorld() { return world; }
}