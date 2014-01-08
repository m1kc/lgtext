package lgtext;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.io.file.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author Makc
 */
public class LGText extends MIDlet implements CommandListener
{
    
    static boolean firstrun = true;

    static Display d;
    static MainCanvas c;
    static FileManager fm;
    static LGText midlet;

    static Form sf;
    public TextField tf;
    public Command save = new Command("Сохранить",Command.OK,1);
    public Command cancel = new Command("Отмена",Command.OK,1);
    
    public boolean quicksaveIsPossible = false;
    public String nameForQuicksave = null;

    public void startApp() 
    {
        if (!firstrun) return;
        firstrun = false;

        midlet = this;
        d = Display.getDisplay(this);
        c = new MainCanvas();
        fm = new FileManager();

        sf = new Form("Сохранение файла");
        tf = new TextField("Имя файла:","textfile.txt",100,TextField.ANY);
        sf.append(tf);
        sf.addCommand(save);
        sf.addCommand(cancel);
        sf.setCommandListener(this);

        d.setCurrent(c);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command comm, Displayable disp)
    {
        if (comm==save)
        {
            saveFile("file:///"+fm.path+tf.getString());
            setFilename(tf.getString());
            d.setCurrent(c);
        }
        if (comm==cancel)
        {
            d.setCurrent(c);
        }
    }

    public void saveFile(String filename)
    {
        try 
        {
            FileConnection fc = (FileConnection) Connector.open(filename);
            if (fc.exists()) fc.delete();
            fc.close();
            fc = null;
            System.gc();

            fc = (FileConnection) Connector.open(filename);
            fc.create();
            OutputStream os = fc.openOutputStream();
            for (int i = 0; i < c.s.length(); i++)
            {
                int j = c.s.charAt(i);
                if (j > 128) j -= 848;
                os.write(j);
            }
            os.flush();

            os.close();
            os = null;
            System.gc();
            fc.close();
            fc = null;
            System.gc();
        }
        catch (Throwable ex)
        {
            // Мы отнесемся к этому философски
        }
    }

    public void quicksave()
    {
        if (quicksaveIsPossible)
        {
            saveFile(nameForQuicksave);
        }
        else
        {
            c.setMessage("Не задано имя файла.");
        }
    }

    public void setFilename(String s)
    {
        tf.setString(s);
        this.nameForQuicksave = "file:///"+fm.path+tf.getString();
        quicksaveIsPossible = true;
    }


}
