package game.newgame.controller;

import game.player.model.ClassType;

public interface NewGameAction
{
    void requestOpenNewGameScreen();
    void requestCharacterChange(ClassType classType);
    void requestConfirmCreation(String name, ClassType classType);
    void requestMainMenuScreen();
}