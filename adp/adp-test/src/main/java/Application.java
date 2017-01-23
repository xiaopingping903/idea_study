import com.haier.adp.config.model.Config;
import com.haier.adp.config.service.ConfigReadService;
import com.haier.adp.jira.dto.BugStatisticsInfo;
import com.haier.adp.kpi.service.KpiService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;

/**
 * TO-DO
 * Author: bandd
 * Mailto:bandd@haier.com
 * On: 2016/7/1
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "dubbo-consumer.xml" });
        context.start();

        KpiService kpiService = (KpiService) context.getBean("kpiService");
        List<BugStatisticsInfo> bugStatisticsInfos = kpiService.getBugStatistics("SAPSRM",new Date("2016/01/01"), new Date("2016/10/31"));
        for (BugStatisticsInfo info : bugStatisticsInfos) {
            System.out.println(String.format("Bug Level: %s, Quantity: %d, Link: %s", info.getLevel(), info.getQuantity(), info.getLink()));
        }
    }
}