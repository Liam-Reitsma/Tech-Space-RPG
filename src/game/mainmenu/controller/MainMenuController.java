package game.mainmenu.controller;

import static game.frame.controller.DisplayTarget.*;

import game.audio.AudioManager;
import game.audio.MusicTrack;
import game.audio.SoundEffect;
import game.frame.controller.FrameAction;
import game.newgame.controller.NewGameAction;
import game.options.controller.OptionsAction;
import game.saves.controller.SavesAction;

public class MainMenuController implements MainMenuAction
{
    private final AudioManager audioManager;
    
    private final FrameAction frameAction;
    private final NewGameAction newGameAction;
    private final OptionsAction optionsAction;
    private final SavesAction savesAction;
    
    public MainMenuController(FrameAction fAction, NewGameAction ngAction, OptionsAction oAction, SavesAction sAction)
    {
        audioManager = AudioManager.getInstance();
        
        frameAction = fAction;
        newGameAction = ngAction;
        optionsAction = oAction;
        savesAction = sAction;
    }
    
    @Override
    public void requestOpenMainMenu()
    {
        frameAction.requestDisplay(MAIN_MENU);
        audioManager.playSound(SoundEffect.MAIN_MENU_SOUNDBITE);
        audioManager.playMusic(MusicTrack.MAIN_MENU_AMBIANCE);
    }
    
    @Override
    public void requestOpenDisplay(MainMenuOption mainMenuOption)
    {
        switch(mainMenuOption)
        {
            case NEW_GAME -> newGameAction.requestOpenNewGameScreen();
            case LOAD_GAME -> savesAction.requestOpenSavesOverlay();
            case OPTIONS -> optionsAction.requestOpenOptionsOverlay();
            case EXIT_GAME -> frameAction.requestCloseGame();
        }
    }
}