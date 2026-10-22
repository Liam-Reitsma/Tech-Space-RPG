package game.items.weapon;

import static org.junit.Assert.*;

import org.junit.Test;

public class WeaponTypeTest
{
    private static final float DELTA = 0.0001f;
    
    public WeaponTypeTest()
    {
    
    }
    
    @Test
    public void testCalculateDamage()
    {
        float expectedDamage = 8.85f;
        
        assertEquals(expectedDamage, WeaponType.RUSTED_KNIFE.calculateDamage(10, 1), DELTA);
        
        expectedDamage = 15.85f;
        
        assertEquals(expectedDamage, WeaponType.ENERGY_SWORD.calculateDamage(12, 1), DELTA);
        
        expectedDamage = 17.65f;
        
        assertEquals(expectedDamage, WeaponType.ENERGY_SWORD.calculateDamage(12, 5), DELTA);
        
        expectedDamage = 29.35f;
        
        assertEquals(expectedDamage, WeaponType.GRAVITY_CONDUIT.calculateDamage(15, 11), DELTA);
    }
    
    @Test
    public void testCalculateStatRequirement()
    {
        int expectedRequirement = 0;
        
        assertEquals(expectedRequirement, WeaponType.RUSTED_KNIFE.calculateStatReq(1));
        
        expectedRequirement = 3;
        
        assertEquals(expectedRequirement, WeaponType.ENERGY_SWORD.calculateStatReq(5));
        
        expectedRequirement = 13;
        
        assertEquals(expectedRequirement, WeaponType.PLASMA_CLEAVER.calculateStatReq(5));
        
        expectedRequirement = 8;
        
        assertEquals(expectedRequirement, WeaponType.GRAVITY_CONDUIT.calculateStatReq(11));
    }
    
    @Test
    public void testCanUseWeapon()
    {
        boolean expectedResult = false;
        
        assertEquals(expectedResult, WeaponType.SHOCK_HAMMER.canUseWeapon(5, 6));
        
        expectedResult = true;
        
        assertEquals(expectedResult, WeaponType.SHOCK_HAMMER.canUseWeapon(6, 6));
        assertEquals(expectedResult, WeaponType.SHOCK_HAMMER.canUseWeapon(10, 6));
        
        expectedResult = false;
        
        assertEquals(expectedResult, WeaponType.PLASMA_CLEAVER.canUseWeapon(12, 5));
        
        expectedResult = true;
        
        assertEquals(expectedResult, WeaponType.PLASMA_CLEAVER.canUseWeapon(13, 5));
    }
    
    @Test
    public void testGetRandomWeaponType()
    {
        WeaponType[] expectedTypes = WeaponType.values();
        
        for(int i = 0; i < 100; i++)
        {
            WeaponType weaponType = WeaponType.getRandomWeaponType();
            
            assertNotNull(weaponType);
            assertTrue(java.util.Arrays.asList(expectedTypes).contains(weaponType));
        }
    }
}