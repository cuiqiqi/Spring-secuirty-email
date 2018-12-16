package cn.com.taiji.controller;


import cn.com.taiji.domain.Msg;
import cn.com.taiji.service.EmailCodeService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 主页控制器.
 */
@Controller
public class MainController {

    @Autowired
    EmailCodeService emailCodeService;

   Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        Msg msg = new Msg("测试标题", "测试内容", "额外信息，只对管理员显示");
        model.addAttribute("msg", msg);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登陆失败，用户名或者密码错误！");
        return "login";
    }
    //邮箱
    @GetMapping("/email")
    public void createEmailCode(HttpServletRequest request, HttpServletResponse response, String emailcode){
        //生成的验证码
        String code = RandomStringUtils.randomNumeric(6);
        logger.info("您输入的邮箱为："+emailcode);
        logger.info("您的验证码为："+code);
        send(emailcode,code);
        emailCodeService.setCode(code);
    }

    @GetMapping("/403")
    public String accesssDenied() {
        return "403";
    }


    @GetMapping("/code/clearSms")
    public void clearSmsCode(HttpServletRequest request, HttpServletResponse response){
        emailCodeService.setCode("");
    }

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * 邮箱配置
     * @param setTo
     * @param Code
     */
    public void send(String setTo,String Code){
        //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        //发送者
        mainMessage.setFrom("2962953834@qq.com");
        //接收者
        mainMessage.setTo(setTo);
        //发送的标题
        mainMessage.setSubject("验证码");
        //发送的内容
        mainMessage.setText("您的验证码为："+Code);
        javaMailSender.send(mainMessage);

    }

}
