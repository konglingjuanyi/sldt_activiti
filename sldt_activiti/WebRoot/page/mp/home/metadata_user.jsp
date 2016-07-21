<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/metadata_user.css">
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/home/metadata_user.js"></script>

<script type="text/javascript">
	var context = '<%=request.getContextPath()%>';
</script>
</head>
<body>
	<div id="home_user_apDiv1">
		<div style="margin-left: 85px; padding-top: 60px;">
			<div id="home_user_apDiv2" class="y">元数据用户</div>
			<div id="home_user_apDiv3"></div>
			<div class="sponsor" title="Click to flip">
				<div id="ysj-zy" class="sponsorFlip" flipped="no">
					<div class="wz">元数据管理专员</div>
					<div class="wz-2">汇总IT项目组长元数据申请</div>
					<div class="wz-k">
						<span id="totalUsers_1">0</span>人
					</div>
					<div class="wz-k">
						<span id="totalDealTimes_1">0</span>+变更
					</div>
					<div class="yx">
						<div class="font_cet_img">
							<img class="f_batx" id='f_batx_1' data-name="gc1200gc" src="../imgs/resource/1-21.jpg">
						</div>
					</div>
					<div class="wz-r">[优秀用户]</div>
					<div class="wz-name">
						<span id="fstName_1"></span>
					</div>
					<div class="wz-bg">
						<span id="fstDealTimes_1">0</span>次变更申请
					</div>
					<div style="clear: both"></div>
				</div>
				<div id="ysj-zy" class="sponsorData">
					<div class="back_uss">
						<div class="back_uss_tit">元数据管理专员</div>
						<div class="back_uss_avt">
							<div class="avator1">
								<a href="javascript:getMetaUserInfo(1)" title="用户1"><img class="batx" data-name="gc1200gc" src="../imgs/resource/1-11.jpg"></a> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-21.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-31.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-41.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-51.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-61.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-71.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-81.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-91.jpg">
							</div>
						</div>
					</div>
					<div class="back_usi_tit">
						<img class="p_batx" id="front_usi_img_1" src="../imgs/resource/1-21.jpg">
						<div class="back_tit_conet">
							<div class="front_usi_nam_1"></div>
							<div class="front_usi_lem">元数据管理专员</div>
						</div>
					</div>
					<div class="back_usi_desc_1">汇总it项目组元数据申请，汇总业务需求申请</div>
				</div>
			</div>

			<div class="sponsor" title="Click to flip">
				<div id="ysj-m" class="sponsorFlip" flipped="no">
					<div class="wz">元数据管理员</div>
					<div class="wz-2">审核元数据申请、解决元数据存在问题</div>
					<div class="wz-k">
						<span id="totalUsers_2">0</span>人
					</div>
					<div class="wz-k">
						<span id="totalDealTimes_2">0</span>+审批
					</div>
					<div class="yx">
						<div class="font_cet_img">
							<img class="f_batx" id='f_batx_2' data-name="gc1200gc" src="../imgs/resource/1-21.jpg">
						</div>
					</div>
					<div class="wz-r">[优秀管理员]</div>
					<div class="wz-name">
						<span id="fstName_2"></span>
					</div>
					<div class="wz-bg">
						<span id="fstDealTimes_2">0</span>次审批
					</div>
					<div style="clear: both"></div>
				</div>

				<div id="ysj-m" class="sponsorData">
					<div class="back_uss">
						<div class="back_uss_tit">元数据管理员</div>
						<div class="back_uss_avt">
							<div class="avator2" data-uid="866894234" data-uname="gc1200gc">
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-11.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-21.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-31.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-41.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-51.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-61.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-71.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-81.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-91.jpg">
							</div>
						</div>
					</div>
					<div class="back_usi_tit">
						<img class="p_batx" id="front_usi_img_2" src="../imgs/resource/1-51.jpg">
						<div class="back_tit_conet">
							<div class="front_usi_nam_2"></div>
							<div class="front_usi_lem">元数据管理员</div>
						</div>
					</div>
					<div class="back_usi_desc_2">管理元数据申请，审批投产申请0次</div>
				</div>
			</div>

			<div class="sponsor" title="Click to flip">
				<div id="ysj-g" class="sponsorFlip" flipped="no">
					<div class="wz">关联方</div>
					<div class="wz-2">配合元数据变更</div>
					<div class="wz-k">
						<span id="totalUsers_3">0</span>人
					</div>
					<div class="wz-k">
						<span id="totalDealTimes_3">0</span>+变更
					</div>
					<div class="yx">
						<div class="font_cet_img">
							<img class="f_batx" id='f_batx_3' data-name="gc1200gc" src="../imgs/resource/1-21.jpg">
						</div>
					</div>
					<div class="wz-r">[优秀用户]</div>
					<div class="wz-name">
						<span id="fstName_3"></span>
					</div>
					<div class="wz-bg">
						<span id="fstDealTimes_3">0</span>次变更配合
					</div>
					<div style="clear: both"></div>
				</div>
				<div id="ysj-g" class="sponsorData">
					<div class="back_uss">
						<div class="back_uss_tit">关联方</div>
						<div class="back_uss_avt">
							<div class="avator3" data-uid="866894234" data-uname="gc1200gc">
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-11.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-21.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-31.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-41.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-51.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-61.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-71.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-81.jpg"> 
								<img class="batx" data-name="gc1200gc" src="../imgs/resource/1-91.jpg">
							</div>
						</div>
					</div>
					<div class="back_usi_tit">
						<img class="p_batx" id="front_usi_img_3" src="../imgs/resource/1-81.jpg">
						<div class="back_tit_conet">
							<div class="front_usi_nam_3"></div>
							<div class="front_usi_lem">关联项目人员</div>
						</div>
					</div>
					<div class="back_usi_desc_3">配合元数据汇总，配合变更0次</div>
				</div>
			</div>
		</div>
		<div id="home_user_apDiv25">
			<div id="home_user_apDiv313" style="float: left; padding-top: 5px;"></div>
			<div id="home_user_apDiv213" class="y" style="padding-left: 15px; float: left;">最新流程动态</div>
			<div id="more3" style="padding-top: 11px;">
				<a style="cursor: pointer;" onclick="openDecInfo()"><img src="../imgs/gf_more.png" /></a>
			</div>
			<div style="margin-top: 30px; background: url(<%=request.getContextPath()%>/page/mp/imgs/version_home/line-y.png) repeat-y 33px 0">
				<div id="container_metauser">
					<div style="height: 600000px;">
						<div id="wait_to_do_user" style="height: 355px; padding-top: 100px;">
							<div class='bg1' style="margin-top: 0px;">
								<strong>20141</strong>
							</div>
							<div class='txt1'>&nbsp;&nbsp;&nbsp;&nbsp;无</div>
						</div>
						<div id="wait_to_do_2_user" style="height: 400px; float: left; padding-top: 100px;"></div>
					</div>
				</div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	initDynamicFlow();
</script>
</html>