/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lgtext;

import javax.microedition.lcdui.*;

/**
 *
 * @author Makc
 */
public class List
{
    public int bgcolor = 0xFFFFFF;
    public int fgcolor = 0x000000;
    String[] elements;
    int sy = 0;
    int ty = 0;
    Font f;
    int x,y,w,h;
    int margin;
    int s = 0;

    public List(String[] elements, Font f, int x, int y, int w, int h, int margin)
    {
        this.elements = elements;
        this.f = f;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.margin = margin;
    }

    public void paint(Graphics g)
    {
        g.setClip(x, y, w, h);
        g.setColor(bgcolor);
        g.fillRect(x, y, w, h);
        for (int i=0; i<elements.length; i++)
        {
            if (s!=i)
            {
                g.setColor(fgcolor);
                g.drawString(elements[i], x+margin, y+i*f.getHeight()-sy, Graphics.LEFT | Graphics.TOP);
            }
            else
            {
                g.setColor(fgcolor);
                g.fillRect(x, y+i*f.getHeight()-sy, w, f.getHeight());
                g.setColor(bgcolor);
                g.drawString(elements[i], x+margin, y+i*f.getHeight()-sy, Graphics.LEFT | Graphics.TOP);
            }
        }

        if (sy != ty)
        {
            int r = ty-sy;
            int delta = r/2;
            if (delta==0) delta=r;
            sy+=delta;
        }

        if (s*f.getHeight()-ty+f.getHeight() > h)
        {
            ty = s*f.getHeight()+f.getHeight()-h;
        }

        if (s*f.getHeight()-ty < 0)
        {
            ty = s*f.getHeight();
        }

        if (elements.length*f.getHeight() < h)
        {
            ty = 0;
        }
    }

}
