/********************待办列表********************/
function initDynamicFlow(){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getDynamicFlow'},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var html = "";
			for(var i = 0 ; i < data.length ; i++){
				html = html +"<div style='padding-top: 20px;padding-left: 0px;'>";
				html = html +"<div class='bg1' style=\"margin-top:7px;\">";
				html = html +"<strong>"+data[i].auditTime.substring(2,10)+"</strong>";
				html = html +"</div>";
				html = html +"<div style='padding-left: 50px;'><div class='txt1'>";
				html = html +""+data[i].roleName+"<span style='color:red;font-weight:bolder;'>"+data[i].userName+"</span>审批任务["+data[i].auditOpinion+"]";
				html = html +"</div></div></div>";
			}
			$("#wait_to_do_user").html(html);
			//待办列表滚动
			scrollUser();
		}
	});
}

/********************滚动实现********************/
function scrollUser(){
	var speed=30;
	var w2=document.getElementById("wait_to_do_2_user");
	var w1=document.getElementById("wait_to_do_user");
	var container=document.getElementById("container_metauser");
	w2.innerHTML=w1.innerHTML; //克隆w1为w2
	function Marquee1(){
		//当滚动至w1与w2交界时
		if(w2.offsetTop-container.scrollTop<=0){
			container.scrollTop-=w1.offsetHeight; //container跳到最顶端
		}else{
			container.scrollTop++;
		}
	}
	var MyMar1=setInterval(Marquee1,speed);//设置定时器
	//鼠标移上时清除定时器达到滚动停止的目的
	container.onmouseover=function() {clearInterval(MyMar1)}
	//鼠标移开时重设定时器
	container.onmouseout=function(){MyMar1=setInterval(Marquee1,speed)}
}


$(document).ready(function(){
	//根据角色id 查询元数据用户信息
	
	$.ajax({ 
		type: "post", 
		url: context+"/metadata.do?method=getMetaUser&metaPeMaId=r_008&metaMaId=r_004&metaRelaId=r_009", 
		dataType:"json", 
        success: function(obj){
        	//获取的用户信息
        	var peMaUsers =  obj.peMaUsers,
        		maUsers = obj.maUsers,
        	    relaUsers = obj.relaUsers,
        	    
        	    peMaStaUsInfo =  obj.peMaStaUsInfo,
        	    maStaUsInfo = obj.maStaUsInfo,
        	    relaStaUsInfo = obj.relaStaUsInfo;
        	
        	$('#totalUsers_1').html(peMaStaUsInfo.totalUsers);
        	$('#totalUsers_2').html(maStaUsInfo.totalUsers);
        	$('#totalUsers_3').html(relaStaUsInfo.totalUsers);
        	
        	$('#totalDealTimes_1').html(peMaStaUsInfo.totalDealTimes);
        	$('#totalDealTimes_2').html(maStaUsInfo.totalDealTimes);
        	$('#totalDealTimes_3').html(relaStaUsInfo.totalDealTimes);
        	
        	$('#fstName_1').html(peMaStaUsInfo.fstName);
        	$('#fstName_2').html(maStaUsInfo.fstName);
        	$('#fstName_3').html(relaStaUsInfo.fstName);
        	
        	$('#fstDealTimes_1').html(peMaStaUsInfo.fstDealTimes);
        	$('#fstDealTimes_2').html(maStaUsInfo.fstDealTimes);
        	$('#fstDealTimes_3').html(relaStaUsInfo.fstDealTimes);
        	
        	$('#f_batx_1').attr('src','../imgs/resource/'+peMaStaUsInfo.fsImgUrl);
        	$('#f_batx_2').attr('src','../imgs/resource/'+maStaUsInfo.fsImgUrl);
        	$('#f_batx_3').attr('src','../imgs/resource/'+relaStaUsInfo.fsImgUrl);
        	
        	
        	
        	var imgStr1 = '',imgStr2='',imgStr3='';
        	for(var i=0;i<(peMaUsers.length>9?9:peMaUsers.length);i++){
        		var imgsrc = '../imgs/resource/'+peMaUsers[i].imgUrl;
        		imgStr1 += '<a href="javascript:getMetaUserInfo(\''+peMaUsers[i].userId+'\',\''+peMaUsers[i].userName+'\',\''+imgsrc+'\',\''+peMaUsers[i].dealTimes+'\',1)" title="'+peMaUsers[i].userName+'"><img class="batx"  src="'+imgsrc+'"></a>';
        	    if(i==0) getMetaUserInfo(peMaUsers[i].userId,peMaUsers[i].userName,imgsrc,peMaUsers[i].dealTimes,1);
        	}
        	for(var i=0;i<(maUsers.length>9?9:maUsers.length);i++){
        		var imgsrc = '../imgs/resource/'+maUsers[i].imgUrl;
        		imgStr2 += '<a href="javascript:getMetaUserInfo(\''+maUsers[i].userId+'\',\''+maUsers[i].userName+'\',\''+imgsrc+'\',\''+maUsers[i].dealTimes+'\',2)" title="'+maUsers[i].userName+'"><img class="batx"  src="'+imgsrc+'"></a>';
        		if(i==0) getMetaUserInfo(maUsers[i].userId,maUsers[i].userName,imgsrc,maUsers[i].dealTimes,2);
        	}
        	for(var i=0;i<(relaUsers.length>9?9:relaUsers.length);i++){
        		var imgsrc = '../imgs/resource/'+relaUsers[i].imgUrl;
        		imgStr3 += '<a href="javascript:getMetaUserInfo(\''+relaUsers[i].userId+'\',\''+relaUsers[i].userName+'\',\''+imgsrc+'\',\''+relaUsers[i].dealTimes+'\',3)" title="'+relaUsers[i].userName+'"><img class="batx"  src="'+imgsrc+'"></a>';
        		if(i==0) getMetaUserInfo(relaUsers[i].userId,relaUsers[i].userName,imgsrc,relaUsers[i].dealTimes,3);
        	}
        	
        	// $('.avator').append(imgStr1);
        	$('.avator1').html(imgStr1);
        	$('.avator2').html(imgStr2);
        	$('.avator3').html(imgStr3);
        	 
        } 
	});

	$(".sponsor").on("mouseenter",function(e){
		filpOn($(this));
	});

});

function filpOn(option){
	option.unbind("mouseleave");
	option.unbind("mouseenter");
	var elem = option.find('.sponsorFlip');
    var c = setTimeout(function(){
    	if(elem.attr('flipped')=='no'){
	    	//console.log(1111111111);
			elem.flip({
				direction:'lr',
				speed: 100,
				onBefore: function(){
					elem.html(elem.siblings('.sponsorData').html());
					elem.attr('flipped','yes');
				}
			});
    	}
    },400); 
    option.on("mouseleave",function(){
    	clearTimeout(c);
    	filpOut(option);
    });
}

function filpOut(option){
	option.unbind("mouseleave");
	option.unbind("mouseenter");
	var elem = option.find('.sponsorFlip');
	var c = setTimeout(function(){
		if(elem.attr('flipped')=='yes'){
			//console.log(222222222);
			elem.revertFlip();
			elem.attr('flipped','no');
		}
	},400);
	option.on("mouseenter",function(){
		clearTimeout(c);
		filpOn(option);
	});
}

function getMetaUserInfo(userId,userName,imgsrc,dealTimes,type){
	if(type==1 || type=='1'){
		$(".front_usi_nam_1").html("用户"+userName);
		$(".back_usi_desc_1").html("用户"+userName+"，共发起了 <font color='red'>"+dealTimes+"</font>次变更申请");
		$("#front_usi_img_1").attr("src",imgsrc);
	}else if(type==2 || type=='2'){
		$(".front_usi_nam_2").html("用户"+userName);
		$(".back_usi_desc_2").html("用户"+userName+"，共进行了 <font color='red'>"+dealTimes+"</font>次审批流程");
		$("#front_usi_img_2").attr("src",imgsrc);
	}else if(type==3 || type=='3'){
		$(".front_usi_nam_3").html("用户"+userName);
		$(".back_usi_desc_3").html("用户"+userName+"，共配合做了 <font color='red'>"+dealTimes+"</font>次变更");
		$("#front_usi_img_3").attr("src",imgsrc);
	}
	imgsrc;
	
}

function openDecInfo(){
	top.changeMainPage("page/mp/flow/declareList.jsp", "nav_5");
}