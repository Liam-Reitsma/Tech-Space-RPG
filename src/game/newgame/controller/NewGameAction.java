package game.newgame.controller;

import game.entities.player.ClassType;
import game.gui.config.Displayable;

public interface NewGameAction
{
    void requestOpenNewGameScreen();
    void requestCharacterChange(ClassType classType);
    void requestConfirmCreation(String name, ClassType classType);
}