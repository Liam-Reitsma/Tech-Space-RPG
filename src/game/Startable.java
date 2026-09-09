package game;

public interface Startable<T, R extends Result<? extends Errors>>
{
    public R start(T input);
}
