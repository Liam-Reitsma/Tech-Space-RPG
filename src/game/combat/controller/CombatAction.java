package game.combat.controller;

import game.items.PotionType;

public interface CombatAction
{
    void requestAttack();
    void requestHeal(PotionType potionType);
    void requestRun();
}