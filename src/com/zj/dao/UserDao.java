package com.zj.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.com.uitl.GetConn;

import com.zj.dao.impl.UserDaoImpl;
import com.zj.entity.User;
/**
 * 
 * @author lijia
 *用户数据库操作实现类
 */
public class UserDao implements UserDaoImpl{
	/*
	 * 获取jdbc连接
	 */
	private QueryRunner qr = new QueryRunner();
	private Connection conn = GetConn.getConn();
	/*
	 * 获取所有用户信息方法
	 */
	public List<User> getAllUserInfo() throws SQLException {
		String sql = "select * from user";
		return qr.query(conn, sql, new BeanListHandler<User>(User.class));
	}
	/*
	 * 通过用户手机号获取单个用户信息方法接口
	 */
	public User getUserInfoByPhone(String phone) throws SQLException {
		String sql = "select * from user where user_phone=?";
		return qr.query(conn, sql, new BeanHandler<User>(User.class),phone);
	}
	/*
	 * 添加用户
	 */
	public int addUser(String user_name, String user_phone) throws SQLException {
		String sql = "insert into user(user_name,user_phone) values(?,?)";
		return qr.update(conn, sql, user_name,user_phone);
	}
	/*
	 * 修改用户
	 */
	public int updateUser(Integer user_id,String user_name,
			String user_email, String user_phone,
			String real_name,String user_describe,String user_IDcard) throws SQLException {
		String sql = "update user set user_name = ? and user_email = ? and user_phone = ? and real_name = ? and user_describe = ? and user_IDcard = ? where user_id = ?";
		return qr.update(conn, sql, user_name,user_email, user_phone,real_name,user_describe,user_IDcard,user_id);
	}
	/**
	 * 通过id查询用户
	 * @throws SQLException 
	 */
	public User queryUser(Integer user_id) throws SQLException {
		String sql = "select * from user where user_id = ?";
		return qr.query(conn, sql, new BeanHandler<User>(User.class), user_id);
	}
	/**
	 * 查询手机号是否存在
	 * @throws SQLException 
	 */
	public Integer queryPhoneExit(String user_phone) throws SQLException {
		String sql = "select * from user where user_phone = ?";
		return qr.query(conn, sql, new ScalarHandler<Integer>(), user_phone);
	}
	/**
	 * 用户设置密码
	 * @throws SQLException 
	 */
	public int setUserPwd(Integer user_id, String user_pwd) throws SQLException {
		String sql = "insert into user(user_pwd) value(?) where user_id = ?";
		return qr.update(conn, sql, user_pwd,user_id);
	}
	/**
	 * 用户修改密码
	 * @throws SQLException 
	 */
	public int updateUserPwd(Integer user_id, String user_pwd) throws SQLException {
		String sql = "update user set user_pwd = ? where user_id = ?";
		return qr.update(conn, sql, user_pwd,user_id);
	}
	/**
	 * 用户上传头像
	 * @throws SQLException 
	 */
	public int addUserHead(Integer user_id, String user_headimg_url) throws SQLException {
		String sql = "insert into user(user_headimg_url) value(?) where user_id = ?";
		return qr.update(conn, sql, user_headimg_url,user_id);
	}
}
