package game.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager
{
    private static AudioManager audioManager;
    
    private AudioManager()
    {
    
    }
    
    public static AudioManager getInstance()
    {
        if(audioManager == null)
            audioManager = new AudioManager();
        
        return audioManager;
    }
    
    public void playSound(String path)
    {
        File audioFile = new File(path);
        try
        {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch(UnsupportedAudioFileException | LineUnavailableException | IOException e)
        {
            throw new RuntimeException(e);
        }
        
    }
}