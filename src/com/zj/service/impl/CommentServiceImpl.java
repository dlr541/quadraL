﻿
package com.zj.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author lijia
 *评论服务层
 */
public interface CommentServiceImpl {
	/**
	 * 将所有的评论打包成list返回方法接口
	 */
	List<Map<String, Object>> getAllComment();
	/**
	 * 评论分页
	 */
	List<Map<String, Object>> getPageCommInfo(Integer commPresentPage,Integer article_id) throws SQLException;
	/**
	 * 添加评论
	 */
	Integer addComm(Map<String, Object> addCommInfo);
}
