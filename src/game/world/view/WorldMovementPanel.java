package game.world.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.world.controller.WorldAction;
import game.world.model.Direction;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class WorldMovementPanel extends JPanel
{
    private final Map<Direction, JButton> buttonMap;
    
    private final WorldAction worldAction;
    
    public WorldMovementPanel(WorldAction wAction)
    {
        setLayout(new ScaledLayout());
        worldAction = wAction;
        
        LayoutBox movementBox = new LayoutBox();
        
        buttonMap = new HashMap<>();
        
        for(Direction direction : Direction.values())
        {
            JButton button = new JButton(direction.toString());
            button.addActionListener(e-> worldAction.requestMoveNextRoom(direction));
            buttonMap.put(direction, button);
            add(button, new LayoutBox(movementBox, 30, 30, direction.getAnchor()));
        }
    }
    
    public void updateAvailableMovementsPanel(Direction[] directions)
    {
        for(JButton button : buttonMap.values())
            button.setVisible(false);
        
        for(Direction direction : directions)
            buttonMap.get(direction).setVisible(true);
    }
}