package com.zj.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.com.uitl.GetConn;

import com.zj.dao.impl.GrogshopOrderDaoImpl;
import com.zj.entity.GrogshopOrder;

/**
 * 订单数据库操作实现类
 * 
 * @author LanceEdward
 * 
 */
public class GrogshopOrderDao implements GrogshopOrderDaoImpl {
	private QueryRunner qr = new QueryRunner();
	private Connection conn = GetConn.getConn();

	/**
	 * 获取所有酒店订单信息DAO层方法
	 * @return
	 * @throws SQLException
	 */
	public List<GrogshopOrder> getAllGrogshopOrderInfo() throws SQLException {
		String sql = "select * from grogshop_order";
		return qr.query(conn, sql, new BeanListHandler<GrogshopOrder>(
				GrogshopOrder.class));
	}

	/**
	 * 通过ID获取酒店订单信息DAO层接口
	 * @param GrogshopOrderID
	 * @return
	 * @throws SQLException
	 */
	public GrogshopOrder getGrogshopOrderInfoByID(Integer GrogshopOrderID)
			throws SQLException {
		String sql = "select * from grogshop_order where grogshop_order_id = ?";
		return   qr.query(conn, sql,
				new BeanHandler<GrogshopOrder>(GrogshopOrder.class),
				GrogshopOrderID);
	}

}