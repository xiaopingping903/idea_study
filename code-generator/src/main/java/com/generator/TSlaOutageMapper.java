package com.generator;

import com.generator.TSlaOutage;

public interface TSlaOutageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TSlaOutage record);

    int insertSelective(TSlaOutage record);

    TSlaOutage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TSlaOutage record);

    int updateByPrimaryKey(TSlaOutage record);
}