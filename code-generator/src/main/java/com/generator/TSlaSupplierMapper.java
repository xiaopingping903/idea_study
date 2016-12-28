package com.generator;

import com.generator.TSlaSupplier;

public interface TSlaSupplierMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TSlaSupplier record);

    int insertSelective(TSlaSupplier record);

    TSlaSupplier selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TSlaSupplier record);

    int updateByPrimaryKey(TSlaSupplier record);
}