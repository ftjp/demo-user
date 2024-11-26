package com.example.demo.infruastructure.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.infruastructure.common.WebBaseResult;
import com.example.demo.infruastructure.enums.BaseResultEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResultUtils {
    private static final ResultUtils resultUtils = new ResultUtils();

    private ResultUtils() {
    }

    public static ResultUtils getInstantiate() {
        return resultUtils;
    }

    public String resultJudgedToData(String jsonString) {
        BaseResult result = (BaseResult) JSONUtil.toBean(jsonString, BaseResult.class);
        if (!StrUtil.equals(result.getCode(), BaseResultEnum.SUCCESS.getCode())) {
            throw new BaseCustomException(result.getCode(), result.getMsg());
        } else {
            return JSONNull.NULL.equals(result.getData()) ? null : result.getData().toString();
        }
    }

    public <T> T resultJudgedToBusinessObject(String jsonString, Class<T> clazz) throws BaseCustomException {
        BaseResult result = (BaseResult) JSONUtil.toBean(jsonString, BaseResult.class);
        if (!StrUtil.equals(result.getCode(), BaseResultEnum.SUCCESS.getCode())) {
            throw new BaseCustomException(result.getCode(), result.getMsg());
        } else {
            return JSONNull.NULL.equals(result.getData()) ? null : this.dataToT(JSONUtil.toJsonStr(result.getData()), clazz);
        }
    }

    public <T> T resultJudgedToBusinessObjectError(String jsonString, Class<T> clazz) throws BaseCustomException {
        BaseResult result = (BaseResult) JSONUtil.toBean(jsonString, BaseResult.class);
        if (!StrUtil.equals(result.getCode(), BaseResultEnum.SUCCESS.getCode())) {
            if (JSONNull.NULL.equals(result.getData())) {
                throw new BaseCustomException(result.getCode(), result.getMsg());
            } else {
                throw new BaseCustomException(result.getCode(), result.getMsg(), JSONUtil.toJsonStr(result.getData()));
            }
        } else {
            return JSONNull.NULL.equals(result.getData()) ? null : this.dataToT(JSONUtil.toJsonStr(result.getData()), clazz);
        }
    }

    public <T> List<T> resultJudgedToListBusinessObject(String jsonString, Class<T> clazz) throws BaseCustomException {
        BaseResult result = (BaseResult) JSONUtil.toBean(jsonString, BaseResult.class);
        if (!StrUtil.equals(result.getCode(), BaseResultEnum.SUCCESS.getCode())) {
            throw new BaseCustomException(result.getCode(), result.getMsg());
        } else {
            return JSONNull.NULL.equals(result.getData()) ? null : this.dataToListT(JSONUtil.toJsonStr(result.getData()), clazz);
        }
    }

    public <T> PageInfo<T> resultJudgedToPage(String jsonString, Class<T> clazz) throws BaseCustomException {
        WebBaseResult result = (WebBaseResult) JSONUtil.toBean(jsonString, WebBaseResult.class);
        if (!StrUtil.equals(result.getCode(), BaseResultEnum.SUCCESS.getCode())) {
            throw new BaseCustomException(result.getCode(), result.getMsg());
        } else {
            PageInfo pageInfo = result.getPageInfo();
            if (ObjectUtil.isNull(pageInfo)) {
                return null;
            } else {
                PageInfo<T> resultPageInfo = new PageInfo();
                if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
                    List<T> listT = getInstantiate().dataToListT(JSONUtil.toJsonStr(pageInfo.getList()), clazz);
                    resultPageInfo.setList(listT);
                } else {
                    resultPageInfo.setList(new ArrayList());
                }

                resultPageInfo.setPageNum(pageInfo.getPageNum());
                resultPageInfo.setPageSize(pageInfo.getPageSize());
                resultPageInfo.setTotal(pageInfo.getTotal());
                resultPageInfo.setPages(pageInfo.getPages());
                return resultPageInfo;
            }
        }
    }

    public <T> T resultJudgedToBusinessObject(BaseResult result, Class<T> clazz) throws BaseCustomException {
        if (!StrUtil.equals(result.getCode(), BaseResultEnum.SUCCESS.getCode())) {
            throw new BaseCustomException(result.getCode(), result.getMsg());
        } else {
            return JSONNull.NULL.equals(result.getData()) ? null : this.dataToT(JSONUtil.toJsonStr(result.getData()), clazz);
        }
    }

    public <T> List<T> resultJudgedToListBusinessObject(BaseResult<Object> result, Class<T> clazz) throws BaseCustomException {
        if (!StrUtil.equals(result.getCode(), BaseResultEnum.SUCCESS.getCode())) {
            throw new BaseCustomException(result.getCode(), result.getMsg());
        } else {
            return JSONNull.NULL.equals(result.getData()) ? null : this.dataToListT(JSONUtil.toJsonStr(result.getData()), clazz);
        }
    }

    public void resultJudged(String jsonString) {
        JSONObject jsonObject1 = new JSONObject(jsonString);
        BaseResult result = (BaseResult) jsonObject1.toBean(BaseResult.class);
        if (!StrUtil.equals(result.getCode(), BaseResultEnum.SUCCESS.getCode())) {
            throw new BaseCustomException(result.getCode(), result.getMsg());
        }
    }

    public void resultJudged(BaseResult result) {
        if (!StrUtil.equals(result.getCode(), BaseResultEnum.SUCCESS.getCode())) {
            throw new BaseCustomException(result.getCode(), result.getMsg());
        }
    }

    public Boolean resultJudgedToBoolean(BaseResult result) {
        return StrUtil.equals(result.getCode(), BaseResultEnum.SUCCESS.getCode());
    }

    public Boolean resultJudgedToBoolean(String jsonString) {
        JSONObject jsonObject1 = new JSONObject(jsonString);
        BaseResult result = (BaseResult) jsonObject1.toBean(BaseResult.class);
        return !StrUtil.equals(result.getCode(), BaseResultEnum.SUCCESS.getCode()) ? false : true;
    }

    public <T> List<T> dataToListT(String jsonString, Class<T> clazz) {
        if (clazz == Integer.class) {
            return (List<T>) JSONUtil.parseArray(jsonString);
        } else {
            List<T> list = new ArrayList();
            List<JSONObject> dataList = (List) this.dataToT(jsonString, List.class);
            if (CollectionUtil.isEmpty(dataList)) {
                return new ArrayList();
            } else {
                Iterator var5 = dataList.iterator();

                while (var5.hasNext()) {
                    JSONObject object = (JSONObject) var5.next();
                    list.add(JSONUtil.toBean(object, clazz));
                }

                return list;
            }
        }
    }

    public <T> T objectToT(Object data, Class<T> clazz) {
        return this.dataToT(JSONUtil.toJsonStr(data), clazz);
    }

    public <T> T dataToT(String jsonString, Class<T> clazz) {
        if (clazz == Integer.class) {
            return (T) JSONUtil.parseArray(jsonString);
        } else if (clazz == Boolean.class) {
            return (T) Boolean.valueOf(jsonString);
        } else if (JSONUtil.isTypeJSONArray(jsonString)) {
            JSONArray jsonArray = JSONUtil.parseArray(jsonString);
            return jsonArray.toBean(clazz);
        } else {
            JSONConfig jsonConfig = new JSONConfig();
            jsonConfig.setIgnoreCase(true);
            return JSONUtil.toBean(jsonString, jsonConfig, clazz);
        }
    }
}
