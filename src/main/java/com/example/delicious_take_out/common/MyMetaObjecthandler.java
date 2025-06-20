package com.example.delicious_take_out.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*元数据对象处理器*/
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {
    /*插入操作，自动填充*/
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }
    /*更新操作，自动填充*/
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }
}
