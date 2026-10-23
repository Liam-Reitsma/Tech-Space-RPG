package game.persistence.dao;

import static org.junit.Assert.*;

import game.player.model.ClassType;
import game.player.model.InventoryResult;
import game.player.model.ItemStack;
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
import org.junit.Test;

public class InventoryDAOTest
{
    private static final String SAVE_NAME = "Test Save";
    
    private final DBManager dbManager;
    private final InventoryDAO inventoryDAO;
    private final Player testPlayer;
    
    public InventoryDAOTest()
    {
        dbManager = new DBManager("jdbc:derby:database/TechSpaceRPG_DB;create=true", "TSDB", "tsdb");
        inventoryDAO = new InventoryDAO(dbManager);
        testPlayer = new Player("John", ClassType.MERCENARY);
        
        DatabaseSchemaManager databaseSchemaManager = new DatabaseSchemaManager(dbManager);
        assertTrue(databaseSchemaManager.dropTables());
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
        ItemStack[] inventory = inventoryDAO.loadInventory(SAVE_NAME);
        
        assertNotNull(inventory);
        
        for(ItemStack stack : inventory)
            assertTrue(stack == null || stack.getItem() == null);
    }
    
    @Test
    public void testAddAndRemoveStoredItems()
    {
        Item item = new Weapon(WeaponType.ENERGY_SWORD, 5);
        
        testPlayer.addToInventory(item);
        assertTrue(inventoryDAO.addItem(SAVE_NAME, item, testPlayer.getInventory().getItemCount(item) - 1, testPlayer.getInventory().getItemIndex(item)));
        assertEquals(1, getItemCount(inventoryDAO.loadInventory(SAVE_NAME), item));
        
        testPlayer.addToInventory(item);
        assertTrue(inventoryDAO.addItem(SAVE_NAME, item, testPlayer.getInventory().getItemCount(item) - 1, testPlayer.getInventory().getItemIndex(item)));
        assertEquals(2, getItemCount(inventoryDAO.loadInventory(SAVE_NAME), item));
        
        testPlayer.removeFromInventory(item, false);
        assertTrue(inventoryDAO.removeItem(SAVE_NAME, item, testPlayer.getInventory().getItemCount(item) + 1, testPlayer.getInventory().getItemIndex(item)));
        assertEquals(1, getItemCount(inventoryDAO.loadInventory(SAVE_NAME), item));
        
        item = new Armour(ArmourMaterial.FABRIC, ArmourSlot.HEAD);
        
        testPlayer.removeFromInventory(item, false);
        assertFalse(inventoryDAO.removeItem(SAVE_NAME, item, testPlayer.getInventory().getItemCount(item) + 1, testPlayer.getInventory().getItemIndex(item)));
        
        testPlayer.addToInventory(item);
        assertTrue(inventoryDAO.addItem(SAVE_NAME, item, testPlayer.getInventory().getItemCount(item) - 1, testPlayer.getInventory().getItemIndex(item)));
        
        ItemStack[] inventory = inventoryDAO.loadInventory(SAVE_NAME);
        assertEquals(2, countOccupiedSlots(inventory));
    }
    
    private int getItemCount(ItemStack[] inventory, Item item)
    {
        for(ItemStack stack : inventory)
            if(stack != null && stack.getItem() != null && stack.getItem().equals(item))
                return stack.getQuantity();
        
        return 0;
    }
    
    private int countOccupiedSlots(ItemStack[] inventory)
    {
        int count = 0;
        
        for(ItemStack stack : inventory)
            if(stack != null && stack.getItem() != null)
                count++;
        
        return count;
    }
    
    @Test
    public void testUpdateEquippedAndLoadItems()
    {
        assertTrue(inventoryDAO.insertEquippedItems(SAVE_NAME, testPlayer));
        assertTrue(inventoryDAO.updateEquippedEquipment(SAVE_NAME, testPlayer.getInventory()));
        Armour armour =new Armour(ArmourMaterial.FABRIC, ArmourSlot.BOOTS);
        testPlayer.addToInventory(armour);
        assertEquals(InventoryResult.SUCCESS, testPlayer.equipItem(armour, armour.getEquipSlot()));
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
        
        ItemStack[] inventory = inventoryDAO.loadInventory(SAVE_NAME);
        ItemStack[] playerInventory = testPlayer.getInventory().getInventorySlots();
        
        assertEquals(playerInventory.length, inventory.length);
        
        for(int i = 0; i < inventory.length; i++)
        {
            assertEquals(playerInventory[i].getItem(), inventory[i].getItem());
            assertEquals(playerInventory[i].getQuantity(), inventory[i].getQuantity());
        }
    }
    
    @After
    public void tearDown()
    {
        dbManager.closeConnection();
    }
}