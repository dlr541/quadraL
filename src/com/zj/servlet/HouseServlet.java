package com.zj.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.zj.service.HouseService;
import com.zj.service.impl.HouseServiceImpl;

import cn.com.util.BaseServlet;
import cn.com.util.FileLoadServletUtil;

public class HouseServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private HouseServiceImpl houseService = new HouseService();
	private Logger log = Logger.getLogger(HouseServlet.class);
	public String houseMap;
	public String houseImgMap;
	public String callback;
	public String user_id;
	public String selectMap;
	private String status;
	private String house_id;
	// 当前页数
	private Integer page;
	// 每页条数
	private Integer limit;

	/**
	 * 添加一个房子信息
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addHouseInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println(houseMap);
		@SuppressWarnings("unchecked")
		Map<String, Object> house = (Map<String, Object>) JSON.parse(houseMap);
		String houseImg = house.get("allHouseImg").toString();
		String house_name = house.get("house_name").toString();
		log.info(house);
		log.info(houseImg);
		Map<String, Object> hint = new HashMap<String, Object>();
		Integer houseNum = houseService.addHouseInfo(house);
		Integer houseImgNum = houseService.addHouseImg(house_name, houseImg);
		if (houseNum > 0 && houseImgNum > 0) {
			hint.put("msg", "发布成功！");
			hint.put("code", 1);
		} else {
			hint.put("msg", "发布失败！");
			hint.put("code", -1);
		}
		JSONObject json = new JSONObject(hint);
		response.getWriter().print(callback + "(" + json + ")");
	}

	/**
	 * 通过用户ID获取此用户的所有房子
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getHouseInfoByUserID(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Integer userID = Integer.valueOf(JSON.parse(user_id).toString());
		Map<String, Object> map = houseService.getLandlordHouseById(userID,limit,page);
		JSONObject json = new JSONObject(map);
		response.getWriter().print(json);
	}

	/**
	 * 通过选择返回一组房子信息
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getHouseInfoBySelect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println(selectMap);
		@SuppressWarnings("unchecked")
		Map<String, Object> select = (Map<String, Object>) JSON
				.parse(selectMap);
		String reserve_date = select.get("reserve_date").toString();
		String check_out_date = select.get("check_out_date").toString();
		String house_address = select.get("house_address").toString();
		System.out.println("reserve_date:" + reserve_date);
		System.out.println("check_out_date:" + check_out_date);
		System.out.println("house_address:" + house_address);
		List<Map<String, Object>> list = houseService.getHouseByDateOrAddress(
				reserve_date, check_out_date, house_address);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("houseList", list);
		JSONObject json = new JSONObject(map);
		response.getWriter().print(callback + "(" + json + ")");
	}

	/**
	 * 上传图片到本地并返回路径
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws FileUploadException
	 */
	public void uploadImg(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			FileUploadException {
		// 图片上传并且返回保存的路径
		String url = FileLoadServletUtil.upload(request, response,
				"D:/MyEclipse2015work/quadraL/WebRoot/image/houseImg/");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		HashMap<String, Object> data = new HashMap<String, Object>();
		// 将图片地址存到数据库(因为浏览器不能直接访问本地路径，会报错)；
		String dataBaseUrl = url.substring(37);
		data.put("src", dataBaseUrl);
		map.put("data", data);
		JSONObject obj = new JSONObject(map);
		// 如果上传成功返回1
		response.getWriter().print(obj);
	}

	/**
	 * 修改房子状态服务层接口
	 * 
	 * @param status
	 * @return
	 * @throws SQLException
	 */
	public void updateHouseStatus(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			FileUploadException {
		Integer data = houseService.updateHouseStatus(Integer.valueOf(status),
				Integer.valueOf(house_id));
		response.getWriter().print(callback + "(" + data + ")");
	}
}
