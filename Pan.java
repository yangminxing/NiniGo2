package com.ninigo2;

/**
 * Created by Yang on 2016/4/12.
 */
import com.ninigo2.ai.Evc;
import com.ninigo2.ai.IPlayer;
import com.ninigo2.ai.Player;
import com.ninigo2.aixml.EvcPhrase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yang on 2016/4/5.
 */
public class Pan extends JPanel implements MouseListener
{
    private static final int MARGIN=35;
    private static final int GRID_SPAN=35;
    public  static final int ROWS=15;
    public static final int COLS=15;

    //人
    private static final Character ENCHARACTER='B';

    private static final Character MYCHARACTER='W';

    //我方已下棋子的点集合
    private List<Cross> myCoses=new LinkedList<Cross>();
    //对方已下棋子的点集合
    private List<Cross> enCoses=new LinkedList<Cross>();

    private IPlayer computer=new Player(MYCHARACTER);

    public Pan()
    {
        addMouseListener(this);
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            public void mouseMoved(MouseEvent e) {
                int x1 = (e.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
                int y1 = (e.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
                if (x1 < 0 || x1 > ROWS || y1 < 0 || y1 > COLS ||GetCross(x1,y1)!=null )
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                else
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
    }

    /**
     * 渲染图像
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for(int i=0;i<=ROWS;i++)
            g.drawLine(MARGIN,MARGIN+i*GRID_SPAN,MARGIN+COLS*GRID_SPAN,MARGIN+i*GRID_SPAN);

        for(int i=0;i<=COLS;i++)
            g.drawLine(MARGIN+i*GRID_SPAN,MARGIN,MARGIN+i*GRID_SPAN,MARGIN+ROWS*GRID_SPAN);

        List<Cross> crosses=new LinkedList<Cross>();
        crosses.addAll(myCoses);
        crosses.addAll(enCoses);
        for(Cross cross:crosses)
        {
            //根据花色下棋
            int xPos=cross.getX()*GRID_SPAN+MARGIN;
            int yPos=cross.getY()*GRID_SPAN+MARGIN;
            if(cross.getValue()=='B')
            {
                RadialGradientPaint paint = new RadialGradientPaint(xPos-35/2+25, yPos-35/2+10, 20, new float[]{0f, 1f}
                        , new Color[]{Color.WHITE, Color.BLACK});
                ((Graphics2D) g).setPaint(paint);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

            }
            else if(cross.getValue()=='W')
            {
                RadialGradientPaint paint = new RadialGradientPaint(xPos-35/2+25, yPos-35/2+10, 70, new float[]{0f, 1f}
                        , new Color[]{Color.WHITE, Color.BLACK});
                ((Graphics2D) g).setPaint(paint);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
            }

            Ellipse2D e = new Ellipse2D.Float(xPos-35/2, yPos-35/2, 34, 35);
            ((Graphics2D) g).fill(e);
        }
    }


    /**
     * 鼠标左键按下
     */
    public void mousePressed(MouseEvent e)
    {
        int w=isWin();
        if(w!=0)
            return;

        int x1 = (e.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
        int y1 = (e.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;

        if (x1 < 0 || x1 > ROWS || y1 < 0 || y1 > COLS || GetCross(x1,y1)!=null)
            return;

        enCoses.add(new Cross(x1,y1,ENCHARACTER));
        repaint();
        w=isWin();
        if(w!=0)
        {
            String msg="";
            if(MYCHARACTER=='W')
            {
                if(w==1)
                    msg="白胜利";
                else
                    msg="黑胜利";
            }

            if(MYCHARACTER=='B')
            {
                if(w==1)
                    msg="黑胜利";
                else
                    msg="白胜利";
            }

            JOptionPane.showMessageDialog(this,msg);
            return;
        }

        Cross cross=computer.go(this);
        if(cross==null)
        {
            JOptionPane.showMessageDialog(this,"居然已经无子可下，遇到高手了,我要关机了，88！");
            System.exit(0);
        }
        else
        {
            myCoses.add(new Cross(cross.getX(),cross.getY(),MYCHARACTER));
            //points.add(new Point(computerPoint.getX(),computerPoint.getY(),'W'));
        }

        w=isWin();
        if(w!=0)
        {
            String msg="";
            if(MYCHARACTER=='W')
            {
                if(w==1)
                    msg="白胜利";
                else
                    msg="黑胜利";
            }

            if(MYCHARACTER=='B')
            {
                if(w==1)
                    msg="黑胜利";
                else
                    msg="白胜利";
            }

            JOptionPane.showMessageDialog(this,msg);
            return;
        }

    }

    public void mouseClicked(MouseEvent e)
    {


    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {

    }

    public void mouseReleased(MouseEvent e)
    {

    }

    /**
     * 判断棋局是否有人胜利 0 无人赢 1 MyCharacter 2 EnCharacter
     */
    public int isWin()
    {
        EvcPhrase evcPhrase1=new EvcPhrase(MYCHARACTER);
        Evc winEvc=evcPhrase1.GetWinEvc();
        Evc falEvc=evcPhrase1.GetFailedEvc();
        List<Cross> crosses=new LinkedList<Cross>();
        crosses.addAll(myCoses);
        crosses.addAll(enCoses);
        for(Cross myCos:crosses) {
            if(computer.CompareEvc(myCos,winEvc)!=-1)
                return 1;
            if(computer.CompareEvc(myCos,falEvc)!=-1)
                return 2;
        }
        return 0;
    }

    /**
     * 根据坐标点获取棋子
     */
    private Cross GetCross(int x,int y)
    {
        //先从my下的棋子中找 再从en下的棋子中找
        List<Cross> crosses=new LinkedList<Cross>();
        crosses.addAll(myCoses);
        crosses.addAll(enCoses);
        for(Cross myCos:crosses)
        {
            if(myCos.getX()==x&&myCos.getY()==y)
                return myCos;
        }

        return null;
    }

    public void restart()
    {
        enCoses.clear();
        myCoses.clear();

        repaint();
    }

    public List<Cross> getMyCoses() {
        return myCoses;
    }

    public List<Cross> getEnCoses() {
        return enCoses;
    }


}
