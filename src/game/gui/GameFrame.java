package game.gui;

import static game.gui.config.GUIConfig.PROGRAM_HEIGHT;
import static game.gui.config.GUIConfig.PROGRAM_WIDTH;

import game.GameViewAction;
import game.entities.player.ClassType;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.maingame.MainGameScreen;
import game.gui.newgame.NewGameScreen;
import game.gui.options.OptionsOverlay;
import game.gui.options.OptionsPanel;
import game.gui.options.OptionsTitlePanel;
import game.gui.saves.SavesOverlay;
import game.gui.saves.SavesPanel;
import game.gui.saves.SavesTitlePanel;
import game.mainmenu.MainMenuViewAction;
import game.gui.mainmenu.MainMenuScreen;
import game.mainmenu.MainMenuAction;
import game.newgame.NewGameAction;
import game.newgame.NewGameViewAction;
import game.options.OptionsAction;
import game.options.OptionsViewAction;
import game.saves.SavesAction;
import game.saves.SavesViewAction;
import game.world.Direction;
import game.world.WorldViewAction;
import game.world.room.RoomState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameFrame extends JFrame implements GameViewAction, MainMenuViewAction, NewGameViewAction, OptionsViewAction
        , SavesViewAction, WorldViewAction
{
    private MainMenuAction mainMenuAction;
    private NewGameAction newGameAction;
    private OptionsAction optionsAction;
    private SavesAction savesAction;
    
    private OverlayContainer overlayContainer;
    
    private OptionsOverlay optionsOverlay;
    private SavesOverlay savesOverlay;
    
    NewGameScreen newGameScreen;
    MainGameScreen mainGameScreen;
    
    public GameFrame(String name)
    {
        super(name);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(PROGRAM_WIDTH, PROGRAM_HEIGHT));
        setLayout(new BorderLayout());
        overlayContainer = new OverlayContainer(new ScaledLayout(PROGRAM_WIDTH, PROGRAM_HEIGHT));
        getLayeredPane().add(overlayContainer, JLayeredPane.MODAL_LAYER);
        
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
        
        pack();
        setLocationRelativeTo(null);
    }
    
    public void setMainMenuAction(MainMenuAction mmAction) { mainMenuAction = mmAction; }
    
    public void setOptionsAction(OptionsAction oAction) { optionsAction = oAction; }
    
    public void setSavesAction(SavesAction sAction) { savesAction = sAction; }
    
    public void setNewGameAction(NewGameAction ngAction) { newGameAction = ngAction; }
    
    @Override
    public void openNewGameScreen()
    {
        newGameScreen = new NewGameScreen(newGameAction);
        getContentPane().removeAll();
        getContentPane().add(newGameScreen);
        revalidate();
        repaint();
    }
    
    @Override
    public void changeCharacterPanel(ClassType classType)
    {
        newGameScreen.updateCharacter(new ImageIcon(classType.getImagePath()).getImage(), classType.getStartingDescription());
    }
    
    @Override
    public void openMainMenuScreen()
    {
        MainMenuScreen mainMenuScreen = new MainMenuScreen(getTitle(), mainMenuAction);
        getContentPane().removeAll();
        getContentPane().add(mainMenuScreen);
        revalidate();
        repaint();
    }
    
    @Override
    public void openLoadGameOverlay(String[] saveNames)
    {
        createSavesOverlay(saveNames);
        savesOverlay.setLocation((getLayeredPane().getWidth() - savesOverlay.getWidth()) / 2,
                (getLayeredPane().getHeight() - savesOverlay.getHeight()) / 2);
        savesOverlay.setVisible(true);
    }
    
    private void createSavesOverlay(String[] saveNames)
    {
        savesOverlay = new SavesOverlay(new SavesPanel(savesAction, saveNames), new SavesTitlePanel(savesAction),
                PROGRAM_WIDTH, PROGRAM_HEIGHT);
        getLayeredPane().add(savesOverlay, JLayeredPane.MODAL_LAYER);
    }
    
    @Override
    public void closeSavesScreen()
    {
        getLayeredPane().remove(savesOverlay);
        savesOverlay = null;
        
        revalidate();
        repaint();
    }
    
    @Override
    public void openOptionsOverlay()
    {
        createOptionsOverlay();
        optionsOverlay.doLayout();
        optionsOverlay.setVisible(true);
    }
    
    private void createOptionsOverlay()
    {
        optionsOverlay = new OptionsOverlay(new OptionsPanel(optionsAction), new OptionsTitlePanel(optionsAction),
                new ScaledLayout(PROGRAM_WIDTH, PROGRAM_HEIGHT));
        getLayeredPane().add(optionsOverlay, JLayeredPane.MODAL_LAYER);
    }
    
    @Override
    public void resizeGameFrame(DisplayResolution resolution)
    {
        getContentPane().setPreferredSize(new Dimension(resolution.getWidth(), resolution.getHeight()));
        pack();
        revalidate();
        repaint();
    }
    
    @Override
    public void closeOptionsScreen()
    {
        getLayeredPane().remove(optionsOverlay);
        optionsOverlay = null;
        
        revalidate();
        repaint();
    }
    
    @Override
    public void closeGameFrame()
    {
        dispose();
    }
    
    @Override
    public void openMainGameScreen()
    {
        mainGameScreen = new MainGameScreen();
        getContentPane().removeAll();
        getContentPane().add(mainGameScreen);
        revalidate();
        repaint();
    }
    
    @Override
    public void updateMainGameScreen()
    {
    
    }
    
    @Override
    public void updateWorldMapPanel(RoomState[][] roomState)
    {
    
    }
    
    @Override
    public void updateAvailableMovementsPanel(Direction[] directions)
    {
    
    }
}