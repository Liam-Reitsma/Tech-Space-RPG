package game.gui;

import game.mainmenu.MainMenuViewAction;
import game.gui.mainmenu.MainMenuPanel;
import game.mainmenu.MainMenuAction;
import game.saves.SavesAction;
import game.saves.SavesViewAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameFrame extends JFrame implements GUIAction, MainMenuViewAction, SavesViewAction
{
    private static final int programWidth = 1280;
    private static final int programHeight = 720;
    
    private final OptionsOverlay optionsOverlay;
    private SavesOverlay savesOverlay;
    private SavesAction savesAction;
    
    public GameFrame(String name)
    {
        super(name);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(programWidth, programHeight));
        setLayout(new BorderLayout());
        
        optionsOverlay = new OptionsOverlay(new OptionsPanel(this), new OptionsTitlePanel(this), programWidth, programHeight);
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
    
    public void setMainMenuAction(MainMenuAction mainMenuAction)
    {
        MainMenuPanel mmPanel = new MainMenuPanel(getTitle(), mainMenuAction);
        add(mmPanel);
    }
    
    public void setSavesAction(SavesAction sAction)
    {
        savesAction = sAction;
    }
    
    //Open New Game
    @Override
    public void openNewGameScreen()
    {
    
    }
    
    @Override
    public void openLoadGameScreen(String[] saveNames)
    {
        createSavesOverlay(saveNames);
        savesOverlay.setLocation((getLayeredPane().getWidth() - savesOverlay.getWidth()) / 2, (getLayeredPane().getHeight() - savesOverlay.getHeight()) / 2);
    }
    
    private void createSavesOverlay(String[] saveNames)
    {
        savesOverlay = new SavesOverlay(new SavesPanel(savesAction, saveNames), new SavesTitlePanel(savesAction), programWidth, programHeight);
        getLayeredPane().add(savesOverlay, JLayeredPane.MODAL_LAYER);
        savesOverlay.setVisible(true);
    }
    
    //Loaded Games
    @Override
    public void openGameScreen()
    {
    
    }
    
    @Override
    public void closeSavesScreen()
    {
        savesOverlay.setVisible(false);
    }
    
    
    @Override
    public void openOptionsScreen()
    {
        optionsOverlay.setLocation((getLayeredPane().getWidth() - optionsOverlay.getWidth()) / 2, (getLayeredPane().getHeight() - optionsOverlay.getHeight()) / 2);
        optionsOverlay.setVisible(true);
    }
    
    @Override
    public void changeResolution(DisplayResolution resolution)
    {
        getContentPane().setPreferredSize(new Dimension(resolution.getWidth(), resolution.getHeight()));
        pack();
        revalidate();
        repaint();
    }
    
    @Override
    public void closeOptionsScreen()
    {
        optionsOverlay.setVisible(false);
    }
    
    @Override
    public void closeGameFrame()
    {
        dispose();
    }
}