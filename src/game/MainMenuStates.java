package game;

public enum MainMenuStates
{
    NEW_GAME("Start a New Game.", 1),
    LOAD_GAME("Load a Saved Game.", 2),
    EXIT("Exit the Game", 3);
    
    final String message;
    final int index;
    
    MainMenuStates(String s, int i)
    {
        message = s;
        index = i;
    }
    
    @Override
    public String toString()
    {
        return message;
    }
    
    public int getIndex()
    {
        return index;
    }
}