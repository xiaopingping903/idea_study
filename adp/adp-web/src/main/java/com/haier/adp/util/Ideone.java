package com.haier.adp.util;

import com.haier.adp.sla.dto.PaasOutageDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */
public class Ideone {
    public List<MyTime> getSlaOutageTime(List<PaasOutageDTO> paasOutageDTOlist) throws Exception {
        List<MyTime> myTimeList = new ArrayList<MyTime>();
        for (int i = 0; i < paasOutageDTOlist.size(); i++) {
            PaasOutageDTO sla = paasOutageDTOlist.get(i);
            myTimeList.add(new MyTime(sla.getDowntimeBegin().getTime(), sla.getDowntimeEnd().getTime()));
        }
        Collections.sort(myTimeList);
        return myTimeList;
    }
}

class MyTime implements Comparable<MyTime>
{
    private long start;
    private long end;
    MyTime(){}
    MyTime(long start, long end)
    {
        this.start = start;
        this.end = end;
    }
    public long getStart()
    {
        return start;
    }
    public long getEnd()
    {
        return end;
    }
    public int compareTo(MyTime other)
    {
        if (start == other.start)
        {
            return (int)(other.end - end);
        }
        return (int)(start - other.start);
    }
}
