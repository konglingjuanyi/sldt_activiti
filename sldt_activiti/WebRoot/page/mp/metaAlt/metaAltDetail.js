$(document).ready(function(){
	//隔行变色
changeTableBackground('metaAltDetailItem');
});

/**
 * 鼠标放上去表格行的颜色改变,传入div id
 * @param divid
 */
function mouseTabRowsColor(divId){
	$("#"+divId+" tr").mouseover(function(){  //."+divClass+" table tr:gt(0)
		//如果鼠标移到class为stripe的表格的tr上时，执行函数  
		$(this).addClass("over");}).mouseout(function(){  
			//给这行添加class值为over，并且当鼠标一出该行时执行函数  
			$(this).removeClass("over");
		}); //移除该行的class 
}

/*****************获取系统版本情况及查询条件窗口数据***********************/
	$.ajax({
		url:context+'/' + 'metadataAlt.do?method=altMetaDeFindWhere',
		method: 'POST',
		async:false,
		dataType :'json',
		data:{dbId:dbId,dbName:dbName,optId:optId},
		success: function(response) {
			var objdata = response.data;
			for(var i = 0;i<objdata.length;i++){
				var objitem = objdata[i];
				$("#sysName").html("<b>"+dbName+"</b>");
				if(optId==''||optId==null){
					$('#targetVerDate').html('ALL');
				}else{
					$('#targetVerDate').html(optId);
				}
				$('#jqVerDate').html(objitem.curVerOptId);
				$('#deveVerDate').html(objitem.developAltHtml);
				$('#forVerDate').html(objitem.forwardAlt);
				
				for(var i = 0;i<objitem.altType.length;i++){
					var item = objitem.altType[i];
					$("#altType").append("<option value='" + item.ITEM_CODE + "'>" + item.ITEM_VALUE + "</option>");
				}
				for(var i = 0;i<objitem.classifer.length;i++){
					var item = objitem.classifer[i];
					$("#classId").append("<option value='" + item.ITEM_CODE + "'>" + item.ITEM_VALUE + "</option>");
				}
				for(var i = 0;i<objitem.lastAltVer.length;i++){
					var item = objitem.lastAltVer[i];
					$("#lastAltOptId").append("<option value='" + item.ALT_VER_DATE_NO + "'>" + item.ALT_VER_DATE_NO + "</option>");
				}
				for(var i = 0;i<objitem.altBatch.length;i++){
					var item = objitem.altBatch[i];
					$("#altBatch").append("<option value='" + item.ALT_BATCH + "'>" + item.ALT_BATCH + "</option>");
				}
				for(var i = 0;i<objitem.altMode.length;i++){
					var item = objitem.altMode[i];
					$("#altMode").append("<option value='" + item.ITEM_CODE + "'>" + item.ITEM_VALUE + "</option>");
				}
			}
			
		},fail : function(){
			alert("cards load fail!");
		}
	});

/****************************************跳转系统版本变更详情*************************/
/*	function reqAltMetaDet(dbId,dbName,optId,altSts){
		var iframe = document.getElementById('formIfr').contentWindow;
		iframe.reqPageForm.dbId.value=dbId;
		iframe.reqPageForm.method.value='reqMetaAltDetailPage';
		iframe.reqPageForm.dbName.value=dbName;
		iframe.reqPageForm.optId.value=optId;
		iframe.reqPageForm.altSts.value=altSts;
		iframe.reqPageForm.target="_reqAltDat";
		iframe.reqPageForm.submit();
	}*/
/*****************获取变更明细数据***********************/
reqAltMetaDetail('', '', '', '', '', '','',0,5);
function reqAltMetaDetail(lastAltOptId,classId,altType,altMode,altBatch,altSts,altObj,start,limit){
	
	function altDetailItem(_item){
		var str = "";
		str += "<tr class=\"\">";
		str += "<td rowspan=2>"+_item.XL+"</td>";
		str += "<td>"+_item.ALTOBJ+"</td>";
		str += "<td>"+_item.CUR_VER_DATE_NO+"</td>";
		str += "<td>"+_item.ALT_VER_DATE_NO+"</td>";
		str += "<td>"+_item.ALTMODE+"</td>";
		str += "<td>"+_item.CLASSIFER+"</td>";
		str += "<td>"+_item.ALTTYPENM+"</td>";
		str += "<td>"+_item.ALT_BATCH+"</td>";
		str += "<td>"+_item.ALT_OPER_DATE+"</td>";
		str += "<td>"+_item.ALT_USER+"</td>";
		var deltailHtml = "<a style=\"cursor: pointer;\" onclick=\"showAltMeta('"+_item.CLASSIFER_TYPE+"','"+_item.ALT_ID+"','"+_item.ALTTYPENM+"','"+_item.METACONTEXT+"')\">详情</a>";
		var analyseLineageHtml = "<a style=\"cursor: pointer;\" onclick=\"analyseAltMeta('"+_item.CLASSIFER_TYPE+"','"+_item.METACONTEXT+"','dispatchLineage')\">血统分析</a>";
		var analyseImpactHtml = "<a style=\"cursor: pointer;\" onclick=\"analyseAltMeta('"+_item.CLASSIFER_TYPE+"','"+_item.METACONTEXT+"','dispatchImpact')\">影响分析</a>";
		var okHtml = "<a style=\"cursor: pointer;\" onclick=\"okAltMeta('2','"+_item.ALT_ID+"','"+_item.METACONTEXT+"')\">确认</a>";
		var delHtml = "<a style=\"cursor: pointer;\" onclick=\"delAltMeta('"+_item.ALT_ID+"')\">删除</a>";
		var caozuo = "<tr style='text-align: right;'>";
		if(_item.ALT_STS=='0'){
			str += "<td>已生效</td>";
			caozuo += "<td colspan=10 style='text-align: right;'> "+deltailHtml+" | "+analyseLineageHtml+" | "+analyseImpactHtml+" </td>";
		}else if(_item.ALT_STS=='1'){
			str += "<td>申报中</td>";
			caozuo += "<td colspan=10 style='text-align: right;'> "+deltailHtml+" | "+analyseLineageHtml+" | "+analyseImpactHtml+" </td>";
		}else if(_item.ALT_STS=='2'){
			str += "<td>未申报</td>";
			caozuo += "<td colspan=10 style='text-align: right;'> "+delHtml+" | "+deltailHtml+" | "+analyseLineageHtml+" | "+analyseImpactHtml+" </td>";
		}else if(_item.ALT_STS=='3'){
			str += "<td>未确认</td>";
			caozuo += "<td colspan=10 style='text-align: right;'> "+okHtml+" | "+delHtml+" | "+deltailHtml+" | "+analyseLineageHtml+" | "+analyseImpactHtml+" </td>";
		}
		caozuo +="</div>";
		str += "</tr>";
		str += caozuo;
		return str;
	}
	$.ajax({
		url:context+'/' + 'metadataAlt.do?method=altMetaDetailItem',
		method: 'POST',
		data:{altSts:altSts,dbId:dbId,optId:optId,start:start,limit:limit,lastAltOptId:lastAltOptId,classId:classId,altType:altType,altMode:altMode,altBatch:altBatch,altObj:altObj},
		async:false,
		dataType :'json',
		success: function(response) {
			var objdata = response.data;
			var str = "";
			for(var i = 0;i<objdata.length;i++){
				var objitem = objdata[i];
				str += altDetailItem(objitem);
			}
			$("#metaAltDetailItem").html(str);
			changeTableBackground('metaAltDetailItem');

			var next = (response.currPage*limit);
			var last = ((response.currPage-2)*limit);
			var pagesDiv = " <div class=\"alt_search_page_right\" onclick=\"sysVerPageByType('"+lastAltOptId+"','"+classId+"','"+altType+"','"+altMode+"','"+altBatch+"','"+altSts+"','"+altObj+"',"+next+","+response.totalPages+")\" style=\"cursor: pointer;\" ></div>"
			+" <div class=\"alt_search_page_mid\" style=\" padding-left: 15px;\">"+response.currPage+"/"+response.totalPages+"</div>"
			+"<div class=\"alt_search_page_left\" onclick=\"sysVerPageByType('"+lastAltOptId+"','"+classId+"','"+altType+"','"+altMode+"','"+altBatch+"','"+altSts+"','"+altObj+"',"+last+","+response.totalPages+")\" style=\"cursor: pointer;\" ></div>";
			
			$("#alt_search").html(pagesDiv);
			mouseTabRowsColor('metaAltDetailItem');
			
		},fail : function(){
			alert("cards load fail!");
		}
	});
}


function sysVerPageByType(lastAltOptId, classId, altType, altMode, altBatch,altSts, altObj,start,totalPages){
	if(start<0){
		return;
	}
	if(start/5>=totalPages){
		return;
	}
	reqAltMetaDetail(lastAltOptId, classId, altType, altMode, altBatch,altSts, altObj, start, 5);
}

function sysVerSearchByType(){
	
	var lastAltOptId = "";
	var classId = "";
	var altType = "";
	var altMode = ""; 
	var altBatch = ""; 
	var altObj = "";
	var altSts = "";
	if($("#lastAltOptId").val() != 'undefined'&&$("#lastAltOptId").val() !=null){
		lastAltOptId = $("#lastAltOptId").val();
	}
	if($("#classId").val() != 'undefined'&&$("#classId").val() !=null){
		classId = $("#classId").val();
	}
	if($("#altType").val() != 'undefined'&&$("#altType").val() !=null){
		altType = $("#altType").val();
	}
	if($("#altMode").val() != 'undefined'&&$("#altMode").val() !=null){
		altMode = $("#altMode").val();
	}
	if($("#altBatch").val() != 'undefined'&&$("#altBatch").val() !=null){
		altBatch = $("#altBatch").val();
	}
	if($("#altObj").val() != 'undefined'&&$("#altObj").val() !=null){
		altObj = $("#altObj").val();
	}
	if($("#altSts").val() != 'undefined'&&$("#altSts").val() !=null){
		altSts = $("#altSts").val();
	}
	reqAltMetaDetail(lastAltOptId, classId, altType, altMode, altBatch , altSts, altObj, 0, 5);
}

/*****************************删除变更元数据*************************************/
function delAltMeta(altIds){
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否确认删除当前变更元数据？',
	        btns: 2,                    
	        type: 4,
	        btn: ['确认','取消'],
	        yes: function(){
		          $.ajax({
					url:context+'/' + 'metadataAlt.do?method=delAltMetaDetail',
					method: 'POST',
					data:{altIds:"'"+altIds+"'"},
					async:false,
					dataType :'json',
					success: function(response) {
						var objdata = response.data;
						layer.msg(objdata.msg, 1, 1);
						sysVerSearchByType();
					},fail : function(){
						alert("cards load fail!");
					}
				});
	        }, no: function(){
	        }
	    }
	});
}
/*****************************确认变更元数据*************************************/
function okAltMeta(altSts,altIds,metaContext){
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否确认当前变更元数据？',
	        btns: 2,                    
	        type: 4,
	        btn: ['确认','取消'],
	        yes: function(){
	            $.ajax({
	    			url:context+'/' + 'metadataAlt.do?method=updAltMetaDetail',
	    			method: 'POST',
	    			data:{altSts:altSts,altIds:"'"+altIds+"'",context:metaContext,dbCode:dbId},
	    			async:false,
	    			dataType :'json',
	    			success: function(response) {
	    				var objdata = response.data;
	    				 layer.msg(objdata.msg, 1, 1);
	    				sysVerSearchByType();
	    			},fail : function(){
	    				alert("cards load fail!");
	    			}
	    		});
	        }, no: function(){
	            layer.closeAll();
	        }
	    }
	});
}
/*****************************查看变更元数据明细*************************************/
function showAltMeta(itemCode,altId,altType,metaContext){
	
	$.ajax({
		url:context+'/' + 'metadataAlt.do?method=showAltMetaDetail',
		method: 'POST',
		data:{itemCode:itemCode,altId:altId},
		async:false,
		dataType :'json',
		success: function(response) {
			var objdata = response.data;
			
			var html = showAltDetailHtml(altType,objdata);
			
			var pageii = $.layer({
				type: 1,
				title: '<b>上下文路径：</b>'+metaContext,
				area: ['542px', '250px'],
				border: [1, 1, '#ccc'],
				closeBtn: [0, true], //去掉默认关闭按钮
				page: {
				    html: html
				}
			});
			//自设关闭
			$('#pagebtn').on('click', function(){
				layer.close(pageii);
			});
		},fail : function(){
			alert("cards load fail!");
		}
	});
}

function showAltDetailHtml(altType,objdata){
	var html = "";
	html = html+"<div class=\"showDivAltDetail\" style='width:500px;height:172px;'>";
	html = html+"<div style='padding:0 0 10px 0;'>";
	html = html+"<table class=\"data_table_alt_detail\" style=\"width: 490px;\">";
	html = html+"<colgroup>";
	html = html+"<col width=15% ></col>";
	html = html+"<col width=15%></col>";
	html = html+"<col width=35%></col>";
	html = html+"<col width=35%></col>";
	
	html = html+"</colgroup>";
	html = html+"<tbody>";
	html = html+"<tr>";
	html = html+"<th>变更属性</th>";
	html = html+"<th>变更类型</th>";
	html = html+"<th>变更前</th>";
	html = html+"<th>变更后</th>";
	html = html+"</tr>";
	for(var i = 0 ; i < objdata.length ; i ++){
		html = html+"<tr>";
		html = html+"<td>"+objdata[i].COLCHNAME+"</td>";
		html = html+"<td>"+altType+"</td>";
		html = html+"<td>"+objdata[i].S_VALUE+"</td>";
		html = html+"<td>"+objdata[i].T_VALUE+"</td>";
		html = html+"</tr>";
	}
	
	html = html+"</tbody>";
	html = html+"</table>";
	html = html+"</div>";
	html = html+"</div>";
	
	
	
	return html;
}

function reqDecPage(dbId,optId){
/*	var iframe = document.getElementById('formIfr').contentWindow;
	iframe.reqPageForm.dbId.value=dbId;
	iframe.reqPageForm.method.value='reqChangeInfoPage';
	iframe.reqPageForm.optId.value=optId;
	iframe.reqPageForm.target="_parent";
	iframe.reqPageForm.forVerDate.value=$("#forVerDate").html();
	iframe.reqPageForm.deveVerDate.value=$("#deveVerDate").html();
	iframe.reqPageForm.jqVerDate.value=$("#jqVerDate").html();
	iframe.reqPageForm.targetVerDate.value=$("#targetVerDate").html();
	iframe.reqPageForm.dbName.value=dbName;
	iframe.reqPageForm.submit();*/
	parent.changeMainPage("page/mp/changeInfo/changeInfo.jsp?dbId="+dbId+"&optId="+optId);

}

function analyseAltMeta(classifer,metaContext,type){
	$.ajax({
		url:context+'/' + 'analyse.do?method=getInstanceId',
		method: 'POST',
		data:{context:metaContext,classifer:classifer,dbCode:dbId},
		async:false,
		dataType :'json',
		success: function(response) {
			var objdata = response.data;
			if(objdata.length>0){
				var mmid = objdata[0].INSTANCE_ID;
				var url = "";
				var name = "";
				if(type=='dispatchLineage'){
					url = "/metamanager/analyseMCommand.do?invoke=dispatchLineage&MDID="+mmid;
					name = "元数据血统分析";
				}else{
					url = "/metamanager/analyseMCommand.do?invoke=dispatchImpact&MDID="+mmid;
					name = "元数据影响分析";
				}
				$.layer({
			    type: 2,
			    border: [2, 1, '#ccc'],
			    title: "<b>"+name+"</b>",
			    shadeClose: false,
			    closeBtn: [0,true],
			    iframe: {src : url},
			    area: ['98%', '507px']
				});
			}else{
				layer.alert("分析失败或未找到映射关系！", 3);
			}
		},fail : function(){
			alert("cards load fail!");
		}
	});
	

}

