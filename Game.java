package com.ninigo2;

/**
 * Created by Yang on 2016/4/16.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Yang on 2016/4/5.
 */
public class Game extends JFrame{
    private JMenuBar menuBar;
    private JMenu sysMenu;
    private JMenuItem startMenuItem,exitMenuItem;

    private Pan pan;

    public Game()
    {
        pan=new Pan();
        setTitle("FiveGo");
        Container container=getContentPane();
        container.add(pan);
        pan.setOpaque(true);

        menuBar=new JMenuBar();
        sysMenu=new JMenu("系统");
        startMenuItem=new JMenuItem("重新开始");
        exitMenuItem=new JMenuItem("退出");
       // backMenuItem=new JMenuItem("悔棋");
        sysMenu.add(startMenuItem);
        sysMenu.add(exitMenuItem);
        MyItemListener listener=new MyItemListener();
        this.startMenuItem.addActionListener(listener);
      //  this.backMenuItem.addActionListener(listener);
        this.exitMenuItem.addActionListener(listener);
        menuBar.add(sysMenu);
        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(800,700);
    }

    public void restart()
    {
        pan.restart();
    }

    private class MyItemListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Object obj=e.getSource();
            if(obj==exitMenuItem)
                System.exit(0);
            else if(obj==startMenuItem)
            {
                restart();
            }
            else {
                System.out.println("Something is ready");
            }
        }
    }
}
