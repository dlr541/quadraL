﻿package com.zj.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.util.PageUtil;

import com.zj.dao.ArticleDao;
import com.zj.dao.CommentDao;
import com.zj.dao.UserDao;
import com.zj.dao.impl.ArticleDaoImpl;
import com.zj.dao.impl.CommentDaoImpl;
import com.zj.dao.impl.UserDaoImpl;
import com.zj.entity.Article;
import com.zj.entity.Comment;
import com.zj.entity.GrogshopOrder;
import com.zj.entity.House;
import com.zj.entity.User;
import com.zj.service.impl.CommentServiceImpl;

/**
 * 
 * @author lijia 评论服务层
 */
public class CommentService implements CommentServiceImpl {

	private CommentDaoImpl commenDaoImpl = new CommentDao();
	private ArticleDaoImpl articleDaoImpl = new ArticleDao();
	private UserDaoImpl userDaoImpl = new UserDao();
	private Logger log = Logger.getLogger(HouseService.class);
	
	/**
	 * 将所有的评论打包成list返回
	 * 
	 * @throws SQLException
	 */

	public Map<String, Object> getAllComment(Integer limit,Integer page) throws SQLException {
		Map<String,Object> map = new HashMap<String,Object>();
		List<Comment> commentList = null;
		try {
			commentList = commenDaoImpl.queryCommentPage((page-1)*10, limit);
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for(Comment comment : commentList){
				Map<String, Object> commentMap = new HashMap<String, Object>();
				commentMap.put("comment_id", comment.getComment_id());
				commentMap.put("article_name", articleDaoImpl.queryArticleById(comment.getArticle_id()).getArticle_name());
				commentMap.put("user_name", userDaoImpl.getUserInfoById(comment.getUser_id()).getUser_name());
				commentMap.put("comment_content", comment.getComment_content());
				commentMap.put("replier_name", userDaoImpl.getUserInfoById(comment.getReplier_id()).getUser_name());
				commentMap.put("comment_date", comment.getComment_date());
				commentMap.put("comment_praise", comment.getComment_praise());
				list.add(commentMap);
			}
			map.put("data", list);
			map.put("count", Integer.valueOf(commenDaoImpl.queryCountComment().toString()));
			map.put("msg", "");
			map.put("code", 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	// 分页显示一篇文章评论
	public List<Map<String, Object>> getPageCommInfo(Integer commPresentPage,
			Integer article_id) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 评论分页
		Long commCount = commenDaoImpl.queryCommCount(article_id);
		PageUtil<Comment> pu = new PageUtil<Comment>();
		pu.setCountRow(commCount.intValue());
		pu.setCurrentPage(commPresentPage);
		int commStartRow = pu.getStartRow();
		int commPageSize = pu.getPageSize();
		List<Comment> pageComm = commenDaoImpl.queryPageComment(article_id,
				commStartRow, commPageSize);
		if (pageComm != null) {
			for (Comment comment : pageComm) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("comment_id", comment.getComment_id());
				map.put("article_id", comment.getArticle_id());
				map.put("user_id", comment.getUser_id());
				User user = userDaoImpl.getUserInfoById(comment.getUser_id());
				map.put("user_name", user.getUser_name());
				map.put("user_img", user.getUser_headimg_url());
				map.put("comment_content", comment.getComment_content());
				map.put("replier_id", comment.getReplier_id());
				map.put("comment_date", comment.getComment_date());
				// 获得该评论的回复数
				Integer comment_id = comment.getComment_id();
				Long replierCount = commenDaoImpl.queryReplierCount(comment_id);
				map.put("replierCount", replierCount);
				// 获得该评论赞数量
				map.put("praiseCount", comment.getComment_praise());
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 增加一条评论
	 */
	public Map<String, Object> addComment(Map<String, Object> info) {
		Comment comment = new Comment();
		Map<String, Object> map = new HashMap<String, Object>();
		// 给文章实体类set值进去
		if (info.get("user_id") != null)
			comment.setUser_id(Integer.valueOf(info.get("user_id").toString()));
		if (info.get("article_id") != null)
			comment.setArticle_id(new Integer(info.get("article_id").toString()));
		if (info.get("comment_content") != null)
			comment.setComment_content(info.get("comment_content").toString());
		if (info.get("replier_id") != null)
			comment.setReplier_id(Integer.valueOf(info.get("replier_id")
					.toString()));
		int cout = -1;
		try {
			cout = commenDaoImpl.addComment(comment);
			if (cout > 0) {
				User user = userDaoImpl.getUserInfoById(new Integer(info.get(
						"user_id").toString()));
				map.put("user_id", user.getUser_id());
				map.put("user_name", user.getUser_name());
				map.put("user_img", user.getUser_headimg_url());
				map.put("comment_content", comment.getComment_content());
				map.put("comment_date", new SimpleDateFormat(
						"YYYY-MM-dd HH:mm:ss").format(new Date()));
				map.put("msg", "评论成功！");
				//改变订单状态
				
			} else {
				map.put("msg", "评论失败！");
				return map;
			}
		} catch (SQLException e) {
			map.put("msg", "评论失败！");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 删除一条评论
	 */
	public Integer deleteComm(Integer comment_id) {
		try {
			Integer dal = commenDaoImpl.deleteComment(comment_id);
			if(dal > 0)
				return 1;
			else
				return 0;
		} catch (SQLException e) {
			log.error("数据库操作异常");
			return -1;
		}
	}

}
