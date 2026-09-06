package game;

import java.util.Optional;

public class InputResult<T> extends Result<Errors>
{
    protected T input;
    protected boolean goBack;
    
    public InputResult(T result)
    {
        input = result;
    }
    
    public InputResult()
    {
        goBack = true;
    }
    
    public Optional<T> getInput()
    {
        if(goBack) return Optional.empty();
        else return Optional.of(input);
    }
}
