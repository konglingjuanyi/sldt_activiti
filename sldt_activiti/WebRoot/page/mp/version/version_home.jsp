<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<SCRIPT type="text/javascript"
	src="<%=request.getContextPath()%>/page/mp/home/wait_to_do.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/public/layer/skin/layer.css">
<LINK rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/page/mp/css/version_home.css">
<LINK rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/page/mp/css/metadata_user.css">
<script type="text/javascript">
    var context = '<%=request.getContextPath()%>';
</script>
<title>版本</title>
</head>
<body>
	<div style="padding: 50px;">
		<div id="home_apDiv15">
			<div id="home_apDiv212" class="y" style="margin-top: 5px;">最新版本动态</div>
			<div id="home_apDiv312"></div>
			<div id="more2">
				<a style="cursor: pointer;" onclick="openAllSysVerPage()"><img src="../imgs/gf_more.png" /></a>
			</div>

			<div style="border-top: 1px solid #dcdcdc;  border-right: 1px solid #dcdcdc;border-bottom: 1px solid #dcdcdc;border-left: 1px solid #dcdcdc;margin-top: 46px; background: url(<%=request.getContextPath()%>/page/mp/imgs/version_home/line-y.png) repeat-y 53px 0">
				<div id="container_metauser_n" style="overflow: hidden; height: 395px; width: 365px;">
					<div style="height: 600000px;">
						<div id="wait_to_do_user_n" style="height: 350px; padding-top: 100px;">
							<div class='bg2' style="margin-top: 10px;">
								<strong>15-01-01</strong>
							</div>
							<div style="padding-left: 10px;">
								<div class='txt2'>邓华 发布版本【POS收单系统（POSP系统）集采S29C版本002】</div>
							</div>
							<div class='bg2' style="margin-top: 0px;">
								<strong>15-01-01</strong>
							</div>
							<div style="padding-left: 10px;">
								<div class='txt2'>邓华 发布版本【POS收单系统（POSP系统）集采S29C版本002】</div>
							</div>
						</div>
						<div id="wait_to_do_2_user_n" style="float: left; height: 400px; padding-top: 100px;"></div>
					</div>
				</div>
				<div class="clear"></div>
			</div>
		</div>

		<div id="home_apDiv25">
			<div id="home_apDiv213" class="y" style="margin-top: 5px;">待办事项</div>
			<div id="home_apDiv313"></div>
			<div id="more3_v">
				<a style="cursor: pointer;" onclick="openMyDecPage()"><img src="../imgs/gf_more.png" /></a>
			</div>
			<div id="home_apDiv26">
				<div id="home_apDiv27">
					<div id="home_apDiv28">当前有：0笔代办</div>
				</div>
				<div style="margin-top: 60px; background: url(<%=request.getContextPath()%>/page/mp/imgs/version_home/x.png) repeat-y 25px 0">
					<div id="container">
						<div style="height: 6000000px;">
							<div id="wait_to_do" style="float: left; height: 180px; padding-top: 100px;"></div>
							<div id="wait_to_do_2" style="float: left; height: 260px; padding-top: 100px;"></div>
						</div>
					</div>
					<div class="clear"></div>
				</div>
				<div id="home_apDiv34"></div>
				<div id="home_apDiv35"></div>
				<div id="home_apDiv36">今天的工作就快完成了(^_^)</div>
			</div>
		</div>

		<div id="home_my_apDiv1">
			<div id="home_my_apDiv2" class="y" style="margin-top: 5px;">我的版本</div>
			<div id="home_my_apDiv3"></div>
			<div id="more_my">
				<a style="cursor: pointer;" onclick="openAllSysVerPage()"><img src="../imgs/gf_more.png" /></a>
			</div>
			<div id="home_my_apDiv5">
				<div class="home_my_apDiv6"></div>
				<div class="home_my_apDiv7">POS收单系统</div>
				<div class="home_my_apDiv8">POS收单系统(POSP系统）_集S29C</div>
				<div class="home_my_apDiv4">
					<div class="home_my_apDiv9">0.0.1</div>
					<div class="home_my_apDiv11">赵薇发布</div>
					<div class="home_my_apDiv10">0.0.2</div>
					<div class="home_my_apDiv12">7月25日</div>
				</div>
				<div class="home_my_apDiv14"></div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	loadTaskList(true);
    newVerItem();
    myVerMetaItem();
</script>
</html>