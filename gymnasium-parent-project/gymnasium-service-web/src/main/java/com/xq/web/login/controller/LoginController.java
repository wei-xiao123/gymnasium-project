package com.xq.web.login.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.xq.jwt.JwtUtils;
import com.xq.utils.ResultUtils;
import com.xq.utils.ResultVo;
import com.xq.web.login.entity.LoginParam;
import com.xq.web.login.entity.LoginResult;
import com.xq.web.member.entity.Member;
import com.xq.web.member.service.MemberService;
import com.xq.web.sys_user.entity.SysUser;
import com.xq.web.sys_user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    //生成验证码
    @PostMapping("/image")
    public ResultVo imageCode(HttpServletRequest request) throws IOException{
        //获取验证码字符
        String text = defaultKaptcha.createText();
        //将验证码存储到session里面
        HttpSession session = request.getSession();
        session.setAttribute("code",text);
        //生成图片
        BufferedImage bufferedImage = defaultKaptcha.createImage(text);
        ByteArrayOutputStream outputStream = null;
        try{
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"jpg",outputStream);
            BASE64Encoder encoder = new BASE64Encoder();
            String base64 = encoder.encode(outputStream.toByteArray());
            //System.out.println("base64:" + base64);
            String captchaBase64 = "data:image/jpeg;base64," + base64.replaceAll("\r\n", "");
            //System.out.println("code:" + captchaBase64);
            ResultVo result = new ResultVo("生成成功",200,captchaBase64);
            return result;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(outputStream != null){
                outputStream.close();
            }
        }
        return null;
    }

    @Autowired
    MemberService memberService;

    @Autowired
    SysUserService sysUserService;
    @Autowired
    JwtUtils jwtUtils;

    //登录
    @PostMapping("/login")
    public ResultVo login(HttpServletRequest request, @RequestBody LoginParam loginParam){
        //获取session
        HttpSession session = request.getSession();
        //获取验证码
        String code = (String) session.getAttribute("code");
        //验证验证码
        if(!code.equals(loginParam.getCode())){
            return ResultUtils.error("验证码错误!");
        }
        String password = DigestUtils.md5DigestAsHex(loginParam.getPassword().getBytes());
        //用户类型判断
        if(loginParam.getUserType().equals("1")){ //会员
            //构造查询条件
            QueryWrapper<Member> query = new QueryWrapper<>();
            query.lambda().eq(Member::getUsername,loginParam.getUsername()).eq(Member::getPassword,loginParam.getPassword());
            Member one = memberService.getOne(query);
            if(one == null){
                return ResultUtils.error("用户名或密码错误!");
            }
            //生成token
            Map<String,String> map = new HashMap<>();
            map.put("userId",Long.toString(one.getMemberId()));
            map.put("username",one.getUsername());
            String token = jwtUtils.generateToken(map);
            System.out.println("token:" + token);
            //返回登录成功信息
            LoginResult result = new LoginResult();
            result.setToken(token);
            result.setUserId(one.getMemberId());
            result.setUsername(one.getName());
            result.setUserType(loginParam.getUserType());
            return ResultUtils.success("登录成功",result);
        }else if(loginParam.getUserType().equals("2")){ //员工
            QueryWrapper<SysUser> query = new QueryWrapper<>();
            query.lambda().eq(SysUser::getPassword,password).eq(SysUser::getUsername,loginParam.getUsername());
            SysUser one = sysUserService.getOne(query);
            if(one == null){
                return ResultUtils.error("用户名或密码错误!");
            }
            //生成token
            Map<String,String> map = new HashMap<>();
            map.put("userId",Long.toString(one.getUserId()));
            map.put("username",one.getUsername());
            String token = jwtUtils.generateToken(map);
            System.out.println("token:" + token);
            //返回登录成功信息
            LoginResult result = new LoginResult();
            result.setToken(token);
            result.setUserId(one.getUserId());
            result.setUsername(one.getNickName());
            result.setUserType(loginParam.getUserType());
            return ResultUtils.success("登录成功",result);
        }else{
            return ResultUtils.error("用户类型错误!");
        }
    }
}
