package com.ninigo2.ai;
import com.ninigo2.Pan;
import com.ninigo2.*;
import com.ninigo2.aixml.EvcPhrase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yang on 2016/4/12.
 */
public class Player implements com.ninigo2.ai.IPlayer
{
    private Character myCharacter; //我的角色类型

    //我方已下棋子的点集合
    List<Cross> myCoses=new LinkedList<Cross>();
    //对方已下棋子的点集合
    List<Cross> enCoses=new LinkedList<Cross>();

    public Player(Character character)
    {
        this.myCharacter=character;
    }

    /**
     * 逻辑中循环
     */
    public Cross go(Pan pan)
    {
        EvcPhrase evcPhrase=new EvcPhrase(myCharacter);

        //获取当前落子
        myCoses=pan.getMyCoses();
        enCoses=pan.getEnCoses();

        for(int rank=100;rank>=10;rank--)
        {
            List<Evc> attackEvcList=evcPhrase.GetAttackRankedEvc(rank);
            List<Evc> defenseEvcList=evcPhrase.GetDefenseRankedEvc(rank);
            if(attackEvcList==null||defenseEvcList==null)
                continue;

            //同评级 攻击为主
            Cross okCos=FindPro(myCoses,attackEvcList);
            if(okCos!=null)
            {
                return okCos;
            }
            //开始防御
            okCos=FindPro(enCoses,defenseEvcList);
            if(okCos!=null)
            {
                return okCos;
            }
        }

        //暂时没有大局观 用点阵打法代替
        for(int i=4;i<11;i++)
        {
            i++;
            for(int j=4;j<11;j++)
            {
                j++;

                if(GetCross(i,j)==null)
                    return new Cross(i,j,myCharacter);
            }
        }

        System.out.println("我已经无字可以下，伐开心");

        return null;
    }

    /**
     * 根据不同落子和EvcList返回落子
     *
     */
    private Cross FindPro(List<Cross> crosses,List<Evc> evcList)
    {
        //评级操作记录
        List<Cross> operateCross=new LinkedList<Cross>();

        for(Cross cross:crosses)
        {
            for (Evc evc : evcList) {
                int d=CompareEvc(cross,evc);
                //说明匹配上了
                if(d!=-1)
                {

                    Cross mac=MakeCross(cross,d,evc);
                    //防止出界
                    if(mac.getY()>0&&mac.getX()>0&&mac.getX()<Pan.COLS&&mac.getY()<Pan.ROWS)
                        operateCross.add(mac);
                }
            }
        }

        if(operateCross.size()==0) {
            //没有匹配上
            return null;
        }
        //否则返回第一个
        return operateCross.get(0);
    }

    /**
     * 判断一个子是否匹配一个Evc，
     * 1 down 2 right 3 rd 4 ld
     * 1 向下，2 向右 ，3 向右下，4 向左下
     * 如果匹配返回方向值，否则为-1
     */
    public int CompareEvc(Cross cross,Evc evc)
    {
        //修正以Z或者T开头的情况
        int toffset=0;
        if(evc.getCompare().startsWith("Z")||evc.getCompare().startsWith("T"))
        {
            if(evc.getCompare().charAt(1)=='Z'||evc.getCompare().charAt(1)=='T')
            {
                toffset=-2;
            }
            else {
                toffset = -1;
            }
        }



        boolean downflag=true, rightflag=true, rdflag=true, ldflag=true;
        int x=cross.getX();
        int y=cross.getY();

        //根据Evc 逐个比对棋子
        for(int i=0;i<evc.getCompare().length();i++)
        {
            rightflag&=JudgeCross(evc.getCompare().charAt(i),x+i+toffset,y);
            downflag&=JudgeCross(evc.getCompare().charAt(i),x,y+i+toffset);
            rdflag&=JudgeCross(evc.getCompare().charAt(i),x+i+toffset,y+i+toffset);
            ldflag&=JudgeCross(evc.getCompare().charAt(i),x-i-toffset,y+i+toffset);
        }

        //斜着的 人一般不太好发现
        int r;
        if(ldflag)
            r=4;
        else if(rdflag)
            r=3;
        else if(rightflag)
            r=2;
        else if(downflag)
            r=1;
        else
            r= -1;//完全没有找到

        return r;
    }

    /**
     * 决定落子
     */
    private Cross MakeCross(Cross cos,int dir,Evc evc)
    {
        int toffset=0;
        if(evc.getCompare().startsWith("Z")||evc.getCompare().startsWith("T"))
        {
            if(evc.getCompare().charAt(1)=='Z'||evc.getCompare().charAt(1)=='T')
            {
                toffset=-2;
            }
            else {
                toffset = -1;
            }
        }

        toffset=toffset+evc.getCompare().indexOf('Z');

        //计算落点坐标
        int xoffset=0;
        int yoffset=0;
        switch (dir)
        {
            case 1:
            {
                yoffset=toffset;
                break;
            }

            case 2:
            {
                xoffset=toffset;
                break;
            }

            //左下
            case 4:
            {
                xoffset=-toffset;
                yoffset=toffset;
                break;
            }

            //右下
            case 3:
            {
                xoffset=toffset;
                yoffset=toffset;
                break;
            }
        }

        return new Cross(cos.getX()+xoffset,cos.getY()+yoffset,myCharacter);
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

    /**
     * 根据坐标点获取棋子 与Evc的某个Character判断逻辑
     */
    private boolean JudgeCross(Character c,int x,int y)
    {
        Cross curCos=GetCross(x,y);

        if(curCos==null)
        {
            //==null 说明为空子
            if(c.equals('T')||c.equals('Z'))
                return true;
            else
                return false;
        }

        if(c.equals(curCos.getValue()))
            return true;
        else
            return false;
    }

}
