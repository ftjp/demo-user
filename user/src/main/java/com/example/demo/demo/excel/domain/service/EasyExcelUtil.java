package com.example.demo.demo.excel.domain.service;

/**
 * @author LJP
 * @date 2025/1/20 15:04
 */

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.deploy.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * description:
 *
 * @author: LJP
 * @date: 2025/1/20 15:05
 */
@Component
@Slf4j
public class EasyExcelUtil {

    private final String DATE_FORMAT = "yyyy-MM-dd";


    /**
     * 设置批量存储最大值,也影响sheet页数
     */
    private final Integer MAX_SHEET_DATA = 100000;
    /**
     * 设置内存最大值
     */
    private final Integer MAX_MEMORY_DATA = 1000;

    // **************************************** 导出 **************************************************

    /**
     * 使用EasyExcel生成Excel  xls
     *
     * @param response      响应对象
     * @param fileNameParam 文件名
     * @param sheetName     表格名
     * @param clazz         导出实体类
     * @param t             查库入参
     * @param func          流式查询方法 Cursor<ResultVo> listOrders(@Param("userName") String userName);
     */
    public <T> void writeExcelXls(HttpServletResponse response, String fileNameParam, String sheetName, Class<?> clazz, T t, Function<T, Cursor<?>> func) throws Exception {
        streamExportExcel(response, fileNameParam, sheetName, clazz, ExcelTypeEnum.XLS.getValue(), t, func);
    }

    /**
     * 使用EasyExcel生成Excel  xlsx
     *
     * @param response      响应对象
     * @param fileNameParam 文件名
     * @param sheetName     表格名
     * @param clazz         导出实体类
     * @param t             查库入参
     * @param func          流式查询方法 Cursor<ResultVo> listOrders(@Param("userName") String userName);
     */
    @Transactional(rollbackFor = Exception.class)
    public <T> void writeExcelXlsx(HttpServletResponse response, String fileNameParam, String sheetName, Class<?> clazz, T t, Function<T, Cursor<?>> func) throws Exception {
        streamExportExcel(response, fileNameParam, sheetName, clazz, ExcelTypeEnum.XLSX.getValue(), t, func);
    }

    /**
     * 流式导出 Excel
     *
     * @param response      响应对象
     * @param fileNameParam 文件名
     * @param sheetName     表格名
     * @param clazz         导出实体类
     * @param excelType     导出类型
     * @param t             查库入参
     * @param func          流式查询方法 Cursor<ResultVo> listOrders(@Param("userName") String userName);
     * @throws Exception 异常
     */
    private <T> void streamExportExcel(HttpServletResponse response, String fileNameParam, String sheetName, Class<?> clazz, String excelType, T t, Function<T, Cursor<?>> func) throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String fileName = fileNameParam + dateTimeFormatter.format(LocalDateTime.now()) + excelType;
        ExcelWriter excelWriter = EasyExcel.write(getOutputStream(fileName, response, excelType), clazz).registerWriteHandler(new CustomCellWeightStrategy()).build();
        // 内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置自动换行，前提内容中需要加「\n」才有效
        contentWriteCellStyle.setWrapped(true);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(null, contentWriteCellStyle);
        Cursor<?> cursor;
        List<Object> list = new ArrayList<>();
        int page = 0;
        WriteSheet writeSheet = EasyExcel.writerSheet(++page, sheetName + page).registerWriteHandler(horizontalCellStyleStrategy).build();
        // 流式数据库查询
        cursor = func.apply(t);
        int count = 0;
        try {
            for (Object o : cursor) {
                list.add(o);
                if (list.size() == MAX_MEMORY_DATA) {
                    count += list.size();
                    excelWriter.write(clazz.equals(list.get(0).getClass()) ? list : BeanUtil.copyToList(list, clazz), writeSheet);
                    list.clear();
                    // 每个sheet页最大存储MAX_SHEET_DATA条数据
                    if (count >= MAX_SHEET_DATA) {
                        writeSheet = EasyExcel.writerSheet(++page, sheetName + page).registerWriteHandler(horizontalCellStyleStrategy).build();
                        count = 0;
                    }
                }
            }
            // 处理最后不足MAX_SHEET_DATA的数据
            if (list.size() > 0) {
                writeSheet = EasyExcel.writerSheet(++page, sheetName + page).registerWriteHandler(horizontalCellStyleStrategy).build();
                excelWriter.write(clazz.equals(list.get(0).getClass()) ? list : BeanUtil.copyToList(list, clazz), writeSheet);
                list.clear();
            }
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("下载文件失败" + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 导出文件时为Writer生成OutputStream
     *
     * @param finalName 文件名
     * @param response  响应对象
     * @param excelType 导出文件类型
     * @return OutputStream
     */
    private OutputStream getOutputStream(String finalName, HttpServletResponse response, String excelType) throws IOException, UnsupportedEncodingException {
        response.reset();
        // 使用 UTF-8 编码文件名
        finalName = URLEncoder.encode(finalName, "UTF-8").replaceAll("\\+", "%20");
        // 设置响应头
        if (ExcelTypeEnum.XLS.getValue().equals(excelType)) {
            response.setContentType("application/vnd.ms-excel");
        } else if (ExcelTypeEnum.XLSX.getValue().equals(excelType)) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }
        response.setCharacterEncoding("utf-8");
        // 设置文件名
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + finalName);
        response.setHeader("Cache-Control", "no-store, max-age=0");
        // 返回输出流
        return response.getOutputStream();
    }

    // **************************************** 导入 **************************************************


    public <T> void importExcel(MultipartFile file, T t, IService service, Class saveClass) throws Exception {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new Exception("上传文件格式不正确");
        }

        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        InputStream inputStream = file.getInputStream();
        EasyExcel.read(inputStream, t.getClass(), new ExcelDataListener<T>(service, saveClass)).sheet(0).doRead();
//        EasyExcel.read(inputStream, t.getClass(), new PageReadListener<T>(dataList -> {
//            for (T demoData : dataList) {
//                log.info("读取到一条数据{}", JSON.toJSONString(demoData));
//            }
//        })).sheet().doRead();
    }

    public <T> void repeatedRead(MultipartFile file, T t, IService service, Class saveClass) throws Exception {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new Exception("上传文件格式不正确");
        }
        // 读取全部sheet
        // 这里需要注意 ExcelDataListener 的 doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个 ExcelDataListener 里面写
        InputStream inputStream = file.getInputStream();
//        EasyExcel.read(inputStream,  t.getClass(), new ExcelDataListener<T>(service, saveClass)).doReadAll();

        // 读取部分sheet
        try (ExcelReader excelReader = EasyExcel.read(inputStream).build()) {
            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet1 = EasyExcel.readSheet(0).head(t.getClass()).registerReadListener(new ExcelDataListener<T>(service, saveClass)).build();
            ReadSheet readSheet2 = EasyExcel.readSheet(1).head(t.getClass()).registerReadListener(new ExcelDataListener<T>(service, saveClass)).build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1, readSheet2);
        }
    }

}
