package game.world.view;

import static game.world.model.WorldModel.VIEWABLE_RANGE;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.world.controller.WorldAction;
import game.world.model.Direction;
import game.world.model.room.RoomState;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class WorldPanel extends JPanel implements WorldViewAction
{
    private final WorldAction worldAction;
    private JPanel[][] roomTiles;
    
    public WorldPanel(WorldAction wAction)
    {
        setLayout(new ScaledLayout());
        setBackground(Color.WHITE);
        setOpaque(true);
        worldAction = wAction;
        createWorldGrid();
    }
    
    private void createWorldGrid()
    {
        LayoutBox mainBox = new LayoutBox();
        LayoutBox[][] grid = new LayoutBox[VIEWABLE_RANGE][VIEWABLE_RANGE];
        float[] splitArray = new float[VIEWABLE_RANGE];
        Arrays.fill(splitArray, 1);
        
        LayoutBox[] verticalBoxes = mainBox.splitVertical(splitArray);
        
        int i = 0;
        for(LayoutBox box : verticalBoxes)
        {
            grid[i++] = box.splitHorizontal(splitArray);
        }
        roomTiles = new JPanel[VIEWABLE_RANGE][VIEWABLE_RANGE];
        
        for(int row = 0; row < VIEWABLE_RANGE; row++)
        {
            for(int col = 0; col < VIEWABLE_RANGE; col++)
            {
                JPanel tile = new JPanel();
                roomTiles[row][col] = tile;
                
                add(tile, grid[row][col].insets(4));
            }
        }
    }
    
    @Override
    public void updateWorldMapPanel(RoomState[][] roomStates)
    {
        for(int row = 0; row < VIEWABLE_RANGE; row++)
        {
            for(int col = 0; col < VIEWABLE_RANGE; col++)
            {
                switch(roomStates[row][col].getRoomTileState())
                {
                    case WALL -> roomTiles[row][col].setBackground(Color.BLACK);
                    case COMPLETED -> roomTiles[row][col].setBackground(Color.GREEN);
                    case FLED, UNVISITED -> roomTiles[row][col].setBackground(Color.GRAY);
                    case CURRENT -> roomTiles[row][col].setBackground(Color.CYAN);
                }
            }
        }
    }
    
    @Override
    public void updateAvailableMovementsPanel(Direction[] directions)
    {
    
    }
}