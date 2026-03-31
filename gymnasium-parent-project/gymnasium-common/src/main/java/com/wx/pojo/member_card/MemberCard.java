package com.wx.pojo.member_card;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("member_card")
public class MemberCard implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.AUTO)
    private Long cardId;
    private String title;
    private String cardType;
    private BigDecimal price;
    private Integer cardDay;
    private String status;
}
