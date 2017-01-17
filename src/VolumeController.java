
import javax.sound.sampled.*;
public class VolumeController {

    VolumeController()
    {
        Mixer.Info []list=AudioSystem.getMixerInfo();
        for(Mixer.Info info: list)
        {
            Mixer mixer=AudioSystem.getMixer(info);
            Line.Info list2[]=mixer.getTargetLineInfo();
            for(Line.Info lin:list2)
            {
                try{
                    Line line=mixer.getLine(lin);
                    if(!line.isOpen()||line instanceof Clip)line.open();
                  System.out.println(((FloatControl)line.getControl(FloatControl.Type.VOLUME)).getValue());
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String arg[])
    {
        new VolumeController();
    }

}
