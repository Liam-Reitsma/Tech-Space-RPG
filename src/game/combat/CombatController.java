package game.combat;

import static game.combat.CombatAction.ATTACK;
import static game.combat.CombatErrors.*;
import static game.combat.CombatOutcome.*;

import game.ConsoleHandler;
import game.Loopable;
import game.entities.*;
import game.entities.player.Player;

import java.util.Optional;

public class CombatController implements Loopable<Player, Enemy, CombatResult>
{
    private final ConsoleHandler consoleH;

    public CombatController(ConsoleHandler cH)
    {
        consoleH = cH;
    }

    @Override
    public CombatResult loop(Player player, Enemy enemy)
    {
        CombatOutcome outcome;
        boolean userTurn = true;
        
        while(true)
        {
            if(userTurn)
            {
                outcome = playerTurn(player, enemy);
                if(outcome != NOTHING) return new CombatResult(SUCCESS, outcome);
                userTurn = false;
            }
            else
            {
                outcome = enemyTurn(player, enemy);
                if(outcome != NOTHING) return new CombatResult(SUCCESS, outcome);
                userTurn = true;
            }
        }
    }

    public CombatOutcome playerTurn(Player player, Enemy enemy)
    {
        Optional<CombatAction> action;
        do
        {
            action = consoleH.confirmInput("What would you like to do?", CombatAction.optionsToString(), CombatAction.class).getInput();
            if(action.isEmpty())
            {
                consoleH.printLine("You cannot return here. Please choose an option");
                continue;
            }
            break;
        } while(true);
        
        ActionResult result = action.get().execute(consoleH, player, enemy);
        consoleH.printLine(result.getMessage());
        return result.getOutcome();
    }

    public CombatOutcome enemyTurn(Player player, Enemy enemy)
    {
        ActionResult result = ATTACK.execute(consoleH, player, enemy);
        consoleH.printLine(result.getMessage());
        return result.getOutcome();
    }
}