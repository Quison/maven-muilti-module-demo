package com.moguichun.mavendemo.dao;

import java.util.List;

import com.moguichun.mavendemo.model.User;

public interface UserDao {
	
	public List<User> queryUser();
}
