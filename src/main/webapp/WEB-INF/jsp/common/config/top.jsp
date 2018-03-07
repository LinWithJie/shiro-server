<%@ page pageEncoding="utf-8"%>
<%--shiro 标签 --%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<% 
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
 %> 
<script baseUrl="<%=basePath%>" src="<%=basePath%>/js/user.login.js"></script>
<div class="navbar navbar-inverse navbar-fixed-top animated fadeInDown" style="z-index: 101;height: 41px;">
	  
      <div class="container" style="padding-left: 0px; padding-right: 0px;">
        <div class="navbar-header">
          <button data-target=".navbar-collapse" data-toggle="collapse" type="button" class="navbar-toggle collapsed">
            <span class="sr-only">导航</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
	     </div>
	     <div role="navigation" class="navbar-collapse collapse">
	          <ul class="nav navbar-nav" id="topMenu">
				<li class="dropdown ">
					<a aria-expanded="false" aria-haspopup="true" role="button" data-toggle="dropdown" class="dropdown-toggle" href="<%=basePath%>/index">
						个人中心<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="<%=basePath%>/index">个人资料</a></li>
						<li><a href="<%=basePath%>/updateSelf" >资料修改</a></li>
						<li><a href="<%=basePath%>/updatePswd" >密码修改</a></li>
						<li><a href="<%=basePath%>/role/mypermission">我的权限</a></li>
					</ul>
				</li>	  
				<%--拥有 角色888888（管理员） ||  100002（用户中心）--%>
				<shiro:hasAnyRoles name='888888,100002'>          
				<li class="dropdown ">
					<a aria-expanded="false" aria-haspopup="true"  role="button" data-toggle="dropdown" class="dropdown-toggle" href="<%=basePath%>/member/list.shtml">
						用户中心<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<shiro:hasPermission name="/member/list">
							<li><a href="<%=basePath%>/member/list">用户列表</a></li>
						</shiro:hasPermission>
					</ul>
				</li>	
				</shiro:hasAnyRoles>         
				<%--拥有 active888888（管理员） ||  100003（权限频道）--%>
				<shiro:hasAnyRoles name='888888,100003'>
					<li class="dropdown active">
						<a aria-expanded="false" aria-haspopup="true"  role="button" data-toggle="dropdown" class="dropdown-toggle" href="/permission/index.shtml">
							权限管理<span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<shiro:hasPermission name="/role/index">
								<li><a href="<%=basePath%>/role/index">角色列表</a></li>
							</shiro:hasPermission>
							<shiro:hasPermission name="/role/allocation">
								<li><a href="<%=basePath%>/role/allocation">角色分配（这是个JSP页面）</a></li>
							</shiro:hasPermission>
							<shiro:hasPermission name="/permission/index">
								<li><a href="<%=basePath%>/permission/index">权限列表</a></li>
							</shiro:hasPermission>
							<shiro:hasPermission name="/permission/allocation">
								<li><a href="<%=basePath%>/permission/allocation">权限分配</a></li>
							</shiro:hasPermission>
						</ul>
					</li>	
				</shiro:hasAnyRoles>    
				<li>
					<a class="dropdown-toggle" target="_blank" href="http://www.sojson.com/tag_shiro.html" target="_blank">
						Shiro相关博客<span class="collapsing"></span>
					</a>
				</li>
	          </ul>
	           <ul class="nav navbar-nav  pull-right" >
				<li class="dropdown " style="color:#fff;">
					<%--已经登录（包括记住我的）--%>
					<shiro:user>  
						<a aria-expanded="false" aria-haspopup="true"  role="button" data-toggle="dropdown" onclick="location.href='<%=basePath%>/index'" href="<%=basePath%>/index" class="dropdown-toggle qqlogin" >
						<shiro:principal/>
						<span class="caret"></span></a>
						<ul class="dropdown-menu" userid="">
							<li><a href="<%=basePath%>/index">个人资料</a></li>
							<li><a href="<%=basePath%>/role/mypermission">我的权限</a></li>
							<li><a href="javascript:void(0);" onclick="logout();">退出登录</a></li>
						</ul>
					</shiro:user>   

					<%--没有登录(游客)--%>
					<shiro:guest>  
						 <a aria-expanded="false" aria-haspopup="true"  role="button" data-toggle="dropdown"  href="javascript:void(0);" class="dropdown-toggle qqlogin" >
						<img src="//qzonestyle.gtimg.cn/qzone/vas/opensns/res/img/Connect_logo_1.png">&nbsp;登录</a>
					</shiro:guest>  
				</li>	
	          </ul>
	          <style>#topMenu>li>a{padding:10px 13px}</style>
	    </div>
  	</div>
</div>
