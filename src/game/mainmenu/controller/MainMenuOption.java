package game.mainmenu.controller;

public enum MainMenuOption
{
    NEW_GAME(),
    LOAD_GAME(),
    OPTIONS(),
    EXIT_GAME();
    
    
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