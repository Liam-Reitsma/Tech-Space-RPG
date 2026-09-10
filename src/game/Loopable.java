package game;

public interface Loopable<I, J, R extends Result<? extends Errors>>
{
    default R loop() { return loop(null, null); }
    default R loop(I input1) { return loop(input1, null); }
    public R loop(I input1, J input2);
}
