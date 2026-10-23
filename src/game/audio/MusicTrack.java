package game.audio;

public enum MusicTrack
{
    MAIN_GAME_AMBIANCE("resources/audio/ambiance/Main Game.wav"),
    MAIN_MENU_AMBIANCE("resources/audio/ambiance/Main Menu.wav"),
    NEW_GAME_AMBIANCE("resources/audio/ambiance/New Game.wav"),
    COMBAT_AMBIANCE("resources/audio/ambiance/Combat.wav");
    
    private final String urlPath;
    
    MusicTrack(String url)
    {
        urlPath = url;
    }
    
    public String getUrlPath() { return urlPath; }
}