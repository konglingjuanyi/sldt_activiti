/********************待办列表********************/
function loadTaskList(isScroll){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'taskList'},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var length = data.length;
			var html = "";
			$.each(data,function(i,val){
				var altOperDate_1 = val.altOperDate.substring(8,9);
				var altOperDate_2 = val.altOperDate.substring(9,10);
				var name = val.name.replace("<","&lt;").replace(">","&gt;");
				html += "<div class='bg'>";
				html += "<strong>"+altOperDate_1+"</strong><strong>"+altOperDate_2+"</strong>日";
				html += "</div>";
				html += "<div class='txt'>";
				var decName = val.decName;
			    html += "<a href=\"javascript:;\" style=\"cursor: pointer;\" onclick=\"todo_dec('"+val.assignee+"','"+val.taskId+"','"+val.decId+"','"+name
			    		+"')\"> "+decName+"</a>"+"<strong class='p'>请按时处理</strong>"; 
			    html += "</div>";
			});
			$.ajax({
				url: context+'/flow.do',
				type: 'post',
				data: {method:'initTaskList'},
				dataType: 'json',
				success: function(response){
					data = response.data;
					length = data.length+length;
					$('#home_apDiv28').html("当前有："+length+"笔代办");
					if(length == 0){
						$('#home_apDiv35').css('background-image','url('+context+'/page/mp/imgs/version_home/g-3.png)');
						$('#home_apDiv36').html("今天的工作已经完成了(^_^)");
					}else if(length > 0 && length <= 10){
						$('#home_apDiv35').css('background-image','url('+context+'/page/mp/imgs/version_home/g.png)');
						$('#home_apDiv36').html("今天的工作快要完成了(^_^)");
					}else {
						$('#home_apDiv35').css('background-image','url('+context+'/page/mp/imgs/version_home/g-2.png)');
						$('#home_apDiv36').html("今天的工作还有很多(>_<)");
					}
					$.each(data,function(i,val){
						var altOperDate_1 = val.altOperDate.substring(8,9);
						var altOperDate_2 = val.altOperDate.substring(9,10);
						html += "<div class='bg'>";
						html += "<strong>"+altOperDate_1+"</strong><strong>"+altOperDate_2+"</strong>日";
						html += "</div>";
						html += "<div class='txt'>";
						var assignee = val.assignee;
						var initDecName = val.initDecName;
					    html += "<a href=\"javascript:;\" style=\"cursor: pointer;\" onclick=\"todo_init_dec('"+val.assignee+"','"+val.taskId+"','"+val.initDecId+"','"+val.name
					    		+"')\"> "+initDecName+"</a>"+"<strong class='p'>请按时处理</strong>"; 
					    html += "</div>";
					});
					$("#wait_to_do").html(html);
					//待办列表滚动
					scroll(isScroll);
				}
			})
		}
	});
}


/********************滚动实现********************/
function scroll(isScroll){
	var speed=30;
	var w2=document.getElementById("wait_to_do_2");
	var w1=document.getElementById("wait_to_do");
	var container=document.getElementById("container");
	w2.innerHTML=w1.innerHTML; //克隆w1为w2
	if(isScroll){
		function Marquee1(){
			//当滚动至w1与w2交界时
			if(w2.offsetTop-container.scrollTop<=0){
				container.scrollTop-=w1.offsetHeight; //container跳到最顶端
			}else{
				container.scrollTop++
			}
		}
		var MyMar1=setInterval(Marquee1,speed)//设置定时器
		//鼠标移上时清除定时器达到滚动停止的目的
		container.onmouseover=function() {clearInterval(MyMar1)}
		//鼠标移开时重设定时器
		container.onmouseout=function(){MyMar1=setInterval(Marquee1,speed)}
	}
}

function reqDecListPage(){
	var iframe = document.getElementById('formIfr').contentWindow;
	iframe.reqPageForm.method.value='reqDecListPage';
	iframe.reqPageForm.target="_reqDecListPage";
	iframe.reqPageForm.submit();
}

/********************滚动实现********************/
function scroll_N(){
	var speed=30;
	var w2=document.getElementById("wait_to_do_2_user_n");
	var w1=document.getElementById("wait_to_do_user_n");
	var container=document.getElementById("container_metauser_n");
	w2.innerHTML=w1.innerHTML; //克隆w1为w2
	function Marquee1(){
		//当滚动至w1与w2交界时
		if(w2.offsetTop-container.scrollTop<=0){
			container.scrollTop-=w1.offsetHeight; //container跳到最顶端
		}else{
			container.scrollTop++;
		}
	}
	var MyMar1=setInterval(Marquee1,speed)//设置定时器
	//鼠标移上时清除定时器达到滚动停止的目的
	container.onmouseover=function() {clearInterval(MyMar1)}
	//鼠标移开时重设定时器
	container.onmouseout=function(){MyMar1=setInterval(Marquee1,speed)}
}

function newVerItem(){
	$.ajax({
		url:context+"/metadataVer.do?method=newVerMetaData",
		method: 'POST',
		async:false,
		dataType :'json',
		data:{start:0,limit:10000},
		success: function(response) {
			var data = response.data;
			var html = "";
			for(var i = 0 ; i < data.length ; i++){
				html = html +"<div style='padding-top: 20px;padding-left: 20px;'>";
				html = html +"<div class='bg2' style=\"margin-top: 5px;\">";
				html = html +"<strong>"+data[i].F_STARTDATE.substring(2,4)+"-"+data[i].F_STARTDATE.substring(4,6)+"-"+data[i].F_STARTDATE.substring(6,8)+"</strong>";
				html = html +"</div>";
				html = html +"<div style='padding-left: 50px;'><div class='txt2'><A style=\"cursor: pointer;\" onclick=\"openVersionInfo('" + data[i].DB_CODE + "','"+data[i].curVerOptId+"')\">";
				if(data[i].USERNAME==''||data[i].USERNAME==null){
					html = html +"<span style='color:red;font-weight:bolder;'>系统</span>发布版本["+data[i].DBCHNAME+"_"+data[i].VER_NAME+"]";
				}else{
					html = html +"<span style='color:red;font-weight:bolder;'>"+data[i].USERNAME+"</span>发布版本["+data[i].DBCHNAME+"_"+data[i].VER_NAME+"]";
				}
				html = html +"</a></div></div></div>";
			}
			$("#wait_to_do_user_n").html(html);
			scroll_N();
		},fail : function(){
			alert("load fail!");
		}
	});
}



function openVersionInfo(dbId,optId,verType){
	parent.changeMainPage("page/mp/version/sysVersion.jsp?dbId="+dbId+"&optId="+optId+"&verType="+verType,"nav_4");
}

function openAllSysVerPage(){
	//var iframe = $(window.parent).find("mainArea");
	//window.location.href ="home.jsp";
	//window.open("home.jsp");
	top.changeMainPage("page/mp/version/all_sys_version_view.jsp","nav_4");
}
function openMyDecPage(){
	/*var iframe = document.getElementById('formIfr').contentWindow;
	iframe.reqPageForm.target="_reqMyDec";
	iframe.reqPageForm.method.value='reqFlowInfoPage';
	iframe.reqPageForm.submit();*/
	top.changeMainPage("page/mp/flow/flow.jsp","nav_3");
}


function myVerMetaItem(){
	var getMyVersionCard = function(_item){
		var str = "";
		str = str +"<div class=\"home_my_apDiv6\"></div>";
		str = str +"<div class=\"home_my_apDiv7\"><A style=\"cursor: pointer;\" onclick=\"openVersionInfo('" + _item.dbId + "','"+_item.curVerOptId+"')\">"+_item.dbChName+"</a></div>";
		str = str +"<div class=\"home_my_apDiv8\">"+_item.verName+"</div>";
		str = str +"<div class=\"home_my_apDiv4\">";
		if(_item.lastVerOptId==''||_item.lastVerOptId==null){
			str = str +"	<div class=\"home_my_apDiv9\">0.0.0</div>";
			str = str +"	<div class=\"home_my_apDiv11\">"+_item.userName+"初始化</div>";
		}else{
			str = str +"	<div class=\"home_my_apDiv9\">"+_item.lastVerOptId+"</div>";
			str = str +"	<div class=\"home_my_apDiv11\">"+_item.userName+"发布</div>";
		}
		
		str = str +"	<div class=\"home_my_apDiv10\">"+_item.curVerOptId+"</div>";
		str = str +"	<div class=\"home_my_apDiv12\">"+_item.altOperDate+"</div>";
		str = str +"</div>";
		str = str +"<div class=\"home_my_apDiv14\"></div>";
		str = str +"<div class=\"clear\"></div>";
		
		return str;
	};
	
	$.ajax({
		url:context+'/' + 'metadataVer.do?method=myVerMetaData',
		method: 'POST',
		async:false,
		data:{start:0,limit:3},
		dataType :'json',
		success: function(response) {
			var cards = response.data;
			var str = "";
			for(var i = 0;i<cards.length;i++){
				var item = cards[i];
				str = str +  getMyVersionCard(item);
			}
			//显示
			$("#home_my_apDiv5").html(str);
		},fail : function(){
			alert("cards load fail!");
		}
	});
}
