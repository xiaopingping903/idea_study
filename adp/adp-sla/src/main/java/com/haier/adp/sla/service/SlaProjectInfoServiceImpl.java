package com.haier.adp.sla.service;


import com.haier.adp.sla.dao.*;
import com.haier.adp.sla.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2016/12/22.
 */
@Service(value="slaProjectInfoService")
public class SlaProjectInfoServiceImpl implements SlaProjectInfoService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SlaProjectInfoDAO slaProjectInfoDAO;
    @Autowired
    private SlaProjectUserInfoDAO slaProjectUserInfoDAO;

    /**
     * 插入
     * @param slaProjectInfoDTO
     */
    @Override
    public SlaProjectInfoDTO insertSlaProjectInfo(SlaProjectInfoDTO slaProjectInfoDTO) {
        try{
            slaProjectInfoDAO.insertSlaProjectInfo(slaProjectInfoDTO);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return slaProjectInfoDTO;
    }

    /**
     * 删除
     */
    @Override
    public void delProjectInfo(Map map) {
        try{
            slaProjectInfoDAO.delProjectInfo(map);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
    }

    @Override
    public List<SlaProjectInfoDTO> getSlaProjectInfoList(Map map) {
        List<SlaProjectInfoDTO> list=new ArrayList<SlaProjectInfoDTO>();
        try{
            list=slaProjectInfoDAO.getSlaProjectInfoList(map);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("数据访问资源失败");
        }
        return list;
    }
    @Override
    public List<SlaProjectInfoDTO> getSlaProjectInfoListByScode(Map map) {
        return slaProjectInfoDAO.getSlaProjectInfoListByScode(map);
    }

    @Override
    public void updateSlaProjectInfo(SlaProjectInfoDTO dto) {
        slaProjectInfoDAO.updateSlaProjectInfo(dto);
    }

    @Override
    public void insertSlaProjectUserInfo(SlaProjectUserInfoDTO userDto) {
        slaProjectUserInfoDAO.insertSlaProjectUserInfo(userDto);
    }

    @Override
    public void delProjectUserInfo(Integer id) {
        Map map=new HashMap();
        map.put("tSlaProjectInfoId",id);
        slaProjectUserInfoDAO.delRoleRel(map);
    }
}
