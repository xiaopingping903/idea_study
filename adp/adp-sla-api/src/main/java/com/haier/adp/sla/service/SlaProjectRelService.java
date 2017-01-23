package com.haier.adp.sla.service;

import com.haier.adp.sla.dto.SlaProjectRelDTO;

import java.util.List;

/**
 * Created by rsdeep on 2016/12/27.
 */
public interface SlaProjectRelService {
    List<SlaProjectRelDTO> getRelList(SlaProjectRelDTO searchDTO);
    int querySlaProjectRelCount(SlaProjectRelDTO searchDTO);
    int insertSlaProjectRel(SlaProjectRelDTO dto);
    void updateRel(List<SlaProjectRelDTO> rel, int listId);
}
