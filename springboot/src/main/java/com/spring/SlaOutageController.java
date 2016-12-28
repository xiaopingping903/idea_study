package com.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by rsdeep on 2016/12/15.
 */
@Controller
@RequestMapping("/adp/sla/outage")
public class SlaOutageController {
    @Autowired
    private SlaOutageService slaOutageService;

    @RequestMapping(value = "/getSlaOutageList", method = RequestMethod.GET)
    public @ResponseBody
    List<SlaOutageDTO> getSlaOutageList() {
        return slaOutageService.getListByDs1();
    }

    @RequestMapping(value = "/getSlaOutageList2", method = RequestMethod.GET)
    public @ResponseBody
    List<SlaOutageDTO> getSlaOutageList2() {
        return slaOutageService.getList();
    }


}
