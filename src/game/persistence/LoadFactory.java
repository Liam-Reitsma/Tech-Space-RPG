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
        try
        {
            return Optional.of((Persistable)Class.forName(name).getConstructor(String[].class).newInstance(data));
        } catch(ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                NoSuchMethodException e)
        {
            return Optional.empty();
        }
    }
}
