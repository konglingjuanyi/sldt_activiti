/**
 * 生成分割线
 * @param No 序号
 * @param name 文字
 * @returns {String}
 */
function getTitle(No,name){
	return '<div class="version_type">'+
	'<div class="xd">'+
	'<div class="xd-1">'+No+'</div>'+
	'</div>'+
	'<div class="xd-name">'+name+'</div>'+
	'<div class="apDiv-line"></div> '+
	'</div>';
}

/**
 * 获取当前用户系统
 */
function getMySysAllVer(){
	$.ajax({
		url:context+"/metadata.do?method=getMySysAllVer",
		method: 'POST',
		async:false,
		dataType :'json',
		success: function(response) {
			//debugger;
			var _dateques = response.data;
			var count = _dateques.length;
			if(count==0){
				return ;
			};
			var mysys = '';
			for(var i=0;i<count;i++){
//				instance_id
				mysys += getSysAllVersion(_dateques[i],i);
			};
			//debugger;
			$("#my_system_version").html(mysys);
		},fail : function(){
			alert("load fail!");
		}
	});
}
/**
 * 获取所有系统
 */
function getAllMetadataDatabase(){
	$.ajax({
		url:context+"/metadata.do?method=getAllMetadataDatabase",
		method: 'POST',
		//async:false,
		dataType :'json',
		success: function(response) {
			var _dateques = response.data;
			var count = _dateques.length;
			if(count==0){
				return ;
			}
			var moreSys = '';
			if(count>9){
				moreSys = '<a style=\"cursor: pointer;\" onclick=\"showHideSys()" class="box"><img src=\"'+context+'/page/mp/imgs/gf_vcomp_zhankai.png\"/><font style="font-size:13px;">&nbsp;展开更多</font></a>';
			}
			var tr2 = '';
		    var tr = '<tr height="20">';
			 for (var i=0;i<count;i++){
				 if(i<9){//默认显示9个系统
					 tr += "<td width=\"33%\"><a style=\"cursor: pointer;\" onclick=\"checkAllVersion('"+_dateques[i].dbChName+"','"+_dateques[i].dbId+"','"+_dateques[i].curVerOptId+"')\">"+_dateques[i].dbChName+'</a></td>';
				 if(i>0&&(i+1)%3==0){//每三个系统作为一行
					 tr += "</tr><tr>";
				 }
				}else{
					if(i==9){
						tr2 += '<tr height="20">';
					}
					tr2 += "<td  width=\"33%\"><a style=\"cursor: pointer;\" onclick=\"checkAllVersion('"+_dateques[i].dbChName+"','"+_dateques[i].dbId+"','"+_dateques[i].curVerOptId+"')\">"+_dateques[i].dbChName+"</a></td>";
				 	if(i>0&&(i+1)%3==0){
						 tr2 += "</tr><tr>";
				 	}
				 	if(i==(count-1)){
				 		tr2 += "</tr>";
				 	}
				}
			} 
			tr += '</tr>';
			tr +='<tr>'+
			      '<td colspan="3" align="right">'+moreSys+'</td>'+
				  '</tr>';
		$("#all_sys_tab1").html(tr);
		$("#all_sys_tab2").html(tr2);
		},fail : function(){
			alert("load fail!");
		}
	});
}
/**
 * 连接到版本查看界面
 */
function checkAllVersion(dbName,dbId,optId){
	parent.changeMainPage("page/mp/version/one_sys_version_view.jsp?dbId="+dbId+"&dbName="+dbName+"&optId="+optId);

}

/**
 * 返回系统版本信息
 * @param obj
 * @returns {String}
 */
function getSysAllVersion(obj,k){
//	debugger;
	var datas = obj.data;
	var data = getEveryVer(datas,k,datas.sys.dbId,obj) ;
	var repl = "";
	var curr="";
	var sna = "";
	if(datas.curlist.length!=0){
		curr = datas.curlist[0].ALT_VER_DATE_NO;
	}
	if(datas.proVer.length!=0){
		sna = datas.proVer[0].VER_CREATE_TIME;
	}
	if(obj.sysBelongCurUser==true){//该系统属于当前用户管辖才能申请
		repl = "<img src=\""+context+"/page/mp/imgs/gf_ver_rep.png\" onclick=\"showDecSysWin('"+obj.instance_id+"','"+datas.sys.dbId+"','"+datas.sys.sysName+"','"+curr+"','"+sna+"')\" name=\""+datas.sys.dbId+","+datas.sys.sysName+","+datas.currVerDate+","+datas.devVer+"\" class=\"cmpVersions\" style=\"cursor:pointer\" title=\"申请以生产快照覆盖当前版本\"/>";
	}
	//debugger;
    var ver =  '<div class="version_view">'+
			'<div style="overflow: hidden;">'+
			'<div class="version_view_sysName">　'+datas.sys.sysName+
				
			'</div>'+
				"<div class=\"version_view_compare\">" +
					"<img src=\""+context+"/page/mp/imgs/gf_ver_comp.png\" name=\""+datas.sys.dbId+","+datas.sys.sysName+","+datas.devVerId+","+datas.proVerId+","+datas.uatVerId+"\" devName=\""+datas.devName+"\" proName=\""+datas.proName+"\" uatName=\""+datas.uatName+"\" class=\"cmpVersion\" style=\"cursor:pointer\" title=\"版本比对\"/>" +
				"</div>"+
			    "<div class=\"version_view_compare\">" +
			    	repl+
				"</div>"+
			'</div>'+
			
			'<div style="margin-left: 8px">'+
				'<table width="100%" class="version_view_table" align="left">'+
					//<!-- 版本基本信息 -->
				data[1]+
				'</table>'+
			'</div>'+
				//<!-- 更多版本信息 -->
			//alert(data[0]);
			 '<div class=\"more_ver_'+k+'\" id="more_ver" style="border: 0px solid red;">'+
				 '<table width="100%" id="more_ver_01">'+
				  data[0]+
				'</table>'+
			'</div>'+
			
		'</div>';
	return ver;
}

function getEveryVer(_dateques,k,dbcode,obj){
	//debugger;
	var more_ver_1 = "";
	if(_dateques.pastList.length>2){
		 more_ver_1 = '<tr height="20">';
	}
	var pastV = '';
	if(_dateques.pastList.length>0){
		for(var i=0;i<_dateques.pastList.length;i++){
			var db_Id = _dateques.pastList[i].ALT_SYS_CODE;
			var ver_Id = _dateques.pastList[i].ALT_VER_DATE_NO;
			if(i<2){//默认显示两个
				pastV += "<a style=\"cursor: pointer;\" onclick=\"reqSysVersion('"+db_Id+"','"+ver_Id+"','','history')\">"+_dateques.pastList[i].VER_NAME+"【"+ver_Id+"】</a>&nbsp;&nbsp;";
			}else{
				more_ver_1 +="<td><a style=\"cursor: pointer;\" onclick=\"reqSysVersion('"+db_Id+"','"+ver_Id+"','','history')\">"+_dateques.pastList[i].VER_NAME+"【"+ver_Id+"】</a></td>";
				if((i-1)%3==0){
					more_ver_1 += "</tr><tr>";
				}
			}
		}
	}
	if(_dateques.pastList.length>2){
		more_ver_1 += "</tr>";
	}
	//当前开发版本
	var devVer = "";
	//远期版本
	var farVer = "";
	if(_dateques.devVerList.length>0){
		//var size = _dateques.farVer.length;
		var data = _dateques.devVerList[0];
		devVer = "<a style=\"cursor: pointer;\" onclick=\"reqSysVersion('"+dbcode+"','"+data.ALT_VER_DATE_NO+"')\" >"+data.VER_NAME+"【"+data.ALT_VER_DATE_NO+"】</a>";
	}
	if(_dateques.farVerList.length>0){
		var size = _dateques.farVerList.length;
		for (var i = 0; i < size; i++) {
			var data = _dateques.farVerList[i];
			farVer += "<a style=\"cursor: pointer;\" onclick=\"reqSysVersion('"+data.ALT_SYS_CODE+"','"+data.ALT_VER_DATE_NO+"','"+data.dbName+"','"+data.isSubmit+"')\" >"+data.VER_NAME+"【"+data.ALT_VER_DATE_NO+"】</a>&nbsp;&nbsp;";
		}
	}
	//debugger;
	var morePast = "";
	var currId = "";
	var currName = "";
	///var dbcode = "";
	var showcurInfo = "";
	
	var preTime = "";
	var preName = "";
	var predbcode = "";
	var showpreInfo = "";
	var uatName = "";
	var uatTime = "";
	var showUatInfo = "";
	var uatdbcode = "";
	
	if(_dateques.curlist.length!=0){
		currId = _dateques.curlist[0].ALT_VER_DATE_NO;
		currName = _dateques.curlist[0].VER_NAME;
		showcurInfo = currName+"【"+currId+"】";
	}
	if(_dateques.proVer.length!=0){
		//debugger;
		preTime = _dateques.proVer[0].VER_CREATE_TIME;
		preName = _dateques.proVer[0].VER_NAME;
		predbcode = _dateques.proVer[0].DB_CODE;
		showpreInfo = preName+"【"+preTime+"】";
	}
	if(_dateques.uatList.length!=0){
		//debugger;
		uatTime = _dateques.uatList[0].VER_CREATE_TIME;
		uatName = _dateques.uatList[0].VER_NAME;
		uatdbcode = _dateques.uatList[0].DB_CODE;
		showUatInfo = uatName+"【"+uatTime+"】";
	}
	
	
	//当前版本信息和生产版本信息没有时候的提示信息
	var tipInfo = "";
	if(_dateques.proVer.length==0){//无生产版本
		tipInfo += "<br/>无生产版本信息，请联系管理员抽取生产版本快照.";
	}
	if(_dateques.devVerList.length==0){//无当前版本
		if(tipInfo == ""){
			tipInfo += "<br/>无任何版本，请点击<a onclick=\"showDecSysWin('"+obj.instance_id+"','"+_dateques.sys.dbId+"','"+_dateques.sys.sysName+"','"+currId+"','"+preTime+"')\" style=\"cursor:pointer\" >这里</a>申请以生产快照建立初始化版本.";
		}
	}

	
	//debugger;
	if(more_ver_1!=""){
		morePast = '<td height="29" colspan="2" align="right"><a href="javascript:void(0)" class="moreVer" id='+k+'><img src=\"'+context+'/page/mp/imgs/gf_vcomp_zhankai.png\"/><font style="font-size:13px;">&nbsp;更多历史版本</font></a></td>';
	}
	if(tipInfo!=''){
		tipInfo = '<td height="29" colspan="2" align="left" class="tip_font">'+tipInfo+'</td>';
	}
	var tr = 
		'<tr >'+
	    '<td class="version_view_table_font">当前开发版本：</td>'+
/*	    "<td>&nbsp;<a style=\"cursor: pointer;\" onclick=\"reqSysVersion('"+dbcode+"','"+currId+"')\" >"+showcurInfo+"</a>"+'</td>'+
*/	    '<td>&nbsp;'+devVer+'</td>'+
	    '</tr>'+
		'<tr >'+
	    '<td class="version_view_table_font">生产快照：</td>'+
	    "<td>&nbsp;<a style=\"cursor: pointer;\" onclick=\"reqSysVersion('"+predbcode+"','"+predbcode+"_PRO','')\">"+showpreInfo+"</a>"+'</td>'+ //<a style=\"cursor: pointer;\"  >onclick=\"reqSysVersion('"+predbcode+"','"+predbcode+"')\"
	    '</tr>'+
	    '<tr>'+
	    '<td class="version_view_table_font">UAT快照：</td>'+
/*	    '<td>&nbsp;'+showUatInfo+'</td>'+
*/	    "<td>&nbsp;<a style=\"cursor: pointer;\" onclick=\"reqSysVersion('"+dbcode+"','"+dbcode+"_UAT','')\">"+showUatInfo+"</a>"+'</td>'+ //<a style=\"cursor: pointer;\"  >onclick=\"reqSysVersion('"+predbcode+"','"+predbcode+"')\"
	    '</tr>'+
	    '<tr>'+
	   '<td class="version_view_table_font">远期版本：</td>'+
	    '<td>&nbsp;'+farVer+'</td>'+
	    '</tr>'+
	    '<tr>'+
	    '<td class="version_view_table_font">历史版本：</td>'+
	    '<td>&nbsp;'+pastV+'</td>'+
	    '</tr>'+
	    '<tr>'
	    		+tipInfo+
	    '</tr>'+
	    '<tr>'
	    		+morePast+
	    ' </tr>';
	var json = [];
	json.push(more_ver_1);
	json.push(tr);
	return json;
}

$(function(){
	sweetTitles.setTipElements('.version_view_compare img');
	sweetTitles.init();
	
});

function showDecSysWin(instanceId,dbId,dbName,curOptId,snaOptId){
	if(snaOptId==''||snaOptId==null){
		layer.alert("因无生产快照，请联系管理员抽取【"+dbName+"】生产快照！", 1);
		return;
	}
	var now = new Date();
	var now_ = now.toLocaleDateString();
	var flag = false;
	if(curOptId==''||curOptId==null){
		flag = true;
	}
	var html = "";
	html = html+"<div class=\"showDivAltDetail\" style='width:365px;height:340px;'>";
	html = html+"<div class=\"okAltTile\">";
	if(!flag){
		html = html+"<b>申请以生产快照覆盖当前版本</b>";
	}else{
		html = html+"<b>申请【"+dbName+"】系统</b>";
	}
	html = html+"</div>";
	html = html+"<div>";
	html = html+"<hr color=\"#ed1a2e\">";
	html = html+"</div>";
	html = html+"<form id=\"uploadimg-form\"  action=\"\" method=\"post\">  ";
	html = html+"<div style='text-align: left;padding-top:10px;'>";
	html = html+"<div style='float:left;font-size:13	px;font-family:\"宋体\";'>";
	html = html+"系统名称：";
	html = html+"</div>";
	html = html+"<div style='float:left;'>";
	html = html+""+dbName;
	html = html+"</div>";
	html = html+"</div>";
	if(curOptId==''||curOptId==null){
		curOptId = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	}
	html = html+"<div style='text-align: left;padding-top:25px;'>";
	html = html+"<div style='float:left;font-size:13px;font-family:\"宋体\";'>";
	html = html+"生产版本：";
	html = html+"</div>";
	html = html+"<div style='float:left;'>";
	html = html+""+snaOptId;
	html = html+"</div>";
	html = html+"<div style='padding-left:30px;float:left;font-size:13px;font-family:\"宋体\";'>";
	html = html+"当前版本：";
	html = html+"</div>";
	html = html+"<div style=''>";
	html = html+""+curOptId;
	html = html+"</div>";
	html = html+"</div>";
	html = html+"</form>";
	
	html = html+"<div style='padding-top:10px;'>";
	html = html+="<div style='float:left;'>申请名称：</div>";
	html = html+="<div><input type='text' id='initDecName' style='width:298px;' value='"+dbName+"初始化"+now_+"'/></div>";
	html = html+"</div>";
	
	html = html+"<div style='padding-top:10px;'>";
	html = html+="<div style='float:left;'>提交给：</div>";
	html = html+="<div><select id='assignee' style='width:300px;margin-left:12px'></select></div>";
	html = html+"</div>";
	
	html = html+"<div style='padding-top:10px;height:100px;'>";
	html = html+="<div>申请事由：</div>";
	html = html+="<div><textarea id='decDesc' style='width:360px;height:100px;'></textarea></div>";
	html = html+"</div>";
	
	html = html+"<div class='showDivBut' style='padding-top:30px;'>";
	html = html+"<div style='padding-left:90px;float:left;'>";
	html = html+"<button class='but' id='decClose' onclick=\"decSys('"+instanceId+"','"+dbId+"')\">&nbsp;申 请 </button>";
	html = html+"</div>";
	html = html+"<div style='padding-left:20px;float:left;'>";
	html = html+"<button id=\"pagebtn\" class='but' onclick=\"\">&nbsp;取 消  </button>";
	html = html+"</div>";
	html = html+"</div>";

	html = html+"</div>";
	
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getAssignee'},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var options = "";
			$.each(data,function(i,val){
				options += "<option value="+val.USERID+">"+val.USERNAME+"</option>";
			});
			$('#assignee').html(options);
		}
	});
	
	var pageii = $.layer({
		type: 1,
		title: false,//'查看并保存变更元数据',
		area: ['auto', 'auto'],
		border: [0], //去掉默认边框
		shade: [0], //去掉遮罩
		closeBtn: [0, false], //去掉默认关闭按钮
		page: {
		    html: html
		}
	});
	//自设关闭
	$('#pagebtn').on('click', function(){
		layer.close(pageii);
	});
}

/**
 * 请求系统版本信息
 */
function reqSysVersion(dbId,dbName,optId){
	parent.changeMainPage("page/mp/version/sysVersion.jsp?optId="+optId+"&dbId="+dbId+"&dbName="+dbName);

}

/**
 * 点击版本对比
 */
function clickCmpVersion(){
	$(".cmpVersion").click(function() {
	/*	 var iframe = document.getElementById('formIfr').contentWindow;
			iframe.reqPageForm.target="_reqVerComp";*/
			var strs= new Array(); //定义一数组 
			var sys = $(this).attr('name');
			var devName = $(this).attr('devName');
			var proName = $(this).attr('proName');
			var uatName = $(this).attr('uatName');
			//debugger;
		    strs = sys.split(",");
		    if(strs[2]==''){
		    	layer.alert('无版本可以比对');
		    	return;
		    }
			parent.changeMainPage("page/mp/version/sys_version_comparison.jsp");
		 	/*iframe.reqPageForm.dbId.value=strs[0];
			iframe.reqPageForm.dbName.value=strs[1]; 
			iframe.reqPageForm.Col_1.value=strs[2]; 
			iframe.reqPageForm.Col_2.value=strs[3]; 
			iframe.reqPageForm.Col_3.value=strs[4]; 
			iframe.reqPageForm.c1_Name.value=devName; 
			iframe.reqPageForm.c2_Name.value=proName; 
			iframe.reqPageForm.c3_Name.value=uatName; 
			iframe.reqPageForm.method.value='reqCompVersionBySys';
			iframe.reqPageForm.submit();*/
	});
}
function decSys(instanceId,dbId){
	var initDecName = $("#initDecName").val();
	var assignee = $.trim($("#assignee").find("option:selected").val());
	if(initDecName==''||initDecName==null){
		layer.alert('请输入申请名称...',3);
		return;
	}
	if(assignee==''||assignee==null){
		layer.alert('提交对象不能为空！',3);
		return;
	}
	//var decDesc = $("#decDesc").html();谷歌浏览器无法获取值
	var decDesc = $("#decDesc").val();
	if(decDesc==''||decDesc==null){
		layer.alert('请输入申报事由...',3);
		return;
	}
	$("#decClose").attr("disabled",true);
	layer.load('正在提交，请稍后...');
	$.ajax({
		url:context+"/flow.do?method=startInitFlow",
		method: 'POST',
		data:{instanceId:instanceId,dbId:dbId,decDesc:decDesc,initDecName:initDecName,assignee:assignee},
		//async:false,
		dataType :'json',
		success: function(response) {
			if(response.data.success=='true')
				layer.alert(response.data.msg, 1);
			else
				layer.msg(response.data.msg, 3);
			setTimeout(closeAll,1000);
		},fail : function(){
			alert("load fail!");
		}
	});
	
	
}

function closeAll(){
	layer.closeAll();
}
