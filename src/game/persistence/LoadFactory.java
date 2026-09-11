package game.persistence;

import game.entities.Persistable;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class LoadFactory
{
    public LoadFactory()
    {
    
    }
    
    public Optional<Persistable> create(String name, String[] data)
    {
        if(name == null) return Optional.empty();
        
        try
        {
            return Optional.of((Persistable)Class.forName(name).getConstructor(String[].class).newInstance((Object)data));
        } catch(ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                NoSuchMethodException | ClassCastException e)
        {
            return Optional.empty();
        }
    }
}
