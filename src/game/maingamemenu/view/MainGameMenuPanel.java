package game.maingamemenu.view;

import static game.frame.controller.DisplayTarget.*;

import game.frame.controller.DisplayTarget;
import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.maingamemenu.controller.MainGameMenuAction;

import javax.swing.*;

public class MainGameMenuPanel extends JPanel
{
    public MainGameMenuPanel(MainGameMenuAction mainGameMenuAction)
    {
        setLayout(new ScaledLayout());
        LayoutBox[] boxes = new LayoutBox().splitHorizontal(20, 20, 20, 20, 20);
        
        PanelButton openSavesButton = new PanelButton("Load Game", e -> {
            mainGameMenuAction.requestOpenDisplay(SAVES);
        });
        add(openSavesButton, boxes[0]);
        
        PanelButton fullSaveButton = new PanelButton("Save Game", e -> {
            mainGameMenuAction.requestSaveGame();
        });
        add(fullSaveButton, boxes[1]);
        
        PanelButton optionsButton = new PanelButton("Options", e -> {
            mainGameMenuAction.requestOpenDisplay(OPTIONS);
        });
        add(optionsButton, boxes[2]);
        
        PanelButton mainMenuButton = new PanelButton("Exit to Main Menu", e -> {
            mainGameMenuAction.requestOpenDisplay(MAIN_MENU);
        });
        add(mainMenuButton, boxes[3]);
        
        PanelButton exitGameButton = new PanelButton("Exit Game", e -> {
            mainGameMenuAction.requestOpenDisplay(EXIT);
        });
        add(exitGameButton, boxes[4]);
    }
}