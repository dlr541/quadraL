package com.zj.servlet;



import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.zj.service.GrogshopOrderService;
import com.zj.service.impl.GrogshopOrderServiceImpl;

import cn.com.util.BaseServlet;

public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private GrogshopOrderServiceImpl orderService = new GrogshopOrderService();
	private Logger log = Logger.getLogger(OrderServlet.class);
	private String callback;
	//订单id
	private String order_id;
	private String grogshopOrderInfo;
	private String checkInPersonInfo;
	private Integer user_id;
	//当前页数
	private  Integer currentPage;
	//总页数
	private  Integer page;
	//订单状态
	private Integer state;
	//每页条数
	private  Integer limit;
	
	// 返回订单所有信息
	public void getAllGrogshopOrderInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {
		Map<String, Object> map = orderService.getAllGrogshopOrderInfo(limit,page);
		JSONObject obj = new JSONObject(map);
		System.out.println("obj:" + obj);
		response.getWriter().print(callback + "(" + obj + ")");
	}

	// 按订单id返回订单信息
	public void getGrogshopOrderInfoByID(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> map = orderService
				.getAllGrogshopOrderInfoByID(order_id);
		System.out.println(map);
		JSONObject obj = new JSONObject(map);
		response.getWriter().print(callback + "(" + obj + ")");
	}
	/**
	 * 添加订单信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addOrderInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println(grogshopOrderInfo);
		@SuppressWarnings("unchecked")
		Map<String, Object> grogshopOrder = (Map<String, Object>) JSON.parse(grogshopOrderInfo);
		System.out.println(checkInPersonInfo);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> checkInPerson = (List<Map<String, Object>>) JSON.parse(checkInPersonInfo);
		log.info(grogshopOrder);
		log.info(checkInPerson);
		Integer count = orderService.addGrogshopOrderInfo(grogshopOrder, checkInPerson);
		log.debug(count);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("hint", count);
		JSONObject json = new JSONObject(map);
		response.getWriter().print(callback+"("+json+")");
	}
	
	/**
	 * 通过用户ID获得此用户的所有订单信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getOrderInfoByUserID(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Map<String, Object>> list = orderService.getGrogshopOrderInfoByUserID(user_id,state);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		JSONObject json = new JSONObject(map);
		response.getWriter().print(callback+"("+json+")");
	}
	
	/**
	 * 通过房东ID获得所有在此房东的房子下单的用户信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getUserInfoByLandlordID(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> map = orderService.getGrogshopOrderInfoByLandlordID(user_id,state,limit,page);
		JSONObject obj = new JSONObject(map);
		System.out.println("obj:" + obj);
		response.getWriter().print(obj);
	}
	
	/**
	 * 修改订单状态并完成该状态下的操作
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateOrder(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println(state);
		Integer code = orderService.updateOrder(order_id, state);
		map.put("code", code);
		JSONObject obj = new JSONObject(map);
		System.out.println("obj:" + obj);
		response.getWriter().print(obj);
	}
}
