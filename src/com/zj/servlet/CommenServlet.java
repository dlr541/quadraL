package com.zj.servlet;



import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.zj.service.ArticleService;
import com.zj.service.CommentService;
import com.zj.service.impl.ArticleServiceImpl;
import com.zj.service.impl.CommentServiceImpl;

import cn.com.util.BaseServlet;
/**
 * 文章评论信息审核 servlet
 * @author LanceEdward
 *
 */
public class CommenServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	private CommentServiceImpl commentService = new CommentService();
	private ArticleServiceImpl articleService = new ArticleService();
	private Logger log = Logger.getLogger(HouseServlet.class);
	
	public String commMap;
	public String callback;
	// 返回所有文章评论信息
	public void getAllComment(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = commentService.getAllComment();
		System.out.println(list);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		JSONObject obj = new JSONObject(map);
		response.getWriter().print(callback + "(" + obj + ")");
	}
	/**
	 * 分页显示评论
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void getPageCommInfo(HttpServletRequest request,HttpServletResponse response) throws SQLException, IOException {
		Integer article_id = Integer.valueOf(JSON.parse("article_id").toString());
		Integer commPresentPage = Integer.valueOf(JSON.parse("commPresentPage").toString());
		List<Map<String, Object>> list = articleService.getPageCommInfo(commPresentPage, article_id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageCommentInfo", list);
		JSONObject json = new JSONObject(map);
		response.getWriter().print(callback+"("+json+")");
	}
	/**
	 * 添加评论
	 * @param request
	 * @param response
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void addCommInfo(HttpServletRequest request,HttpServletResponse response) throws  IOException {
		@SuppressWarnings("unchecked")
		Map<String, Object> commInfo = (Map<String, Object>) JSON.parse(commMap);
		log.info(commInfo);
		Map<String, Object> hint = new HashMap<String, Object>();
		Integer commNum = commentService.addComm(commInfo);
		if(commNum> 0) {
			hint.put("msg", "评论成功！");
			hint.put("code", 1);
		}else {
			hint.put("msg", "评论失败！");
			hint.put("code", -1);
		}
		JSONObject json = new JSONObject(hint);
		response.getWriter().print(callback + "(" + json + ")");
	}
}
