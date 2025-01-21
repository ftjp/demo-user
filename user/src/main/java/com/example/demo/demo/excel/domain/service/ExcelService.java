package com.example.demo.demo.excel.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ExcelService {

    @Resource
    private ExcelDataService excelDataService;


    /**
     * description: 新增 20 万数据
     *
     * @author: LJP
     * @date: 2025/1/20 14:18
     */
    public void addData(Integer num) {
        if (num == null) {
            num = 200;
        }
        LocalDateTime allStarTime = LocalDateTime.now();
        for (int i = 0; i < num; i++) {
            LocalDateTime starTime = LocalDateTime.now();
            log.info("第{}次开始新增数据,时间为{}", i, LocalDateTime.now());
            excelDataService.addData(1000);
            LocalDateTime endTime = LocalDateTime.now();
            Duration duration = Duration.between(starTime, endTime);
            log.info("第{}次开始新增结束,时间为{}，耗时{}", i, LocalDateTime.now(), duration.getSeconds());
        }
        LocalDateTime allEndTime = LocalDateTime.now();
        Duration duration = Duration.between(allStarTime, allEndTime);
        log.info("新增{}条数据结束,时间为{}-{}，耗时{}", num * 1000, allStarTime, allEndTime, duration.getSeconds());
    }


}