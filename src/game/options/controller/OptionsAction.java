package game.options.controller;

import game.audio.VolumeType;
import game.gui.config.DisplayResolution;

public interface OptionsAction
{
    void requestApplyChanges();
    void requestCloseOptions();
    void requestSetDisplayResolution(DisplayResolution resolution);
    void requestSetVolume(VolumeType volumeType, int volume);
}