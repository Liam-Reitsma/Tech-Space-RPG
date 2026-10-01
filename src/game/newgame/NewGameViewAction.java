package game.newgame;

import game.entities.player.ClassType;

public interface NewGameViewAction
{
    void changeCharacterPanel(ClassType classType);
    void openMainMenuScreen();
}