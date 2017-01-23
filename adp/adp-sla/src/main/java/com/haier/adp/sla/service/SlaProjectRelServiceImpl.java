package com.haier.adp.sla.service;

import com.haier.adp.sla.dao.SlaProjectRelDAO;
import com.haier.adp.sla.dto.SlaProjectRelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rsdeep on 2016/12/28.
 */
@Service
@Slf4j
public class SlaProjectRelServiceImpl implements SlaProjectRelService {
    @Autowired
    private SlaProjectRelDAO slaProjectRelDAO;
    @Override
    public List<SlaProjectRelDTO> getRelList(SlaProjectRelDTO searchDTO) {
        List<SlaProjectRelDTO> result = slaProjectRelDAO.search(searchDTO);
        return result;
    }

    @Override
    public int querySlaProjectRelCount(SlaProjectRelDTO searchDTO) {
        return 0;
    }
    @Override
    public int insertSlaProjectRel(SlaProjectRelDTO dto){
        int relId = slaProjectRelDAO.insertSlaProjectRel(dto);
        return relId;
    }
    @Override
    public void updateRel(List<SlaProjectRelDTO> rel, int listId) {

    }
}
