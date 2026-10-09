package game.maingamemenu.view;

import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.maingamemenu.controller.MainGameMenuAction;
import game.maingamemenu.controller.MainGameMenuOption;

import javax.swing.*;

public class MainGameMenuPanel extends JPanel
{
    private final PanelButton openSavesButton;
    private final PanelButton fullSaveButton;
    private final PanelButton optionsButton;
    private final PanelButton mainMenuButton;
    private final PanelButton exitGameButton;
    
    public MainGameMenuPanel(MainGameMenuAction mainGameMenuAction)
    {
        setLayout(new ScaledLayout());
        LayoutBox[] boxes = new LayoutBox().splitHorizontal(20, 20, 20, 20, 20);
        
        openSavesButton = new PanelButton("Load Game", e ->
        {
            mainGameMenuAction.requestOpenDisplay(MainGameMenuOption.LOAD_GAME);
        });
        add(openSavesButton, boxes[0]);
        
        fullSaveButton = new PanelButton("Save Game", e ->
        {
            mainGameMenuAction.requestSaveGame();
        });
        add(fullSaveButton, boxes[1]);
        
        optionsButton = new PanelButton("Options", e ->
        {
            mainGameMenuAction.requestOpenDisplay(MainGameMenuOption.OPTIONS);
        });
        add(optionsButton, boxes[2]);
        
        mainMenuButton = new PanelButton("Exit to Main Menu", e ->
        {
            mainGameMenuAction.requestOpenDisplay(MainGameMenuOption.MAIN_MENU);
        });
        add(mainMenuButton, boxes[3]);
        
        exitGameButton = new PanelButton("Exit Game", e ->
        {
            mainGameMenuAction.requestOpenDisplay(MainGameMenuOption.EXIT_GAME);
        });
        add(exitGameButton, boxes[4]);
    }
}