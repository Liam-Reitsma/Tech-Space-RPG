package game.combat.controller;

import game.entities.Enemy;
import game.items.PotionType;

public interface CombatAction
{
    void startCombat(Enemy enemy);
    void requestAttack();
    void requestHeal(PotionType potionType);
    void requestRun();
}