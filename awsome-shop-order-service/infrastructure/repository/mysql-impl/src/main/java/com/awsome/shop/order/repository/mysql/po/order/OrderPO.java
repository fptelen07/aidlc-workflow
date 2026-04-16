package com.awsome.shop.order.repository.mysql.po.order;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Order 持久化对象
 */
@Data
@TableName(value = "redemption_order", autoResultMap = true)
public class OrderPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String userName;

    private Long productId;

    private String productName;

    private Integer pointsAmount;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
