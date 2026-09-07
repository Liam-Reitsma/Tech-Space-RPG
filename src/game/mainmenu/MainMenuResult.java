package game.mainmenu;

import game.Result;
import game.entities.Persistable;

import java.util.List;

public class MainMenuResult extends Result<MainMenuErrors>
{
    protected List<List<Persistable>> result;
    protected MainMenuStates stateUsed;
    
    public MainMenuResult(MainMenuErrors e, List<List<Persistable>> list, MainMenuStates state)
    {
        error = e;
        result = list;
        stateUsed = state;
    }
    
    public int size() { return result.size(); }
    
    public List<List<Persistable>> getList() { return result;}
    
    @Override
    public String toString()
    {
        StringBuilder stringToReturn = new StringBuilder();
        for(List<Persistable> list : result)
        {
            for(Persistable line : list) stringToReturn.append(line).append('\n');
        }
        return stringToReturn.toString();
    }
}
