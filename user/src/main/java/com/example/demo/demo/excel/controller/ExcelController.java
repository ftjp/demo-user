package com.example.demo.demo.excel.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.demo.excel.domain.entity.ExcelData;
import com.example.demo.demo.excel.domain.service.EasyExcelUtil;
import com.example.demo.demo.excel.domain.service.ExcelDataExport;
import com.example.demo.demo.excel.domain.service.ExcelDataService;
import com.example.demo.demo.excel.domain.service.ExcelService;
import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.infruastructure.config.Permit;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author LJP
 * @date 2024/11/26 9:57
 */
@RestController
@RequestMapping("/demo/excel")
@Slf4j
public class ExcelController {

    @Resource
    private ExcelService excelService;
    @Resource
    private ExcelDataService excelDataService;
    @Resource
    private EasyExcelUtil easyExcelUtil;


    @Permit
    @PostMapping("/addData")
    public BaseResult<Void> addData(@RequestBody ExcelData param) {
        log.info("getExtendUserInfo接口，param :{}", JSON.toJSONString(param));
        BaseResult<Void> result = new BaseResult<>();
        excelService.addData(param.getNum());
        return result;
    }


    @Permit
    @PostMapping("/export1")
    public void export1(HttpServletResponse response, HttpServletRequest request) throws Exception {
        ExcelData excelData = new ExcelData();
        excelData.setDeleted(false);
        LocalDateTime allStarTime = LocalDateTime.now();
        easyExcelUtil.writeExcelXlsx(
                response,
                "数据导出表",
                "表格区域",
                ExcelDataExport.class,
                excelData,
                param -> excelDataService.listByDeleted(param.getDeleted()));
        LocalDateTime allEndTime = LocalDateTime.now();
        Duration duration = Duration.between(allStarTime, allEndTime);
        log.info("导出数据结束,时间为{}-{}，耗时{}", allStarTime, allEndTime, duration.getSeconds());
    }

    @Permit
    @PostMapping("/import1")
    public void import1(MultipartFile file, HttpServletResponse response, HttpServletRequest request) throws Exception {
        LocalDateTime allStarTime = LocalDateTime.now();
        easyExcelUtil.importExcel(file, new ExcelDataExport(), excelDataService, ExcelData.class);
        LocalDateTime allEndTime = LocalDateTime.now();
        Duration duration = Duration.between(allStarTime, allEndTime);
        log.info("导入数据结束,时间为{}-{}，耗时{}", allStarTime, allEndTime, duration.getSeconds());
    }

    @Permit
    @PostMapping("/import2")
    public void import2(MultipartFile file, HttpServletResponse response, HttpServletRequest request) throws Exception {
        LocalDateTime allStarTime = LocalDateTime.now();
        easyExcelUtil.repeatedRead(file, new ExcelDataExport(), excelDataService, ExcelData.class);
        LocalDateTime allEndTime = LocalDateTime.now();
        Duration duration = Duration.between(allStarTime, allEndTime);
        log.info("导入数据结束,时间为{}-{}，耗时{}", allStarTime, allEndTime, duration.getSeconds());
    }


}