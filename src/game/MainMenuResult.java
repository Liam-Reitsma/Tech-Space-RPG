package game;

import game.entities.Persistable;

import java.util.List;

public class MainMenuResult extends Result<MainMenuErrors>
{
    protected List<Persistable> result;
    protected MainMenuStates stateUsed;
    
    public MainMenuResult(MainMenuErrors e, List<Persistable> list, MainMenuStates state)
    {
        error = e;
        result = list;
        stateUsed = state;
    }
    
    public int size() { return result.size(); }
    
    public List<Persistable> getList() { return result;}
    
    @Override
    public String toString()
    {
        StringBuilder stringToReturn = new StringBuilder();
        for(Persistable line : result) stringToReturn.append(line).append('\n');
        return stringToReturn.toString();
    }
}
