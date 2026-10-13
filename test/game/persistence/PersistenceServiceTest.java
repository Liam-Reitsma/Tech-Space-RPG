package game.persistence;

import game.player.model.ClassType;
import game.player.model.Player;
import game.items.Item;
import game.items.Potion;
import game.items.PotionType;
import game.persistence.dao.InventoryDAO;
import game.persistence.dao.PlayerDAO;
import game.persistence.dao.WorldDAO;
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

public class PersistenceServiceTest
{
    private static final String SAVE_NAME = "Test Save";
    
    private DBManager dbManager;
    private PersistenceService persistenceService;
    
    public PersistenceServiceTest()
    {
    
    }
    
    @Before
    public void setUp()
    {
        dbManager = new DBManager();
        InventoryDAO inventoryDAO = new InventoryDAO(dbManager);
        PlayerDAO playerDAO = new PlayerDAO(dbManager);
        WorldDAO worldDAO = new WorldDAO(dbManager);
        DatabaseSchemaManager databaseSchemaManager = new DatabaseSchemaManager(dbManager);
        databaseSchemaManager.dropTables();
        databaseSchemaManager.createTables();
        
        persistenceService = new PersistenceService(databaseSchemaManager, inventoryDAO, playerDAO, worldDAO);
    }
    
    private Map<Point, RuntimeRoom> createTestWorld()
    {
        Map<Point, RuntimeRoom> world = new HashMap<>();
        Point point = new Point(0, 0);
        RoomState state = new RoomState(RoomTileState.CURRENT, RoomType.COMBAT);
        RuntimeRoom room = new RuntimeRoom(point, state);
        world.put(point, room);
        
        return world;
    }
    
    @Test
    public void testCreateAndLoadSaveGame()
    {
        Map<Point, RuntimeRoom> world = createTestWorld();
        assertTrue(persistenceService.createNewSaveGame(SAVE_NAME, "John", ClassType.SCAVENGER, world));
        
        SavedData savedData = persistenceService.loadSaveGame(SAVE_NAME);
        assertNotNull(savedData);
        assertEquals("John", savedData.player.getName());
        assertEquals(ClassType.SCAVENGER, savedData.player.getClassType());
        assertNotNull(savedData.equipped);
        assertNotNull(savedData.inventory);
        assertNotNull(savedData.world);
        assertEquals(1, savedData.world.size());
    }
    
    @Test
    public void testFullSaveAndReload()
    {
        Map<Point, RuntimeRoom> world = createTestWorld();
        assertTrue(persistenceService.createNewSaveGame(SAVE_NAME, "John", ClassType.SHADOW, world));
        
        SavedData savedData = persistenceService.loadSaveGame(SAVE_NAME);
        Player player = savedData.player;
        Item potion = new Potion(PotionType.SMALL_HEALTH);
        player.addToInventory(potion);
        RuntimeRoom room = world.get(new Point(0, 0));
        room.setCompleted();
        assertTrue(persistenceService.fullSave(player, world));
        
        SavedData loadedData = persistenceService.loadSaveGame(SAVE_NAME);
        
        assertNotNull(loadedData);
        assertEquals(1, (int) loadedData.inventory.get(potion));
        
        assertEquals(RoomTileState.COMPLETED, loadedData.world.get(new Point(0, 0)).getRoomState().getRoomTileState());
    }
    
    @Test
    public void testDeleteSaveGame()
    {
        Map<Point, RuntimeRoom> world = createTestWorld();
        assertTrue(persistenceService.createNewSaveGame(SAVE_NAME,"John",ClassType.SCOUNDREL, world));
        assertNotNull(persistenceService.loadSaveGame(SAVE_NAME));
        assertTrue(persistenceService.deleteSaveGame(SAVE_NAME));
        assertNull(persistenceService.loadSaveGame(SAVE_NAME));
    }
    
    @After
    public void tearDown()
    {
        dbManager.closeSQLConnection();
    }
}