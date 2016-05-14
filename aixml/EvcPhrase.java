package com.ninigo2.aixml;

import com.ninigo2.ai.Evc;
import org.xml.sax.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yang on 2016/4/11.
 */
public class EvcPhrase {

    private Character mycharacter;

    public EvcPhrase(Character myCharacter)
    {
        this.mycharacter=myCharacter;
    }

    public Evc GetWinEvc()
    {
        List<Evc> list=GetAllEvcA();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getRank()==9999)
               return new Evc(list.get(i).getRank(), list.get(i).getCompare().replace('1',mycharacter));
        }

        return null;
    }

    public Evc GetFailedEvc()
    {
        List<Evc> list=GetAllEvcA();
        for(int i=0;i<list.size();i++)
            if(list.get(i).getRank()==9999)
                return new Evc(list.get(i).getRank(),list.get(i).getCompare().replace('1',mycharacter=='B'?'W':'B'));
        return null;
    }

    /**
     * 取得可获胜情况的比较
     */
    public List<Evc> GetAttackEvc()
    {
        List<Evc> list=GetAllEvcA();
        List<Evc> alist=new LinkedList<Evc>();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getRank()<9000)
                alist.add(new Evc(list.get(i).getRank(), list.get(i).getCompare().replace('1',mycharacter)));
        }
        return alist;
    }

    /**
     * 取得可获胜情况的比较 Ranked
     */
    public List<Evc> GetAttackRankedEvc(int rank)
    {
        List<Evc> list=GetAttackEvc();
        return GetRankedEvc(list,rank);
    }

    /**
     * 取得防御情况的比较
     */
    public List<Evc> GetDefenseEvc()
    {
        List<Evc> list=GetAttackEvc();
        List<Evc> alist=new LinkedList<Evc>();
        for(int i=0;i<list.size();i++)
            if(list.get(i).getRank()<9000)
            alist.add(new Evc(list.get(i).getRank(),list.get(i).getCompare().replace(mycharacter,mycharacter=='B'?'W':'B')));
        return alist;
    }

    /**
     * 取得防御情况的比较 Ranked
     */
    public List<Evc> GetDefenseRankedEvc(int rank)
    {
        List<Evc> list=GetDefenseEvc();
        return GetRankedEvc(list,rank);
    }

    private List<Evc> GetRankedEvc(List<Evc> list,int rank)
    {
        List<Evc> alist=new LinkedList<Evc>();
        for(Evc e:list)
        {
            int r=e.getRank();
            if(r==rank)
            {
                alist.add(new Evc(r,e.getCompare()));
            }
        }
        if(alist.size()>0)
        {
            return alist;
        }
        else
        {
            return null;
        }
    }

    /**
     * 取得所有比较情况
     */
    private List<Evc> GetAllEvcA()
    {
        try
        {
            List<Evc> list = new LinkedList<Evc>();
            XMLPhrase.phraseA(list);
            return list;
        }
        catch (Exception e)
        {
            System.out.println("Error to xml phrase" +e);
        }
        return null;
    }

    private List<Evc> GetAllEvcD()
    {
        try
        {
            List<Evc> list = new LinkedList<Evc>();
            XMLPhrase.phraseD(list);
            return list;
        }
        catch (Exception e)
        {
            System.out.println("Error to xml phrase" +e);
        }
        return null;
    }
}
