package game.mainmenu;

public enum MainMenuState implements MainMenuStateAction
{
    NEW_GAME
        {
            @Override
            public void execute(MainMenuAction mmAction)
            {
                mmAction.newGame();
            }
        },
    LOAD_GAME
        {
            @Override
            public void execute(MainMenuAction mmAction)
            {
                mmAction.loadGame();
            }
        },
    OPTIONS
        {
            @Override
            public void execute(MainMenuAction mmAction)
            {
                mmAction.openOptions();
            }
        },
    EXIT_GAME
        {
            @Override
            public void execute(MainMenuAction mmAction)
            {
                mmAction.exitGame();
            }
        };
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        String[] words = name().split("_");
        for(String word : words)
        {
            sb.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
        }
        
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}