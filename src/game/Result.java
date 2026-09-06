package game;

public abstract class Result<E extends Errors>
{
    protected E error;
    
    public E getError() { return error; }
}
