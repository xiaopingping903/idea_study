package com.haier.adp.sla.service;

import com.haier.adp.sla.dao.SlaBugInfoDAO;
import com.haier.adp.sla.dto.BugInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by rsdeep on 2016/12/26.
 */
public class SlaBugInfoServiceImpl implements SlaBugInfoService {
    @Autowired
    private SlaBugInfoDAO slaBugInfoDAO;
    @Override
    public List<BugInfoDTO> getBugInfoList(BugInfoDTO searchDTO) {
        List<BugInfoDTO> result = slaBugInfoDAO.search(searchDTO);
        return result;
    }

    @Override
    public int querySlaBugInfoCount(BugInfoDTO searchDTO) {
        int bugCount = slaBugInfoDAO.queryCount(searchDTO);
        return bugCount;
    }
    @Override
    public int insertBugInfo(BugInfoDTO insertDTO){
        int bugId = slaBugInfoDAO.insertBugInfo(insertDTO);
        return bugId;
    }
    @Override
    public void updateBugListId(List<BugInfoDTO> list, int listId){
        for (BugInfoDTO info : list) {
            info.setListId(listId);
            slaBugInfoDAO.insertBugInfo(info);
        }
    }
}
