package com.qbwyyds.community.community;

import com.qbwyyds.community.community.utils.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)//加注解以原版配置运行
class CommunityApplicationTests implements ApplicationContextAware {//beanFactory接口的实现接口
	ApplicationContext applicationContext;//暂存容器
	@Autowired
	SensitiveFilter sensitiveFilter;

	@Test
	void contextLoads() {
	}

	@Override
	@Test
	public void setApplicationContext(ApplicationContext applicationContext) {//获取容器
		this.applicationContext=applicationContext;
		System.out.println(this.applicationContext);
	}

	//主动从容器中获取，拿到bean去用
	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Test
	public void testsesitive(){
		String s=sensitiveFilter.filter("哈哈哈 我要赌博！！！");
		System.out.println(s);
	}

}
