package game.persistence.dao;

import game.player.model.ClassType;
import game.player.model.Player;
import game.persistence.DBManager;
import game.persistence.DatabaseSchemaManager;
import game.world.model.Point;
import game.world.model.room.RoomState;
import game.world.model.room.RoomTileState;
import game.world.model.room.RoomType;
import game.world.model.room.RuntimeRoom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class WorldDAOTest
{
    private static final String SAVE_NAME = "Test Save";
    
    private DBManager dbManager;
    private WorldDAO worldDAO;
    
    private Player testPlayer;
    
    public WorldDAOTest()
    {
    
    }
    
    @Before
    public void setUp()
    {
        testPlayer = new Player("John", ClassType.MERCENARY);
        dbManager = new DBManager();
        worldDAO = new WorldDAO(dbManager);
        DatabaseSchemaManager databaseSchemaManager = new DatabaseSchemaManager(dbManager);
        databaseSchemaManager.dropTables();
        databaseSchemaManager.createTables();
        PlayerDAO playerDAO = new PlayerDAO(dbManager);
        playerDAO.insertPlayer(SAVE_NAME, testPlayer);
    }
    
    @Test
    public void testInsertAndLoadWorld()
    {
        Map<Point, RuntimeRoom> world = new HashMap<>();
        Point pointOne = new Point(0, 0);
        Point pointTwo = new Point(1, 0);
        world.put(pointOne, new RuntimeRoom(pointOne, new RoomState(RoomTileState.CURRENT, RoomType.COMBAT)));
        world.put(pointTwo, new RuntimeRoom(pointTwo, new RoomState(RoomTileState.UNVISITED, RoomType.CHEST)));
        assertTrue(worldDAO.insertWorld(SAVE_NAME, world));
        
        Map<Point, RuntimeRoom> loadedWorld = worldDAO.loadSavedWorld(SAVE_NAME);
        assertNotNull(loadedWorld);
        assertEquals(2, loadedWorld.size());
        
        RuntimeRoom loadedOne = loadedWorld.get(pointOne);
        RuntimeRoom loadedTwo = loadedWorld.get(pointTwo);
        assertNotNull(loadedOne);
        assertNotNull(loadedTwo);
        assertEquals(RoomTileState.CURRENT, loadedOne.getRoomState().getRoomTileState());
        assertEquals(RoomType.COMBAT, loadedOne.getRoomState().getRoomType());
        assertEquals(RoomTileState.UNVISITED, loadedTwo.getRoomState().getRoomTileState());
        assertEquals(RoomType.CHEST, loadedTwo.getRoomState().getRoomType());
    }
    
    @Test
    public void testUpdateRoomStatus()
    {
        Map<Point, RuntimeRoom> world = new HashMap<>();
        Point point = new Point(0, 0);
        RuntimeRoom room = new RuntimeRoom(point, new RoomState(RoomTileState.CURRENT, RoomType.COMBAT));
        world.put(point, room);
        assertTrue(worldDAO.insertWorld(SAVE_NAME, world));
        
        room.setCompleted();
        assertTrue(worldDAO.updateRoomStatus(SAVE_NAME, room));
        
        Map<Point, RuntimeRoom> loadedWorld = worldDAO.loadSavedWorld(SAVE_NAME);
        assertEquals(RoomTileState.COMPLETED, loadedWorld.get(point).getRoomState().getRoomTileState());
    }
    
    @Test
    public void testUpdateAndLoadWorld()
    {
        Map<Point, RuntimeRoom> world = new HashMap<>();
        Point firstPoint = new Point(0, 0);
        world.put(firstPoint, new RuntimeRoom(firstPoint, new RoomState(RoomTileState.CURRENT, RoomType.COMBAT)));
        assertTrue(worldDAO.insertWorld(SAVE_NAME, world));
        
        world.clear();
        Point newPoint = new Point(5, 3);
        world.put(newPoint, new RuntimeRoom(newPoint, new RoomState(RoomTileState.UNVISITED, RoomType.MINIBOSS)));
        assertTrue(worldDAO.updateMap(SAVE_NAME, world));
        
        Map<Point, RuntimeRoom> loadedWorld = worldDAO.loadSavedWorld(SAVE_NAME);
        assertEquals(1, loadedWorld.size());
        assertNull(loadedWorld.get(firstPoint));
        assertNotNull(loadedWorld.get(newPoint));
        assertEquals(RoomType.MINIBOSS, loadedWorld.get(newPoint).getRoomState().getRoomType());
    }
    
    @Test
    public void testDeleteWorld()
    {
        Map<Point, RuntimeRoom> world = new HashMap<>();
        Point point = new Point(0, 0);
        world.put(point,new RuntimeRoom(point, new RoomState(RoomTileState.CURRENT,RoomType.COMBAT)));
        assertTrue(worldDAO.insertWorld(SAVE_NAME, world));
        assertTrue(worldDAO.deleteWorld(SAVE_NAME));
        
        Map<Point, RuntimeRoom> loadedWorld = worldDAO.loadSavedWorld(SAVE_NAME);
        assertNotNull(loadedWorld);
        assertTrue(loadedWorld.isEmpty());
    }
    
    @After
    public void tearDown()
    {
        dbManager.closeSQLConnection();
    }
}
