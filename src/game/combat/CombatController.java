package game.combat;

import game.ConsoleHandler;
import game.Loopable;
import game.entities.*;

import java.util.Optional;

public class CombatController implements Loopable<CombatResult>
{
    private final ConsoleHandler consoleH;

    private final Player player;
    private final Enemy opponent;
    private boolean inCombat = true;
    private CombatOutcome outcome;

    public CombatController(ConsoleHandler cH, Player p, Enemy opp)
    {
        consoleH = cH;
        player = p;
        opponent = opp;
    }

    @Override
    public CombatResult loop()
    {
        boolean userTurn = true;
        while (inCombat)
        {
            // check if combat should end
            if (player.getHealth() <= 0)
            {
                return new CombatResult(CombatErrors.SUCCESS, CombatOutcome.LOSE);
            }
            else if (opponent.getHealth() <= 0)
            {
                return new CombatResult(CombatErrors.SUCCESS, CombatOutcome.WIN);
            }
            // checking if users turn or enemies turn
            else {
                if (userTurn) {
                    playerTurn();
                    userTurn = false;
                } else {
                    enemyTurn();
                    userTurn = true;
                }
            }
        }
        return new CombatResult(CombatErrors.SUCCESS, outcome);
    }

    public void playerTurn()
    {
        int response = -1;
        // printing options and checking user input
        do
        {
            consoleH.printLine("What would you like to do?");
            for (CombatAction action : CombatAction.values()) {
                consoleH.printLine((action.ordinal() + 1) + ". " + action.toString());
            }
            Optional<Integer> userInput = consoleH.nextInt().getInput();
            if  (userInput.isPresent() && userInput.get() > 0 && userInput.get() <= CombatAction.values().length) {
                response = userInput.get();
                break;
            }
        } while(true);

        CombatAction action = CombatAction.values()[response - 1];

        // Functions of what the player is going to do and execute it
        ActionDecider result = action.execute(consoleH, player, opponent);
        consoleH.printLine(result.message());
        if (result.endReason().isPresent())
        {
            inCombat = false;
            outcome = result.endReason().get();
        }
    }

    public void enemyTurn()
    {
        int oppDamage = opponent.getStats()[0];
        player.takeDamage(oppDamage);
        consoleH.printLine("Enemy has attacked.\nRemaining health: " + player.getHealth());
    }
}
