package com.zj.service.imp;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.zj.entity.House;
/**
 * 
 * @author lijia
 *房子服务层接口
 */
public interface HouseServiceImpl {
	/**
	 * 将所有房子信息包装成一个list<map>返回方法接口
	 */
	List<Map<String, Object>> getAllHouseInfo();
	/**
	 * 将单个房子信息包装成map返回方法接口
	 */
	Map<String, Object> getHouseInfoByID(Integer house_id);
	/**
	 * 添加一个房子信息方法接口
	 * @param house 一个房子信息
	 * @return
	 * @throws SQLException
	 */
	String addHouseInfo(House house) throws SQLException;
}
