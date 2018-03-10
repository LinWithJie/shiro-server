<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8" />
		<title>${token.nickname} —个人中心</title>
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
		<link   rel="icon" href="${basePath}/favicon.ico" type="image/x-icon" />
		<link   rel="shortcut icon" href="${basePath}/favicon.ico" />
		<link href="${basePath}/js/common/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet"/>
		<link href="${basePath}/css/common/base.css" rel="stylesheet"/>
		<script  src="${basePath}/js/common/jquery/jquery1.8.3.min.js"></script>
		<script  src="${basePath}/js/common/layer/layer.js"></script>
		<script  src="${basePath}/js/common/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	</head>
	<body data-target="#one" data-spy="scroll">
		<@_top.top 1/>
		<div class="container" style="padding-bottom: 15px;min-height: 300px; margin-top: 40px;">
			<div class="row">
				<@_left.user 1/>
				<div class="col-md-10">
					<h2>个人资料</h2>
					<hr>
					<table class="table table-bordered">
						<tr>
							<th>昵称</th>
							<td>${token.nickname?default('未设置')}</td>
						</tr>
						<tr>
							<th>帐号</th>
							<td>${token.id?default('未设置')}</td>
						</tr>
						<tr>
							<th>目前消费折扣</th>
							<td>${token.discount?default(1.0)}</td>
						</tr>
						<tr>
							<th>购物积分</th>
							<td>${token.cardMoney?string('0')}</td>
						</tr>
                        <tr>
                            <th>共消费金额</th>
                            <td>${token.total?string('0')}</td>
                        </tr>
					</table>
				</div>
			</div>
			<#--/row-->
		</div>
			
	</body>
</html>