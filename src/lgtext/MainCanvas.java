package lgtext;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Vector;
import javax.microedition.lcdui.*;

/**
 * @author Makc
 */
public class MainCanvas extends Canvas
{
    //String s = "Строка 1\nСтрока 2\nСтрока 3. Она слишком длинная для того, чтобы уместиться на экране, поэтому она будет разбита на несколько.\nСтрока 4.\nyy\nttewew\n\neytee\newreerw\newerweqqew\nweqqewweqewqwqe\nmmm\nmmm\nmmm\nmmm\nmmm\nmmm\nmmm\nmmm\nmmm";
    String s = "";
    Vector keys = new Vector();

    boolean english = true;
    boolean symbolic = true;
    boolean shift = false;

    Font f = Font.getDefaultFont();

    int h = f.getHeight();

    int c = 0;

    boolean wink = true;

    int RoS;

    int startIndex = 0;

    boolean menuShown = false;
    String[] menu = new String[]{"Открыть","Сохранить","Быстрое сохранение","Выход"};
    int mc = 0;

    Vector strings = new Vector();
    Vector words = new Vector();
    Vector wrapped = new Vector();
    int startFrom = 0;
    int x = 0;
    int y = 0;

    String message = null;

    char[] chars = new char[]{'-',':','!','?','(',')','\"',';','+','_','/','\\','@','#','$','%','^','&','*','[',']','{','}','<','>','~'};
    int charNum = 0;
    boolean charing = false;
    long firstchar;

    /**
     * constructor
     */
    public MainCanvas()
    {
        setFullScreenMode(true);

        // <editor-fold defaultstate="collapsed" desc=" register? yes, sir! ">
        register(126, "~", "~");
        register(36, "$", "$");
        register(37, "%", "%");
        register(49, "1", "1");
        register(50, "2", "2");
        register(51, "3", "3");
        register(42, "*", "*");
        register(94, "х", "х");
        register(38, "ъ", "ъ");
        register(45, "ю", "ю");
        register(91, "[", "[");
        register(93, "]", "]");
        register(96, "`", "`");
        register(52, "4", "4");
        register(53, "5", "5");
        register(54, "6", "6");
        register(35, "#", "#");
        register(40, "(", "(");
        register(41, ")", ")");
        register(43, "+", "+");
        register(61, "=", "=");
        register(55, "7", "7");
        register(56, "8", "8");
        register(57, "9", "9");
        register(48, "0", "0");
        register(59, "ж", "ж");
        register(47, "@", "@");
        register(33, ",", ",");
        register(39, ".", ".");

        register(32, " ", " ");

        register(113, "й", "q");
        register(119, "ц", "w");
        register(101, "у", "e");
        register(114, "к", "r");
        register(116, "е", "t");
        register(121, "н", "y");
        register(117, "г", "u");
        register(105, "ш", "i");
        register(111, "щ", "o");
        register(112, "з", "p");
        register(97, "ф", "a");
        register(115, "ы", "s");
        register(100, "в", "d");
        register(102, "а", "f");
        register(103, "п", "g");
        register(104, "р", "h");
        register(106, "о", "j");
        register(107, "л", "k");
        register(108, "д", "l");
        register(122, "я", "z");
        register(120, "ч", "x");
        register(99, "с", "c");
        register(118, "м", "v");
        register(98, "и", "b");
        register(110, "т", "n");
        register(109, "ь", "m");
        register(63, "б", "?");
        register(64, "/", "/");
        register(44, "!", "!");
        register(46, "'", "'");

        register(81, "Й", "Q");
        register(87, "Ц", "W");
        register(69, "У", "E");
        register(82, "К", "R");
        register(84, "Е", "T");
        register(89, "Н", "Y");
        register(85, "Г", "U");
        register(73, "Ш", "I");
        register(79, "Щ", "O");
        register(80, "З", "P");
        register(65, "Ф", "A");
        register(83, "Ы", "S");
        register(68, "В", "D");
        register(70, "А", "F");
        register(71, "П", "G");
        register(72, "Р", "H");
        register(74, "О", "J");
        register(75, "Л", "K");
        register(76, "Д", "L");
        register(90, "Я", "Z");
        register(88, "Ч", "X");
        register(67, "С", "C");
        register(86, "М", "V");
        register(66, "И", "B");
        register(78, "Т", "N");
        register(77, "Ь", "M");

        register(-20,"\n","\n");
        // </editor-fold>

        Thread winking = new Thread(){
            public void run()
            {
                while(true)
                {
                    wink = !wink;
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException ex)
                    {
                    }
                }
            }
        };
        winking.start();

        RoS = (getHeight()-f.getHeight())/f.getHeight()-2;

        performWrap();
    }

    private void register(int code, String ru, String en)
    {
        keys.addElement(new Key(code, ru, en));
    }
    
    /**
     * paint
     */
    public void paint(Graphics g)
    {
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0x000000);

        //formatOut(s,g);
        formatOut(g);

        String t = "";
        String t2 = "";
        t2 = english?"[EN]":"[RU]";
        t = t + t2;
        t2 = shift?"[ABC]":"[abc]";
        t = t + t2;
        t2 = symbolic?"[num]":"[let]";
        t = t + t2;

        g.setColor(0xFFFFFF);
        g.fillRect(0, getHeight()-h, getWidth(), h);
        g.setColor(0x000000);
        g.drawLine(0, getHeight()-h, getWidth(), getHeight()-h);
        g.drawString(t, 0, getHeight(), Graphics.LEFT | Graphics.BOTTOM);
        g.drawString("Позиция: "+c+"/"+s.length(), getWidth(), getHeight(), Graphics.RIGHT | Graphics.BOTTOM);

        if (menuShown)
        {
            int start = 0;
            start = getHeight()/2;
            start -= menu.length*f.getHeight()/2;
            g.setColor(0xFFFFFF);
            g.fillRect(20, start, getWidth()-40, menu.length*f.getHeight());
            g.setColor(0x000000);
            g.drawRect(20, start, getWidth()-40, menu.length*f.getHeight());
            for (int i=0; i<menu.length; i++)
            {
                if (i != mc)
                {
                    g.setColor(0x000000);
                    g.drawString(menu[i], getWidth()/2, start+i*f.getHeight(), Graphics.HCENTER | Graphics.TOP);
                }
                else
                {
                    g.setColor(0x000000);
                    g.fillRect(20, start+i*f.getHeight(), getWidth()-40, f.getHeight());
                    g.setColor(0xFFFFFF);
                    g.drawString(menu[i], getWidth()/2, start+i*f.getHeight(), Graphics.HCENTER | Graphics.TOP);
                }
            }
        }

        if (message != null)
        {
            g.setColor(0xFFFFFF);
            g.fillRect(0, 0, getWidth(), f.getHeight()+10);
            g.setColor(0x000000);
            g.drawLine(0, f.getHeight()+10, getWidth(), f.getHeight()+10);
            g.drawString(message, 5, 5, Graphics.LEFT | Graphics.TOP);
        }

        if (charing)
        {
            g.setColor(0xFFFFFF);
            g.fillRect(0, 0, getWidth(), f.getHeight()+10);
            g.setColor(0x000000);
            g.drawLine(0, f.getHeight()+10, getWidth(), f.getHeight()+10);
            g.drawString(""+chars[charNum], getWidth()/2, 5, Graphics.HCENTER | Graphics.TOP);

            if (System.currentTimeMillis()-firstchar > 1000)
            {
                insert(""+chars[charNum]);
                charing = false;
            }
        }

        repaint();
    }
    
    /**
     * Called when a key is pressed.
     */
    protected  void keyPressed(int keyCode)
    {
        boolean skip = false;

        if ((keyCode==94)&&(symbolic)&&(shift)) { insert("Х"); skip = true; }
        if ((keyCode==94)&&(symbolic)&&(!shift)) { insert("х"); skip = true; }
        if ((keyCode==38)&&(symbolic)&&(shift)) { insert("Ъ"); skip = true; }
        if ((keyCode==38)&&(symbolic)&&(!shift)) { insert("ъ"); skip = true; }
        if ((keyCode==45)&&(symbolic)&&(shift)) { insert("Ю"); skip = true; }
        if ((keyCode==45)&&(symbolic)&&(!shift)) { insert("ю"); skip = true; }
        if ((keyCode==59)&&(symbolic)&&(shift)) { insert("Ж"); skip = true; }
        if ((keyCode==59)&&(symbolic)&&(!shift)) { insert("ж"); skip = true; }
        if ((keyCode==58)&&(symbolic)&&(shift)) { insert("Э"); skip = true; }
        if ((keyCode==58)&&(symbolic)&&(!shift)) { insert("э"); skip = true; }
        if ((keyCode==63)&&(!symbolic)&&(!english)&&(shift)) { insert("Б"); skip = true; }
        if ((keyCode==63)&&(!symbolic)&&(!english)&&(!shift)) { insert("б"); skip = true; }

        if (!skip)
        {
            for (int i=0; i<keys.size(); i++)
            {
                Key k = (Key) keys.elementAt(i);
                if (k.code==keyCode)
                {
                    if (english) insert(k.en);
                    if (!english) insert(k.ru);
                }
            }
        }

        if (keyCode==-101) symbolic = !symbolic;
        if (keyCode==0) english = !english;
        if (keyCode==-21) shift = !shift;

        if (keyCode==-16) backspace();

        //if (keyCode==-1) backspace();

        message = null;

        if (!menuShown)
        {
            checkRanges();
            int z = wrapped.elementAt(y).toString().length();
            if (keyCode==-1) c-=z;
            if (keyCode==-2) c+=z;
            moveCursor();
        }
        else
        {
            if (keyCode==-1) mc--;
            if (keyCode==-2) mc++;
            if (mc<0) mc=0;
            if (mc>=menu.length) mc = menu.length-1;
        }

        if (keyCode==-3) { c--; moveCursor(); }
        if (keyCode==-4) { c++; moveCursor(); }

        if (keyCode==-5)
        {
            if (!menuShown)
            {
                mc = 0;
            }
            else
            {
                if (mc==0)
                {
                    LGText.fm.openFile = true;
                    LGText.d.setCurrent(LGText.fm);
                }
                if (mc==1)
                {
                    LGText.fm.openFile = false;
                    LGText.d.setCurrent(LGText.fm);
                }
                if (mc==2)
                {
                    LGText.midlet.quicksave();
                }
                if (mc==3)
                {
                    LGText.midlet.notifyDestroyed();
                }
            }
            //
            menuShown = !menuShown;
        }

        if (keyCode==-6)
        {
            if (menuShown)
            {
                menuShown = false;
            }
            else
            {
                if (x==0)
                {
                    c+=(wrapped.elementAt(y).toString().length()-1);
                    moveCursor();
                }
                else
                {
                    c-=x;
                    moveCursor();
                }
            }
        }

        if (keyCode==-7)
        {
            if (menuShown)
            {
                menuShown = false;
            }
            else
            {
                if (c==0)
                {
                    c = s.length();
                    moveCursor();
                }
                else
                {
                    c = 0;
                    moveCursor();
                }
            }
        }

        if (keyCode==-100)
        {
            if (!charing)
            {
                charing = true;
                charNum = 0;
                firstchar = System.currentTimeMillis();
            }
            else
            {
                charNum++;
                if (charNum >= chars.length) charNum = 0;
                firstchar = System.currentTimeMillis();
            }
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

    private void formatOut(Graphics g)
    {
        String ww = wrapped.elementAt(y).toString();
        ww = ww.substring(0, x);
        int cx = 5+f.stringWidth(ww);

        g.setColor(0x000000);
        for(int i=startFrom; i<wrapped.size(); i++)
        {
            g.drawString(wrapped.elementAt(i).toString(), 5, 5+f.getHeight()*(i-startFrom), Graphics.LEFT|Graphics.TOP);
            if ((i==y)&&(wink)) g.fillRect(cx, 5+f.getHeight()*(i-startFrom), 2, f.getHeight());
        }

        if (y-startFrom > RoS)
        {
            startFrom++;
        }

        if (y < startFrom)
        {
            startFrom--;
            if (startFrom<0) startFrom = 0;
        }
    }

    private void insert(String string)
    {
        String s1="",s2="";

        try
        {
            s1 = s.substring(0, c);
        }
        catch (Throwable ex)
        {
            s1 = "";
        }

        try
        {
            s2 = s.substring(c);
        }
        catch (Throwable ex)
        {
            s2 = "";
        }
        
        s = s1+string+s2;
        c++;
        performWrap();
        moveCursor();
    }

    private void backspace()
    {
        String s1="",s2="";

        try
        {
            s1 = s.substring(0, c);
        }
        catch (Throwable ex)
        {
            s1 = "";
        }

        try
        {
            s2 = s.substring(c);
        }
        catch (Throwable ex)
        {
            s2 = "";
        }

        //s = s1+string+s2;
        s1 = s1.substring(0, s1.length()-1);
        s = s1+s2;
        c--;
        performWrap();
        moveCursor();
    }

    private void splitBy(String s, String splitter, Vector target)
    {
        int i;
        target.removeAllElements();
        while(true)
        {
            i = s.indexOf(splitter);
            if (i != -1)
            {
                target.addElement(s.substring(0,i));
                s = s.substring(i+1);
            }
            else
            {
                target.addElement(s);
                return;
            }
        }
    }

    public final void performWrap()
    {
        wrapped.removeAllElements();
        splitBy(s,"\n",strings);
        for (int i=0; i<strings.size(); i++)
        {
            words.removeAllElements();
            splitBy(strings.elementAt(i).toString()," ",words);
            String t = "";
            for (int j=0; j<words.size(); j++)
            {
                if (f.stringWidth(t+words.elementAt(j).toString()) <= getWidth()-10)
                {
                    t = t+words.elementAt(j).toString()+" ";
                }
                else
                {
                    wrapped.addElement(t);
                    t = words.elementAt(j).toString()+" ";
                }
            }
            wrapped.addElement(t);
        }
    }

    private void moveCursor()
    {
        if (c>s.length()) c=s.length();
        if (c<0) c=0;
        int z = c;
        int i = 0;
        while(true)
        {
            if (z>wrapped.elementAt(i).toString().length()-1)
            {
                z-=wrapped.elementAt(i).toString().length();
                i++;
            }
            else
            {
                x = z;
                y = i;
                checkRanges();
                return;
            }
        }
    }

    private void checkRanges()
    {
        if (y>=wrapped.size()) y = wrapped.size()-1;
        if (x>wrapped.elementAt(y).toString().length()-1) x=wrapped.elementAt(y).toString().length()-1;
    }

    public void setMessage(final String message)
    {
        this.message = message;
        Thread unmessage = new Thread(){
            public void run()
            {
                try
                {
                    Thread.sleep(2500);
                }
                catch (InterruptedException ex)
                {
                }
                clearMessage();
            }
        };
        unmessage.start();
    }

    public void clearMessage()
    {
        message = null;
    }

}