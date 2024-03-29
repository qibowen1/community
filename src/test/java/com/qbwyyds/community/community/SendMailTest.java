package com.qbwyyds.community.community;

import com.qbwyyds.community.community.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)//加注解以原版配置运行
public class SendMailTest {
    @Autowired
    MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;


    @Test
    public void send(){
        mailClient.sendEmail("2564429454@qq.com","TEST","hi,this is a test");
    }

    @Test
    public void sendHtmlMail(){
        Context context = new Context();
        context.setVariable("festival","清明节");
        context.setVariable("date", new Date());

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendEmail("2564429454@qq.com","TEST",content);
    }
}
