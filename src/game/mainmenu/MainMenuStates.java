package game.mainmenu;

public enum MainMenuStates implements Menu
{
    NEW_GAME("Start a New Game.")
            {
                @Override
                public MainMenuResult execute()
                {
                    return new MainMenuResult(MainMenuErrors.SUCCESS, this);
                }
            },
    LOAD_GAME("Load a Saved Game.")
            {
                @Override
                public MainMenuResult execute()
                {
                    return new MainMenuResult(MainMenuErrors.SUCCESS, this);
                }
            },
    EXIT("Exit the Game")
            {
                @Override
                public MainMenuResult execute()
                {
                    return new MainMenuResult(MainMenuErrors.SUCCESS, this);
                }
            };
    
    final String message;
    
    MainMenuStates(String s)
    {
        message = s;
    }
    
    @Override
    public String toString()
    {
        return message;
    }
}