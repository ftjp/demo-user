package com.example.demo.demo.excel.domain.service;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author LJP
 * @date 2025/1/21 9:42
 */
public class ExcelDataConverter implements Converter<Object> {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 支持导入的Java类型
    @Override
    public Class<?> supportJavaTypeKey() {
        return Long.class;
    }

    // 支持导出的Excel类型
    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    // 转换为Java
    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    // 转换为Excel
    @Override
    public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (value == null) {
            // 如果值为null，返回空字符串
            return new WriteCellData<>(CellDataTypeEnum.STRING, "");
        }

        // 根据对象的类型进行转换
        if (LocalDateTime.class.equals(value.getClass())) {
            // 处理LocalDateTime类型
            String dateStr = ((LocalDateTime) value).format(dateTimeFormatter);
            return new WriteCellData<>(CellDataTypeEnum.STRING, dateStr);
        }
        if (Date.class.equals(value.getClass())) {
            // 处理Date类型
            String dateStr = formatter.format(value);
            return new WriteCellData<>(CellDataTypeEnum.STRING, dateStr);
        }

        // 对于其他类型，直接返回其toString()结果
        return new WriteCellData<>(CellDataTypeEnum.STRING, value.toString());


    }
}
