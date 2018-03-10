<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8" />
		<title>资料修改 —个人中心</title>
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
		<link   rel="icon" href="${basePath}/favicon.ico" type="image/x-icon" />
		<link   rel="shortcut icon" href="${basePath}/favicon.ico" />
		<link href="${basePath}/js/common/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet"/>
		<link href="${basePath}/css/common/base.css" rel="stylesheet"/>
		<script  src="${basePath}/js/common/jquery/jquery1.8.3.min.js"></script>
		<script  src="${basePath}/js/common/layer/layer.js"></script>
		<script  src="${basePath}/js/common/bootstrap/3.3.5/js/bootstrap.min.js"></script>
		<script >
		</script>
	</head>
	<body data-target="#one" data-spy="scroll">
		
		<@_top.top 1/>
		<div class="container" style="padding-bottom: 15px;min-height: 300px; margin-top: 40px;">
			<div class="row">
				<@_left.user 1/>
				<div class="col-md-10">
					<h2>资料修改</h2>
					<hr>
					<form id="formId" enctype="multipart/form-data" action="${basePath}/updateSelf" method="post">
						  <div class="form-group">
						    <label for="nickname">昵称</label>
						    <input type="text" class="form-control" autocomplete="off" id="nickname" maxlength="8" name="nickname" value="${token.nickname?default('未设置')}" placeholder="请输入昵称">
						  </div>
						  <div class="form-group">
						    <label for="id">手机号</label>
						    <input type="text" class="form-control " readonly="readonly" autocomplete="off" id="id" maxlength="64" name="id" value="${token.id?default('未设置')}" placeholder="">
						  </div>
                        <div class="form-group">
                            <label for="cardMoney">购物积分</label>
                            <input type="text" class="form-control " readonly="readonly" autocomplete="off" id="cardMoney" maxlength="64" name="cardMoney" value="${token.cardMoney?default('未设置')}" placeholder="">
                        </div>
                        <div class="form-group">
                            <label for="discount">当前折扣</label>
                            <input type="text" class="form-control " readonly="readonly" autocomplete="off" id="discount" maxlength="64" name="discount" value="${token.discount?default('未设置')}" placeholder="">
                        </div>
                        <div class="form-group">
                            <label for="idCard">身份证（参考）</label>
                            <input type="text" class="form-control " readonly="readonly" autocomplete="off" id="idCard" maxlength="64" name="idCard" value="${token.idCard?default('未设置')}" placeholder="">
                        </div>
                        <div class="form-group">
                            <label for="total">总消费金额</label>
                            <input type="text" class="form-control " readonly="readonly" autocomplete="off" id="total" maxlength="64" name="total" value="${token.total?default('未设置')}" placeholder="">
                        </div>
						  <div class="form-group">
							  <button type="submit" class="btn btn-success">确定修改</button>
						  </div>
						</form>
				</div>
				 <#--地图
				<@_html.tool_map/>
				-->
			</div><#--/row-->
		</div>
		<#-- 页脚
		<@_footer.footer 0/>
		-->
		<script src="${basePath}/js/common/jquery/jquery.form-2.82.js?${_v}"></script>
		<script>
			$(function(){
				var load;
				$("#formId").ajaxForm({
			    	success:function (result){
			    		layer.close(load);
			    		if(result && result.status == 300){
			    			layer.msg(result.message,function(){});
			    			return !1;
			    		}
			    		if(result && result.status == 500){
			    			layer.msg("操作失败！",function(){});
			    			return !1;
			    		}
			    		layer.msg('操作成功！');
			    	},
			    	beforeSubmit:function(){
			    		//判断参数
			    		if($.trim($("#nickname").val()) == ''){
				    		layer.msg('昵称不能为空！',function(){});
				    		$("#nickname").parent().removeClass('has-success').addClass('has-error');
				    		return !1;
			    		}else{
			    			$("#nickname").parent().removeClass('has-error').addClass('has-success');
			    		}
			    		load = layer.load();
			    	},
			    	dataType:"json",
			    	clearForm:false
				});
			
		});
		</script>
			
	</body>
</html>