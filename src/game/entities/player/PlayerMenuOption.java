package game.entities.player;

public enum PlayerMenuOption
{
    VIEW_INVENTORY,
    EQUIP_WEAPON,
    UNEQUIP_WEAPON,
    EQUIP_ARMOR,
    UNEQUIP_ARMOR;

    public static String optionsToString()
    {
        StringBuilder result = new StringBuilder();
        int i = 1;
        for(PlayerMenuOption state : PlayerMenuOption.values())
        {
            result.append(i++).append(". ").append(state.toString()).append("\n");
        }
        return result.toString();
    }
}
