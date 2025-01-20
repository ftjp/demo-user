package com.example.demo.demo.test;

import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.infruastructure.common.UserContext.LocalUserContext;
import com.example.demo.infruastructure.common.UserContext.UserInfoContext;
import com.example.demo.infruastructure.enums.BaseResultEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user/demo/test")
@Api(tags = "demotest")
@Slf4j
public class DemoTestController {

    @ApiOperation(value = "test接口", notes = "")
    @GetMapping("/test")
    public BaseResult<String> test() {
        log.info("test接口");
        BaseResult<String> result = new BaseResult<>();
        result.setSuccess();
        try {

            System.out.println("*********************************");
            UserInfoContext userInfo = LocalUserContext.getUserInfo();
            log.info("userInfo:{}", userInfo);
            result.setData("test");

        } catch (BaseCustomException e) {
            log.error("test接口: ", e);
            result.setCodeAndMsg(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.error("test接口: ", e);
            result.setCodeAndMsg(BaseResultEnum.SYSTEM_ERROR);
        }
        return result;
    }

}
