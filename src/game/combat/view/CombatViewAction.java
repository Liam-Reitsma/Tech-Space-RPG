package game.combat.view;

import game.items.PotionType;

public interface CombatViewAction
{
    void showAvailablePotions(PotionType[] potions);
    void displayMessage(String message);
    void setActionEnabled(boolean actionEnabled);
}
