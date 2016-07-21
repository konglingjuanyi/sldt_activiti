var getCurVerSys = function(_item){
	var str = "";
	str = str + " <div id=\"change_04\">";
	str = str + "<div class=\"wb\">";
	str = str + "<ul class=\"list1\">";
	str = str + " <li class=\"cl1\">元数据代码：</li> ";
	str = str + " <li class=\"cl2\">"+_item.DB_CODE+"</li> ";
	str = str + "  <li class=\"cl3\">元数据名称：</li> ";
	str = str + " <li  class=\"cl4\">"+_item.DBCHNAME+"</li>";
	str = str + " <li  class=\"cl3\">生效时间：</li> ";
	str = str + "  <li  class=\"cl4\">"+_item.curVerOptId+"</li> ";
	str = str + "  <li  class=\"cl1\">所属开发科室：</li>  ";
	str = str + " <li  class=\"cl2\">"+_item.DEPT+"</li>";
	str = str + " <li  class=\"cl3\">DBA：</li>";
	str = str + " <li  class=\"cl4\">"+_item.DBA+"</li>";
	str = str + "  <li  class=\"cl1\">开发商：</li>  ";
	str = str + " <li  class=\"cl2\">"+_item.PRO_M_FAC+"</li>";
	str = str + " <li  class=\"cl3\">开发负责人：</li>";
	str = str + " <li  class=\"cl4\">"+_item.DEVLOPER+"</li>";
	str = str + " <li class=\"cl3\">元数据类型：</li>  ";
	str = str + " <li  class=\"cl4\">系统</li> ";
	str = str + " <li  class=\"cl1\" style='height:40px;'>功能简介：</li>  ";
	str = str + "  <li  class=\"tiledcl5\"style='height:40px;'>"+_item.REMARK+"</li>";
	
	str = str + " <li class=\"cl1\" style='font-size:15px;color:red;'>版本概述：</li>  ";
	str = str + " <li class=\"tiledcl5\" ></li> ";
	
	str = str + " <li class=\"cl1\">模式信息：</li>  ";
	str = str + " <li class=\"cl2\" id=\"schemaCount\">/</li> ";
	str = str + " <li class=\"cl3\">模块信息：</li>  ";
	str = str + " <li  class=\"cl4\" id=\"moduleCount\"></li> ";
	
	str = str + " <li class=\"cl1\">表级信息：</li>  ";
	str = str + " <li class=\"cl2\" id=\"tableCount\"></li> ";
	str = str + " <li class=\"cl3\">字段信息：</li>  ";
	str = str + " <li  class=\"cl4\" id=\"colCount\"></li> ";
	
	str = str + " <li class=\"cl1\">版本说明：</li>  ";
	str = str + " <li class=\"tiledcl5\" id=\"verDesc\">"+_item.VER_DESC+"</li> ";
	
	str = str + "  </ul>  ";
	str = str + " </div> ";
	str = str + "  </div>  ";

    
	return str;
};

$.ajax({
	url:context+'/' + 'metadataVer.do?method=verMetaItem&dbId='+dbId+'&optId='+optId+"&verType="+verType,
	method: 'POST',
	async:false,
	dataType :'json',
	success: function(response) {
		var verMetaItem = response.data.list;
		var isEdit = response.data.isEdit;
		if(verMetaItem.length>0){
			var str = "";
			var edit= "";
			for(var i = 0;i<1;i++){
				var item = verMetaItem[i];
				str = str +  getCurVerSys(item);
				//debugger;
				$("#change_02").html(item.DBCHNAME+"_<span style='font-size:18px;'>"+item.VER_NAME+"</span>");
				var down="<div id='downExcel' style='float: right;padding-right: 20px;'><a style=\"cursor: pointer;\" onclick=\"expSysMeta('"+dbId+"','"+item.curVerOptId+"')\"><img title=\"下载当前系统数据字典信息\" onmouseover=\"this.src='../imgs/excel_1.png';\" onmouseout=\"this.src='../imgs/excel.png';\" style='vertical-align:bottom;' src=\"../imgs/excel.png\" /></a></div>";
				var comp="<div id='compMeta' style='float: right;padding-right: 0px;'><a style=\"cursor: pointer;\" onclick=\"clickCmpVersion('"+dbId+"','"+optId+"','"+item.VER_NAME+"','"+item.DBCHNAME+"')\"><img title=\"版本比对\" onmouseover=\"this.src='../imgs/gf_ver_comp-2.png';\" onmouseout=\"this.src='../imgs/gf_ver_comp2.png';\" style='vertical-align:center;' src=\"../imgs/gf_ver_comp2.png\" /></a></div>";
				if(isEdit){
					edit="<div id='editSys' style='float: right;padding-right: 0px;'><a style=\"cursor: pointer;\" onclick=\"editMetadata('"+dbId+"','"+optId+"')\"><img title=\"在线编辑当前系统元数据\" onmouseover=\"this.src='../imgs/edit_1.png';\" onmouseout=\"this.src='../imgs/edit.png';\" style='vertical-align:center;' src=\"../imgs/edit.png\" /></a></div>";
				}
				$("#downExcel").html(down);
				$("#editSys").html(edit);
				$("#compSys").html(comp);
				$("#change_02_desc").html(item.REMARK+down+comp+edit);
				$("#change_04").html(str);
			}
		}
	},fail : function(){
		alert("cards load fail!");
	}
});

/*********************************************通过系统ID、版本ID查询当前版本统计信息***********************************/

$.ajax({
	url:context+'/' + 'metadataVer.do?method=curSysVerCount&dbId='+dbId+'&optId='+optId,
	method: 'POST',
	async:false,
	dataType :'json',
	success: function(response) {
		var verMetaItem = response.data;
		for(var i = 0;i<1;i++){
			var item = verMetaItem[i];
			$("#colCount").html(item.COLCOUNT);
			$("#moduleCount").html(item.MODCOUNT);
			$("#schemaCount").html(item.SCHCOUNT);
			$("#tableCount").html(item.TABCOUNT);
		}
	},fail : function(){
		alert("cards load fail!");
	}
});


function expSysMeta(dbId,curVerOptId){
	layer.load('正在导出，请稍后...');
	$.ajax({
		url:context+'/' + 'metaExc.do?method=expMetadata&dbId='+dbId+'&curVerOptId='+curVerOptId,
		method: 'POST',
		async:false,
		dataType :'json',
		success: function(response) {
		
		},fail : function(){
			alert("cards load fail!");
		}
	});
/*	var iframe = document.getElementById('formIfr').contentWindow;
	iframe.metaExcelForm.method.value='expMetadata';
	iframe.metaExcelForm.target="";
	iframe.metaExcelForm.dbId.value=dbId;
	iframe.metaExcelForm.optId.value=curVerOptId;
	iframe.metaExcelForm.submit();*/
}

function editMetadata(dbId,optId){
	/*var iframe = document.getElementById('formIfr').contentWindow;
	iframe.reqPageForm.method.value='editMetadataPage';
	iframe.reqPageForm.target="_editMeta";
	iframe.reqPageForm.dbId.value=dbId;
	iframe.reqPageForm.submit();*/
	parent.changeMainPage("page/mp/sysnav/editMetaPage.jsp?dbId="+dbId+"&optId="+optId);

}

/**
 * 点击版本对比
 */
function clickCmpVersion(dbId,verId,verName,dbName){
  /* var iframe = document.getElementById('formIfr').contentWindow;
		iframe.reqPageForm.target="_reqVerComp";
	 	iframe.reqPageForm.dbId.value=dbId;
		iframe.reqPageForm.dbName.value=dbName; 
		iframe.reqPageForm.Col_1.value=verId; 
		iframe.reqPageForm.c1_Name.value=verName; 
		iframe.reqPageForm.method.value='reqCompVersionBySys';
		iframe.reqPageForm.submit();*/
	parent.changeMainPage("page/mp/version/sys_version_comparison.jsp?dbId="+dbId+"&verId="+verId+"&verName="+verName+"&dbName="+dbName);

}

$(document).ready(function(){
	onloadDiv();
	$("#popDiv").mouseenter(function(){
	    //鼠标移入
	    $("#sysMgr").show();
	    $("#popDiv").css("height","100px"); 
		$(this).css("opacity","1"); 
	}).mouseleave(function(){
	    //鼠标移出
	    $("#sysMgr").hide(); 
	    $("#popDiv").css("height","25px"); 
		$(this).css("opacity","0.3"); 
	});
});

function onloadDiv(){
	$("#sysMgr").hide(); 
	$("#popDiv").css("height","25px"); 
	$("#popDiv").css("opacity","0.3"); 
}