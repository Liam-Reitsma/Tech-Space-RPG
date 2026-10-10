package game.options.view;

import game.audio.VolumeType;

import java.util.Map;

public interface OptionsViewAction
{
    void setNewVolume(Map<VolumeType, Integer> volumeMap);
}