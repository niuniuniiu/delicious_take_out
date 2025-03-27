package com.example.delicious_take_out.config.emtity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址簿
 */
@Data
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)  // 自增
    private Integer id;  // id 类型为 int 或 Integer


    //用户id
    private Integer userId;


    //收货人
    private String consignee;


    //手机号
    private String phone;


    //性别 0 女 1 男
    private String sex;

    //城市名称
    private String cityName;

    //详细地址
    private String detail;

    //标签
    private String label;

    //是否是默认地址 0 否 1是
    private Integer isDefault;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    //是否删除
    private Integer isDeleted;
}
