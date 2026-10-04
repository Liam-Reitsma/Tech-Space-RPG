package game.newgame.controller;

import game.entities.player.ClassType;

public interface NewGameAction
{
    void requestOpenNewGameScreen();
    void requestCharacterChange(ClassType classType);
    void requestConfirmCreation(String name, ClassType classType);
    void requestCloseNewGameScreen();
}