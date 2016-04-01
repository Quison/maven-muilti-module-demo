package com.moguichun.service.mavendemo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moguichun.mavendemo.dao.UserDao;
import com.moguichun.mavendemo.model.User;
import com.moguichun.mavendemo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	public List<User> displayAllUser() {
		System.out.println(userDao);
		List<User> users = userDao.queryUser();
		for(User user : users) {
			System.out.println(user.toString());
		}
		
		return users;
	}

}
