package game.mainmenu.controller;

import game.frame.controller.DisplayTarget;

public enum MainMenuOption
{
    NEW_GAME(DisplayTarget.NEW_GAME),
    LOAD_GAME(DisplayTarget.SAVES),
    OPTIONS(DisplayTarget.OPTIONS),
    EXIT_GAME(DisplayTarget.EXIT);
    
    private final DisplayTarget displayTarget;
    
    MainMenuOption(DisplayTarget display)
    {
        displayTarget = display;
    }
    
    public DisplayTarget getDisplayTarget() { return displayTarget; }
    
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