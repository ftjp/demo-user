package com.example.demo.infruastructure.util.compare;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author LJP
 * @date 2024/12/13 14:48
 */
@Slf4j
@Data
public class CompareTestData {


    private Long id;
    @Compare("int基础类型")
    private int anInt;
    @Compare("integer类型")
    private Integer integer;
    @Compare("string类型")
    private String string;


    private List<Integer> intList;
    private List<Long> longList;
    private List<String> stringList;
    private List<CompareTestData2> compareTestData2List;

}
