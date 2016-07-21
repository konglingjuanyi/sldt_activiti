

function showImpExcWin(){

	var html = "";
	html = html+"<div class=\"showDivAltDetail\" style='width:445px;height:340px;font-size:12px;'>";
	html = html+"<div class=\"okAltTile\">";
	html = html+"<b>导入数据字典</b>";
	html = html+"</div>";
	html = html+"<div style='padding-bottom:20px;'>";
	html = html+"<hr style='background-color:#ed1a2e;padding-top:2px;'>";
	html = html+"</div>";
	
	html = html+"<form id=\"uploadimg-form\"  action=\"\" method=\"post\">  ";
	html = html+"<span style='text-align: left;'>导入方式：";
	html = html+"<select id=\"uploadType\" name=\"uploadType\" style=\"width: 105px;\"><option value=''>请选择...</option><option value='0'>增量</option><option value='1'>全量</option><option value='2'>删除</option></select>";
	html = html+"</span>";
	html = html+"<div style='text-align: left;padding-top:20px;'>";
	html = html+"<span>";
	html = html+"当前版本：";
	html = html+"<select id=\"currOptId\" name=\"currOptId\" style=\"width: 108px;\"><option value=''>请选择...</option></select>";
	html = html+"</span>";
	html = html+"<span style='padding-left:20px;'>";
	html = html+"目标投产版本：";
	html = html+"<select id=\"optSelect\" name=\"optSelect\" style=\"width: 108px;\"><option value=''>请选择...</option></select>";
	html = html+"</span>";
	
	html = html+"<div style='text-align: left;padding-top:10px;'>";
	html = html+"<div style='float:left;font-size:13	px;font-family:\"宋体\";'>";
	html = html+"目标文件：";
	html = html+"</div>";
	html = html+"<div style='float:left;'>";
	html = html+"<input style='width:320px;' type='file'  name=\"fileUpload\" id=\"fileUpload\" />";
	html = html+"</div>";
	html = html+"</div>";
	html = html+"</div>";
	html = html+"</form>";
	
	html = html+"<div style='padding-top:40px;height:100px;' id='impMsg'>";
	html = html+="<div><b>请选择导入方式、当前导入版本、目标投产版本日期以及上传的数据字典excel文件！</b></div>";
	html = html+"</div>";
	
	html = html+"<div class='showDivBut' style='padding-top:0px;'>";
	html = html+"<div style='padding-left:120px;float:left;'>";
	html = html+"<button class='but' id='impE' onclick=\"impExc()\">&nbsp;导 入 </button>";
	html = html+"</div>";
	html = html+"<div style='padding-left:20px;float:left;'>";
	html = html+"<button id=\"pagebtn\" class='but' onclick=\"\">&nbsp;取 消  </button>";
	html = html+"</div>";
	html = html+"</div>";
	
	html = html+"</div>";

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
	
	initOptId();
	initCurrOptId();
	//自设关闭
	$('#pagebtn').on('click', function(){
		layer.close(pageii);
	});
}

function impExc(){
	var type = $("#uploadType").val();
	var opt = $("#optSelect").val();
	var currOpt = $("#currOptId").val();
	var file = $("#fileUpload").val();
	if(type==''||type==null){
		layer.alert("请选择导入方式：增量/全量/删除！", 3);
		return;
	}
	if(currOpt==''||currOpt==null){
		layer.alert("请选择当前版本日期！", 3);
		return;
	}
	if(opt==''||opt==null){
		layer.alert("请选择目标投产日期！", 3);
		return;
	}
	if(file==''||file==null){
		layer.alert("请选择上传需要导入的文件！", 3);
		return;
	}
	layer.load('正在提交，请稍后...');
	$("#impE").attr("disabled",true);
    $("#uploadimg-form").ajaxSubmit({  
        url:context+"/metaExc.do?method=impMetaExcel",  
        type:"post",  
        enctype:"multipart/form-data",  
        contentType: "application/x-www-form-urlencoded; charset=utf-8",  
        dataType:"json",  
        success: function(data){  
        },  
        error: function() {  
        	layer.msg("文件上传成功，后台正在处理中，请稍后查看！",1, 1);
        	setTimeout(closeAll,1000);
        	findUploadData();
        }  
    });  
}

function closeAll(){
	layer.closeAll();
}

function initCurrOptId(){
	$.ajax({
		url:context+'/' + 'metadataVer.do?method=getMetaVerDate',
		method: 'POST',
		async:false,
		data:{lastVer:0},
		dataType :'json',
		success: function(response) {
			var objData = response.data;
			for(var i = 0 ; i < objData.length;i++){
				var item = objData[i];
				$("#currOptId").append("<option value='" + item.OPT_ID + "'>" + item.OPT_ID + "</option>");
			}
		},fail : function(){
			alert("cards load fail!");
		}
	});
}

function initOptId(){
	$.ajax({
		url:context+'/' + 'metadataVer.do?method=getMetaVerDate',
		method: 'POST',
		async:false,
		data:{},
		dataType :'json',
		success: function(response) {
			var objData = response.data;
			for(var i = 0 ; i < objData.length;i++){
				var item = objData[i];
				$("#optSelect").append("<option value='" + item.OPT_ID + "'>" + item.OPT_ID + "</option>");
			}
		},fail : function(){
			alert("cards load fail!");
		}
	});
}





function delUploadInfo(id,batchId){
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否确认删除当前上传的变更元数据？',
	        btns: 2,                    
	        type: 4,
	        btn: ['确认','取消'],
	        yes: function(){
	        	$.ajax({
	        		url:context+'/metaExc.do?method=delUploadInfo',
	        		method: 'POST',
	        		async:false,
	        		data:{uploadId:id,batchId:batchId},
	        		dataType :'json',
	        		success: function(response) {
	        			layer.alert(response.data.msg, 0);
	        			$("#grid-table").trigger("reloadGrid");
	        		},fail : function(){
	        			alert("cards load fail!");
	        		}
	        	});
	        }, no: function(){
	        }
	    }
	});
}

function findUploadData(){
	
	var upload_m = $("#upload_m").val();
	var batchId = $("#batchNum").val();
	$("#grid-table").jqGrid('setGridParam', {
		page : 1,
		url:context+'/metaExc.do?method=getUploadList&upload_m='+upload_m+'&batchId='+batchId,
		datatype : "json"
	}).trigger("reloadGrid");
}



$(document).ready(function(){
	
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	jQuery(grid_selector).jqGrid({
		//direction: "rtl",
		url:context+'/metaExc.do?method=getUploadList',
		datatype: "json",
		//data: grid_data,
		//datatype: "local",
		height: 400,
		colNames:[ '批次编号','上传用户','上传时间', '上传文件名称','导入方式', '分析状态','变更元数据结论', '操作'],
		colModel:[
			{align:'center',name:'UPLOAD_BATCH_ID',index:'UPLOAD_BATCH_ID', width:80, sorttype:"int", editable: true},
			{align:'center',name:'UPLOAD_USER',index:'UPLOAD_USER',width:60, editable:true, sorttype:"date"},
			{align:'center',name:'UPLOAD_TIME',index:'UPLOAD_TIME', width:100,editable: true,editoptions:{size:"20",maxlength:"30"}},
			{align:'center',name:'UPLOAD_FIME_NAME',index:'UPLOAD_FIME_NAME', width:140, editable: true,edittype:"checkbox",editoptions: {value:"Yes:No"}},
			{align:'center',name:'UPLOAD_M',index:'UPLOAD_M', width:60, editable: true,edittype:"select",editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"}},
			{align:'center',name:'UPLOAD_STS',index:'UPLOAD_STS', width:60, editable: true,edittype:"select",editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"}},
			{align:'center',name:'UPLOAD_ALT_DESC',index:'UPLOAD_ALT_DESC', width:300, sortable:false,editable: true,edittype:"textarea", editoptions:{rows:"2",cols:"10"}},
			{align:'center',name:'UPLOAD_METHOD',index:'UPLOAD_METHOD', width:50, editable: true,edittype:"checkbox",editoptions: {value:"Yes:No"}}
		], 

		viewrecords : true,
		rowNum:15,
		rowList:[15,30,45,60,75,90],
		pager : pager_selector,
		altRows: true,
		//toppager: true,
		
		multiselect: true,
		//multikey: "ctrlKey",
        multiboxonly: true,
		loadComplete : function() {
			var table = this;
			setTimeout(function(){
				updatePagerIcons(table);
				enableTooltips(table);
			}, 0);
		},
		//editurl: "/dummy.html",//nothing is saved
		//caption: "申报流水表格",
		autowidth: true

	});
	
	jQuery(grid_selector).jqGrid('navGrid',pager_selector,{edit:false,add:false,del:false,refresh:false,search:false});
	
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
