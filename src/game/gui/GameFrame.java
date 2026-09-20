package game.gui;

import game.mainmenu.MainMenuController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameFrame extends JFrame implements GUIAction
{
    private static final int programWidth = 1280;
    private static final int programHeight = 720;
    
    private final OptionsOverlay optionsOverlay;
    
    public GameFrame(String name)
    {
        super(name);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(programWidth, programHeight));
        setLayout(new BorderLayout());
        
        MainMenuController mmController = new MainMenuController(this);
        add(new MainMenuPanel(name, mmController));
        pack();
        
        setLocationRelativeTo(null);
        
        optionsOverlay = new OptionsOverlay(new OptionsPanel(this));
        getLayeredPane().add(optionsOverlay, JLayeredPane.MODAL_LAYER);
        getLayeredPane().addComponentListener((new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                for(Component component : getLayeredPane().getComponents())
                {
                    if(component instanceof JPanel panel)
                    {
                        panel.setSize(getLayeredPane().getSize());
                    }
                }
            }
        }));
        optionsOverlay.setVisible(false);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    @Override
    public void openGameScreen()
    {
    
    }
    
    @Override
    public void openSavesScreen()
    {
    
    }
    
    @Override
    public void openOptionsScreen()
    {
        optionsOverlay.setLocation((getLayeredPane().getWidth() - optionsOverlay.getWidth()) / 2, (getLayeredPane().getHeight() - optionsOverlay.getHeight()) / 2);
        optionsOverlay.setVisible(true);
    }
    
    @Override
    public void closeGame()
    {
        dispose();
    }
    
    @Override
    public void changeResolution(DisplayResolution resolution)
    {
        getContentPane().setPreferredSize(new Dimension(resolution.getWidth(), resolution.getHeight()));
        pack();
        revalidate();
        repaint();
    }
}