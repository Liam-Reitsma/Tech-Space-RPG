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
    
    @Override
    public void requestSetDisplayResolution(DisplayResolution resolution)
    {
        displayResolution = resolution;
    }
    
    @Override
    public void requestSetVolume(VolumeType volumeType, int volume)
    {
        pendingVolumeMap.replace(volumeType, volume);
    }
}