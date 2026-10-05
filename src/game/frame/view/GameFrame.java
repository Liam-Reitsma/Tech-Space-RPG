package game.frame.view;

import static game.gui.config.GUIConfig.PROGRAM_HEIGHT;
import static game.gui.config.GUIConfig.PROGRAM_WIDTH;

import game.frame.controller.DisplayTarget;
import game.gui.config.DisplayResolution;
import game.gui.config.Displayable;
import game.gui.config.DisplayableOverlay;
import game.gui.config.DisplayableScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

public class GameFrame extends JFrame implements FrameViewAction
{
    private final OverlayContainer overlayContainer;
    
    private Displayable currentDisplayedScreen;
    private Displayable currentDisplayedOverlay;
    
    private final Map<DisplayTarget, Displayable> displayMap;
    
    public GameFrame(String name, OverlayContainer oContainer, Map<DisplayTarget, Displayable> map)
    {
        super(name);
        
        overlayContainer = oContainer;
        
        displayMap = map;
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(PROGRAM_WIDTH, PROGRAM_HEIGHT));
        setLayout(new BorderLayout());
        getLayeredPane().add(overlayContainer, JLayeredPane.MODAL_LAYER);
        
        getLayeredPane().addComponentListener((new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                overlayContainer.setBounds(0, 0, getLayeredPane().getWidth(), getLayeredPane().getHeight());
                overlayContainer.revalidate();
            }
        }));
        
        pack();
        setLocationRelativeTo(null);
    }
    
    @Override
    public void display(DisplayTarget display)
    {
        Displayable displayScreen = displayMap.get(display);
        
        if(displayScreen instanceof DisplayableOverlay overlay)
        {
            if(currentDisplayedOverlay != null)
                currentDisplayedOverlay.closeDisplay();
            currentDisplayedOverlay = overlay;
        }
        else if(displayScreen instanceof DisplayableScreen screen)
        {
            if(currentDisplayedScreen != null)
                currentDisplayedScreen.closeDisplay();
            currentDisplayedScreen = screen;
        }
        
        displayScreen.openDisplay();
    }
    
    @Override
    public void closeOverlay()
    {
        if(currentDisplayedOverlay != null)
        {
            currentDisplayedOverlay.closeDisplay();
            currentDisplayedOverlay = null;
        }
    }
    
    @Override
    public void resizeGameFrame(DisplayResolution resolution)
    {
        getContentPane().setPreferredSize(new Dimension(resolution.getWidth(), resolution.getHeight()));
        pack();
        setLocationRelativeTo(null);
    }
    
    @Override
    public void closeGameFrame()
    {
        dispose();
    }
}