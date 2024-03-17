package com.qbwyyds.community.community;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)//加注解以原版配置运行
public class LoggerTest {
    private static final Logger logger= LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void test1(){
        System.out.println(logger);
        logger.debug("debug logger");
        logger.info("info logger");
        logger.warn("warn logger");
        logger.error("error logger");
    }
}
