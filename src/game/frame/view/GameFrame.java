package game.frame.view;

import static game.gui.config.GUIConfig.*;

import game.frame.controller.DisplayTarget;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.common.scaledlayout.ScaledLayoutAnchor;
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
    
    private Map<DisplayTarget, Displayable> displayMap;
    
    public GameFrame(String name, OverlayContainer oContainer)
    {
        super(name);
        
        overlayContainer = oContainer;
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(PROGRAM_WIDTH, PROGRAM_HEIGHT));
        setLayout(new ScaledLayout(PROGRAM_WIDTH, PROGRAM_HEIGHT));
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
    
    public void registerDisplay(Map<DisplayTarget, Displayable> map)
    {
        displayMap = map;
        
        for(Displayable display : displayMap.values())
        {
            if(display instanceof DisplayableOverlay)
                overlayContainer.add(display.getDisplay(), new LayoutBox(null, 35, 70, ScaledLayoutAnchor.CENTER));
            else if(display instanceof DisplayableScreen)
                add(display.getDisplay(), new LayoutBox());
        }
        revalidate();
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
            if(currentDisplayedOverlay != null)
                currentDisplayedOverlay.closeDisplay();
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