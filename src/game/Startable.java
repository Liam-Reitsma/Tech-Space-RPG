package game;

public interface Startable<R extends Result<? extends Errors>>
{
    public R start();
}
