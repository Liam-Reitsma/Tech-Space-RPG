package game.frame.view;

import static game.gui.config.GUIConfig.*;

import game.frame.controller.DisplayTarget;
import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.common.scaledlayout.ScaledLayoutAnchor;
import game.gui.config.DisplayResolution;
import game.gui.config.Displayable;
import game.gui.config.DisplayableOverlay;
import game.gui.config.DisplayableScreen;
import game.gui.popup.PopupError;
import game.gui.popup.PopupMessage;
import game.gui.popup.PopupOverlay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.Map;

public class GameFrame extends JFrame implements DragViewAction, FrameViewAction, PopupViewAction
{
    private final OverlayContainer overlayContainer;
    
    private Displayable currentDisplayedScreen;
    private Displayable currentDisplayedOverlay;
    private Displayable currentDisplayedPopup;
    
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
    public void closePopup()
    {
        if(currentDisplayedPopup != null)
        {
            currentDisplayedPopup.closeDisplay();
            currentDisplayedPopup = null;
            overlayContainer.setVisible(false);
        }
    }
    
    @Override
    public void closeGameFrame()
    {
        dispose();
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
    public void display(DisplayTarget display)
    {
        Displayable displayScreen = displayMap.get(display);
        
        if(displayScreen instanceof DisplayableOverlay overlay)
        {
            if(currentDisplayedOverlay != null)
                currentDisplayedOverlay.closeDisplay();
            currentDisplayedOverlay = overlay;
            overlayContainer.setVisible(true);
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
    public void displayPopupError(PopupError error, ActionListener leftButton, ActionListener rightButton)
    {
        if(currentDisplayedPopup != null)
            currentDisplayedPopup.closeDisplay();
        
        currentDisplayedPopup = error.createOverlay(leftButton, rightButton);
        overlayContainer.add(currentDisplayedPopup.getDisplay(), new LayoutBox(null, 15, 30, ScaledLayoutAnchor.CENTER));
        currentDisplayedPopup.openDisplay();
        overlayContainer.setVisible(true);
        
        overlayContainer.revalidate();
        overlayContainer.repaint();
    }
    
    @Override
    public void displayPopupConfirmation(PopupMessage popupMessage, ActionListener onCancel, ActionListener onConfirm)
    {
        if(currentDisplayedPopup != null)
            currentDisplayedPopup.closeDisplay();
        
        PanelButton cancelButton = new PanelButton(popupMessage.leftButtonText(), onCancel);
        PanelButton confirmButton = new PanelButton(popupMessage.rightButtonText(), onConfirm);
        
        currentDisplayedPopup = new PopupOverlay(popupMessage.title(), popupMessage.description(), cancelButton, confirmButton);
        overlayContainer.add(currentDisplayedPopup.getDisplay(), new LayoutBox(null, 15, 30, ScaledLayoutAnchor.CENTER));
        currentDisplayedPopup.openDisplay();
        overlayContainer.setVisible(true);
        
        overlayContainer.revalidate();
        overlayContainer.repaint();
    }
    
    @Override
    public void displayOnOverlay(MouseEvent e, JComponent component)
    {
        if(component.getParent() != getLayeredPane())
            getLayeredPane().add(component, JLayeredPane.DRAG_LAYER);
        Point position = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), getLayeredPane());
        component.setSize(e.getComponent().getSize());
        component.setBounds(position.x - component.getWidth() / 2, position.y - component.getHeight() / 2,
                component.getWidth(), component.getHeight());
    }
    
    @Override
    public Component getDropTarget(MouseEvent e)
    {
        Point position = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), getLayeredPane());
        return SwingUtilities.getDeepestComponentAt(getLayeredPane(), position.x, position.y);
    }
    
    @Override
    public void removeFromOverlay(JComponent component)
    {
        getLayeredPane().remove(component);
        getLayeredPane().repaint();
        overlayContainer.setVisible(false);
    }
    
    @Override
    public void resizeGameFrame(DisplayResolution resolution)
    {
        getContentPane().setPreferredSize(new Dimension(resolution.getWidth(), resolution.getHeight()));
        pack();
        setLocationRelativeTo(null);
    }
}