package com.example.demo.demo.database.service.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;


@Accessors(chain = true)
@Data
@Document(indexName = "userinfoes_index")
//@Setting(shards = 2, replicas = 2, refreshInterval = "2s", indexStoreType = "mmapfs")
public class UserInfoEs implements Serializable {

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 用户名
     */
    @Field(type = FieldType.Keyword)
    private String userName;

    /**
     * 用户密码
     */
    @Field
    private String userPwd;

    /**
     * 手机号
     */
    @Field
    private String mobile;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date,
            format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss")

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Field(type = FieldType.Date,
            format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss")

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @Field
    private Integer deleted;

}