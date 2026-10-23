package game.options.controller;

import game.audio.AudioManager;
import game.audio.VolumeType;
import game.frame.controller.FrameAction;
import game.gui.config.DisplayResolution;
import game.options.view.OptionsViewAction;

import java.util.HashMap;
import java.util.Map;

public class OptionsController implements OptionsAction
{
    private DisplayResolution displayResolution;
    
    private final AudioManager audioManager;
    private final Map<VolumeType, Integer> pendingVolumeMap;
    
    private final FrameAction frameAction;
    
    private OptionsViewAction optionsViewAction;
    
    public OptionsController(FrameAction fAction)
    {
        pendingVolumeMap = new HashMap<>();
        for(VolumeType volumeType : VolumeType.values())
            pendingVolumeMap.put(volumeType, 100);
        
        audioManager = AudioManager.getInstance();
        frameAction = fAction;
    }
    
    public void setOptionsViewAction(OptionsViewAction optionsVA) { optionsViewAction = optionsVA; }

    /**
     * Applies pending volume settings and changes the resolution if one has been selected.
     * Updates the view's stored applied volume values, allowing sliders to reset to them later.
     */
    @Override
    public void requestApplyChanges()
    {
        for(VolumeType volumeType : VolumeType.values())
            audioManager.setVolume(volumeType, pendingVolumeMap.get(volumeType));
        
        if(displayResolution != null)
            frameAction.requestResolutionChange(displayResolution);
        
        optionsViewAction.setNewVolume(pendingVolumeMap);
    }
    
    @Override
    public void requestCloseOptions()
    {
        frameAction.requestCloseOverlay();
    }

    /**
     * Stores the pending resolution setting without immediately applying it.
     *
     * @param resolution The pending resolution.
     */
    @Override
    public void requestSetDisplayResolution(DisplayResolution resolution)
    {
        displayResolution = resolution;
    }

    /**
     * Stores the pending volume setting without immediately applying it.
     *
     * @param volumeType The volume category to update.
     * @param volume The pending volume value (0–100).
     */
    @Override
    public void requestSetVolume(VolumeType volumeType, int volume)
    {
        pendingVolumeMap.replace(volumeType, volume);
    }
}