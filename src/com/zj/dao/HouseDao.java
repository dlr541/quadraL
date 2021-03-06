package com.zj.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.com.util.GetConn;

import com.zj.dao.impl.HouseDaoImpl;
import com.zj.entity.GrogshopOrder;
import com.zj.entity.House;

/**
 * 
 * @author lijia 房子数据库操作实现类
 */
public class HouseDao implements HouseDaoImpl {
	/**
	 * 获取jdbc连接
	 */
	private QueryRunner qr = new QueryRunner();
	private Connection conn = null;

	/**
	 * 获取所有房子信息方法
	 */
	public List<House> getAllHouseInfo() throws SQLException {
		conn = GetConn.getConn();
		String sql = "select * from house";
		List<House> data = qr.query(conn, sql, new BeanListHandler<House>(
				House.class));
		GetConn.closeConn(conn);
		return data;
	}

	/**
	 * 获房子分页
	 */
	public List<House> queryHousePage(Integer startRow, Integer pageSize)
			throws SQLException {
		conn = GetConn.getConn();
		String sql = "select * from house limit ?,?";
		List<House> data = qr.query(conn, sql, new BeanListHandler<House>(
				House.class), startRow, pageSize);
		GetConn.closeConn(conn);
		return data;
	}

	/**
	 * 房子总页数
	 */
	public Long queryCountHouse() throws SQLException {
		conn = GetConn.getConn();
		String sql = "select count(*) from house";
		Long data = qr.query(conn, sql, new ScalarHandler<Long>());
		GetConn.closeConn(conn);
		return data;
	}

	/**
	 * 通过房子ID获取单个房子信息方法
	 */
	public House getHouseInfoByID(Integer HouseID) throws SQLException {
		conn = GetConn.getConn();
		String sql = "select * from house where house_id=?";
		House data = qr.query(conn, sql, new BeanHandler<House>(House.class),
				HouseID);
		GetConn.closeConn(conn);
		return data;
	}

	/**
	 * 添加一个房子DAO层方法
	 */
	public int addHouseInfo(House house) throws SQLException {
		conn = GetConn.getConn();
		String sql = "insert into house(User_id,house_name,house_intake,lease_type,may_check_in_date"
				+ ",may_check_out_date,house_type,house_particulars_id,travel_information,house_price"
				+ ",house_address,location_id) value(?,?,?,?,?,?,?,?,?,?,?,?)";
		int data = qr.update(conn, sql, house.getUser_id(),
				house.getHouse_name(), house.getHouse_intake(),
				house.getLease_type(), house.getMay_check_in_date(),
				house.getMay_check_out_date(), house.getHouse_type(),
				house.getHouse_particulars_id(), house.getTravel_information(),
				house.getHouse_price(), house.getHouse_address(),
				house.getLocation_id());
		GetConn.closeConn(conn);
		return data;
	}

	/**
	 * 按时间和地址联合查询房间
	 * 
	 * @param reserve_date
	 *            预定时间
	 * @param check_out_date
	 *            退房时间
	 * @param house_address
	 *            房子地址
	 * @return
	 * @throws SQLException
	 */
	public List<House> getHouseByDateAndAdd(String reserve_date,
			String check_out_date, String house_address) throws SQLException {
		conn = GetConn.getConn();
		String sql = "select * from reserve,house where (reserve_date > ? or check_out_date < ?) and "
				+ "(house.house_address LIKE \"%\"?\"%\") and reserve.house_id=house.house_id and (house_state = 1 or house_state=2)";
		List<House> date = qr.query(conn, sql, new BeanListHandler<House>(
				House.class), check_out_date, reserve_date, house_address);
		GetConn.closeConn(conn);
		return date;
	}

	/**
	 * 按是否有空闲时间查询房子
	 * 
	 * @param reserve_date
	 * @param check_out_date
	 * @return
	 * @throws SQLException
	 */
	public List<House> getHouseByDate(String reserve_date, String check_out_date)
			throws SQLException {
		conn = GetConn.getConn();
		String sql = "select * from reserve,house where (reserve_date > '?' or check_out_date < '?') and "
				+ "reserve.house_id=house.house_id and (house_state = 1 or house_state=2)";
		List<House> data = qr.query(conn, sql, new BeanListHandler<House>(
				House.class), check_out_date, reserve_date);
		GetConn.closeConn(conn);
		return data;
	}

	/**
	 * 按房子地址模糊查询房子信息
	 * 
	 * @param house_address
	 * @return
	 * @throws SQLException
	 */
	public List<House> getHouseByAdd(String house_address) throws SQLException {
		conn = GetConn.getConn();
		String sql = "select * from house where house_address  like \"%\"?\"%\" and (house_state = 1 or house_state=2)";
		List<House> data = qr.query(conn, sql, new BeanListHandler<House>(
				House.class), house_address);
		GetConn.closeConn(conn);
		return data;
	}

	/**
	 * 通过用户ID获取此用户旗下所有房子信息
	 * 
	 * @param user_id
	 * @return
	 * @throws SQLException
	 */
	public List<House> getHouseByID(Integer user_id) throws SQLException {
		conn = GetConn.getConn();
		String sql = "select * from house where user_id=?";
		List<House> data = qr.query(conn, sql, new BeanListHandler<House>(
				House.class), user_id);
		GetConn.closeConn(conn);
		return data;
	}

	/**
	 * 通过用户ID获取此用户旗下所有已审核房子信息
	 * 
	 * @param user_id
	 * @return
	 * @throws SQLException
	 */
	public List<House> getHouseByIDByState(Integer user_id) throws SQLException {
		conn = GetConn.getConn();
		String sql = "select * from house where user_id=? and (house_state = 1 or house_state=2)";
		List<House> data = qr.query(conn, sql, new BeanListHandler<House>(
				House.class), user_id);
		GetConn.closeConn(conn);
		return data;
	}

	/**
	 * 通过房子名字获得id
	 * 
	 * @param house_name
	 *            房子名字
	 * @return id
	 * @throws SQLException
	 */
	public Integer getHouseByName(String house_name) throws SQLException {
		conn = GetConn.getConn();
		String sql = "select house_id from house where house_name=?";
		Integer data = qr.query(conn, sql, new ScalarHandler(), house_name);
		conn.close();
		return data;
	}

	/**
	 * 修改房子状态
	 * 
	 * @param status
	 * @return
	 * @throws SQLException
	 */
	public Integer updateHouseStatus(Integer status, Integer house_id)
			throws SQLException {
		conn = GetConn.getConn();
		String sql = "update house set house_state=? where house_id=?";
		Integer data = qr.update(conn, sql, status, house_id);
		GetConn.closeConn(conn);
		return data;
	}

	/**
	 * 通过房子id获得房子
	 * 
	 * @param house_id
	 * @return
	 * @throws SQLException
	 */
	public House getHouseByHouseId(Integer house_id) throws SQLException {
		conn = GetConn.getConn();
		String sql = "select * from house where house_id=?";
		House data = qr.query(conn, sql, new BeanHandler<House>(House.class),
				house_id);
		conn.close();
		return data;
	}

	/**
	 * 分页查到一个房东名下所有的房子
	 * 
	 * @param user_id
	 * @param startRow
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	public List<House> getLandlordHouseById(Integer user_id, Integer startRow,
			Integer pageSize) throws SQLException {
		conn = GetConn.getConn();
		String sql = "(select * from house where user_id = ?) limit ?,?";
		List<House> data = qr.query(conn, sql, new BeanListHandler<House>(
				House.class), user_id, startRow, pageSize);
		GetConn.closeConn(conn);
		return data;
	}

	/**
	 * 获取一个房东房子总数
	 * 
	 * @param user_id
	 * @return
	 * @throws SQLException
	 */
	public Long queryLandlordHouse(Integer user_id) throws SQLException {
		conn = GetConn.getConn();
		String sql = "select count(*) from house where user_id = ?";
		Long data = qr.query(conn, sql, new ScalarHandler<Long>(),user_id);
		GetConn.closeConn(conn);
		return data;
	}

}
