package com.zj.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.com.uitl.GetConn;

import com.zj.dao.impl.ReserveDaoImpl;
import com.zj.entity.Landlord;
import com.zj.entity.Reserve;

/**
 * 预定信息dao层实现类
 * 
 */
public class ReserveDao implements ReserveDaoImpl{
	/*
	 * 获取jdbc连接
	 */
	private QueryRunner qr = new QueryRunner();
	private Connection conn = GetConn.getConn();
	/*
	 * 获取所有房东信息方法
	 */
	public List<Reserve> getAllReserve() throws SQLException {
		String sql = "select * from reserve";
		return qr.query(conn, sql, new BeanListHandler<Reserve>(Reserve.class));
	}
}