package game.mainmenu;

public enum MainMenuStates
{
    NEW_GAME,
    LOAD_GAME,
    EXIT_GAME;
    
    
    @Override
    public String toString()
    {
        String[] s = this.name().split("_");
        return s[0].charAt(0) + s[0].substring(1).toLowerCase() + " " + s[1].charAt(0) + s[1].substring(1).toLowerCase();
    }
    
    public static String optionsToString()
    {
        StringBuilder result = new StringBuilder();
        int i = 1;
        for(MainMenuStates state : MainMenuStates.values())
        {
            result.append(i++).append(". ").append(state.toString()).append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}