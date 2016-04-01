package com.moguichun.mavendemo.daotest;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.moguichun.mavendemo.dao.UserDao;
import com.moguichun.mavendemo.model.User;


@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration("classpath*:spring-mybatis.xml") 
@Transactional 
public class UserDaoTest {
	
	@Autowired
	private UserDao userDao;
	
/*	@Before
	public void setUp() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
	}*/
	
	@Test
	public void testQueryUser() {
		List<User> users = userDao.queryUser();
		assertNotNull(users);
	}

}
