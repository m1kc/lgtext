package lgtext;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.InputStream;
import java.util.*;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;
import javax.microedition.io.file.*;

/**
 * @author Makc
 */
public final class FileManager extends Canvas
{
    String[] fs = new String[0];

    boolean openFile = true;

    Font f = Font.getDefaultFont();

    String path = "";

    Thread navigator;

    String[] none = new String[0];
    List list;

    /**
     * constructor
     */
    public FileManager()
    {
        setFullScreenMode(true);

        list = new List(none,f,0,f.getHeight()*2,getWidth(),getHeight()-f.getHeight()*3,5);

        resetNavigator();
        navigator.start();
    } 

    public void processEnumeration(Enumeration e)
    {
        Vector v = new Vector();
        while (e.hasMoreElements()) v.addElement(e.nextElement());
        fs = new String[v.size()];
        for (int i=0; i<v.size(); i++)
        {
            fs[i] = (String) v.elementAt(i);
        }
        if (v.isEmpty())
        {
            fs = new String[]{"Файлов нет."};
        }
        list.elements = fs;
    }

    /**
     * paint
     */
    public void paint(Graphics g)
    {
        g.setClip(0, 0, getWidth(), getHeight());
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, getWidth(), getHeight());

        list.paint(g);

        g.setClip(0, 0, getWidth(), getHeight());
        g.setColor(0x000000);
        g.drawString(path, 5, 0, Graphics.LEFT | Graphics.TOP);
        g.drawString("Нажмите \"влево\" для возврата на уровень вверх.", 5, f.getHeight(), Graphics.LEFT | Graphics.TOP);
        g.drawLine(0, f.getHeight()*2, getWidth(), f.getHeight()*2);
        
        g.setColor(0x000000);
        g.drawLine(0, getHeight()-f.getHeight(), getWidth(), getHeight()-f.getHeight());
        if (!openFile) g.drawString("Сохр. здесь", getWidth(), getHeight(), Graphics.RIGHT | Graphics.BOTTOM);
        g.drawString("Отмена", 0, getHeight(), Graphics.LEFT | Graphics.BOTTOM);

        repaint();
    }
    
    /**
     * Called when a key is pressed.
     */
    protected  void keyPressed(int keyCode)
    {
        if (keyCode==-1) list.s--;
        if (keyCode==-2) list.s++;
        if (list.s<0) list.s=fs.length-1;
        if (list.s>=fs.length) list.s=0;

        if ((keyCode==-3)&&(path.length()>0))
        {
            path = path.substring(0, path.length()-1);
            while (!(path.length()==0 || path.endsWith("/")))
            {
                path = path.substring(0, path.length()-1);
            }
            resetNavigator();
            navigator.start();
            list.s = 0;
        }

        if (keyCode==-5) 
        {
            if (fs[list.s].endsWith("/"))
            {
                path = path + fs[list.s];
                resetNavigator();
                navigator.start();
            }
            else if (openFile)
            {
                try 
                {
                    FileConnection fc = (FileConnection) Connector.open("file:///"+path + fs[list.s], Connector.READ);
                    InputStream is = fc.openInputStream();

                    String ss = "";
                    int l = is.available();
                    int j;
                    for (int i=0; i<l; i++)
                    {
                        j = is.read();
                        if (j>128) j+=848;
                        ss = ss + (char)j;
                    }
                    // Done!
                    LGText.c.s = ss;
                    LGText.c.performWrap();
                    LGText.c.c = LGText.c.x = LGText.c.y = 0;
                    LGText.d.setCurrent(LGText.c);
                    // And then??
                    // And then we will destroy this all. Without a sound.
                    is.close();
                    is = null;
                    System.gc();
                    fc.close();
                    fc = null;
                    System.gc();
                    // AND THEN???
                    LGText.midlet.setFilename(fs[list.s]);
                }
                catch (Throwable ex)
                {
                }
            }
            else if (!openFile)
            {
                LGText.midlet.tf.setString(fs[list.s]);
                LGText.d.setCurrent(LGText.sf);
            }
            list.s = 0;
        }

        if (keyCode==-6)
        {
            LGText.d.setCurrent(LGText.c);
        }

        if ((keyCode==-7)&&(!openFile))
        {
            LGText.d.setCurrent(LGText.sf);
        }
    }
    
    /**
     * Called when a key is released.
     */
    protected  void keyReleased(int keyCode)
    {
    }

    /**
     * Called when a key is repeated (held down).
     */
    protected  void keyRepeated(int keyCode)
    {
    }
    
    /**
     * Called when the pointer is dragged.
     */
    protected  void pointerDragged(int x, int y)
    {
    }

    /**
     * Called when the pointer is pressed.
     */
    protected  void pointerPressed(int x, int y)
    {
    }

    /**
     * Called when the pointer is released.
     */
    protected  void pointerReleased(int x, int y)
    {
    }

    public void resetNavigator()
    {
        navigator = new Thread(){
            public void run()
            {
                try
                {
                    if (path.hashCode()=="".hashCode())
                    {
                        Enumeration e = javax.microedition.io.file.FileSystemRegistry.listRoots();
                        processEnumeration(e);
                    }
                    else
                    {
                        FileConnection fc = (FileConnection) Connector.open("file:///"+path, Connector.READ);
                        //System.out.println("connected");
                        Enumeration e = fc.list();
                        //System.out.println("Readed");
                        processEnumeration(e);
                        //System.out.println("ok");
                    }
                }
                catch (Throwable ex)
                {
                }
            }
        };
    }

}