package game.saves.view;

import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayoutAnchor;
import game.saves.controller.SavesAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class SavesPanel extends JPanel
{
    private static final Image SAVES_CONTENT_IMAGE = new ImageIcon("resources/images/Saves Content.png").getImage();
    private static final Image SAVES_BORDER_IMAGE = new ImageIcon("resources/images/Saves Border.png").getImage();
    private static final int SAVES_BACKGROUND_IMAGE_TOTAL_WIDTH = SAVES_BORDER_IMAGE.getWidth(null);
    private static final int SAVES_BACKGROUND_IMAGE_TOTAL_HEIGHT = SAVES_BORDER_IMAGE.getHeight(null);
    
    private static final int SAVES_IMAGE_BORDER_WIDTH = (SAVES_BACKGROUND_IMAGE_TOTAL_WIDTH - SAVES_CONTENT_IMAGE.getWidth(null)) / 2;
    private static final int SAVES_IMAGE_BORDER_HEIGHT = (SAVES_BACKGROUND_IMAGE_TOTAL_HEIGHT - SAVES_CONTENT_IMAGE.getHeight(null)) / 2;
    
    private final DefaultListModel<String> listModel;
    
    public SavesPanel(LayoutManager2 layoutManager2, SavesAction savesAction, String[] saveNames)
    {
        super(layoutManager2);
        
        LayoutBox[] boxes = new LayoutBox().splitVertical(80, 20);
        listModel = new DefaultListModel<>();
        JList<String> saveList = new JList<>(listModel);
        
        createSaveList(saveNames, saveList, listModel, boxes[0]);
        
        ActionListener saveButtonListener = e ->
        {
            String selectedSaveName = saveList.getSelectedValue();
            
            if(selectedSaveName != null)
            {
                savesAction.requestSelectSave(selectedSaveName);
            }
        };
        PanelButton selectSaveButton = new PanelButton("Select", saveButtonListener);
        add(selectSaveButton, boxes[1]);
        
        addMouseListener(new MouseAdapter() {});
        
    }
    
    private void createSaveList(String[] saveNames, JList<String> saveList, DefaultListModel<String> listModel, LayoutBox box)
    {
        
        add(saveList, new LayoutBox(box, 40, 60, ScaledLayoutAnchor.CENTER));
        
        for(String save : saveNames)
        {
            listModel.addElement(save);
        }
        
        saveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        saveList.setCellRenderer((list, value, index, isSelected, cellHasFocus) ->
        {
            JLabel saveToDisplay = new JLabel(value);
            saveToDisplay.setFont(list.getFont());
            saveToDisplay.setOpaque(true);
            
            if(isSelected)
            {
                saveToDisplay.setBackground(Color.lightGray);
            }
            else
            {
                saveToDisplay.setBackground(Color.white);
            }
            
            return saveToDisplay;
        });
    }
    
    public void updateSavesList(String[] saveNames)
    {
        listModel.removeAllElements();
        for(String saveName : saveNames)
        {
            listModel.addElement(saveName);
        }
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(SAVES_BORDER_IMAGE, 0, 0, getWidth(), getHeight(), null);
        
        int insetX = (int)(getWidth() * ((double)SAVES_IMAGE_BORDER_WIDTH / SAVES_BACKGROUND_IMAGE_TOTAL_WIDTH));
        int insetY = (int)(getHeight() * ((double)SAVES_IMAGE_BORDER_HEIGHT / SAVES_BACKGROUND_IMAGE_TOTAL_HEIGHT));
        
        g.drawImage(SAVES_CONTENT_IMAGE, insetX, insetY, getWidth() - insetX * 2, getHeight() - insetY * 2, null);
    }
}