package game.persistence.dao;

import game.player.model.ClassType;
import game.player.model.Inventory;
import game.player.model.Player;
import game.items.EquippableItem;
import game.items.Item;
import game.items.Potion;
import game.items.PotionType;
import game.items.armour.Armour;
import game.items.armour.ArmourMaterial;
import game.items.armour.ArmourSlot;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponType;
import game.persistence.DBManager;
import game.persistence.DatabaseSchemaManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.Map.Entry;

public class InventoryDAOTest
{
    private static final String SAVE_NAME = "Test Save";
    
    private DBManager dbManager;
    private InventoryDAO inventoryDAO;
    
    private Player testPlayer;
    
    public InventoryDAOTest()
    {
    
    }
    
    @Before
    public void setUp()
    {
        testPlayer = new Player("John", ClassType.MERCENARY);
        dbManager = new DBManager();
        inventoryDAO = new InventoryDAO(dbManager);
        DatabaseSchemaManager databaseSchemaManager = new DatabaseSchemaManager(dbManager);
        databaseSchemaManager.dropTables();
        databaseSchemaManager.createTables();
        PlayerDAO playerDAO = new PlayerDAO(dbManager);
        playerDAO.insertPlayer(SAVE_NAME, testPlayer);
    }

    @Test
    public void testInsertAndLoadEquippedItems()
    {
        assertTrue(inventoryDAO.insertEquippedItems(SAVE_NAME, testPlayer));
        EquippableItem[] equippedItems = inventoryDAO.loadEquippedItems(SAVE_NAME);
        
        assertNotNull(equippedItems);
        assertTrue(equippedItems.length > 0);
    }
    
    @Test
    public void testLoadEmptyInventory()
    {
        Map<Item, Integer> inventory = inventoryDAO.loadInventory(SAVE_NAME);
        assertNotNull(inventory);
        assertTrue(inventory.isEmpty());
    }
    
    @Test
    public void testAddAndRemoveStoredItems()
    {
        Item item = new Weapon(WeaponType.ENERGY_SWORD, 5);
        assertTrue(inventoryDAO.addItem(SAVE_NAME, item, testPlayer.getInventory().getItemCount(item)));
        testPlayer.addToInventory(item);
        Map<Item, Integer> inventory = inventoryDAO.loadInventory(SAVE_NAME);
        assertEquals(1, (int)inventory.get(item));
        
        assertTrue(inventoryDAO.addItem(SAVE_NAME, item, testPlayer.getInventory().getItemCount(item)));
        testPlayer.addToInventory(item);
        inventory = inventoryDAO.loadInventory(SAVE_NAME);
        assertEquals(2, (int)inventory.get(item));
        
        assertTrue(inventoryDAO.removeItem(SAVE_NAME, item, testPlayer.getInventory().getItemCount(item)));
        testPlayer.removeFromInventory(item, false);
        inventory = inventoryDAO.loadInventory(SAVE_NAME);
        assertEquals(1, (int)inventory.get(item));
        
        item = new Armour(ArmourMaterial.FABRIC, ArmourSlot.HEAD);
        assertFalse(inventoryDAO.removeItem(SAVE_NAME, item, testPlayer.getInventory().getItemCount(item)));
        testPlayer.removeFromInventory(item, false);
        
        assertTrue(inventoryDAO.addItem(SAVE_NAME, item, testPlayer.getInventory().getItemCount(item)));
        testPlayer.addToInventory(item);
        inventory = inventoryDAO.loadInventory(SAVE_NAME);
        assertEquals(2, inventory.size());
    }
    
    @Test
    public void testUpdateEquippedAndLoadItems()
    {
        assertTrue(inventoryDAO.insertEquippedItems(SAVE_NAME, testPlayer));
        assertTrue(inventoryDAO.updateEquippedEquipment(SAVE_NAME, testPlayer.getInventory()));
        testPlayer.equipItem(new Armour(ArmourMaterial.FABRIC, ArmourSlot.BOOTS));
        assertTrue(inventoryDAO.updateEquippedEquipment(SAVE_NAME, testPlayer.getInventory()));
        
        EquippableItem[] equippedItems = inventoryDAO.loadEquippedItems(SAVE_NAME);
        assertNotNull(equippedItems);
        assertEquals(2, equippedItems.length);
    }
    
    @Test
    public void testUpdateStoredEquipmentAndLoadInventory()
    {
        Item[] items = new Item[]{new Potion(PotionType.SMALL_HEALTH), new Potion(PotionType.SMALL_HEALTH), new Armour(ArmourMaterial.FABRIC, ArmourSlot.BOOTS), new Armour(ArmourMaterial.FABRIC, ArmourSlot.SHINS), new Weapon(WeaponType.GRAVITY_CONDUIT, 5)};
        for(Item item : items)
            testPlayer.addToInventory(item);
        
        assertTrue(inventoryDAO.updateStoredEquipment(SAVE_NAME, testPlayer.getInventory()));
        Map<Item, Integer> inventory = inventoryDAO.loadInventory(SAVE_NAME);
        
        Inventory playerInventory = testPlayer.getInventory();
        
        assertEquals(playerInventory.getItemsInInventory().length, inventory.size());
        for(Entry<Item, Integer> inventoryEntry : inventory.entrySet())
            assertEquals(playerInventory.getItemCount(inventoryEntry.getKey()), (int)inventoryEntry.getValue());
    }
    
    @After
    public void tearDown()
    {
        dbManager.closeSQLConnection();
    }
}