package com.example.delicious_take_out.config.emtity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
/**
 * 用户信息
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)  // 自增
    private Integer id;  // id 类型为 int 或 Integer

    //邮箱
    private String email;

    //密码
    private String password;

    //姓名
    private String name;

    //性别 0 女 1 男
    private String sex;

    //头像
    private String avatar;


    //状态 0:禁用，1:正常
    private Integer status;
}
