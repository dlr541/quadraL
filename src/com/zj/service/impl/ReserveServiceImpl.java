package com.zj.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * 预定信息接口
 * @author LanceEdward
 *
 */
public interface ReserveServiceImpl {
	/**
	 * 将所有预定信息包装成一个list<map>返回
	 */
	Map<String,Object> getAllReserve(Integer orderPresentPage,Integer pageSize)throws SQLException;
//	/**
//	 * 添加一个订单预定信息方法接口
//	 * @param reserve
//	 * @return
//	 */
//	String addReserveInfo(Map<String, Object> reserveInfo);
}
