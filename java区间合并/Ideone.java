package com.haier.adp.sla.util;

import com.haier.adp.sla.dto.SlaOutageDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by Administrator on 2017/1/4.
 */
public class Ideone {
    public int getTotalTime(List<SlaOutageDTO> slaOutageDTOList) throws java.lang.Exception
    {
        List<MyTime> myTimeList = new ArrayList<MyTime>();
        for (int i = 0; i < slaOutageDTOList.size(); i++) {
            SlaOutageDTO sla = slaOutageDTOList.get(i);
            myTimeList.add(new MyTime((int) sla.getOutageStartDate().getTime(), (int) sla.getOutageEndDate().getTime()));
        }
        Collections.sort(myTimeList);

        System.out.println(myTimeList.size());

        Stack s = new Stack();
        Stack e = new Stack();
        s.push(0);
        e.push(0);
        for (MyTime time : myTimeList)
        {
            System.out.println(time.getStart() + " " + time.getEnd());
            if (time.getStart() > time.getEnd())
                throw new Exception("The time is incorrect.");

            if (time.getStart() > (int)e.peek()) //没有交集
            {
                s.push(time.getStart());
                e.push(time.getEnd());
            }
            else if (time.getEnd() > (int)e.peek()) //有部分交集，取并集
            {
                e.pop();
                e.push(time.getEnd());
            }
            //else {} //完全被覆盖
        }

        int total = 0;
        while (!s.empty())
        {
            System.out.println(s.peek() + " ~ " + e.peek());
            total += (int)e.pop() - (int)s.pop();
        }
        System.out.println("Total: " + total);
        return total;
    }
}

class MyTime implements Comparable<MyTime>
{
    private int start;
    private int end;
    MyTime(){}
    MyTime(int start, int end)
    {
        this.start = start;
        this.end = end;
    }
    public int getStart()
    {
        return start;
    }
    public int getEnd()
    {
        return end;
    }
    public int compareTo(MyTime other)
    {
        if (start == other.start)
        {
            return other.end - end;
        }
        return start - other.start;
    }
}
