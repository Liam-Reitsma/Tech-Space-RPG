package game.options.controller;

import game.audio.VolumeType;
import game.gui.config.DisplayResolution;

import java.util.Map;

public interface OptionsAction
{
    void requestSetVolume(VolumeType volumeType, int volume);
    void requestSetDisplayResolution(DisplayResolution resolution);
    void requestApplyChanges();
    void requestCloseOptions();
}