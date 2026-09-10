package game;

public interface Startable<I, J, R extends Result<? extends Errors>>
{
    default public R start() { return start(null, null); }
    default public R start(I input) { return start(input, null); }
    public R start(I input1, J input2);
}