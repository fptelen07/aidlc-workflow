package com.awsome.shop.point.repository.mysql.po.points;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分交易持久化对象
 */
@Data
@TableName("points_transaction")
public class PointsTransactionPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    /** grant / deduct / expire */
    private String type;

    private Long amount;

    private String reason;

    private Long orderId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
