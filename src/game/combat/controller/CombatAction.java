package game.combat.controller;

import game.entities.Enemy;
import game.items.PotionType;

public interface CombatAction
{
    void requestStartCombat(Enemy enemy);
    void requestResetCombat();
    
    void requestAttack();
    void requestHeal(PotionType potionType);
    void requestRun();
    
    void requestDeathMessage();
}