package game.combat.controller;

import game.entities.Enemy;
import game.items.Potion;

public interface CombatAction
{
    void requestAttackEnemy();
    void requestAttackPlayer();
    void requestHeal();
    void requestRun();
}