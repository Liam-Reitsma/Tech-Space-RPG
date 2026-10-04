package game.newgame.view;

import game.entities.player.ClassType;

public interface NewGameViewAction
{
    void openNewGameScreen();
    void changeCharacterPanel(ClassType classType);
    void closeNewGameScreen();
}