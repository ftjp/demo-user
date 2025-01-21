package com.example.demo.demo.excel.domain.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.demo.excel.domain.entity.ExcelData;
import com.example.demo.demo.excel.domain.mapper.ExcelDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExcelDataService extends ServiceImpl<ExcelDataMapper, ExcelData> {


    public void addData(Integer num) {
        List<ExcelData> saveList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            ExcelData excelData = new ExcelData();
            excelData.setData("data" + i);
            excelData.setNum(i);
            excelData.setCreateTime(LocalDateTime.now());
            excelData.setUpdateTime(LocalDateTime.now());
            saveList.add(excelData);
        }
        this.saveBatch(saveList);
    }

    public Cursor<ExcelData> listByDeleted(Boolean deleted) {
        return baseMapper.listByDeleted(deleted);
    }


}