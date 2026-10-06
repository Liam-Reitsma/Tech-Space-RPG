package game.world.view;

import static game.world.model.generation.WorldGenConfig.MAX_RANGE;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.world.controller.WorldAction;
import game.world.model.Direction;
import game.world.model.room.RuntimeRoom;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class WorldPanel extends JPanel implements WorldViewAction
{
    private static final Image MAP_IMAGE = new ImageIcon("resources/images/World Map.png").getImage();
    
    private final WorldAction worldAction;
    private JPanel[][] roomTiles;
    
    public WorldPanel(WorldAction wAction)
    {
        setLayout(new ScaledLayout());
        setOpaque(true);
        worldAction = wAction;
        createWorldGrid();
    }
    
    private void createWorldGrid()
    {
        int arrayMaxRange = MAX_RANGE * 2 + 1;
        LayoutBox mainBox = new LayoutBox();
        LayoutBox[][] grid = new LayoutBox[arrayMaxRange][arrayMaxRange];
        float[] splitArray = new float[arrayMaxRange];
        Arrays.fill(splitArray, 1);
        
        LayoutBox[] verticalBoxes = mainBox.splitVertical(splitArray);
        
        int i = 0;
        for(LayoutBox box : verticalBoxes)
        {
            grid[i++] = box.splitHorizontal(splitArray);
        }
        roomTiles = new JPanel[arrayMaxRange][arrayMaxRange];
        
        for(int row = 0; row < arrayMaxRange; row++)
        {
            for(int col = 0; col < arrayMaxRange; col++)
            {
                JPanel tile = new JPanel();
                tile.setBorder(BorderFactory.createRaisedBevelBorder());
                roomTiles[row][col] = tile;
                
                add(tile, grid[row][col].insets(5));
            }
        }
    }
    
    @Override
    public void updateWorldMapPanel(RuntimeRoom[][] rooms)
    {
        for(int row = 0; row < rooms.length; row++)
        {
            for(int col = 0; col < rooms[row].length; col++)
            {
                if(!rooms[row][col].isSeen())
                    roomTiles[row][col].setOpaque(false);
                else
                {
                    switch(rooms[row][col].getRoomState().getRoomTileState())
                    {
                        case WALL -> roomTiles[row][col].setBackground(Color.BLACK);
                        case COMPLETED -> roomTiles[row][col].setBackground(Color.GREEN);
                        case CURRENT -> roomTiles[row][col].setBackground(Color.CYAN);
                        default ->
                        {
                            switch(rooms[row][col].getRoomState().getRoomType())
                            {
                                case COMBAT -> roomTiles[row][col].setBackground(Color.RED);
                                case BOSS -> roomTiles[row][col].setBackground(Color.MAGENTA);
                                case MINIBOSS -> roomTiles[row][col].setBackground(Color.PINK);
                                case CHEST -> roomTiles[row][col].setBackground(Color.YELLOW);
                                default -> roomTiles[row][col].setBackground(Color.GRAY);
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void updateAvailableMovementsPanel(Direction[] directions)
    {
    
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(MAP_IMAGE, 0, 0, getWidth(), getHeight(), this);
    }
}