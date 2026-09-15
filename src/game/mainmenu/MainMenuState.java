package game.mainmenu;

public enum MainMenuState
{
    NEW_GAME,
    LOAD_GAME,
    OPTIONS,
    EXIT_GAME;
    
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