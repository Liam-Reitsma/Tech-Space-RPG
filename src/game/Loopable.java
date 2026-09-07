package game;

public interface Loopable<R extends Result<? extends Errors>>
{
    public R loop();
}
