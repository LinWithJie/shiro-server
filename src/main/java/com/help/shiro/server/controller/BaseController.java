package com.help.shiro.server.controller;


import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


public class BaseController {

	
	protected int pageNo =1;
	public static  int pageSize = 10;
	protected final static Logger logger = Logger.getLogger(BaseController.class);
	protected Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
	public static String URL404 =  "/404.html";
	
	private final static String PARAM_PAGE_NO = "pageNo";
	
	protected String pageSizeName = "pageSize";
	
	/**
	 * 往Request里带值
	 * @param request
	 * @param key
	 * @param value
	 */
	protected static void setValue2Request(HttpServletRequest request,String key,Object value){
		request.setAttribute(key, value);
	}
	
	/**
	 * [获取session]
	 * @param request
	 * @return
	 */
	public static HttpSession getSession(HttpServletRequest request){
		return request.getSession();
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		BaseController.pageSize = pageSize;
	}
	
	public ModelAndView redirect(String redirectUrl, Map<String,Object>...parament){
		ModelAndView view = new ModelAndView(new RedirectView(redirectUrl));
		if(null != parament && parament.length > 0){
			view.addAllObjects(parament[0]);
		}
		return view;
	}
	public ModelAndView redirect404(){
		return new ModelAndView(new RedirectView(URL404));
	}

	@ModelAttribute
	public void initPath(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String base = request.getContextPath();
		String fullPath = request.getScheme()+"://"+request.getServerName()+base;
		model.addAttribute("basePath", base);
		model.addAttribute("_v", String.valueOf(System.currentTimeMillis()));

	}

}
