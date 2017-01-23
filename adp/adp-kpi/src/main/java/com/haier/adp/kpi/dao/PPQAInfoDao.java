package com.haier.adp.kpi.dao;

import com.haier.adp.kpi.dto.PPQAInfo;
import com.haier.adp.kpi.dto.PPQASearchCondition;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by 01435337 on 2016/11/16.
 */
@Repository("ppqaDao")
public class PPQAInfoDao extends MyBatisDao<PPQAInfo> {

    /**
     * 查询是否开始日大于所有记录的结束日
     *
     * @param startDate
     * @return
     */
    public boolean isLaterThanEndDate(Date startDate) {
        Integer count = this.getSqlSession().selectOne("com.haier.adp.kpi.dto.PPQAInfo.isLaterThanEndDate", startDate);
        return (count == null || count == 0);
    }

    /**
     * 查询是否结束日小于所有记录的开始日
     *
     * @param endDate
     * @return
     */
    public boolean isEalierThanStartDate(Date endDate) {
        Integer count = this.getSqlSession().selectOne("com.haier.adp.kpi.dto.PPQAInfo.isEalierThanStartDate", endDate);
        return (count == null || count == 0);
    }

    /**
     * 插入一条新PPQA纪录
     *
     * @param ppqa
     */
    public void insert(PPQAInfo ppqa) {
        this.getSqlSession().insert("com.haier.adp.kpi.dto.PPQAInfo.insert", ppqa);
    }

    /**
     * 根据查询条件查询所有的PPQA记录
     *
     * @param condition
     * @return
     */
    public List<PPQAInfo> search(PPQASearchCondition condition) {
        return this.getSqlSession().selectList("com.haier.adp.kpi.dto.PPQAInfo.search", condition);
    }
}
