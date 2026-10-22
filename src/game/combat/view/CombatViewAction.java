package game.combat.view;

import game.items.PotionType;

public interface CombatViewAction
{
    void clearMessage();
    void displayMessage(String message);
    void setActionEnabled(boolean actionEnabled);
    void showAvailablePotions(PotionType[] potions);
}