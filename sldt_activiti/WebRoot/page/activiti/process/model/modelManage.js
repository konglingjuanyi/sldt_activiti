$(document).ready(function(){
	var grid_selector = "#grid-table-model";
	var pager_selector = "#grid-pager-model";
	jQuery(grid_selector).jqGrid({
		url: context+'/activiti/processModel.do?method=getProcessModelByPage',
		datatype: "json",
		height: 400,
		regional: 'cn', 
		colNames:[ 'ID','Name','key','Version', '创建时间', '最后更新时间','元数据', '操作'],
		colModel:[
			{name:'id',index:'id', width:60, editable: true,align : 'center'},
			{name:'name',index:'name', width:60, editable: true,align : 'center'},
			{name:'key',index:'key',width:80, editable:true,align : 'center'},
			{name:'version',index:'version', width:80,editable: true,align : 'center'},
			{name:'createTime',index:'createTime', width:70, editable: true,align : 'center'},
			{name:'lastUpdateTime',index:'lastUpdateTime', width:70, editable: true,align : 'center'},
			{name:'metaInfo',index:'metaInfo', width:120, sortable:false,align : 'center',editable: true,edittype:"textarea", editoptions:{rows:"2",cols:"10"}},
			{name:'action',index:'action', width:100, editable: true,align : 'center'},
		], 
		gridComplete : function() {
			var ids = jQuery(grid_selector).jqGrid('getDataIDs');
			for ( var i = 0; i < ids.length; i++) {
				var rowData = jQuery(grid_selector).jqGrid('getRowData', ids[i]);
				var exportUrl = context+"/activiti/processModel.do?method=exportBpmnFile&modelId="+rowData.id;
				var view1 = "<a href=\"#\" onclick=\"modifyModel('" + rowData.id + "')\" title=\"修改\">修改</a>";
				var view2 = "<a href=\"#\" onclick=\"deployModel('" + rowData.id + "')\" title=\"部署\">部署</a>";
				var view3 = "<a href=\""+exportUrl+"\"  title=\"导出\">导出</a>";
				
				jQuery(grid_selector).jqGrid('setRowData', ids[i], {action : view1+"&nbsp;&nbsp;"+view2+"&nbsp;&nbsp;"+view3});
			}
		},
		viewrecords : true,
		rowNum:15,
		rowList:[15,30,45,60,75,90,105,120],
		pager : pager_selector,
		altRows: true,
		multiselect: true,
        multiboxonly: true,
		loadComplete : function() {
			var table = this;
			setTimeout(function(){
				updatePagerIcons(table);
				enableTooltips(table);
			}, 0);
		},
		autowidth: true

	});
	
	jQuery(grid_selector).jqGrid('navGrid',pager_selector,{edit:false,add:false,del:false,refresh:false,search:false});
	
	//绑定点击事件
	$('#seachModelButton').click(function() {
		reloadGridData();
    });
	
	/**
	 * 添加
	 */
	$("#createModelButton").click(function(){
		$("#addModal").modal("show");
	});
	
	/**
	 * 新增模型
	 */
	$("#saveModelButton").click(function(){
		var name = $("#editDiv").find("input[name=name]").val();
		var key = $("#editDiv").find("input[name=key]").val();
		var description = $("#editDiv").find("textarea[name=description]").val();
		
		if(isEmpty(name)){
			$("#name-span").text("名称不能为空！");
			return false;
		}else if(isChinese(name)){
			$("#name-span").text("名称不支持中文！");
			return false;
		}else{
			$("#name-span").text("");
		}
		
		if(isEmpty(key)){
			$("#key-span").text("key不能为空！");
			return false;
		}else if(isChinese(name)){
			$("#key-span").text("key值不支持中文！");
			return false;
		}else{
			$("#key-span").text("");
		}
		
		if(isEmpty(description)){
			$("#des-span").text("描述不能为空！");
			return false;
		}else{
			$("#des-span").text("");
		}
		
		$("#add-modal-form").submit();
		
		/**var params = {
				name :name,
				key :key,
				description :description,
				method:'addProcessModel'
		};
		
		
		$.ajax({
			url: context+'/activiti/processModel.do',
			type: 'post',
			data: params,
			dataType: 'json',
			success: function(){
				$("#addModal").modal("hide");
				layer.alert("新增成功！",1);
				reloadGridData();
			},
			error: function(){
				layer.alert("撤销失败！",3);
			}
		});**/
		
	});
	
	
	/**
	 * 删除
	 */
	$("#deleteModelButton").bind("click",function(){
		var ids = $("#grid-table-model").jqGrid("getGridParam", "selarrrow");
		
		var idsStr = "";
		
		if(confirm("您确定要删除选中的记录吗？")){
			// 获取要删除的taskId
			ids.forEach(function(id){
				//var rowData = $("#grid-table-model").jqGrid('getRowData', id),
				//modelId = rowData.id;
				idsStr += "," + id;
			});
			deleteModelByIds(idsStr.substring(1));
		}
	});
	
	/**
	 * 重置
	 */
	$("#resetModelButton").click(function(){
		$("#editDiv").find("input[type=text]").val("");
		$("#editDiv").find("textarea[name=description]").val("");
	});
	/**
	 * 撤销
	 */
	$("#backModelButton").click(function(){
		$("#addModal").modal("hide");
	});
	
});


/**
 * 批量删除
 * 
 * @param idsStr
 */
function deleteModelByIds(idsStr){
	$.ajax({
		type: 'post', 
        url: context + '/activiti/processModel.do?method=deleteProcessModel', 
        data: "&idsStr=" + idsStr, 
        success: function(msg){
        	if(msg.code == '0'){
        		//alert(msg.message); // 弹出成功msg
        		// 刷新表格数据
        		$("#grid-table-model").trigger("reloadGrid");
        	}else{
        		layer.alert("删除失败。失败信息如下:" + msg.message, 2) // 弹出失败msg
        	}
        },
        error: function(msg){
        	layer.alert("访问服务器出错！",2)
        }
	});
}


/**
 * 判断是否为空
 */
function isEmpty(param){
	return param === undefined || param == null || param.length==0;
}

//replace icons with FontAwesome icons like above
function updatePagerIcons(table) {
	var replacement = 
	{
		'ui-icon-seek-first' : 'fa fa-angle-double-left bigger-140',
		'ui-icon-seek-prev' : 'fa fa-angle-left bigger-140',
		'ui-icon-seek-next' : 'fa fa-angle-right bigger-140',
		'ui-icon-seek-end' : 'fa fa-angle-double-right bigger-140'
	};
	$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
		var icon = $(this);
		var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
		if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
	})
}

function enableTooltips(table) {
	$('.navtable .ui-pg-button').tooltip({container:'body'});
	$(table).find('.ui-pg-div').tooltip({container:'body'});
}

function reloadGridData(){
	var name = $("#name").val();
	$("#grid-table-model").jqGrid('setGridParam', {
	    page : 1,
	    url: context+'/activiti/processModel.do?method=getProcessModelByPage&name='+encodeURI(encodeURI(name)),
	    datatype : "json"
	}).trigger("reloadGrid");
}

/**
 * 修改
 * @param modelId
 */
function modifyModel(modelId){
	window.location.href = context+"/service/editor?id="+modelId;
}

/**
 * 部署
 * @param modelId
 */
function deployModel(modelId){
	$.ajax({
		url: context+'/activiti/processModel.do',
		type: 'post',
		data: {modelId:modelId,method:'deploy'},
		dataType: 'json',
		success: function(data){
			if(data.code == '0'){
				layer.alert("部署成功！", 1);
				reloadGridData();
			}else{
				layer.alert("部署失败！", 3);
			}
		},
		error: function(){
			layer.alert("访问服务器出错！", 3);
		}
	});
	
}
/**
 * 导出
 * @param modelId
 */
function exportModel(modelId){
	window.location.href = context+"/activiti/processModel.do?method=exportBpmnFile&modelId="+modelId;
}

function arrToStr(arr){
	if(arr == null || arr.length==0) return "";
	var str = "";
	for(var i=0;i<arr.length;i++){
		str += arr[i]+",";
	}
	return str.substring(0,str.length-1);
}

/**
 * 判断是否含有中文
 * @param str
 * @returns {Boolean}
 */
function isChinese(str){
	if((/[\u4e00-\u9fa5]+/).test(str)){
		return true;
	}else{
		return false;
	}
}


