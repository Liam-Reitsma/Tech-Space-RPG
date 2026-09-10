package game.reward;

import game.ConsoleHandler;
import game.Loopable;
import game.entities.*;

import java.util.Optional;


public class RewardController implements Loopable<Player, Enemy, RewardResult>
{
    private final ConsoleHandler consoleH;

    public  RewardController(ConsoleHandler cH)
    {
        consoleH = cH;
    }

    @Override
    public RewardResult loop(Player player, Enemy opponent)
    {
        int currentLevel = player.getLevel();
        int opponentXp = opponent.getEnemyType().getXpDrop();
        player.applyXP(opponentXp);
        for (int i = 0; i < player.getLevel() - currentLevel; i++) {
            Optional<StatType> userInput;
            do {
                userInput = consoleH.confirmInput("Which stat would you like to level up?", "Strength, Dexterity, and Intelligence", StatType.class).getInput();
                if (userInput.isEmpty()) consoleH.printLine("You need to enter a stat type.");
                else
                {
                    player.addStat(userInput.get());
                    break;
                }
            } while (true);
        }
        player.addGold(opponent.getEnemyType().getGoldDrop());
        return new RewardResult(RewardErrors.SUCCESS);
    }
}
