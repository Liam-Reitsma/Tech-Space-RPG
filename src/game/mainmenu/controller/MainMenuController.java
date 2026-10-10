package game.mainmenu.controller;

import static game.frame.controller.DisplayTarget.*;

import game.audio.AudioManager;
import game.audio.MusicTrack;
import game.audio.SoundEffect;
import game.frame.controller.DisplayTarget;
import game.frame.controller.FrameAction;

public class MainMenuController implements MainMenuAction
{
    private final AudioManager audioManager;
    
    private final FrameAction frameAction;
    
    public MainMenuController(FrameAction fAction)
    {
        audioManager = AudioManager.getInstance();
        
        frameAction = fAction;
    }
    
    @Override
    public void requestOpenMainMenu()
    {
        audioManager.playSound(SoundEffect.MAIN_MENU_SOUNDBITE);
        audioManager.playMusic(MusicTrack.MAIN_MENU_AMBIANCE);
    }
    
    @Override
    public void requestOpenDisplay(DisplayTarget displayTarget)
    {
        frameAction.requestDisplay(displayTarget);
    }
}