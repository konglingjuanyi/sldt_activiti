/*function loadMyAltMeta(decId){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getMyAltMetaForHandle',decId:decId},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var tBody = "";
			$.each(data,function(i, val){
				var tr = "";
				if(i % 2 == 0){
					tr = "<tr class='data_table_even'><td>"+val.altId+"</td><td>"+val.altSysCode+"</td><td>"+val.curVerDateNo+"</td><td>"+val.altVerDateNo+"</td>" +
							"<td>"+val.altOperDate+"</td><td>"+val.altUser+"</td><td>"+val.lastModifier+"</td><td>"+val.lastModifierTime+"</td><td>"+val.classiferType+"</td>" +
							"<td>"+val.altType+"</td></tr>";
				}else{
					tr = "<tr class='data_table_odd'><td>"+val.altId+"</td><td>"+val.altSysCode+"</td><td>"+val.curVerDateNo+"</td><td>"+val.altVerDateNo+"</td>" +
					"<td>"+val.altOperDate+"</td><td>"+val.altUser+"</td><td>"+val.lastModifier+"</td><td>"+val.lastModifierTime+"</td><td>"+val.classiferType+"</td>" +
					"<td>"+val.altType+"</td></tr>";
				}
				tBody += tr;
			});
			$('#handleTable').append(tBody);
		},
		error: function(){
			
		}
	});
}*/

function loadMyAltMeta(decId,start,limit){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getMyAltMetaForHandleByPage',decId:decId,start:start,limit:limit},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var totalPages = response.totalPages;
			var currPage = response.currPage;
			$('#tablePages').html(currPage+"/"+totalPages);
			var tBody = "";
			$.each(data,function(i, val){
				var tr = "";
				var analyseLineageHtml = "<a style=\"cursor: pointer;\" onclick=\"analyseAltMeta('"+val.CLASSIFER_TYPE+"','"+val.METACONTEXT+"','"+val.ALT_SYS_CODE+"','dispatchLineage')\">血统分析</a>";
				var analyseImpactHtml = "<a style=\"cursor: pointer;\" onclick=\"analyseAltMeta('"+val.CLASSIFER_TYPE+"','"+val.METACONTEXT+"','"+val.ALT_SYS_CODE+"','dispatchImpact')\">影响分析</a>";
				var detail = "<a style=\"cursor: pointer;\" onclick=\"showAltMeta('"+val.CLASSIFER_TYPE+"','"+val.ALT_ID+"','"+val.ALTTYPENM+"','"+val.METACONTEXT+"')\">详情</a>";
				if(i % 2 == 0){
					tr = "<tr class='data_table_even'><td style='display:none'>"+val.ALT_ID+"</td><td rowspan=2>"+val.XL+"</td><td>"+val.ALTOBJ+"</td><td>"+val.CUR_VER_DATE_NO+"</td>" +
							"<td >"+val.ALT_VER_DATE_NO+"</td><td>"+val.ALTMODE+"</td><td>"+val.CLASSIFER+"</td><td>"+val.ALTTYPENM+"</td>" +
							"<td>"+val.ALT_BATCH+"</td><td>"+val.ALT_OPER_DATE+"</td><td>"+val.ALT_USER+"</td>" +
							"</tr>";
					tr+="<tr ><td colspan=9 style='text-align: right;padding-right: 10px;'>"+detail+" | "+analyseLineageHtml+" | "+analyseImpactHtml+"</td></tr>";
				}else{
					tr = "<tr class='data_table_odd'><td style='display:none'>"+val.ALT_ID+"</td><td rowspan=2>"+val.XL+"</td><td>"+val.ALTOBJ+"</td><td>"+val.CUR_VER_DATE_NO+"</td>" +
							"<td>"+val.ALT_VER_DATE_NO+"</td><td>"+val.ALTMODE+"</td><td>"+val.CLASSIFER+"</td><td>"+val.ALTTYPENM+"</td>" +
							"<td>"+val.ALT_BATCH+"</td><td>"+val.ALT_OPER_DATE+"</td><td>"+val.ALT_USER+"</td>" +
							"</tr>";
					tr+="<tr ><td colspan=9 style='text-align: right;padding-right: 10px;'>"+detail+" | "+analyseLineageHtml+" | "+analyseImpactHtml+"</td></tr>";
				}
				tBody += tr;
			});
			$('#handleTable').append(tBody);
			mouseTabRowsColorId('#handleTable');
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
function loadFlowInfo(decId){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getDeclareInfo',decId:decId},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var hmtl = "";
			$.each(data,function(i,val){
				var name = val.name.replace("<","&lt;").replace(">","&gt;");
				html = "<li class=\"cl1\">流程名称：</li>"+
						   "<li class=\"cl2\">变更申报审核流程</li>"+
						   "<li class=\"cl3\">流程状态：</li>"+
						   "<li class=\"cl4\">"+name+"</li>"+
						   "<li class=\"cl1\">申报人：</li>"+
						   "<li class=\"cl2\">"+val.altUserName+"</li>"+
						   "<li class=\"cl3\">申报时间：</li>"+
						   "<li class=\"cl4\">"+val.altOperDate+"</li>";
			});
			$('#list1').html(html);
		},
		error: function(){
			
		}
	});
}

function loadCheckHistory(decId){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getCheckHistory',decId:decId},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var value = "";
			$.each(data,function(i,val){
				if(val.auditResult == ""){
					value += val.userName+"【"+val.department+"】审核任务，审核时间："+val.auditTime+"，审核意见："+val.auditOpinion+"<br/>";
				}else{
					value += val.userName+"【"+val.department+"】审核任务，审核时间："+val.auditTime+"，审核结果："+val.auditResult+"，审核意见："+val.auditOpinion+"<br/>";
				}
			});
			$('#checkInfo').html(value);
			/*if(data.records == 0){
				$('#moreBtnDiv').hide();
				$('#checkInfoDiv').hide();
			}else{
				var trs = "";
				trs = "<tr>"+
							"<td class='msg_table_odd'>审核人</td>"+
							"<td class='msg_table_even'>"+data.auditor+"</td>"+
						"</tr>"+
						"<tr>"+
							"<td class='msg_table_odd'>所在部门</td>"+
							"<td class='msg_table_even'>"+data.department+"</td>"+
						"</tr>"+
						"<tr>"+
							"<td class='msg_table_odd'>审核时间</td>"+
							"<td class='msg_table_even'>"+data.auditTime+"</td>"+
						"</tr>"+
						"<tr>"+
							"<td class='msg_table_odd'>审核结果</td>"+
							"<td class='msg_table_even'>"+data.auditResult+"</td>"+
						"</tr>"+
						"<tr>"+
							"<td class='msg_table_odd'>审核意见</td>"+
							"<td class='msg_table_even'>"+data.auditOpinion+"</td>"+
						"</tr>";
				$('#checkInfo').append(trs);
				if(data.records == 1){
					$('#moreBtnDiv').hide();
				}
			}*/
		},
		error: function(){
			
		}
	});
}

function loadMoreCheckHistory(decId){
	$('#moreBtn').click(function(){
		$('#moreBtnDiv').hide();
		$.ajax({
			url: context+'/flow.do',
			type: 'post',
			data: {method:'getMoreCheckHistory',decId:decId},
			dataType: 'json',
			success: function(response){
				var data = response.data;
				var divs = "";
				$.each(data,function(i,val){
					var div = "<div><table class='msg_table'><tr><td class='msg_table_odd'>审核人</td><td class='msg_table_even'>"+val.auditor+"</td></tr>" +
						"<tr><td class='msg_table_odd'>所在部门</td><td class='msg_table_even'>"+val.department+"</td></tr>" +
						"<tr><td class='msg_table_odd'>审核时间</td><td class='msg_table_even'>"+val.auditTime+"</td></tr>" +
						"<tr><td class='msg_table_odd'>审核结果</td><td class='msg_table_even'>"+val.auditResult+"</td></tr>" +
						"<tr><td class='msg_table_odd'>审核意见</td><td class='msg_table_even'>"+val.auditOpinion+"</td></tr>" +
						"</table></div>";
					divs += div;
				});
				$('#handle').append(divs);
				
				$.ajax({
					url: context+'/flow.do',
					type: 'post',
					data: {method:'getMoreCheckHistoryForSign',decId:decId},
					dataType: 'json',
					success: function(response){
						var count = 0;
						var data = response.data;
						var div = "<div><table class='data_table'><tr><th>会签人</th><th>所在部门</th><th>会签时间</th><th>会签意见</th></tr>";
						$.each(data,function(i,val){
							count++;
							div += "<tr><td>"+val.auditor+"</td><td>"+val.department+"</td><td>"+val.auditTime+"</td><td>"+val.auditOpinion+"</td></tr>";
						});
						div += "</table</div>";
						if(count != 0){
							$('#handle').append(div);
						}
					},
					error: function(){
						
					}
				});
			},
			error: function(){
				
			}
		});
	});
}

function paging(){
	$('#tableNextPage').click(function(){
		var mid = $('#tablePages').text();
		var mids = mid.split("/");
		var currPage = mids[0];
		var totalPages = mid[2];
		if(currPage == totalPages){
			return false;
		}
		$('#handleTable tr:gt(0)').remove();
		loadMyAltMeta(decId,currPage*limit,limit);
	});
	
	$('#tableLastPage').click(function(){
		var mid = $('#tablePages').text();
		var mids = mid.split("/");
		var currPage = mids[0];
		if(currPage == 1){
			return false;
		}
		$('#handleTable tr:gt(0)').remove();
		loadMyAltMeta(decId,(currPage-1)*limit-limit,limit);
	});
}

function page_ai(decId,limit,start,totalPages){
	if(start<0){
		return;
	}
	if(start/limit>=totalPages){
		return;
	}
	$('#reportInfo_table_ai tr:gt(0)').remove();
	loadAltertionImpact(start,limit,decId)
}

function page_cc(decId,limit,start,totalPages){
	if(start<0){
		return;
	}
	if(start/limit>=totalPages){
		return;
	}
	$('#reportInfo_table_cc tr:gt(0)').remove();
	loadMetaIntColCodeDec(start,limit,decId)
}

function page_if(decId,limit,start,totalPages){
	if(start<0){
		return;
	}
	if(start/limit>=totalPages){
		return;
	}
	$('#reportInfo_table_if tr:gt(0)').remove();
	loadMetaIntFileDec(start,limit,decId)
}

function close(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
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

function loadAltertionImpact(start,limit,decId){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'loadAltertionImpactByDecId',decId:decId,start:start,limit:limit},
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
			var pagesDiv = " <div class=\"search_page_left_check\" onclick=\"page_ai('"+decId+"','"+limit+"',"+last+","+totalPages+")\" style=\"cursor: pointer;\" ></div>"
				+" <div class=\"search_page_mid_check\" >"+currPage+"/"+totalPages+"</div>"
				+"<div class=\"search_page_right_check\" onclick=\"page_ai('"+decId+"','"+limit+"',"+next+","+totalPages+")\" style=\"cursor: pointer;\" ></div>";
			$("#search_page_ai").html(pagesDiv);
		}
	});
}

function loadMetaIntColCodeDec(start,limit,decId){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'loadMetaIntColCodeDecByDecId',decId:decId,start:start,limit:limit},
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
			var pagesDiv = " <div class=\"search_page_left_check\" onclick=\"page_cc('"+decId+"','"+limit+"',"+last+","+totalPages+")\" style=\"cursor: pointer;\" ></div>"
				+" <div class=\"search_page_mid_check\" >"+currPage+"/"+totalPages+"</div>"
				+"<div class=\"search_page_right_check\" onclick=\"page_cc('"+decId+"','"+limit+"',"+next+","+totalPages+")\" style=\"cursor: pointer;\" ></div>";
			$("#search_page_cc").html(pagesDiv);
		}
	});
}

function loadMetaIntFileDec(start,limit,decId){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'loadMetaIntFileDecByDecId',decId:decId,start:start,limit:limit},
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
			var pagesDiv = " <div class=\"search_page_left_check\" onclick=\"page_if('"+decId+"','"+limit+"',"+last+","+totalPages+")\" style=\"cursor: pointer;\" ></div>"
				+" <div class=\"search_page_mid_check\" >"+currPage+"/"+totalPages+"</div>"
				+"<div class=\"search_page_right_check\" onclick=\"page_if('"+decId+"','"+limit+"',"+next+","+totalPages+")\" style=\"cursor: pointer;\" ></div>";
			$("#search_page_if").html(pagesDiv);
		}
	});
}