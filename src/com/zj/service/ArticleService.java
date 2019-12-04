﻿package com.zj.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import cn.com.util.PageUtil;

import com.zj.dao.ArticleDao;
import com.zj.dao.ArticleImgDao;
import com.zj.dao.CommentDao;
import com.zj.dao.HouseDao;
import com.zj.dao.HouseImgDao;
import com.zj.dao.UserDao;
import com.zj.dao.impl.ArticleDaoImpl;
import com.zj.dao.impl.ArticleImgDaoImpl;
import com.zj.dao.impl.CommentDaoImpl;
import com.zj.dao.impl.HouseDaoImpl;
import com.zj.dao.impl.HouseImgDaoImpl;
import com.zj.dao.impl.UserDaoImpl;
import com.zj.entity.Article;
import com.zj.entity.ArticleImg;
import com.zj.entity.Comment;
import com.zj.entity.GrogshopOrder;
import com.zj.entity.House;
import com.zj.entity.HouseImg;
import com.zj.entity.User;
import com.zj.service.impl.ArticleServiceImpl;

/**
 * 文章服务层
 * @author liyifeng
 *
 */
public class ArticleService implements ArticleServiceImpl{
	private ArticleDaoImpl articleDaoImpl = new ArticleDao();
	private ArticleImgDaoImpl articleImgDaoImpl = new ArticleImgDao();
	private CommentDaoImpl commentDaoImpl = new CommentDao();
	private UserDaoImpl userDaoImpl = new UserDao();
	private HouseDaoImpl houseDaoImpl = new HouseDao();
	private HouseImgDaoImpl houseImgDaoImpl = new HouseImgDao();
	
	/**
	 * 获得所有文章
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> getAllArticle(Integer articlePresentPage,Integer pageSize) throws SQLException{
		//文章图片
		//文章作者
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 文章分页
		Long articleCount = articleDaoImpl.queryCountArticle();
		PageUtil<Article> pu = new PageUtil<Article>();
		pu.setCountRow(articleCount.intValue());
		pu.setCurrentPage(articlePresentPage);
		pu.setPageSize(pageSize);
		
		int articleStartRow = pu.getStartRow();
		int articlePageSize = pu.getPageSize();
		
		List<Article> articleOrder = articleDaoImpl.queryArticlePage(articleStartRow, articlePageSize);
		List<ArticleImg> articleImg = articleImgDaoImpl.queryArticleImg();
		List<User> user = userDaoImpl.getAllUserInfo();
		Map<String,Object> dataMap = new HashMap<String, Object>();
		dataMap.put("article", articleOrder);
		dataMap.put("articleImg", articleImg);
		dataMap.put("user", user);
		pu.setMap(dataMap);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pageUtil", pu);
		return map;
	}
	
	/**
	 * 分页显示文章所需信息
	 * @param articlePresentPage 当前页
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getPageArticleInfo(Integer articlePresentPage) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//文章分页
		Long articleCount = articleDaoImpl.queryCountArticle();
		PageUtil<Article> pu = new PageUtil<Article>();
		pu.setCountRow(articleCount.intValue());
		pu.setCurrentPage(articlePresentPage);
		int articleStartRow = pu.getStartRow();
		int articlePageSize = pu.getPageSize();
		List<Article> pageArticle = articleDaoImpl.queryPageArticle(articleStartRow, articlePageSize);
		if(pageArticle != null) {
			for(Article article : pageArticle) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("article_id", article.getArticle_id());
				map.put("user_id", article.getUser_id());
				map.put("article_name", article.getArticle_name());
				map.put("article_content", article.getArticle_content());
				map.put("article_collect", article.getArticle_collect());
				map.put("article_praise", article.getArticle_praise());
				//获得该文章的第一张图片
				Integer article_id = article.getArticle_id();
				List<ArticleImg> currenArticleImg = articleImgDaoImpl.queryArticleImgByArticleId(article_id);
				ArticleImg firstImg  = currenArticleImg.get(0);
				map.put("image_id", firstImg.getImage_id());
				map.put("image_url", firstImg.getImage_url());
				//获取该文章评论数量
				Long commCount = commentDaoImpl.queryCommCount(article_id);
				map.put("commCount", commCount);
				//获得用户头像
				Integer user_id = article.getUser_id();
				User user = userDaoImpl.getUserInfoById(user_id);
				String userHeadUrl = user.getUser_headimg_url();
				map.put("user_headimg_url", userHeadUrl);
				list.add(map);
			}
		} 
		return list;
	}
	
	/**
	 * 一篇文章所有信息
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> getOneArticleInfo(Integer article_id) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//获得该文章
		Article oneArticle = articleDaoImpl.queryArticleById(article_id);
		if(oneArticle != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("article_id", oneArticle.getArticle_id());
			map.put("user_id", oneArticle.getUser_id());
			map.put("article_name", oneArticle.getArticle_name());
			map.put("article_content", oneArticle.getArticle_content());
			map.put("article_date", oneArticle.getArticle_date());
			map.put("article_collect", oneArticle.getArticle_collect());
			map.put("article_praise", oneArticle.getArticle_praise());
			map.put("house_id",oneArticle.getHouse_id());
			Map<String, Object> imgMap = new HashMap<String, Object>();
			//获得该文章所有图片
			List<ArticleImg> currenArticleImg = articleImgDaoImpl.queryArticleImgByArticleId(article_id);
			for(ArticleImg articleImg : currenArticleImg) {
				imgMap.put("image_id", articleImg.getImage_id());
				imgMap.put("image_url", articleImg.getImage_url());
			}
			map.put("article_images", imgMap);
			//获得该文章评论数量
			Long commCount = commentDaoImpl.queryCommCount(article_id);
			map.put("commNum", commCount);
			//获得该文章所有评论
			List<Comment> currenComment = commentDaoImpl.queryPageComment(article_id,1,5);
			for(Comment comment : currenComment) {
				map.put("comment_id", comment.getComment_id());
				map.put("comment_content", comment.getComment_content());
				map.put("replier_id", comment.getReplier_id());
			}
			//获得用户头像与昵称
			Integer user_id = oneArticle.getUser_id();
			User user = userDaoImpl.getUserInfoById(user_id);
			String userHeadUrl = user.getUser_headimg_url();
			String user_name = user.getUser_name();
			map.put("user_headimg_url", userHeadUrl);
			map.put("user_name", user_name);
			Map<String, Object> houseMap = new HashMap<String, Object>();
			//获取房子图片标题房子类型(house_type)
			Integer house_id = oneArticle.getHouse_id();
			House house= houseDaoImpl.getHouseInfoByID(house_id);
			houseMap.put("house_name", house.getHouse_name());
			houseMap.put("house_type", house.getHouse_type());
			//第一张图片
			List<HouseImg> currenHouseImg = houseImgDaoImpl.getHouseImgByHouseID(house_id);
			HouseImg firstImg  = currenHouseImg.get(0);
			houseMap.put("house_image_id", firstImg.getHouse_img_id());
			houseMap.put("house_image_url", firstImg.getHouse_img_url());
			map.put("house", houseMap);
			//获取相关文章
			String keyWord = oneArticle.getArticle_name().substring(0,2);
			List<Article> relatedArticles = articleDaoImpl.queryFuzzyQuery(keyWord);
			List<Map<String, Object>> articleInfo =  new ArrayList<Map<String,Object>>();
			Map<String, Object> article = new HashMap<String, Object>();
			for(Article relatedArticle : relatedArticles) {
				//一篇相关文章
				article.put("article_id", relatedArticle.getArticle_id());
				article.put("article_name", relatedArticle.getArticle_name());
				article.put("article_praise", relatedArticle.getArticle_praise());
				//获得相关文章文章的第一张图片
				Integer relatedArticleArticle_id = relatedArticle.getArticle_id();
				List<ArticleImg> relatedArticleCurrenArticleImg = articleImgDaoImpl.queryArticleImgByArticleId(relatedArticleArticle_id);
				ArticleImg relatedArticleFirstImg  = relatedArticleCurrenArticleImg.get(0);
				article.put("article_img", relatedArticleFirstImg.getImage_url());
				//获取相关文章文章评论数量
				Long relatedArticleCommCount = commentDaoImpl.queryCommCount(relatedArticleArticle_id);
				article.put("article_collect", relatedArticleCommCount);
				//获得相关文章用户头像
				Integer relatedArticleUser_id = relatedArticle.getUser_id();
				User relatedArticleUser = userDaoImpl.getUserInfoById(relatedArticleUser_id);
				String relatedArticleUserHeadUrl = relatedArticleUser.getUser_headimg_url();
				article.put("user_img", relatedArticleUserHeadUrl);
			}
			articleInfo.add(article);
			map.put("articleInfo", articleInfo);
			list.add(map);
			return list;
		}
		return null;
	}
	
	/**
	 * 分页显示评论
	 */
	public List<Map<String, Object>> getPageCommInfo(Integer commPresentPage,Integer article_id) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//评论分页
		Long commCount = commentDaoImpl.queryCommCount(article_id);
		PageUtil<Comment> pu = new PageUtil<Comment>();
		pu.setCountRow(commCount.intValue());
		pu.setCurrentPage(commPresentPage);
		int commStartRow = pu.getStartRow();
		int commPageSize = pu.getPageSize();
		List<Comment> pageComm = commentDaoImpl.queryPageComment(article_id, commStartRow, commPageSize);
		if(pageComm != null) {
			for(Comment comment : pageComm) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("comment_id", comment.getComment_id());
				map.put("article_id", comment.getArticle_id());
				map.put("user_id", comment.getUser_id());
				map.put("comment_content", comment.getComment_content());
				map.put("replier_id", comment.getReplier_id());
				//获得该评论的回复数
				Integer comment_id = comment.getComment_id();
				Long replierCount = commentDaoImpl.queryReplierCount(comment_id);
				map.put("replierCount", replierCount);
				//获得该评论赞数量
				Long praiseCount = commentDaoImpl.queryPraiseCount(comment_id);
				map.put("praiseCount", praiseCount);
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public int addArticle(Integer user_id, String article_name,
			String article_content, Integer house_id) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteArticleById(Integer article_id) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}


}
