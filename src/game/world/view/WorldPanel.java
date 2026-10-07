package game.world.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.world.model.Direction;
import game.world.model.room.RuntimeRoom;

import javax.swing.*;

public class WorldPanel extends JPanel implements WorldViewAction
{
    private final WorldMapPanel worldMapPanel;
    private final WorldMovementPanel worldMovementPanel;
    
    public WorldPanel(WorldMapPanel mapPanel, WorldMovementPanel movementPanel)
    {
        setLayout(new ScaledLayout());
        worldMapPanel = mapPanel;
        worldMovementPanel = movementPanel;
        
        LayoutBox[] verticalBoxes = new LayoutBox().splitVertical(50, 50);
        
        add(worldMapPanel, verticalBoxes[0]);
        add(worldMovementPanel, verticalBoxes[1].insets(15));
    }
    
    @Override
    public void updateWorldMapPanel(RuntimeRoom[][] rooms)
    {
        worldMapPanel.updateWorldMapPanel(rooms);
    }
    
    @Override
    public void updateAvailableMovementsPanel(Direction[] directions)
    {
        worldMovementPanel.updateAvailableMovementsPanel(directions);
    }
}