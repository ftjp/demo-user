package com.example.demo.demo.excel.domain.service;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.enums.BooleanEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@ContentRowHeight(20)
public class ExcelDataExport implements Serializable {

    /**
     * id
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "id", converter = ExcelDataConverter.class)
    private Long id;

    /**
     * 文字数据
     */
    @ExcelProperty("文字数据")
    private String data;
    /**
     * 数字数据
     */
    @ExcelProperty(value = "数字数据", converter = ExcelDataConverter.class)
    private Integer num;
    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", converter = ExcelDataConverter.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间", converter = ExcelDataConverter.class)
    private Date updateTime;

    /**
     * 是否删除
     */
    @ContentFontStyle(color = 10, italic = BooleanEnum.TRUE)
    @ExcelProperty(value = "是否删除")
    private Boolean deleted;

    /**
     * 小数
     */
    @NumberFormat("#.##%")
//    @ExcelProperty(value = {"数字", "aDouble"})
    @ExcelProperty(value = "aDouble")
    private Double aDouble;

    /**
     * 小数
     */
//    @ExcelProperty(value = {"数字", "aBigDecimal"})
    @ExcelProperty(value = "aBigDecimal")
    private BigDecimal aBigDecimal;

}