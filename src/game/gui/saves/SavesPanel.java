package game.gui.saves;

import static game.gui.GUIConfig.PROGRAM_HEIGHT;
import static game.gui.GUIConfig.PROGRAM_WIDTH;

import game.gui.PanelButton;
import game.saves.SavesAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;

public class SavesPanel extends JPanel
{
    private static final Image SAVES_CONTENT_IMAGE = new ImageIcon("resources/images/Saves Content.png").getImage();
    private static final Image SAVES_BORDER_IMAGE = new ImageIcon("resources/images/Saves Border.png").getImage();
    private static final int SAVES_BACKGROUND_IMAGE_TOTAL_WIDTH = SAVES_BORDER_IMAGE.getWidth(null);
    private static final int SAVES_BACKGROUND_IMAGE_TOTAL_HEIGHT = SAVES_BORDER_IMAGE.getHeight(null);
    
    private static final int SAVES_IMAGE_BORDER_WIDTH = (SAVES_BACKGROUND_IMAGE_TOTAL_WIDTH - SAVES_CONTENT_IMAGE.getWidth(null)) / 2;
    private static final int SAVES_IMAGE_BORDER_HEIGHT = (SAVES_BACKGROUND_IMAGE_TOTAL_HEIGHT - SAVES_CONTENT_IMAGE.getHeight(null)) / 2;
    
    private static final int LIST_PREFERRED_WIDTH = 400;
    private static final int LIST_PREFERRED_HEIGHT = 200;
    
    private static final int BUTTON_PREFERRED_WIDTH = 200;
    private static final int BUTTON_PREFERRED_HEIGHT = 45;
    
    private static final int LABEL_FONT_SIZE = 120;
    
    public SavesPanel(SavesAction savesAction, String[] saveNames)
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        
        setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> saveList = new JList<>(listModel);
        
        createSaveList(saveNames, saveList, listModel);
        
        gbConstraints.anchor = GridBagConstraints.SOUTHEAST;
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.gridy = 1;
        gbConstraints.weightx = 1.0;
        gbConstraints.weighty = 1.0;
        
        ActionListener saveButtonListener = e ->
        {
            String selectedSaveName = saveList.getSelectedValue();
            
            if(selectedSaveName != null)
            {
                savesAction.requestSelectSave(selectedSaveName);
            }
        };
        PanelButton selectSaveButton = new PanelButton("Select", saveButtonListener);
        selectSaveButton.resizeButton(200, 50);
        add(selectSaveButton, gbConstraints);
        
        addMouseListener(new MouseAdapter() {});
        addComponentListener(new ComponentAdapter()
        {
            @Override public void componentResized(ComponentEvent e)
            {
            Container parent = getParent();
            
            double panelWidth = parent.getWidth();
            double panelHeight = parent.getHeight();
            
            for(Component component : getComponents())
            {
                if(component instanceof PanelButton button)
                {
                    button.resizeButton((float)((BUTTON_PREFERRED_WIDTH * panelWidth) / PROGRAM_WIDTH), ((float)(BUTTON_PREFERRED_HEIGHT * panelHeight) / PROGRAM_HEIGHT));
                    button.setFont(button.getFont().deriveFont((float)(LABEL_FONT_SIZE / 3f * panelWidth) / PROGRAM_WIDTH));
                }
                else if(component instanceof JList<?> list)
                {
                    float fontSize = (float)(LABEL_FONT_SIZE / 2f * panelWidth) / PROGRAM_WIDTH;
                    Font newFont = list.getFont().deriveFont(fontSize);
                    list.setFont(newFont);
                    
                    int rowHeight = list.getFontMetrics(newFont).getHeight();
                    list.setFixedCellHeight(rowHeight);
                    
                    int listWidth = (int) Math.round(LIST_PREFERRED_WIDTH * 1.5f * (panelWidth / PROGRAM_WIDTH));
                    int listHeight = rowHeight * list.getModel().getSize();
                    
                    list.setPreferredSize(new Dimension(listWidth, listHeight));
                }
            }
            
            revalidate();
            repaint();
        }
    });
    }
    
    private void createSaveList(String[] saveNames, JList<String> saveList, DefaultListModel<String> listModel)
    {
        GridBagConstraints gbConstraints = new GridBagConstraints();
        
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        gbConstraints.weightx = 1;
        
        add(saveList, gbConstraints);
        
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