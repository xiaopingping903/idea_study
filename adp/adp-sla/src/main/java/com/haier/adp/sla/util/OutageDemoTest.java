package com.haier.adp.sla.util;

import com.haier.adp.sla.Application;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

/*import org.junit.Test;
import org.junit.runner.RunWith;*/

/**
 * Created by Administrator on 2017/1/9.
 */
/*@RunWith(SpringJUnit4ClassRunner.class)*/ // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringApplicationConfiguration(classes =Application.class) // 指定我们SpringBoot工程的Application启动类
@WebAppConfiguration
public class OutageDemoTest {
 /*    @Autowired
    private SlaProjectInfoService slaProjectInfoService;

    private SlaOutageService slaOutageService;*/
   /* @Test*/
    public void testInsertOutage() {
        System.out.println("dddddddddddddddddddddddddddddddddddddddddd");
      /*  Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String start_time = sdf.format(date) + " 00:00:00";
        String end_time = sdf.format(date) + " 12:12:12";
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long t1 = 0;
        long t2 = 0;
        Date randomDate1;
        Date randomDate2;
        String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        while (true) {

            randomDate1 = com.test.TestGenerator.randomDate(start_time, end_time);
            randomDate2 = com.test.TestGenerator.randomDate(start_time, end_time);
            t1 = randomDate1.getTime();
            t2 = randomDate2.getTime();
            if (t1 >= t2) {
            } else {
                break;
            }
        }
        System.out.println(sdf.format(randomDate1));
        System.out.println(sdf.format(randomDate2));
        for (int i = 4; i < 100; i++) {
            SlaOutageDTO sladto = new SlaOutageDTO();
            //获得项目信息列表
            List<SlaProjectInfoDTO> slaProjectInfoList = slaProjectInfoService.getSlaProjectInfoList(new HashMap());
            Random r = new Random();
            SlaProjectInfoDTO slaProjectInfo = slaProjectInfoList.get(r.nextInt(slaProjectInfoList.size()));
            sladto.setAlmShortName(slaProjectInfo.getAlmShortName());
            sladto.setAlmApplicationId(slaProjectInfo.getAlmApplicationId());
            sladto.setProjectId(slaProjectInfo.getProjectId());
            sladto.setProjectName(slaProjectInfo.getProjectName());
            sladto.setCreateTime(new Timestamp(new Date().getTime()));
            sladto.setOutageStartDate(new Timestamp(randomDate1.getTime()));
            sladto.setOutageEndDate(new Timestamp(randomDate2.getTime()));
            sladto.setServiceId(i + "");
            sladto.setServiceName(str[r.nextInt(str.length)]);
            int ti = (int) (t2 - t1) / 1000 / 60;
            sladto.setOutageTime(ti);
            sladto.setIfOvertime(ti > 30 ? "1" : "0");
            sladto.setIfNotRun("0");
            slaOutageService.insertSlaOutagelData(sladto);
        }*/
    }

  /*  @Test*/
    public void testName() throws Exception {
        System.out.println("eeeeeeeeeeeee");
    }
}
