package game.audio;

public enum SoundEffect
{
    BUTTON_CLICK_SOUNDBITE("resources/audio/soundfx/Button Click.wav"),
    NEW_GAME_SOUNDBITE("resources/audio/soundfx/New Game Screen Sound.wav"),
    
    MAIN_GAME_BACKSTORY_SOUNDBITE("resources/audio/soundfx/Intro To Backstory.wav"),
    DYING_SOUNDBITE("resources/audio/soundfx/Dying.wav");
    
    private final String urlPath;
    
    SoundEffect(String URL)
    {
        urlPath = URL;
    }
    
    public String getUrlPath() { return urlPath; }
}