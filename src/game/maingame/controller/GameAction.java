package game.maingame.controller;

import game.player.model.Player;
import game.persistence.SavedData;

public interface GameAction
{
    void requestOpenGameScreen();
    void requestNewGame(Player newPlayer);
    void requestLoadGame(SavedData savedData);
    void requestGameUpdate();
}