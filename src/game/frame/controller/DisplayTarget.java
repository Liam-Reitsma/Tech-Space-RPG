package game.frame.controller;

public enum DisplayTarget
{
    MAIN_MENU,
    NEW_GAME,
    MAIN_GAME,
    SAVES,
    OPTIONS,
    EXIT;
    
    @Override
    public String toString()
    {
        String[] strings = name().split("_");
        StringBuilder name = new StringBuilder();
        
        for(String string : strings)
        {
            name.append(string.charAt(0)).append(string.substring(1).toLowerCase()).append(" ");
        }
        
        name.deleteCharAt(name.length() - 1);
        
        return name.toString();
    }
}