$(document).ready(function(){
	loadMyAltMeta(0,limit,altSts,dbId,optId);
	
	search(limit,altSts,dbId,optId);
	
	declare(dbId,optId);
	
	$.ajax({
		url:context+'/' + 'metadataAlt.do?method=altMetaDeFindWhere',
		method: 'POST',
		async:false,
		dataType :'json',
		data:{dbId:dbId,dbName:dbName,optId:optId},
		success: function(response){
			var objdata = response.data;
			for(var i = 0;i<objdata.length;i++){
				var objitem = objdata[i];
				for(var i = 0;i<objitem.altBatch.length;i++){
					var item = objitem.altBatch[i];
					$("#altBatch").append("<option value='" + item.ALT_BATCH + "'>" + item.ALT_BATCH + "</option>");
				}
			}
		}
	});
});

function reqAltMetaDet(_dbId,dbName,optId,type){
	loadMyAltMeta(0,limit,altSts,dbId,optId);
}

/******************************加载变更流水信息******************************/
function loadMyAltMeta(start,limit,altSts,dbId,optId){
	var classId = $('#classId').val();
	var altType = $('#altType').val();
	var altBatch = $('#altBatch').val();
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getMyAltMetaByPage',start:start,limit:limit,altSts:altSts,dbId:dbId,optId:optId,altType:altType,classId:classId,altBatch:altBatch},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var totalPages = response.totalPages;
			var currPage = response.currPage;
			var tBody = "";
			$('#changeInfo_table_tbody tr').remove();
			$.each(data,function(i,val){
				var tr = "";
				var altId = val.ALT_ID;
				var flag = existed(altId);
				if(flag == true){
					tr = "<tr class='data_table_selected'><td style=\"width: 30px;\"><input type='checkbox'/></td><td style='display:none'>"+val.ALT_ID+"</td>" +
							"<td style=\"width: 40px;\">"+val.XL+"</td><td style=\"width: 100px;\">"+val.ALTOBJ+"</td>" +
							"<td style=\"width: 60px;\">"+val.CUR_VER_DATE_NO+"</td>" +
							"<td style=\"width: 60px;\">"+val.ALT_VER_DATE_NO+"</td><td style=\"width: 40px;\">"+val.ALTMODE+"</td>" +
							"<td style=\"width: 40px;\">"+val.CLASSIFER+"</td><td style=\"width: 40px;\">"+val.ALTTYPENM+"</td>" +
							"<td style=\"width: 60px;\">"+val.ALT_BATCH+"</td><td style=\"width: 78px;\">"+val.ALT_OPER_DATE+"</td>" +
							"<td style=\"width: 60px;\">"+val.ALT_USER+"</td></tr>";
				}else{
					if(i % 2 == 0){
						tr = "<tr class='data_table_ci_even'><td style=\"width: 31px;\"><input type='checkbox'/></td><td style='display:none'>"+val.ALT_ID+"</td>" +
							"<td style=\"width: 41px;\">"+val.XL+"</td><td style=\"width: 100px;\">"+val.ALTOBJ+"</td>" +
							"<td style=\"width: 61px;\">"+val.CUR_VER_DATE_NO+"</td>" +
							"<td style=\"width: 61px;\">"+val.ALT_VER_DATE_NO+"</td><td style=\"width: 41px;\">"+val.ALTMODE+"</td>" +
							"<td style=\"width: 41px;\">"+val.CLASSIFER+"</td><td style=\"width: 41px;\">"+val.ALTTYPENM+"</td>" +
							"<td style=\"width: 61px;\">"+val.ALT_BATCH+"</td><td style=\"width: 79px;\">"+val.ALT_OPER_DATE+"</td>" +
							"<td style=\"width: 61px;\">"+val.ALT_USER+"</td></tr>";
					}else{
						tr = "<tr class='data_table_ci_odd'><td style=\"width: 31px;\"><input type='checkbox'/></td><td style='display:none'>"+val.ALT_ID+"</td>" +
						"<td style=\"width: 41px;\">"+val.XL+"</td><td style=\"width: 100px;\">"+val.ALTOBJ+"</td>" +
						"<td style=\"width: 61px;\">"+val.CUR_VER_DATE_NO+"</td>" +
						"<td style=\"width: 61px;\">"+val.ALT_VER_DATE_NO+"</td><td style=\"width: 41px;\">"+val.ALTMODE+"</td>" +
						"<td style=\"width: 41px;\">"+val.CLASSIFER+"</td><td style=\"width: 41px;\">"+val.ALTTYPENM+"</td>" +
						"<td style=\"width: 61px;\">"+val.ALT_BATCH+"</td><td style=\"width: 79px;\">"+val.ALT_OPER_DATE+"</td>" +
						"<td style=\"width: 61px;\">"+val.ALT_USER+"</td></tr>";
					}
				}
				tBody += tr;
			});
			$('#changeInfo_table_tbody').append(tBody);
			mouseTabRowsColorId("#changeInfo_table_tbody");
			var next = currPage*limit;
			var last = (currPage-2)*limit;
			var pagesDiv = " <div class=\"search_page_left_ci\" onclick=\"page('"+altSts+"','"+limit+"','"+last+"','"+totalPages+"','"+dbId+"','"+optId+"')\" style=\"cursor: pointer;\" ></div>"
				+" <div class=\"search_page_mid_ci\" >"+currPage+"/"+totalPages+"</div>"
				+"<div class=\"search_page_right_ci\" onclick=\"page('"+altSts+"','"+limit+"','"+next+"','"+totalPages+"','"+dbId+"','"+optId+"')\" style=\"cursor: pointer;\" ></div>";
			$("#search_page").html(pagesDiv);
			
			var inputs = " <input id='add' class='arrowInput' type='button' value='>' onclick=\"add('"+currPage+"','"+limit+"','"+altSts+"','"+dbId+"','"+optId+"')\" />"+
						 " <input id='addAll' class='arrowInput' type='button' value='>>' onclick=\"addAll('"+currPage+"','"+limit+"','"+altSts+"','"+dbId+"','"+optId+"')\" />"+
						 " <input id='removeAll' class='arrowInput' type='button' value='<<' onclick=\"removeAll('"+currPage+"','"+limit+"','"+altSts+"','"+dbId+"','"+optId+"')\" />"+
						 " <input id='remove' class='arrowInput' type='button' value='<' onclick=\"remove('"+currPage+"','"+limit+"','"+altSts+"','"+dbId+"','"+optId+"')\" />";
			$("#arrowDiv").html(inputs);
			
			clickTr();
			
			stopBubble();
		},
		error: function(){
			
		}
	});
}

function existed(altId){
	var flag = false;
	$('#selected_table tr:gt(0)').each(function(i,val){
		var altId_ = $(val).find("td:eq(1)").text();
		if(altId == altId_){
			flag = true;
		}
	});
	return flag;
}

function getExistedCount(){
	var count = 0;
	$('#selected_table tr:gt(0)').each(function(i,val){
		count++;
	});
	return count;
}

function add(currPage,limit,altSts,dbId,optId){
	var count = 0;
	var tBody = "";
	$('#changeInfo_table_tbody input:checked').each(function(i,val){
		count++;
		var altId = $(val).parent().parent().find('td:eq(1)').text();
		var xl = $(val).parent().parent().find('td:eq(2)').text();
		var altobj = $(val).parent().parent().find('td:eq(3)').text();
		var tr = "<tr class='data_table_even'><td><input type='checkbox'/></td><td style='display:none'>"+altId+"</td><td>"+xl+"</td><td>"+altobj+"</td>";
		var flag = existed(altId);
		if(flag != true){
			tBody += tr;
		}
	});
	
	if(count == 0){
		layer.alert("对不起，没有记录可以操作，请至少选择一行记录！",0);
		return false;
	}
	$('#selected_table').append(tBody);
	changeTableBackground('selected_table');
	mouseTabRowsColorId('#selected_table');
	$('#changeInfo_table_tbody tr').remove();
	loadMyAltMeta((currPage-1)*limit,limit,altSts,dbId,optId);
}

function addAll(currPage,limit,altSts,dbId,optId){
	var tBody = "";
	$('#changeInfo_table_tbody tr').each(function(i,val){
		var altId = $(val).find('td:eq(1)').text();
		var xl = $(val).find('td:eq(2)').text();
		var altobj = $(val).find('td:eq(3)').text();
		var tr = "<tr class='data_table_even'><td><input type='checkbox'/></td><td style='display:none'>"+altId+"</td><td>"+xl+"</td><td>"+altobj+"</td>";
		var flag = existed(altId);
		if(flag != true){
			tBody += tr;
		}
	});
	$('#selected_table').append(tBody);
	changeTableBackground('selected_table');
	mouseTabRowsColorId('#selected_table');
	$('#changeInfo_table_tbody tr').remove();
	loadMyAltMeta((currPage-1)*limit,limit,altSts,dbId,optId);
}

function removeAll(currPage,limit,altSts,dbId,optId){
	$('#selected_table tr:gt(0)').each(function(i,val){
		$(val).remove();
	});
	$('#changeInfo_table_tbody tr').remove();
	loadMyAltMeta((currPage-1)*limit,limit,altSts,dbId,optId);
}

function remove(currPage,limit,altSts,dbId,optId){
	var count = 0;
	$('#selected_table input:checked').each(function(i,val){
		count++;
		$(val).parent().parent().remove();
	});
	if(count == 0){
		layer.alert("对不起，没有记录可以操作，请至少选择一行记录！",0);
		return false;
	}
	$('#changeInfo_table_tbody tr').remove();
	loadMyAltMeta((currPage-1)*limit,limit,altSts,dbId,optId);
}

function page(altSts,limit,start,totalPages,dbId,optId){
	if(start<0){
		return;
	}
	if(start/limit>=totalPages){
		return;
	}
	$('#changeInfo_table_tbody tr').remove();
	loadMyAltMeta(start,limit,altSts,dbId,optId);
}

function clickTr(){
	$("#changeInfo_table_tbody tr").each(function(i,val){
		$(val).click(function(){
			$ck = $(val).find("td input:checkBox");
			ck = $ck.get(0);
			if(ck.checked == true){
				$ck.attr("checked", false);
			}else{
				$ck.attr("checked", true);
			}
		});
	});
	
	$("#selected_table tr:gt(0)").each(function(i,val){
		$(val).click(function(){
			$ck = $(val).find("td input:checkBox");
			ck = $ck.get(0);
			if(ck.checked == true){
				$ck.attr("checked", false);
			}else{
				$ck.attr("checked", true);
			}
		});
	});
}

function stopBubble(){
	$('#changeInfo_table_tbody tr').each(function(i,val){
		$(val).find('td:eq(0)').find('input:checkbox').click(function(e){
			e.stopPropagation();
		});
	});
	
	$('#selected_table tr').each(function(i,val){
		$(val).find('td:eq(0)').find('input:checkbox').click(function(e){
			e.stopPropagation();
		});
	});
}

/******************************查询******************************/
function search(limit,altSts,dbId,optId){
	$('#searchBtn').click(function(){
		$('#changeInfo_table_tbody tr').remove();
		loadMyAltMeta(0,limit,altSts,dbId,optId);
	});
}

/******************************申报******************************/
function declare(dbId,optId){
	$('#reportBtn').click(function(){
		var count = 0;
		var altIds = "";
		$('#selected_table tr:gt(0)').each(function(i,val){
			var altId = $(val).find('td:eq(1)').text();
			altIds += altId+",";
			count++;
		});
		if(count == 0){
			layer.alert("对不起，没有记录可以操作！",0);
			return false;
		}
		altIds = altIds.substring(0, altIds.lastIndexOf(","));
		$.layer({
		    type: 2,
		    border: [1, 1, '#ccc'],
		    title: false,
		    shadeClose: false,
		    closeBtn: [0,false],
		    iframe: {src : context+'/page/mp/changeInfo/reportInfo.jsp?altIds='+altIds+'&dbId='+dbId+'&optId='+optId+'&dbName='+dbName},
		    area: ['950px', '605px']
		});
		/*var url = context+'/page/changeInfo/reportInfo.jsp';
		var width = 1000; //弹出窗口的宽度;
		var height = 550; //弹出窗口的高度;
		var top = (window.screen.availHeight-30-height)/2; //获得窗口的垂直位置;
		var left = (window.screen.availWidth-10-width)/2; //获得窗口的水平位置;
		var config = 'width='+width+', top='+top+', left='+left+', scrollbars=yes';
		window.open(url,'reportInfo',config);*/
	});
}
