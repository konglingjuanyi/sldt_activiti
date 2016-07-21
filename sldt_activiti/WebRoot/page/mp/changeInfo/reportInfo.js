$(document).ready(function(){
	loadMyAltMeta(0,limit,altIds);
	
	loadAltertionImpact(0,limit,altIds);
	
	//loadMetaIntFileDec(0,limit,altIds);
	
	//loadMetaIntColCodeDec(0,limit,altIds);
	
	getAssignee();
	
	$('#reset').click(function(){
		$('#reportForm').get(0).reset();
	});
	
	$('#cancel').click(function(){
		close();
	});
	
	$('#start').click(function(){
		var decName = $.trim($('#decName').val());
		var decDesc = $.trim($('#decDesc').val());
		var assignee = $("#assignee").find("option:selected").val();
		var decType = $("#decType").find("option:selected").val();
		/*var choice = layer.confirm("确定是否提交申报？");
		if(choice == true){
			$.ajax({
				url: context+'/flow.do',
				type: 'post',
				data: {method:'startFlow',decName:decName,decDesc:decDesc,altIds:altIds},
				dataType: 'json',
				success: function(){
					alert("流程已启动！");
					close();
				},
				error: function(){
					alert("流程启动失败！");
				}
			});
		}*/
		$.layer({
			shade: [0],
			area: ['auto','auto'],
			dialog: {
				msg: '您确定提交申报吗？',
				btns: 2,
				type: 4,
				yes: function(){
					if(decName == ""){
						layer.alert("申报名称不能为空！",0);
					}else if(assignee == ""){
						layer.alert("提交对象不能为空！",0);
					}else{
						layer.load('正在提交，请稍后...');
						$("#start").attr("disabled",true);
						$.ajax({
							url: context+'/flow.do',
							type: 'post',
							data: {method:'startFlow',decName:decName,decDesc:decDesc,altIds:altIds,assignee:assignee,decType:decType},
							dataType: 'json',
							success: function(response){
								if(response.data.success=='true'){
									layer.alert(response.data.msg, 1);
									parent.$('#selected_table tr:gt(0)').remove();
									parent.$('#changeInfo_table_tbody tr').remove();
									window.parent.loadMyAltMeta(0,10,altSts,dbId,optId);
								}else{
									layer.alert(response.data.msg, 3);
								}
								setTimeout(close,1000);
							},
							error: function(){
								alert("load fail!");
							}
						});
					}
				},
				no: function(){
					
				}
			}
		});
	});
	
});

function loadMyAltMeta(start,limit,altIds){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getMyAltMetaForHandleByPage',start:start,limit:limit,altIds:altIds},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var totalPages = response.totalPages;
			var currPage = response.currPage;
			$('#tablePages').html(currPage+"/"+totalPages);
			var tBody = "";
			$.each(data,function(i,val){
				var tr = "";
				var analyseLineageHtml = "<a style=\"cursor: pointer;\" onclick=\"analyseAltMeta('"+val.CLASSIFER_TYPE+"','"+val.METACONTEXT+"','"+val.ALT_SYS_CODE+"','dispatchLineage')\">血统分析</a>";
				var analyseImpactHtml = "<a style=\"cursor: pointer;\" onclick=\"analyseAltMeta('"+val.CLASSIFER_TYPE+"','"+val.METACONTEXT+"','"+val.ALT_SYS_CODE+"','dispatchImpact')\">影响分析</a>";
				var detail = "<a style=\"cursor: pointer;\" onclick=\"showAltMeta('"+val.CLASSIFER_TYPE+"','"+val.ALT_ID+"','"+val.ALTTYPENM+"','"+val.METACONTEXT+"')\">详情</a>";
				if(i % 2 == 0){
					tr = "<tr class='data_table_even'><td rowspan=2>"+val.XL+"</td><td>"+val.ALTOBJ+"</td><td>"+val.CUR_VER_DATE_NO+"</td><td>"+val.ALT_VER_DATE_NO+"</td>" +
							"<td>"+val.ALTMODE+"</td><td>"+val.CLASSIFER+"</td><td>"+val.ALTTYPENM+"</td><td>"+val.ALT_BATCH+"</td><td>"+val.ALT_OPER_DATE+"</td>" +
							"<td>"+val.ALT_USER+"</td></tr>";
					tr+="<tr ><td colspan=9 style='text-align: right;padding-right: 10px;'>"+detail+" | "+analyseLineageHtml+" | "+analyseImpactHtml+"</td></tr>";
				}else{
					tr = "<tr class='data_table_odd'><td rowspan=2>"+val.XL+"</td><td>"+val.ALTOBJ+"</td><td>"+val.CUR_VER_DATE_NO+"</td><td>"+val.ALT_VER_DATE_NO+"</td>" +
					"<td>"+val.ALTMODE+"</td><td>"+val.CLASSIFER+"</td><td>"+val.ALTTYPENM+"</td><td>"+val.ALT_BATCH+"</td><td>"+val.ALT_OPER_DATE+"</td>" +
					"<td>"+val.ALT_USER+"</td></tr>";
					tr+="<tr ><td colspan=9 style='text-align: right;padding-right: 10px;'>"+detail+" | "+analyseLineageHtml+" | "+analyseImpactHtml+"</td></tr>";
				}
				tBody += tr;
			});
			$('#reportInfo_table').append(tBody);
			mouseTabRowsColorId('#reportInfo_table');
			var next = currPage*limit;
			var last = (currPage-2)*limit;
			var pagesDiv = " <div class=\"search_page_left\" onclick=\"page('"+altIds+"','"+limit+"',"+last+","+totalPages+")\" style=\"cursor: pointer;\" ></div>"
				+" <div class=\"search_page_mid\" >"+currPage+"/"+totalPages+"</div>"
				+"<div class=\"search_page_right\" onclick=\"page('"+altIds+"','"+limit+"',"+next+","+totalPages+")\" style=\"cursor: pointer;\" ></div>";
			$("#search_page").html(pagesDiv);
		},
		error: function(){
			
		}
	});
}


function analyseAltMeta(classifer,metaContext,dbCode,type){
	$.ajax({
		url:context+'/' + 'analyse.do?method=getInstanceId',
		method: 'POST',
		data:{context:metaContext,classifer:classifer,dbCode:dbCode},
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
				area: ['auto', 'auto'],
				border: [0], //去掉默认边框
				shade: [0], //去掉遮罩
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
	html = html+"<div class=\"showDivAltDetail\">";
	html = html+"<div style='padding:0 0 10px 0;'>";
	html = html+"<table class=\"data_table_alt_detail\" style=\"width: 590px;\">";
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


function page(altIds,limit,start,totalPages){
	if(start<0){
		return;
	}
	if(start/limit>=totalPages){
		return;
	}
	$('#reportInfo_table tr:gt(0)').remove();
	loadMyAltMeta(start,limit,altIds);
}

function page_ai(altIds,limit,start,totalPages){
	if(start<0){
		return;
	}
	if(start/limit>=totalPages){
		return;
	}
	$('#reportInfo_table_ai tr:gt(0)').remove();
	loadAltertionImpact(start,limit,altIds);
}

function page_if(altIds,limit,start,totalPages){
	if(start<0){
		return;
	}
	if(start/limit>=totalPages){
		return;
	}
	$('#reportInfo_table_if tr:gt(0)').remove();
	loadMetaIntFileDec(start,limit,altIds);
}

function page_cc(altIds,limit,start,totalPages){
	if(start<0){
		return;
	}
	if(start/limit>=totalPages){
		return;
	}
	$('#reportInfo_table_cc tr:gt(0)').remove();
	loadMetaIntColCodeDec(start,limit,altIds);
}

function getAssignee(){
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
		},
		error: function(){
			
		}
	});
}

function loadAltertionImpact(start,limit,altIds){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'loadAltertionImpact',altIds:altIds,start:start,limit:limit},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var totalPages = response.totalPages;
			var currPage = response.currPage;
			var tBody = "";
			$.each(data,function(i,val){
				var tr = "";
				if(i % 2 == 0){
					tr = "<tr class='data_table_even'><td>"+val.XL+"</td><td>"+val.ALT_IMP_SYS+"("+val.ALT_IMP_SYS_NM+")"+"</td>" +
						 "<td>"+val.ALT_IMP_TAB_COUNT+"</td><td>"+val.ALT_IMP_USER+"("+val.ALT_IMP_USER_DEP+")"+"</td>" +
						 "<td>"+val.ALT_ANA_BASIS_TYPE+"</td></tr>";
				}else{
					tr = "<tr class='data_table_odd'><td>"+val.XL+"</td><td>"+val.ALT_IMP_SYS+"("+val.ALT_IMP_SYS_NM+")"+"</td>" +
					 	 "<td>"+val.ALT_IMP_TAB_COUNT+"</td><td>"+val.ALT_IMP_USER+"("+val.ALT_IMP_USER_DEP+")"+"</td>" +
					 	 "<td>"+val.ALT_ANA_BASIS_TYPE+"</td></tr>";
				}
				tBody += tr;
			});
			$('#reportInfo_table_ai').append(tBody);
			mouseTabRowsColorId('#reportInfo_table_ai');
			var next = currPage*limit;
			var last = (currPage-2)*limit;
			var pagesDiv = " <div class=\"search_page_left\" onclick=\"page_ai('"+altIds+"','"+limit+"',"+last+","+totalPages+")\" style=\"cursor: pointer;\" ></div>"
				+" <div class=\"search_page_mid\" >"+currPage+"/"+totalPages+"</div>"
				+"<div class=\"search_page_right\" onclick=\"page_ai('"+altIds+"','"+limit+"',"+next+","+totalPages+")\" style=\"cursor: pointer;\" ></div>";
			$("#search_page_ai").html(pagesDiv);
		}
	});
}

function loadMetaIntFileDec(start,limit,altIds){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'loadMetaIntFileDec',altIds:altIds,start:start,limit:limit},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var totalPages = response.totalPages;
			var currPage = response.currPage;
			var tBody = "";
			$.each(data,function(i,val){
				var tr = "";
				if(i % 2 == 0){
					tr = "<tr class='data_table_even'><td>"+val.XL+"</td><td>"+val.DB_CODE+"</td><td>"+val.REF_DB_CODE+"</td>" +
						 "<td>"+val.REF_SCHNAME+"</td><td>"+val.REF_MODNAME+"</td><td>"+val.REF_TABNAME+"</td>" +
						 "<td>"+val.C_FUNC_NAME+"</td><td>"+val.C_DESC+"</td><td>"+val.C_TYPE+"</td></tr>";
				}else{
					tr = "<tr class='data_table_odd'><td>"+val.XL+"</td><td>"+val.DB_CODE+"</td><td>"+val.REF_DB_CODE+"</td>" +
					 	 "<td>"+val.REF_SCHNAME+"</td><td>"+val.REF_MODNAME+"</td><td>"+val.REF_TABNAME+"</td>" +
					 	 "<td>"+val.C_FUNC_NAME+"</td><td>"+val.C_DESC+"</td><td>"+val.C_TYPE+"</td></tr>";
				}
				tBody += tr;
			});
			$('#reportInfo_table_if').append(tBody);
			mouseTabRowsColorId('#reportInfo_table_if');
			var next = currPage*limit;
			var last = (currPage-2)*limit;
			var pagesDiv = " <div class=\"search_page_left\" onclick=\"page_if('"+altIds+"','"+limit+"',"+last+","+totalPages+")\" style=\"cursor: pointer;\" ></div>"
				+" <div class=\"search_page_mid\" >"+currPage+"/"+totalPages+"</div>"
				+"<div class=\"search_page_right\" onclick=\"page_if('"+altIds+"','"+limit+"',"+next+","+totalPages+")\" style=\"cursor: pointer;\" ></div>";
			$("#search_page_if").html(pagesDiv);
		}
	});
}

function loadMetaIntColCodeDec(start,limit,altIds){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'loadMetaIntColCodeDec',altIds:altIds,start:start,limit:limit},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var totalPages = response.totalPages;
			var currPage = response.currPage;
			var tBody = "";
			$.each(data,function(i,val){
				var tr = "";
				if(i % 2 == 0){
					tr = "<tr class='data_table_even'><td>"+val.XL+"</td><td>"+val.DB_CODE+"</td><td>"+val.SCHNAME+"</td><td>"+val.MODNAME+"</td>" +
						 "<td>"+val.TABNAME+"</td><td>"+val.COLNAME+"</td><td>"+val.CODE_CH_NAME+"</td><td>"+val.CODE_DESC+"</td>" +
						 "<td>"+val.CODE_STS+"</td><td>"+val.CODE_START_DATE+"</td><td>"+val.CODE_END_DATE+"</td><td>"+val.DS_REF+"</td></tr>";
				}else{
					tr = "<tr class='data_table_odd'><td>"+val.XL+"</td><td>"+val.DB_CODE+"</td><td>"+val.SCHNAME+"</td><td>"+val.MODNAME+"</td>" +
					 	 "<td>"+val.TABNAME+"</td><td>"+val.COLNAME+"</td><td>"+val.CODE_CH_NAME+"</td><td>"+val.CODE_DESC+"</td>" +
					 	 "<td>"+val.CODE_STS+"</td><td>"+val.CODE_START_DATE+"</td><td>"+val.CODE_END_DATE+"</td><td>"+val.DS_REF+"</td></tr>";
				}
				tBody += tr;
			});
			$('#reportInfo_table_cc').append(tBody);
			mouseTabRowsColorId('#reportInfo_table_cc');
			var next = currPage*limit;
			var last = (currPage-2)*limit;
			var pagesDiv = " <div class=\"search_page_left\" onclick=\"page_cc('"+altIds+"','"+limit+"',"+last+","+totalPages+")\" style=\"cursor: pointer;\" ></div>"
				+" <div class=\"search_page_mid\" >"+currPage+"/"+totalPages+"</div>"
				+"<div class=\"search_page_right\" onclick=\"page_cc('"+altIds+"','"+limit+"',"+next+","+totalPages+")\" style=\"cursor: pointer;\" ></div>";
			$("#search_page_cc").html(pagesDiv);
		}
	});
}

function close(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}