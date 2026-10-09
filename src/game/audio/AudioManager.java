package game.audio;

import static javax.sound.sampled.FloatControl.Type.MASTER_GAIN;

import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.Type;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioManager
{
    private static AudioManager audioManager;
    private Clip currentMusic;

    private final Map<VolumeType, Float> volumeMap;
    
    private AudioManager()
    {
        volumeMap = new HashMap<>();
        volumeMap.put(VolumeType.MASTER, 1.0f);
        volumeMap.put(VolumeType.MUSIC, 1.0f);
        volumeMap.put(VolumeType.SOUND, 1.0f);
    }
    
    public static AudioManager getInstance()
    {
        if(audioManager == null)
            audioManager = new AudioManager();
        
        return audioManager;
    }
    
    public void playSound(SoundEffect sound)
    {
        try(AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(sound.getUrlPath())))
        {
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(e ->
            {
                if(e.getType() == Type.STOP)
                    clip.close();
            });
            clip.open(audioStream);
            applyVolume(clip, VolumeType.SOUND);
            clip.start();
        } catch(UnsupportedAudioFileException | LineUnavailableException | IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public void playMusic(MusicTrack music)
    {
        stopMusic();
        try(AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(music.getUrlPath())))
        {
            currentMusic = AudioSystem.getClip();
            currentMusic.open(audioStream);
            applyVolume(currentMusic, VolumeType.MUSIC);
            currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(UnsupportedAudioFileException | LineUnavailableException | IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public void stopMusic()
    {
        if(currentMusic != null)
        {
            currentMusic.stop();
            currentMusic.close();
            currentMusic = null;
        }
    }
    
    public void setVolume(VolumeType volumeType, int newVolume)
    {
        volumeMap.replace(volumeType, Math.clamp(newVolume / 100f, 0.0f, 1.0f));
        
        if((volumeType == VolumeType.MUSIC || volumeType == VolumeType.MASTER) && currentMusic != null)
            applyVolume(currentMusic, VolumeType.MUSIC);
    }
    
    private void applyVolume(Clip clip, VolumeType volumeType)
    {
        float volume = volumeMap.get(VolumeType.MASTER) * volumeMap.get(volumeType);
        if(clip.isControlSupported(MASTER_GAIN) && clip.getControl(MASTER_GAIN) instanceof FloatControl floatControl)
        {
            float gain;
            if(volume == 0.0f)
                gain = floatControl.getMinimum();
            else
                gain = 20.0f * (float)Math.log10(volume);
            
            floatControl.setValue(Math.clamp(gain, floatControl.getMinimum(), floatControl.getMaximum()));
        }
    }
}