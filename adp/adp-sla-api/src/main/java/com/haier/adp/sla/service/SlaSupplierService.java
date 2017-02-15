package com.haier.adp.sla.service;

import com.haier.adp.sla.dto.SlaSupplierDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by rsdeep on 2017/1/23.
 */
public interface SlaSupplierService {
    List<SlaSupplierDTO> getSlaSupplierList(Map map);

    String addSlaSupplier(Map map);

    String delSlaSupplier(Map map);
}
