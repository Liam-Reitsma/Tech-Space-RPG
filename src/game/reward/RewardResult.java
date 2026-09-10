package game.reward;

import game.Result;

public class RewardResult extends Result<RewardErrors>
{
    public RewardResult(RewardErrors er)
    {
        error = er;
    }
}
