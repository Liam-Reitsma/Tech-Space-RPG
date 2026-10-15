package game.persistence.dao;

import game.player.model.ClassType;
import game.player.model.Player;
import game.persistence.DBManager;
import game.persistence.DatabaseSchemaManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PlayerDAOTest
{
    private DBManager dbManager;
    private PlayerDAO playerDAO;
    
    public PlayerDAOTest()
    {
    
    }
    
    @Before
    public void setUp()
    {
        dbManager = new DBManager();
        playerDAO = new PlayerDAO(dbManager);
        DatabaseSchemaManager databaseSchemaManager = new DatabaseSchemaManager(dbManager);
        databaseSchemaManager.dropTables();
        databaseSchemaManager.createTables();
    }

    @Test
    public void testInsertPlayerAndLoadPlayer()
    {
        String saveName = "Test Save";
        Player player = new Player("John", ClassType.GUARDIAN);
        float delta = 0.0001f;
        assertTrue(playerDAO.insertPlayer(saveName, player));
        Player loadedPlayer = playerDAO.loadSavedPlayer(saveName);
        
        assertNotNull(loadedPlayer);
        assertEquals(player.getName(), loadedPlayer.getName());
        assertEquals(player.getLevel(), loadedPlayer.getLevel());
        assertEquals(player.getHealth(), loadedPlayer.getHealth(), delta);
        assertEquals(player.getXP(), loadedPlayer.getXP());
        assertEquals(player.getGold(), loadedPlayer.getGold());
        assertEquals(player.getClassType(), loadedPlayer.getClassType());
        assertEquals(player.getPoint().getX(), loadedPlayer.getPoint().getX());
        assertEquals(player.getPoint().getY(), loadedPlayer.getPoint().getY());
    }
    
    @Test
    public void testInsertPlayerAndLoadPlayerDuplicatePrimaryKey()
    {
        String saveName = "Test Save";
        Player playerOne = new Player("Jack", ClassType.SAGE);
        Player playerTwo = new Player("John", ClassType.GUARDIAN);
        
        float delta = 0.0001f;
        assertTrue(playerDAO.insertPlayer(saveName, playerOne));
        assertFalse(playerDAO.insertPlayer(saveName, playerTwo));
        Player loadedPlayer = playerDAO.loadSavedPlayer(saveName);
        
        assertNotNull(loadedPlayer);
        assertEquals(playerOne.getName(), loadedPlayer.getName());
        assertEquals(playerOne.getLevel(), loadedPlayer.getLevel());
        assertEquals(playerOne.getHealth(), loadedPlayer.getHealth(), delta);
        assertEquals(playerOne.getXP(), loadedPlayer.getXP());
        assertEquals(playerOne.getGold(), loadedPlayer.getGold());
        assertEquals(playerOne.getClassType(), loadedPlayer.getClassType());
        assertEquals(playerOne.getPoint().getX(), loadedPlayer.getPoint().getX());
        assertEquals(playerOne.getPoint().getY(), loadedPlayer.getPoint().getY());
    }

    @Test
    public void testUpdatePlayerAndLoadPlayer()
    {
        String saveName = "Test Save";
        Player playerOne = new Player("Jack", ClassType.SAGE);
        Player playerTwo = new Player("John", ClassType.GUARDIAN);
        
        float delta = 0.0001f;
        assertTrue(playerDAO.insertPlayer(saveName, playerOne));
        assertTrue(playerDAO.updatePlayer(saveName, playerTwo));
        Player loadedPlayer = playerDAO.loadSavedPlayer(saveName);
        
        assertNotNull(loadedPlayer);
        assertEquals(playerTwo.getName(), loadedPlayer.getName());
        assertEquals(playerTwo.getLevel(), loadedPlayer.getLevel());
        assertEquals(playerTwo.getHealth(), loadedPlayer.getHealth(), delta);
        assertEquals(playerTwo.getXP(), loadedPlayer.getXP());
        assertEquals(playerTwo.getGold(), loadedPlayer.getGold());
        assertEquals(playerTwo.getClassType(), loadedPlayer.getClassType());
        assertEquals(playerTwo.getPoint().getX(), loadedPlayer.getPoint().getX());
        assertEquals(playerTwo.getPoint().getY(), loadedPlayer.getPoint().getY());
    }

    @Test
    public void testDeletePlayerAndLoadPlayer()
    {
        String saveName = "Test Save";
        Player playerOne = new Player("Jack", ClassType.SAGE);
        
        assertTrue(playerDAO.insertPlayer(saveName, playerOne));
        assertTrue(playerDAO.deletePlayer(saveName));
        Player loadedPlayer = playerDAO.loadSavedPlayer(saveName);
        
        assertNull(loadedPlayer);
    }

    @Test
    public void testGetAllSaves()
    {
        String saveName1 = "saveOne";
        String saveName2 = "saveTwo";
        String saveName3 = "saveThree";
        
        Player player1 = new Player("John", ClassType.GUARDIAN);
        Player player2 = new Player("Jack", ClassType.SAGE);
        Player player3 = new Player("Jill", ClassType.POWERTECH);
        
        assertTrue(playerDAO.insertPlayer(saveName1, player1));
        assertTrue(playerDAO.insertPlayer(saveName2, player2));
        assertTrue(playerDAO.insertPlayer(saveName3, player3));
        
        Map<String, Player> loadedPlayers = playerDAO.getAllSaves();
        assertNotNull(loadedPlayers);
        assertEquals(3, loadedPlayers.size());
        
        Set<String> loadedNames = new HashSet<>();
        
        for(Entry<String, Player> player : loadedPlayers.entrySet())
            loadedNames.add(player.getKey());
        
        assertTrue(loadedNames.contains(saveName1));
        assertTrue(loadedNames.contains(saveName2));
        assertTrue(loadedNames.contains(saveName3));
    }

    @After
    public void tearDown()
    {
        dbManager.closeSQLConnection();
    }
}