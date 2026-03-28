package com.xq.config.springSecurity;

import org.springframework.security.core.AuthenticationException;

//自定义异常处理器
public class CustomerAuthenticationException extends AuthenticationException {
    public CustomerAuthenticationException(String msg) {
        super(msg);
    }
}
