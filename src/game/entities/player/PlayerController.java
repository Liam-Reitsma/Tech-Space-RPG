    package game.entities.player;
    
    import static game.entities.player.PlayerErrors.*;
    
    import game.ConsoleHandler;
    import game.items.Item;
    import game.items.armour.Armour;
    import game.items.weapon.Weapon;
    
    import java.util.Optional;
    
    /** PlayerController implements both Startable and Loopable. Startable takes Player, Void, and PlayerResult, while Loopable takes Void, Void, and PlayerResult.*/
    public class PlayerController
    {
        private final ConsoleHandler consoleH;
        private Player player;
        
        public PlayerController(ConsoleHandler cH)
        {
            consoleH = cH;
        }
        
        public PlayerResult<?> start(Player startPlayer, Void input1)
        {
            player = startPlayer;
            return new PlayerResult<>(SUCCESS);
        }
    
        public PlayerResult<?> loop(Void input1, Void input2)
        {
            Optional<PlayerMenuOptions> response = consoleH.confirmInputInteger("What would you like to do?", PlayerMenuOptions.optionsToString(), PlayerMenuOptions.class).getInput();
            if(response.isEmpty()) return new PlayerResult<>(USER_WENT_BACK);
            PlayerMenuOptions option = response.get();
            return switch(option)
            {
                case PROCEED_PAST -> new PlayerResult<Void>(SKIP);
                case VIEW_INVENTORY ->
                {
                    consoleH.printLine("Your current level is: " + player.getLevel());
                    consoleH.printLine("Your current XP is: " + player.getXP() + "/" + player.getXpToLevelUp());
                    consoleH.printLine("Your current gold is: " + player.getGold());
                    for(Item item : player.getInventory().getItemsInInventory())
                    {
                        if(item instanceof Weapon weapon)  {consoleH.printLine(weapon.getName() + "(" + player.attack() + " damage)"); }
                        else if(item instanceof Armour armour) { consoleH.printLine(armour.getName() + "(" + armour.calculateProtection() + " armour)"); }
                        else { consoleH.printLine(item.getName()); }
                    }

                    yield new PlayerResult<Void>(RETRY);
                }
                case EQUIP_WEAPON ->
                {
//                    PlayerResult<Weapon> result = player.getWeaponsInInventory();
//                    if(result.getError() != SUCCESS)
//                    {
//                        consoleH.printLine(result.getError().getMessage());
//                        yield new PlayerResult<>(result.getError());
//                    }
//
//                    StringBuilder weaponOptions = new StringBuilder();
//
//                    int i = 1;
//                    for(Weapon weapon : result.getResultArray())
//                    {
//                        weaponOptions.append(i++).append(". ").append(weapon.getName()).append("(").append(player.attack()).append(" damage)").append(", ");
//                    }
//
//                    weaponOptions.delete(weaponOptions.length() - ", ".length(), weaponOptions.length());
//
//                    Optional<Integer> choice = consoleH.confirmInput("Which weapon would you like to equip?", weaponOptions.toString(), ", ").getInput();
//                    if(choice.isEmpty()) yield new PlayerResult<>(USER_WENT_BACK);
//                    PlayerErrors error = player.equipWeapon(result.getResultArray()[choice.get() - 1]);
//                    if(error != SUCCESS) consoleH.printLine(error.getMessage());
//
                    yield new PlayerResult<>(SUCCESS);
                }
                case UNEQUIP_WEAPON ->
                {
//                    Weapon weapon = player.getEquippedWeapon();
//                    PlayerErrors error = player.unequipWeapon();
//                    if(error != SUCCESS)
//                    {
//                        consoleH.printLine(error.getMessage());
//                        yield new PlayerResult<>(error);
//                    }
//                    else
//                    {
//                        consoleH.printLine(weapon.getName() + " successfully unequipped");
//                        yield new PlayerResult<>(SUCCESS);
//                    }
                    yield new PlayerResult<>(SUCCESS);
                }
                case EQUIP_ARMOR ->
                {
//                    PlayerResult<Armour> result = player.getArmourInInventory();
//                    if(result.getError() != SUCCESS)
//                    {
//                        consoleH.printLine(result.getError().getMessage());
//                        yield new PlayerResult<>(result.getError());
//                    }
//
//                    StringBuilder armourOptions = new StringBuilder();
//                    int i = 1;
//                    for(Armour armour : result.getResultArray())
//                    {
//                        armourOptions.append(i++).append(". ").append(armour.getName()).append("(").append(armour.calculateProtection()).append(" armour)").append(", ");
//                    }
//
//                    armourOptions.delete(armourOptions.length() - ", ".length(), armourOptions.length());
//
//                    Optional<Integer> choice = consoleH.confirmInput("Which armour would you like to equip?", armourOptions.toString(), ", ").getInput();
//                    if(choice.isEmpty()) yield new PlayerResult<>(USER_WENT_BACK);
//                    PlayerErrors error = player.equipArmour(result.getResultArray()[choice.get() - 1]);
//                    if(error != SUCCESS) consoleH.printLine(error.getMessage());
                    
                    yield new PlayerResult<>(SUCCESS);
                }
                case UNEQUIP_ARMOR ->
                {
//                    List<Armour> armourList = player.getEquippedArmour();
//                    if(armourList.isEmpty())
//                    {
//                        consoleH.printLine(NO_ARMOUR_EQUIPPED.getMessage());
//                        yield new PlayerResult<>(NO_ARMOUR_EQUIPPED);
//                    }
//                    StringBuilder armourOptions = new StringBuilder();
//                    int i = 1;
//                    for(Armour armour : armourList)
//                    {
//                        armourOptions.append(i++).append(". ").append(armour.getName()).append("(").append(armour.calculateProtection()).append(" armour)").append(", ");
//                    }
//
//                    Optional<Integer> choice = consoleH.confirmInput("Which armour would you like to unequip?", armourOptions.toString(), ", ").getInput();
//                    if(choice.isEmpty()) yield new PlayerResult<>(USER_WENT_BACK);
//
//                    Armour armour = armourList.get(choice.get() - 1);
//
//                    PlayerErrors error = player.unequipArmour(armour);
//                    if(error != SUCCESS)
//                    {
//                        consoleH.printLine(error.getMessage());
//                        yield new PlayerResult<>(error);
//                    }
//                    else
//                    {
//                        consoleH.printLine(armour.getName() + " successfully unequipped");
//                        yield new PlayerResult<>(SUCCESS);
//                    }
                    yield new PlayerResult<>(SUCCESS);
                }
                case SAVE_GAME -> new PlayerResult<>(SAVE_GAME);
                case EXIT_GAME -> new PlayerResult<>(EXIT_GAME);
            };
        }
    }