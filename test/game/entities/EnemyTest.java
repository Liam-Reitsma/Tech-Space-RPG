package game.entities;

import static org.junit.Assert.*;

import org.junit.Test;

public class EnemyTest
{
    private static final float DELTA = 0.0001f;
    
    public EnemyTest()
    {
    
    }
    
    @Test
    public void testConstructorInitialisesAndScalesStats()
    {
        Enemy levelOneEnemy = new Enemy(EnemyType.BRUISER, 1);
        Enemy levelFiveEnemy = new Enemy(EnemyType.BRUISER, 5);
        
        String expectedName = "Bruiser";
        int expectedLevel = 1;
        float expectedHealth = 9.333333f;
        float expectedDamage = 2.222222f;
        int expectedGold = 2;
        int expectedXP = 6;
        
        assertEquals(expectedName, levelOneEnemy.getName());
        assertEquals(expectedLevel, levelOneEnemy.getLevel());
        assertEquals(expectedHealth, levelOneEnemy.getHealth(), DELTA);
        assertEquals(expectedDamage, levelOneEnemy.calculateAttackDamage(), DELTA);
        assertEquals(expectedGold, levelOneEnemy.getGold());
        assertEquals(expectedXP, levelOneEnemy.getXP());
        
        expectedLevel = 5;
        expectedHealth = 46.666668f;
        expectedDamage = 11.111111f;
        expectedGold = 11;
        expectedXP = 34;
        
        assertEquals(expectedName, levelFiveEnemy.getName());
        assertEquals(expectedLevel, levelFiveEnemy.getLevel());
        assertEquals(expectedHealth, levelFiveEnemy.getHealth(), DELTA);
        assertEquals(expectedDamage, levelFiveEnemy.calculateAttackDamage(), DELTA);
        assertEquals(expectedGold, levelFiveEnemy.getGold());
        assertEquals(expectedXP, levelFiveEnemy.getXP());
    }
    
    @Test
    public void testConstructorEnforcesMinimumLevel()
    {
        Enemy zeroLevelEnemy = new Enemy(EnemyType.BRUISER, 0);
        Enemy negativeLevelEnemy = new Enemy(EnemyType.BRUISER, -5);
        
        int expectedLevel = 1;
        float expectedHealth = 9.333333f;
        
        assertEquals(expectedLevel, zeroLevelEnemy.getLevel());
        assertEquals(expectedHealth, zeroLevelEnemy.getHealth(), DELTA);
        
        assertEquals(expectedLevel, negativeLevelEnemy.getLevel());
        assertEquals(expectedHealth, negativeLevelEnemy.getHealth(), DELTA);
    }
    
    @Test
    public void testArmourPenetration()
    {
        Enemy partialAP = new Enemy(EnemyType.BRUISER, 5);
        Enemy fullAP = new Enemy(EnemyType.BRUISER, 5);
        Enemy excessiveAP = new Enemy(EnemyType.BRUISER, 5);
        Enemy negativeAP = new Enemy(EnemyType.BRUISER, 5);
        
        float expectedHealth = 36.966668f;
        
        partialAP.applyDamage(10f, 0.02f);
        assertEquals(expectedHealth, partialAP.getHealth(), DELTA);
        
        expectedHealth = 36.666668f;
        
        fullAP.applyDamage(10f, 0.5f);
        assertEquals(expectedHealth, fullAP.getHealth(), DELTA);
        
        excessiveAP.applyDamage(10f, 100f);
        assertEquals(expectedHealth, excessiveAP.getHealth(), DELTA);
        
        expectedHealth = 46.666668f;
        
        negativeAP.applyDamage(10f, -100f);
        assertEquals(expectedHealth, negativeAP.getHealth(), DELTA);
    }
    
    @Test
    public void testDamage()
    {
        Enemy zeroDamage = new Enemy(EnemyType.BRUISER, 5);
        Enemy negativeDamage = new Enemy(EnemyType.BRUISER, 5);
        Enemy sequentialDamage = new Enemy(EnemyType.BRUISER, 5);
        
        float expectedHealth = 46.666668f;
        
        zeroDamage.applyDamage(0f, 0f);
        assertEquals(expectedHealth, zeroDamage.getHealth(), DELTA);
        
        negativeDamage.applyDamage(-10f, 0f);
        assertEquals(expectedHealth, negativeDamage.getHealth(), DELTA);
        
        expectedHealth = 32.416668f;
        
        sequentialDamage.applyDamage(10f, 0f);
        sequentialDamage.applyDamage(5f, 0f);
        assertEquals(expectedHealth, sequentialDamage.getHealth(), DELTA);
    }
    
    @Test
    public void testEdgeCases()
    {
        Enemy lethalDamage = new Enemy(EnemyType.BRUISER, 1);
        Enemy repeatedDamage = new Enemy(EnemyType.BRUISER, 1);
        
        float expectedHealth = 0f;
        
        lethalDamage.applyDamage(100f, 0f);
        assertEquals(expectedHealth, lethalDamage.getHealth(), DELTA);
        
        repeatedDamage.applyDamage(100f, 0f);
        repeatedDamage.applyDamage(10f, 0f);
        assertEquals(expectedHealth, repeatedDamage.getHealth(), DELTA);
    }
    
    @Test
    public void testArmour()
    {
        Enemy normalArmour = new Enemy(EnemyType.BRUISER, 5);
        Enemy cappedArmour = new Enemy(EnemyType.BRUISER, 100);
        
        float expectedHealth = 37.166668f;
        
        normalArmour.applyDamage(10f, 0f);
        assertEquals(expectedHealth, normalArmour.getHealth(), DELTA);
        
        expectedHealth = 903.333313f;
        
        cappedArmour.applyDamage(100f, 0f);
        assertEquals(expectedHealth, cappedArmour.getHealth(), DELTA);
    }
}