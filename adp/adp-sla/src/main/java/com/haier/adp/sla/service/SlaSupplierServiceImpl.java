package com.haier.adp.sla.service;

import com.haier.adp.sla.dao.SlaSupplierDAO;
import com.haier.adp.sla.dto.SlaSupplierDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rsdeep on 2017/1/23.
 */
public class SlaSupplierServiceImpl implements SlaSupplierService {
    @Autowired
    private SlaSupplierDAO slaSupplierDAO;
    @Override
    public List<SlaSupplierDTO> getSlaSupplierList(Map map) {
        List<SlaSupplierDTO> result = new ArrayList<SlaSupplierDTO>();
        result = slaSupplierDAO.getSlaSupplierList(map);
        return result;
    }

    @Override
    public String addSlaSupplier(Map map) {
        return null;
    }

    @Override
    public String delSlaSupplier(Map map) {
        return null;
    }
}
