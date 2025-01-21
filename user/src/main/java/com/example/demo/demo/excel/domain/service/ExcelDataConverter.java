package com.example.demo.demo.excel.domain.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.math.BigDecimal;
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
        return null;
    }

    // 支持导出的Excel类型
    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    // 转换为Java
    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String value = cellData.getStringValue();
        value = StrUtil.isNotBlank(value) ? value : cellData.getOriginalNumberValue().toString();
        Class<?> type = contentProperty.getField().getType();
        if (StrUtil.isBlank(value)) {
            return null;
        }
        if (type.equals(LocalDateTime.class)) {
            // 处理LocalDateTime类型
            return LocalDateTime.parse(value, dateTimeFormatter);
        }
        if (type.equals(Date.class)) {
            // 处理LocalDateTime类型
            return formatter.parse(value);
        }
        if (type.equals(Integer.class)) {
            // 转换为 Integer
            return Integer.parseInt(value);
        }
        if (type.equals(Long.class)) {
            // 转换为 Integer
            return Long.valueOf(value);
        }
        if (type.equals(Double.class)) {
            // 转换为 Double
            return Double.parseDouble(value);
        }
        if (type.equals(Boolean.class)) {
            // 转换为 Boolean
            return "TRUE".equalsIgnoreCase(value);
        }
        if (type.equals(BigDecimal.class)) {
            // 转换为 BigDecimal
            return new BigDecimal(value);
        }
        // 直接返回字符串
        return value;
    }

    // 转换为Excel
    @Override
    public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {

        Class<?> type = contentProperty.getField().getType();
        if (value == null) {
            // 如果值为null，返回空字符串
            return new WriteCellData<>(CellDataTypeEnum.STRING, "");
        }
        // 根据对象的类型进行转换
        if (type.equals(LocalDateTime.class)) {
            // 处理LocalDateTime类型
            String dateStr = ((LocalDateTime) value).format(dateTimeFormatter);
            return new WriteCellData<>(CellDataTypeEnum.STRING, dateStr);
        }
        if (type.equals(Date.class)) {
            // 处理Date类型
            String dateStr = formatter.format(value);
            return new WriteCellData<>(CellDataTypeEnum.STRING, dateStr);
        }
//        if (type.equals(Integer.class)) {
//            return new WriteCellData<>(CellDataTypeEnum.STRING, value.toString());
//        }
//        if (value instanceof String) {
//            return new WriteCellData<>(CellDataTypeEnum.STRING, value.toString());
//        }
//        if (value instanceof Boolean) {
//            return new WriteCellData<>(CellDataTypeEnum.STRING, value.toString());
//        }
//        if (value instanceof BigDecimal) {
//            return new WriteCellData<>(CellDataTypeEnum.STRING, value.toString());
//        }
//        if (value instanceof Long) {
//            return new WriteCellData<>(CellDataTypeEnum.STRING, value.toString());
//        }

        // 对于其他类型，直接返回其toString()结果
        return new WriteCellData<>(CellDataTypeEnum.STRING, value.toString());


    }
}
