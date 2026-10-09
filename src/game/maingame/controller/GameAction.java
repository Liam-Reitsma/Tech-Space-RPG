package game.maingame.controller;

import game.entities.player.Player;
import game.persistence.SavedData;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.Map;

public interface GameAction
{
    void requestOpenGameScreen();
    void requestNewGame(Player newPlayer);
    void requestLoadGame(SavedData savedData);
    void requestGameUpdate();
}