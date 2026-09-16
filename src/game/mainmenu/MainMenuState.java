package game.mainmenu;

public enum MainMenuState implements MainMenuAction
{
    NEW_GAME
        {
            @Override
            public void executeStateAction(MainMenuController mmController)
            {
                mmController.newGame();
            }
        },
    LOAD_GAME
        {
            @Override
            public void executeStateAction(MainMenuController mmController)
            {
                mmController.loadGame();
            }
        },
    OPTIONS
        {
            @Override
            public void executeStateAction(MainMenuController mmController)
            {
                mmController.openOptions();
            }
        },
    EXIT_GAME
        {
            @Override
            public void executeStateAction(MainMenuController mmController)
            {
                mmController.exitGame();
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