package com.example.demo.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.example.demo.response.Result;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @version V1.0.0
 * @ClassName: {@link BaseEntity}
 * @Description: MybatisPlus 实体基类
 * @author: 兰州
 * @date: 2019/7/16 9:22
 * @Copyright: @2019 All rights reserved.
 */
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    protected Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 最后修改人
     */
    @TableField(fill = FieldFill.UPDATE)
    protected Long updateBy;

    /**
     * 最后修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime updateTime;

}
