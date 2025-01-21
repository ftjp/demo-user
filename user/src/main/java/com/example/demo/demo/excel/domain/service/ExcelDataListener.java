package com.example.demo.demo.excel.domain.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.demo.excel.domain.entity.ExcelData;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * excel读取监听器
 * 有个很重要的点 ExcelDataExportListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @author LJP
 * @date 2025/1/21 10:38
 */
@Slf4j
public class ExcelDataListener<T> implements ReadListener<T> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 1000;
    /**
     * 缓存的数据
     */
    private List<Object> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private IService service;
    private Class clazz;


    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param service
     */
    public ExcelDataListener(IService service, Class clazz) {
        this.service = service;
        this.clazz = clazz;
    }

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        if (CollUtil.isEmpty(cachedDataList)) {
            return;
        }
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        List list = clazz.equals(cachedDataList.get(0).getClass()) ? cachedDataList : BeanUtil.copyToList(cachedDataList, clazz);
        service.saveBatch(list);
        log.info("存储数据库成功！");
        cachedDataList.clear();
    }
}