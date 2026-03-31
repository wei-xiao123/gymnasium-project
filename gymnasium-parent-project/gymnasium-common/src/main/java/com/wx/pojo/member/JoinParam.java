package com.wx.pojo.member;

import lombok.Data;

import java.io.Serializable;

@Data
public class JoinParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long memberId;
    private Long cardId;
}
