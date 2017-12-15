package com.cy.controller;

import com.cy.entity.UserInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cy
 * 2017/12/15 17:22
 */
@Controller
@Log4j2
public class LoginController {

    /**
     * 登录页面
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(String message, ModelMap modelMap) {
        if(!"".equals(message)){
            modelMap.addAttribute("message",message);
        }
        return "login";
    }

    /**
     * 登录动作
     * @param user
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(HttpServletRequest req, UserInfo user, RedirectAttributes attr){
        if("".equals(user.getUsername()) || "".equals(user.getPassword())){
            attr.addAttribute("message","用户名或密码不能为空");
            return "redirect:/login";
        }
        String userName = user.getUsername();
        String pwd = user.getPassword();
        System.out.println(userName+pwd);
        //开始调用shiro验证
        UsernamePasswordToken token = new UsernamePasswordToken(userName,pwd,"login");
        //获取当前的subject
        Subject currentUser = SecurityUtils.getSubject();
        System.out.println("asdasd");
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            log.info("对用户[" + userName + "]进行登录验证..验证开始");
            currentUser.login(token);
            log.info("对用户[" + userName + "]进行登录验证..验证通过");
        }catch(UnknownAccountException uae){
            log.info("对用户[" + userName + "]进行登录验证..验证未通过,未知账户");
            attr.addAttribute("message", "用户名不存在");
        }catch(IncorrectCredentialsException ice){
            log.info("对用户[" + userName + "]进行登录验证..验证未通过,错误的凭证");
            attr.addAttribute("message", "密码不正确");
        }catch(LockedAccountException lae){
            log.info("对用户[" + userName + "]进行登录验证..验证未通过,账户已锁定");
            attr.addAttribute("message", "账户已锁定");
        }catch(ExcessiveAttemptsException eae){
            log.info("对用户[" + userName + "]进行登录验证..验证未通过,错误次数过多");
            attr.addAttribute("message", "用户名或密码错误次数过多");
        }catch(AuthenticationException ae){
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            log.info("对用户[" + userName + "]进行登录验证..验证未通过,堆栈轨迹如下");
            ae.printStackTrace();
            attr.addAttribute("message", "用户名或密码不正确");
        }
        if(req.getParameter("forceLogout") != null) {
            attr.addAttribute("error", "您已经被管理员强制退出，请重新登录");
            return "redirect:/login";
        }
        //验证是否登录成功
        if(currentUser.isAuthenticated()){
            return "redirect:/index";
        }else{
            token.clear();
            return "redirect:/login";
        }
    }
}
