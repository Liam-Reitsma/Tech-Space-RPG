package game.newgame.controller;

import static game.frame.controller.DisplayTarget.*;

import game.audio.AudioManager;
import game.audio.MusicTrack;
import game.audio.SoundEffect;
import game.frame.controller.FrameAction;
import game.maingame.controller.GameAction;
import game.entities.player.ClassType;
import game.entities.player.Player;
import game.newgame.view.NewGameViewAction;

public class NewGameController implements NewGameAction
{
    private final AudioManager audioManager;
    
    private final FrameAction frameAction;
    private final GameAction gameAction;
    
    private NewGameViewAction newGameViewAction;
    
    public NewGameController(FrameAction fAction, GameAction gAction)
    {
        audioManager = AudioManager.getInstance();
        
        frameAction = fAction;
        gameAction = gAction;
    }
    
    public void setNewGameViewAction(NewGameViewAction ngVAction) { newGameViewAction = ngVAction; }
    
    @Override
    public void requestOpenNewGameScreen()
    {
        frameAction.display(NEW_GAME);
        audioManager.playMusic(MusicTrack.NEW_GAME_AMBIANCE);
    }
    
    @Override
    public void requestCharacterChange(ClassType classType)
    {
        newGameViewAction.changeCharacterPanel(classType);
    }
    
    @Override
    public void requestConfirmCreation(String name, ClassType classType)
    {
        gameAction.requestNewGame(new Player(name, classType));
    }
    
    @Override
    public void requestMainMenuScreen()
    {
        frameAction.display(MAIN_MENU);
    }
}