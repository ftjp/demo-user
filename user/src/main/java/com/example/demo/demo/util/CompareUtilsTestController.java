package com.example.demo.demo.util;


import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.infruastructure.config.Permit;
import com.example.demo.infruastructure.util.compare.CompareTestData;
import com.example.demo.infruastructure.util.compare.CompareUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/user/test")
@Api(tags = "test")
@Slf4j
public class CompareUtilsTestController {

    @Resource
    private CompareUtils compareUtils;

    @Permit
    @GetMapping("/compareUtils/test")
    public BaseResult<String> test() {

        CompareTestData compareTestData = new CompareTestData();
        compareTestData.setId(1L);
        compareTestData.setAnInt(1);
        compareTestData.setInteger(1);
        compareTestData.setString("string1");


        CompareTestData compareTestData2 = new CompareTestData();
        compareTestData2.setId(2L);
        compareTestData2.setAnInt(2);
        compareTestData2.setInteger(2);
        compareTestData2.setString("string2");

        String compare = compareUtils.compare(compareTestData, compareTestData2);

        log.info(compare);

        CompareTestData compareTestData3 = new CompareTestData();
        compareTestData3.setId(3L);
        compareTestData3.setString("string2");
        String compare23 = compareUtils.compare(compareTestData2, compareTestData3);
        log.info(compare23);


        BaseResult<String> baseResult = new BaseResult<>();
        baseResult.setSuccess();
        baseResult.setData(compare);
        return baseResult;
    }


}
