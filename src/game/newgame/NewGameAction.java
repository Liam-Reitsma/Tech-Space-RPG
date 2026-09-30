package game.newgame;

import game.entities.player.ClassType;

public interface NewGameAction
{
    void requestGoBack();
    void requestCharacterChange();
    void requestConfirmCreation(String name, ClassType classType);
}