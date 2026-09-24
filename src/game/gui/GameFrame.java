package game.gui;

import game.mainmenu.MainMenuController;
import game.persistence.PersistenceController;
import game.persistence.SaveFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameFrame extends JFrame implements GUIAction
{
    private static final int programWidth = 1280;
    private static final int programHeight = 720;
    
    private final MainMenuController mmController;
    private final PersistenceController pController;
    
    private final OptionsOverlay optionsOverlay;
    private final SavesOverlay savesOverlay;
    
    public GameFrame(String name)
    {
        super(name);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(programWidth, programHeight));
        setLayout(new BorderLayout());
        
        mmController = new MainMenuController(this);
        pController = new PersistenceController();
        MainMenuPanel mmPanel = new MainMenuPanel(name, mmController);
        add(mmPanel);
        
        optionsOverlay = new OptionsOverlay(new OptionsPanel(this), new OptionsTitlePanel(this), programWidth, programHeight);
        savesOverlay = new SavesOverlay(new SavesPanel(this, pController.getSaveOptions()), new SavesTitlePanel(this), programWidth, programHeight);
        getLayeredPane().add(optionsOverlay, JLayeredPane.MODAL_LAYER);
        getLayeredPane().add(savesOverlay, JLayeredPane.MODAL_LAYER);
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
        savesOverlay.setVisible(false);
        
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
        savesOverlay.setLocation((getLayeredPane().getWidth() - savesOverlay.getWidth()) / 2, (getLayeredPane().getHeight() - savesOverlay.getHeight()) / 2);
        savesOverlay.setVisible(true);
    }
    
    @Override
    public void selectSave(SaveFile saveFile)
    {
        pController.loadSaveGame(saveFile);
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
    public void closeGame()
    {
        dispose();
    }
}