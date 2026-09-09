package game.mainmenu;

import game.Result;

public class MainMenuResult extends Result<MainMenuErrors>
{
    protected MainMenuStates stateUsed;
    
    public MainMenuResult(MainMenuErrors e, MainMenuStates state)
    {
        error = e;
        stateUsed = state;
    }
    
    public MainMenuStates getStateUsed() { return stateUsed; }
}
