package game.player.model;

import static game.entities.StatType.*;
import static org.junit.Assert.*;

import game.items.EquippableItem;
import game.items.armour.Armour;
import game.items.armour.ArmourMaterial;
import game.items.armour.ArmourSlot;
import game.items.weapon.Weapon;
import org.junit.Test;

public class PlayerTest
{
    private final float delta;
    private final Player player;
    
    public PlayerTest()
    {
        delta = 0.0001f;
        player = new Player("Test Name", ClassType.GUARDIAN);
    }
    
    @Test
    public void testApplyXPGainNoLevelUp()
    {
        assertEquals(0, player.applyXPGain(14));
        assertEquals(14, player.getXP());
    }
    
    @Test
    public void testApplyXPGainSingleLevelUp()
    {
        assertEquals(1, player.applyXPGain(15));
        assertEquals(0, player.getXP());
        assertEquals(36, player.getXpToLevelUp());
    }
    
    @Test
    public void testApplyXPGainMultipleLevelUps()
    {
        assertEquals(2, player.applyXPGain(51));
        assertEquals(0, player.getXP());
        assertEquals(59, player.getXpToLevelUp());
    }
    
    @Test
    public void testApplyXPGainWithRemainingXP()
    {
        assertEquals(1, player.applyXPGain(20));
        assertEquals(5, player.getXP());
        assertEquals(36, player.getXpToLevelUp());
    }
    
    @Test
    public void testApplyDamageNoDamage()
    {
        float originalHealth = player.getHealth();
        
        player.applyDamage(0, 0);
        assertEquals(originalHealth, player.getHealth(), delta);
        
        player.applyDamage(-10, 0);
        assertEquals(originalHealth, player.getHealth(), delta);
    }
    
    @Test
    public void testApplyDamageNormalDamage()
    {
        float originalHealth = player.getHealth();
        
        player.applyDamage(20, 0);
        assertEquals(originalHealth - 20, player.getHealth(), delta);
    }
    
    @Test
    public void testApplyDamageWithArmour()
    {
        Armour armour = new Armour(ArmourMaterial.TITANIUM, ArmourSlot.CHEST);
        player.setEquipped(new EquippableItem[]{armour});
        
        player.applyDamage(20, 0);
        assertEquals(108.6324f, player.getHealth(), delta);
    }
    
    @Test
    public void testApplyDamageWithArmourPenetration()
    {
        Armour armour = new Armour(ArmourMaterial.TITANIUM, ArmourSlot.CHEST);
        player.setEquipped(new EquippableItem[]{armour});
        
        player.applyDamage(20, 0.10f);
        assertEquals(106.6324f, player.getHealth(), delta);
    }
    
    @Test
    public void testApplyDamageWithFullArmourPenetration()
    {
        Armour armour = new Armour(ArmourMaterial.TITANIUM, ArmourSlot.CHEST);
        player.setEquipped(new EquippableItem[]{armour});
        
        player.applyDamage(20, 1f);
        assertEquals(105f, player.getHealth(), delta);
    }
    
    @Test
    public void testApplyDamageLethalDamage()
    {
        player.applyDamage(1000, 0);
        assertEquals(0f, player.getHealth(), delta);
    }
    
    @Test
    public void testGetProtectionValueNoArmour()
    {
        assertEquals(0f, player.getProtectionValue(), delta);
    }
    
    @Test
    public void testGetProtectionValueSingleArmour()
    {
        Armour armour = new Armour(ArmourMaterial.TITANIUM, ArmourSlot.CHEST);
        
        player.setEquipped(new EquippableItem[]{armour});
        assertEquals(0.18162f, player.getProtectionValue(), delta);
    }
    
    @Test
    public void testGetProtectionValueMultipleArmour()
    {
        Armour chest = new Armour(ArmourMaterial.TITANIUM, ArmourSlot.CHEST);
        Armour head = new Armour(ArmourMaterial.TITANIUM, ArmourSlot.HEAD);
        
        player.setEquipped(new EquippableItem[]{chest, head});
        assertEquals(0.2559371f, player.getProtectionValue(), delta);
    }
    
    @Test
    public void testCalculateAttackDamageUnarmed()
    {
        Weapon weapon = player.getInventory().getEquippedWeapon();
        assertEquals(InventoryResult.SUCCESS, player.unequipItem(weapon, 0));
        
        assertEquals(5f, player.calculateAttackDamage(), delta);
    }
    
    @Test
    public void testCalculateAttackDamageWithWeapon()
    {
        assertEquals(12.9286f, player.calculateAttackDamage(), delta);
    }
    
    @Test
    public void testApplyStatGainStrength()
    {
        player.applyDamage(62.5f, 0); // 50% health
        
        player.applyStatGain(STR);
        
        assertEquals(6, player.getSTR());
        assertEquals(130f, player.getMaximumHealth(), delta);
        assertEquals(65f, player.getHealth(), delta);
    }
    
    @Test
    public void testApplyStatGainDexterity()
    {
        player.applyStatGain(DEX);
        
        assertEquals(4, player.getDEX());
        assertEquals(0.020f, player.getArmourPenetration(), delta);
    }
    
    @Test
    public void testApplyStatGainIntelligence()
    {
        player.applyStatGain(INT);
        
        assertEquals(4, player.getINT());
        assertEquals(1.024f, player.getDamageAmplification(), delta);
        assertEquals(1.012f, player.getDamageReduction(), delta);
    }
}