
$(document).ready(function(){
	
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	jQuery(grid_selector).jqGrid({
		url:context+'/metadataVer.do?method=fileIntList&dbId='+dbId+'&optId='+optId+'&refType=1',
		datatype: "json",
		height: 400,
		colNames:[ '系统标识','引用系统标识','引用模式名', '引用模块名', '引用表英文名','接口/功能名', '接口/功能实现说明','关系依赖类型'],
		colModel:[
			{align:'center',name:'DB_CODE',index:'DB_CODE', width:40, sorttype:"int", editable: true},
			{align:'center',name:'REF_DB_CODE',index:'REF_DB_CODE',width:50, editable:true, sorttype:"date"},
			{align:'center',name:'REF_SCHNAME',index:'REF_SCHNAME', width:40,editable: true,editoptions:{size:"20",maxlength:"30"}},
			{align:'center',name:'REF_MODNAME',index:'REF_MODNAME', width:40, editable: true,edittype:"checkbox",editoptions: {value:"Yes:No"}},
			{align:'center',name:'REF_TABNAME',index:'REF_TABNAME', width:100, editable: true,edittype:"select",editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"}},
			{align:'center',name:'C_FUNC_NAME',index:'C_FUNC_NAME', width:150, editable: true,edittype:"select",editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"}},
			{align:'center',name:'C_DESC',index:'C_DESC', width:100, sortable:false,editable: true,edittype:"textarea", editoptions:{rows:"2",cols:"10"}},
			{align:'center',name:'C_TYPE',index:'C_TYPE', width:50, editable: true,edittype:"checkbox",editoptions: {value:"Yes:No"}}
		], 
		viewrecords : true,
		rowNum:15,
		rowList:[15,30,45,60,75,90],
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
});
loadRefDb();
function loadRefDb(){
	var grid_selector = "#grid-table_ref";
	var pager_selector = "#grid-pager_ref";
	jQuery(grid_selector).jqGrid({
		url:context+'/metadataVer.do?method=fileIntList&dbId='+dbId+'&optId='+optId+'&refType=0',
		datatype: "json",
		height: 400,
		colNames:[ '系统标识','引用系统标识','引用模式名', '引用模块名', '引用表英文名','接口/功能名', '接口/功能实现说明','关系依赖类型'],
		colModel:[
					{align:'center',name:'DB_CODE',index:'DB_CODE', width:40, sorttype:"int", editable: true},
					{align:'center',name:'REF_DB_CODE',index:'REF_DB_CODE',width:50, editable:true, sorttype:"date"},
					{align:'center',name:'REF_SCHNAME',index:'REF_SCHNAME', width:40,editable: true,editoptions:{size:"20",maxlength:"30"}},
					{align:'center',name:'REF_MODNAME',index:'REF_MODNAME', width:40, editable: true,edittype:"checkbox",editoptions: {value:"Yes:No"}},
					{align:'center',name:'REF_TABNAME',index:'REF_TABNAME', width:100, editable: true,edittype:"select",editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"}},
					{align:'center',name:'C_FUNC_NAME',index:'C_FUNC_NAME', width:150, editable: true,edittype:"select",editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"}},
					{align:'center',name:'C_DESC',index:'C_DESC', width:100, sortable:false,editable: true,edittype:"textarea", editoptions:{rows:"2",cols:"10"}},
					{align:'center',name:'C_TYPE',index:'C_TYPE', width:50, editable: true,edittype:"checkbox",editoptions: {value:"Yes:No"}}
		], 

		viewrecords : true,
		rowNum:15,
		rowList:[15,30,45,60,75,90],
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
	});
}

function enableTooltips(table) {
	$('.navtable .ui-pg-button').tooltip({container:'body'});
	$(table).find('.ui-pg-div').tooltip({container:'body'});
}


function findFileInt(type){
	if(type=='1'){
		var refDbId = $("#refDbId").val();
		var refSchema = $("#refSchema").val();
		var refModule = $("#refModule").val();
		var refTable = $("#refTable").val();
		var refFuncName = $("#refFuncName").val();
		var cType = $("#cType").val();
		$("#grid-table_ref").jqGrid('setGridParam', {
			page : 1,
			url:context+'/metadataVer.do?method=fileIntList&dbId='+dbId+'&optId='+optId+'&refType=0&refDbId='+refDbId+'&refSchema='+refSchema+'&refModule='+refModule+'&refTable='+refTable+'&refFuncName='+refFuncName+'&cType='+cType,
			datatype : "json"
		}).trigger("reloadGrid");
		
	}else{
		var refDbId = $("#my_refDbId").val();
		var refSchema = $("#my_refSchema").val();
		var refModule = $("#my_refModule").val();
		var refTable = $("#my_refTable").val();
		var refFuncName = $("#my_refFuncName").val();
		var cType = $("#my_cType").val();
		$("#grid-table").jqGrid('setGridParam', {
			page : 1,
			url:context+'/metadataVer.do?method=fileIntList&dbId='+dbId+'&optId='+optId+'&refType=1&refDbId='+refDbId+'&refSchema='+refSchema+'&refModule='+refModule+'&refTable='+refTable+'&refFuncName='+refFuncName+'&cType='+cType,
			datatype : "json"
		}).trigger("reloadGrid");
	}
}

