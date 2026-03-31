package com.wx.pojo.member_apply;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("member_apply")
public class MemberApply implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long applyId;
    private Long memberId;
    private String cardType;
    private Integer cardDay;
    private BigDecimal price;
    private Date createTime;
    private String createUser;
}
