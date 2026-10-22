package game.items.armour;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArmourMaterialTest
{
    private static final float DELTA = 0.0001f;
    
    public ArmourMaterialTest()
    {
    
    }
    
    @Test
    public void testMaterialProperties()
    {
        int expectedLevel = 1;
        float expectedProtection = 0.15f;
        
        assertEquals(expectedLevel, ArmourMaterial.FABRIC.getRequiredLevel());
        assertEquals(expectedProtection, ArmourMaterial.FABRIC.getProtValue(), DELTA);
        
        expectedLevel = 10;
        expectedProtection = 0.45f;
        
        assertEquals(expectedLevel, ArmourMaterial.TITANIUM.getRequiredLevel());
        assertEquals(expectedProtection, ArmourMaterial.TITANIUM.getProtValue(), DELTA);
        
        expectedLevel = 35;
        expectedProtection = 0.75f;
        
        assertEquals(expectedLevel, ArmourMaterial.NANOWEAVE.getRequiredLevel());
        assertEquals(expectedProtection, ArmourMaterial.NANOWEAVE.getProtValue(), DELTA);
    }
    
    @Test
    public void testRandomMaterialRespectsLevelRequirements()
    {
        for(int i = 0; i < 100; i++)
        {
            ArmourMaterial material = ArmourMaterial.getRandomMaterial(5);
            
            assertTrue(material == ArmourMaterial.FABRIC
                    || material == ArmourMaterial.POLYMER);
            
            material = ArmourMaterial.getRandomMaterial(10);
            
            assertTrue(material == ArmourMaterial.FABRIC
                    || material == ArmourMaterial.POLYMER
                    || material == ArmourMaterial.TITANIUM);
            
            material = ArmourMaterial.getRandomMaterial(20);
            
            assertNotNull(material);
            assertNotEquals(ArmourMaterial.NANOWEAVE, material);
            
            material = ArmourMaterial.getRandomMaterial(35);
            
            assertNotNull(material);
        }
    }
    
    @Test
    public void testRandomMaterialBelowMinimumLevel()
    {
        ArmourMaterial expectedMaterial = ArmourMaterial.FABRIC;
        
        assertEquals(expectedMaterial, ArmourMaterial.getRandomMaterial(0));
        assertEquals(expectedMaterial, ArmourMaterial.getRandomMaterial(-5));
        assertEquals(expectedMaterial, ArmourMaterial.getRandomMaterial(1));
        assertEquals(expectedMaterial, ArmourMaterial.getRandomMaterial(4));
    }
}