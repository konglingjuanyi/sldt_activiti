$(document).ready(function(){
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	jQuery(grid_selector).jqGrid({
		url: context+'/flow.do?method=declareList',
		datatype: "json",
		height: 400,
		regional: 'cn', 
		colNames:[ '申报流水号','流程类型','申报名称','申报描述', '提交申报时间', '提交申报用户','流程ID', '申报单状态', '修改者','修改时间'],
		colModel:[
			{name:'decId',index:'decId', width:120, sorttype:"int", editable: true, hidden:true},
			{name:'type',index:'type', width:80, sorttype:"int", editable: true},
			{name:'decName',index:'decName',width:90, editable:true, sorttype:"date"},
			{name:'decDesc',index:'decDesc', width:150,editable: true,editoptions:{size:"20",maxlength:"30"}},
			{name:'altOperDate',index:'altOperDate', width:70, editable: true,edittype:"checkbox",editoptions: {value:"Yes:No"}},
			{name:'altUser',index:'altUser', width:50, editable: true,edittype:"select",editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"}},
			{name:'actId',index:'actId', width:50, sortable:false,editable: true,edittype:"textarea", editoptions:{rows:"2",cols:"10"},hidden:true},
			{name:'actSts',index:'actSts', width:50, editable: true,edittype:"checkbox",editoptions: {value:"Yes:No"}},
			{name:'lastModifier',index:'lastModifier', width:50, editable: true,edittype:"select",editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"},hidden:true},
			{name:'lastModifyTime',index:'lastModifyTime', width:150, sortable:false,editable: true,edittype:"textarea", editoptions:{rows:"2",cols:"10"},hidden:true} 
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
			
			var ids = $(grid_selector).getDataIDs();  
			for ( var i=0; i<ids.length;i++ ){                 
	            var val = $(grid_selector).getCell( ids[i],"actSts" ),
	                actId = $(grid_selector).getCell( ids[i],"actId" )
	            var bkcolor = "#FFFFFF",
	                ftcolor = "#000",
	                disRes =  val;
	            switch (val) {
	    		case '0':
	    			ftcolor ='blue',
	    			disRes='未启动';
	    		break;
	    		case '1':
	    			ftcolor ='green',
	    			disRes='执行中';
	    		break;
	    		case '2':
	    			ftcolor ='green',
	    			disRes='审批通过';
	    		break;
	    		case '3':
	    			ftcolor ='red',
	    			disRes='审批回退';
	    		break;
	    		case '4':
	    			ftcolor ='red',
	    			disRes='审批失败';
	    		break;
	    		case '5':
	    			ftcolor ='#FCD209',
	    			disRes='撤销';
	    		break;
	    		case '6':
	    			ftcolor ='red',
	    			disRes='无效';
	    		break;
	    	   }
	            var atag = '<a class="flowTrace" href="#" actSts='+val+'  pid="'+actId+'" title="点击查看流程图">'+disRes+'</a>'
    			$(grid_selector).jqGrid('setCell',ids[i],'actSts',atag,{color:ftcolor});
	        };
	    	$('.flowTrace').click(graphTrace); 
		},
		autowidth: true

	});
	
	jQuery(grid_selector).jqGrid('navGrid',pager_selector,{edit:false,add:false,del:false,refresh:false,search:false});
	
	//绑定点击事件
	$('#seachDeclare').click(function() {
		reloadGridData();
    });
	
	$('#cancel').click(function() {
		cancel();
    });

});


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
	var decName = $("#decName").val(),
	     actSts = $("#actSts").val();
	actSts = actSts=='all'?'':actSts;
	$("#grid-table").jqGrid('setGridParam', {
	    page : 1,
	    url: context+'/flow.do?method=declareList&decName='+encodeURI(encodeURI(decName))+'&actSts='+actSts,
	    datatype : "json"
	}).trigger("reloadGrid");
}

function cancel(){
	var ids = $("#grid-table").jqGrid("getGridParam", "selarrrow");
	if(ids.length != 1){
		layer.alert("请选择一条记录进行操作！",0);
		return false;
	}
	actId = $("#grid-table").getCell( ids[0],"actId" ),
	decId = $("#grid-table").getCell( ids[0],"decId" ),
	type = $("#grid-table").getCell( ids[0],"type" )
	var actSts = $("#grid-table").jqGrid('getRowData',ids[0]).actSts;
	var index = actSts.indexOf("actSts");
	actSts = actSts.substring(index+8,index+9);
	if(actSts != 1){
		layer.alert("流程不处于执行中，不可撤销！",0);
		return false;
	}
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'finishProcess',actId:actId,decId:decId,type:type},
		dataType: 'json',
		success: function(){
			layer.alert("撤销成功！",1);
			$("#grid-table").jqGrid('setGridParam', {
			    page : 1,
			    url:'/dmc/flow.do?method=declareList',
			    datatype : "json"
			}).trigger("reloadGrid");
		},
		error: function(){
			layer.alert("撤销失败！",3);
		}
	});
}