package game.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager
{
    private static AudioManager audioManager;
    private Clip currentMusic;
    
    private AudioManager()
    {
    
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
                if(e.getType() == LineEvent.Type.STOP)
                    clip.close();
            });
            clip.open(audioStream);
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
}