package com.awsome.shop.point.repository.mysql.po.points;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分规则持久化对象
 */
@Data
@TableName("points_rule")
public class PointsRulePO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String type;

    private Long amount;

    private Boolean enabled;

    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
