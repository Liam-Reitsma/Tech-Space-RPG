package game.gui;

import static game.gui.config.GUIConfig.PROGRAM_HEIGHT;
import static game.gui.config.GUIConfig.PROGRAM_WIDTH;

import game.GameViewAction;
import game.entities.player.ClassType;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.common.scaledlayout.ScaledLayoutAnchor;
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
    
    private final OverlayContainer overlayContainer;
    private OptionsOverlay optionsOverlay;
    private SavesOverlay savesOverlay;
    private NewGameScreen newGameScreen;
    private MainGameScreen mainGameScreen;
    
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
                overlayContainer.setBounds(0, 0, getLayeredPane().getWidth(), getLayeredPane().getHeight());
                overlayContainer.revalidate();
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
        savesOverlay = new SavesOverlay(new ScaledLayout(), new SavesPanel(new ScaledLayout(), savesAction, saveNames),
                new SavesTitlePanel(new ScaledLayout(), savesAction));
        overlayContainer.setOverlay(savesOverlay, new LayoutBox(null, 25, 55, ScaledLayoutAnchor.CENTER));
    }
    
    @Override
    public void closeSavesScreen()
    {
        overlayContainer.remove(savesOverlay);
        savesOverlay = null;
        
        revalidate();
        repaint();
    }
    
    @Override
    public void openOptionsOverlay()
    {
        optionsOverlay = new OptionsOverlay(new ScaledLayout(), new OptionsPanel(new ScaledLayout(), optionsAction),
                new OptionsTitlePanel(new ScaledLayout(), optionsAction));
        overlayContainer.setOverlay(optionsOverlay, new LayoutBox(null, 25, 55, ScaledLayoutAnchor.CENTER));
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
        overlayContainer.remove(optionsOverlay);
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