package com.example.demo.demo.excel.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.demo.excel.domain.entity.ExcelData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;

/**
 * @author LJP
 * @date 2024/11/26 9:49
 */
@Mapper
public interface ExcelDataMapper extends BaseMapper<ExcelData> {


    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
    @ResultType(ExcelData.class)
    @Select("SELECT * FROM `excel_data` WHERE deleted = #{deleted}")
    Cursor<ExcelData> listByDeleted(@Param("deleted") Boolean deleted);


}