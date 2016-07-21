$(document).ready(function(){
        /*展开收起*/
        toggle();
        //下面是文本框提示信息用到
        tip("TEXTAREA,input[focucmsg]");
    	//初始化头部
    	inintTopVersionInfo();
        //初始化列表信息
        initVerList();
    //   debugger;
    	sweetTitles.setTipElements('.tab_tr_width_01 font');
    	sweetTitles.init();
    	initTextField();
});

var bgcolor1 = "#FFFFFF";//白色
var bgcolor2 = "#FFE7DB";//对比色

function initTextField(){
	var tabUrl = context+'/' + 'metadataVer.do?method=comptableAutoComplete&dbId='
		+dbId+'&start=0'+'&limit=5'
		+"&Col_1="+Col_1+"&Col_3="+Col_1+"&Col_4="+Col_4+"&Col_5="+Col_5+"&Col_2="+Col_2;
	var colUrl = context+'/' + 'metadataVer.do?method=compcolumnAutoComplete&dbId='
	+dbId+'&start=0'+'&limit=5'
	+"&Col_1="+Col_1+"&Col_3="+Col_1+"&Col_4="+Col_4+"&Col_5="+Col_5+"&Col_2="+Col_2;
	searchDiv("columnPageDiv", "search_colId", "colsubmit",colUrl);
	searchDiv("tablePageDiv", "ser_cmp_tab_searchTable", "tabsubmit",tabUrl);
    searchDiv("columnTablePageDiv", "ser_cmp_col_searchTable", "coltabsubmit",tabUrl);
}

/**
 * 初始化比对信息
 */
function initVerList(){
	//初始化数据库信息
	initCompDataBase();
	//初始化模式信息
	initCompShema();
	//初始化模块信息
	initCompModule();
	var noSameTable = $(".isTabChange").attr("checked");
	var noSameCol = $(".isColChange").attr("checked");
	//初始化表信息
	initCompTable('','','',noSameTable,0,15);
	//初始化字段信息
	initCompColumn('','','','',noSameCol,0,15);
}

/*****************************************头部版本信息 begin**********************/


/**
 * 初始化头部信息
 */
function inintTopVersionInfo(){
	//debugger;
	//1.第一列
	var col1 = "";
	var col2 = "";
	var col3 = "";
	if(Col_1==''){
		$("#fullTxt").show();
	}else{
		if(Col_1.indexOf('PRO')>=0||Col_1.indexOf('UAT')>=0){
			col1 = '<div class="cmp_font_04" id="vname1">'+c1_Name+'</div>';
		}else{
			col1 = 
				'<div class="cmp_font_04" id="vname1">'+c1_Name+'</div>'+
	            '<div class="cmp_font_04" id="vdate1">'+Col_1+'</div>';
		}
/*		         	'<div class="cmp_font_05"> <a href="javascript:deleteVersionInfo(\'#fullTxt\',\'#hideTxt\')"><font class="cmp_font_05">移除</font></a></div>';
*/				$("#hideTxt").html(col1);
	}
	
	if(Col_2==''){
		$("#fullTxt2").show();
	}else{
		if(Col_2.indexOf('PRO') >= 0||Col_2.indexOf('UAT') >= 0){
			col2 = '<div class="cmp_font_04" id="vname2">'+c2_Name+'</div>';
		}else{
			col2 = 
				'<div class="cmp_font_04" id="vname2">'+c2_Name+'</div>'+
	            '<div class="cmp_font_04" id="vdate2">'+Col_2+'</div>';
		}
		col2 += '<div class="cmp_font_05"> <a href="javascript:deleteVersionInfo(\'#fullTxt2\',\'#hideTxt2\')"><font class="cmp_font_05">移除</font></a></div>';
		$("#hideTxt2").html(col2);
		$("#hideTxt2").show(col2);
	}
	if(Col_3==''){
		$("#fullTxt3").show();
	}else{
		if(Col_3.indexOf('PRO') >= 0||Col_3.indexOf('UAT') >= 0){
			col3 = '<div class="cmp_font_04" id="vname3">'+c3_Name+'</div>';
		}else{
			col3 = 
				'<div class="cmp_font_04" id="vname3">'+c3_Name+'</div>'+
	            '<div class="cmp_font_04" id="vdate3">'+Col_3+'</div>';
		}
		col3 += '<div class="cmp_font_05"> <a href="javascript:deleteVersionInfo(\'#fullTxt3\',\'#hideTxt3\')"><font class="cmp_font_05">移除</font></a></div>';
		$("#hideTxt3").html(col3);
		$("#hideTxt3").show(col3);
	}
	
	}

/**
 * 移除版本
 * @param show
 * @param hide
 */
function deleteVersionInfo(show,hide){
	if(show=='#fullTxt'){
		Col_1 = '';
		$("#ver1").val('请选择版本');
	}else if(show=='#fullTxt2'){
		$("#ver2").val('请选择版本');
		Col_2 = '';
	}else if(show=='#fullTxt3'){
		$("#ver3").val('请选择版本');
		Col_3 = '';
	}else if(show=='#fullTxt4'){
		$("#ver4").val('请选择版本');
		Col_4 = '';
	}else if(show=='#fullTxt5'){
		$("#ver5").val('请选择版本');
		Col_5 = '';
	}
	
	initVerList();
	$(show).show();
	$(hide).hide();
}

/**
 * 添加版本
 * @param show
 * @param hide
 */
function addVersionInfo(show,hide){
	if(hide=='#fullTxt'){
		if($("#ver1").val()=='请选择版本'){
			layer.alert('请选择版本');
			return;
		}
	}else if(hide=='#fullTxt2'){
		if($("#ver2").val()=='请选择版本'){
			layer.alert('请选择版本');
			return;
		}
	}else if(hide=='#fullTxt3'){
		if($("#ver3").val()=='请选择版本'){
			layer.alert('请选择版本');
			return;
		}
	}else if(hide=='#fullTxt4'){
		if($("#ver4").val()=='请选择版本'){
			layer.alert('请选择版本');
			return;
		}
	}else if(hide=='#fullTxt5'){
		if($("#ver5").val()=='请选择版本'){
			layer.alert('请选择版本');
			return;
		}
	}
	initVerList();
	/*debugger;*/
	$(show).show();
	$(hide).hide();
}

/**
 * 选择版本
 * @param sysId
 * @param verId
 */
function chooseVer(textId){
	//将窗口居中
	makeCenter();
	getSysVersion(textId);
}

function getSysVersion(textId){
	//原先的列表清空
	$('#choose-a-version').html('');
	$.ajax({
		url:context+"/metadata.do?method=getSysMetadataVersion",
		method: 'POST',
		async:false,
		dataType :'json',
		data:{dbId:dbId,sysName:sysName,verType:Col_1},
		success: function(response) {
		var _dateques = response.data;
		var ver = getSysAllVersion(_dateques.data);
		$("#choose-a-version").html(ver); 
		//添加列表项的click事件
		$('.version-item').bind('click', function(){
			var item=$(this);
			//debugger;
			var verId = item.attr('ver-id');
			var showverId = verId;
			//生产和快照没有版本日期
			if(verId.indexOf('PRO') >= 0||verId.indexOf('UAT') >= 0){
				showverId = "";
			}
			var verName = item.attr('ver-name');
			//debugger;
			if(verId==Col_4||verId==Col_5||verId==Col_3||verId==Col_2||verId==Col_1){
				layer.alert('该版本已添加比较，请选择其他版本！');
				return;
			}
			//更新选择文本框中的值
			if(textId=='#ver4'){
				$("#vname4").html(verName);
				$("#vdate4").html(showverId);
				Col_4 = verId;
			}else if(textId=='#ver5'){
				$("#vname5").html(verName);
				$("#vdate5").html(showverId);
				Col_5 = verId;
			}else if(textId=='#ver3'){
				$("#vname3").html(verName);
				$("#vdate3").html(showverId);
				Col_3 = verId;
			}else if(textId=='#ver1'){
				$("#vname1").html(verName);
				$("#vdate1").html(showverId);
				Col_1 = verId;
			}else if(textId=='#ver2'){
				$("#vname2").html(verName);
				$("#vdate2").html(showverId);
				Col_2 = verId;
			}
			$(textId).val(item.text());
			
			//关闭弹窗
			hide();
		});
		},fail : function(){
			alert("load fail!");
		}
	});
}

//隐藏窗口
function hide(){
	$('#choose-box-wrapper').css("display","none");
}

//居中
function makeCenter(){
	$('#choose-box-wrapper').css("display","block");
	$('#choose-box-wrapper').css("position","absolute");
	$('#choose-box-wrapper').css("top", Math.max(0, (($(window).height() - $('#choose-box-wrapper').outerHeight()) / 2) + $(window).scrollTop()) + "px");
	$('#choose-box-wrapper').css("left", Math.max(0, (($(window).width() - $('#choose-box-wrapper').outerWidth()) / 2) + $(window).scrollLeft()) + "px");
}

/*****************************************头部版本信息 end************************/

/*****************************************数据库信息 begin***********************/

/**
 * 数据库比对
 */
function initCompDataBase(){
	$.ajax({
		url:context+"/metadataVer.do?method=metadataVerCompDataBase",
		method: 'POST',
		async:false,
		dataType :'json',
		data:{dbId:initDbId,Col_1:Col_1,Col_2:Col_2,Col_3:Col_3,Col_4:Col_4,Col_5:Col_5},
		success: function(response) {
			//debugger;
		var _dateques = response.data;
		
		var tab = $("#dataBase_tab_info");
		tab.empty();//初始化
		//第一列显示的信息
		var title = ['数据库标识','数据库中文名称','所属开发科室','开发负责人','一线维护人员','DBA','数据库描述',
		             '开发商','可用总大小','已用大小','总表数','总对象数'];
		//1.添加第一列
		addTabTitle(tab,title);
		if(_dateques.col1.length==0){//第一列没有添加,不比对
			addCol(_dateques.col1,_dateques.col1);
			addCol(_dateques.col2,_dateques.col2);
			addCol(_dateques.col3,_dateques.col3);
			addCol(_dateques.col4,_dateques.col4);
			addCol(_dateques.col5,_dateques.col5);
		}else{
			addCol(_dateques.col1,_dateques.col1);
			addCol(_dateques.col2,_dateques.col1);
			addCol(_dateques.col3,_dateques.col1);
			
			addCol(_dateques.col4,_dateques.col1);
			addCol(_dateques.col5,_dateques.col1);
			
		}
		mouseTabRowsColorId('#dataBase_tab_info');
		},fail : function(){
			alert("load fail!");
		}
	});
}
/**
 * 动态添加列(数据库)
 */
function addCol(obj,obj2){
	//debugger;
	$("#dataBase_tab_info tr").each(function(no){
	var trHtml = $(this).html();
	var tdText = '';
	var bgColor = bgcolor1;
	if(obj.length!=0){
		//debugger;
		if(no==0){
			tdText = obj[0].DB_CODE;
			if(obj[0].DB_CODE!=obj2[0].DB_CODE){
				bgColor=bgcolor2;
			}
		}else if(no==1){
			tdText = obj[0].DBCHNAME;
			if(obj[0].DBCHNAME!=obj2[0].DBCHNAME){
				bgColor=bgcolor2;
			}
		}else if(no==2){
			tdText = obj[0].DEPT;
			if(obj[0].DEPT!=obj2[0].DEPT){
				bgColor=bgcolor2;
			}
		}else if(no==3){
			tdText = obj[0].DEVLOPER;
			if(obj[0].DEVLOPER!=obj2[0].DEVLOPER){
				bgColor=bgcolor2;
			}
		}else if(no==4){
			tdText = obj[0].MAINTAINER;
			if(obj[0].MAINTAINER!=obj2[0].MAINTAINER){
				bgColor=bgcolor2;
			}
		}else if(no==5){
			tdText = obj[0].DBA;
			if(obj[0].DBA!=obj2[0].DBA){
				bgColor=bgcolor2;
			}
		}else if(no==6){
			tdText = obj[0].REMARK;
			if(obj[0].REMARK!=obj2[0].REMARK){
				bgColor=bgcolor2;
			}
		}else if(no==7){
			tdText = obj[0].PRO_M_FAC;
			if(obj[0].PRO_M_FAC!=obj2[0].PRO_M_FAC){
				bgColor=bgcolor2;
			}
		}else if(no==8){
			tdText = obj[0].TOTALSIZE;
			if(obj[0].TOTALSIZE!=obj2[0].TOTALSIZE){
				bgColor=bgcolor2;
			}
		}else if(no==9){
			tdText = obj[0].USEDSIZE;
			if(obj[0].USEDSIZE!=obj2[0].USEDSIZE){
				bgColor=bgcolor2;
			}
		}else if(no==10){
			tdText = obj[0].TABCNT;
			if(obj[0].TABCNT!=obj2[0].TABCNT){
				bgColor=bgcolor2;
			}
		}else if(no==11){
			tdText = obj[0].OBJCNT;
			if(obj[0].OBJCNT!=obj2[0].OBJCNT){
				bgColor=bgcolor2;
			}
		}
	}
	trHtml += "<td class=\"tab_tr_width_02\" bgcolor=\""+bgColor+"\">"+tdText+"</td>";
	$(this).html(trHtml);
	});
	 
	}


/*****************************************数据库信息 end***********************/


/******************************比较schema begin*********************************/
/**
 * 初始化schema
 */
function initCompShema(){
	$.ajax({
		url:context+"/metadataVer.do?method=initCompShema",
		method: 'POST',
		//async:false,
		dataType :'json',
		data:{dbId:initDbId,Col_1:Col_1,Col_2:Col_2,Col_3:Col_3,Col_4:Col_4,Col_5:Col_5},
		success: function(response) {
			var _dateques = response.data;
			if(_dateques.sch.length==0){
				return;
			}
			//第一列
			var tab = $("#schema_tab_list");
			tab.html('');
			initSchmaSearch(_dateques.sch);
			for(var j=0;j<_dateques.sch.length;j++){
				var rows = "<tr><td class=\"tab_tr_width_01\">"+_dateques.sch[j].schname+"</td></tr>";
				tab.append(rows);
			}
			//debugger;
			var sch = _dateques.sch;
			var sch1 = _dateques.sch1;
			var sch2 = _dateques.sch2;
			var sch3 = _dateques.sch3;
			var sch4 = _dateques.sch4;
			var sch5 = _dateques.sch5;
			var tabId = "#schema_tab_list"; 
			try {
				if(sch1.length==0){//不用比较，第一列被移除
					compCol(tabId,sch,sch1,true);
					compCol(tabId,sch,sch2,true);
					compCol(tabId,sch,sch3,true);
					compCol(tabId,sch,sch4,true);
					compCol(tabId,sch,sch5,true);
				}
				else{
					compCol(tabId,sch,sch1,true);
					compCol(tabId,sch,sch2,false);
					compCol(tabId,sch,sch3,false);
					compCol(tabId,sch,sch4,false);
					compCol(tabId,sch,sch5,false);
				}
			} catch (e) {
			}	
			//1、模式双击行
		    $("#schema_tab_list tr").click(function() {//dblclick
		     $('.schemaList').hide();
		     $('.schemaInfo').show();
		     var schema = $(this).children("td:first").html();
		     $("#schema_title_name").html("<a href=\"javascript:changeTitle('schema_title_name','模式信息','schemaList','schemaInfo')\">模式信息</a>&nbsp|&nbsp<font class=\"cmp_font_08\">"+schema+"</font>");
		     loadSchDetail(schema);
		    });
		    mouseTabRowsColor('schemaList');
			//debugger;
		},fail : function(){
			alert("load fail!");
		}
	});
}

/**
 * 模式对比详情
 * @param schema
 */
function loadSchDetail(schema){
	$.ajax({
		url:context+"/metadataVer.do?method=getMetaCompSchDetail",
		method: 'POST',
		async:false,
		dataType :'json',
		data:{dbId:dbId,schemaName:schema,Col_1:Col_1,Col_2:Col_2,Col_3:Col_3,Col_4:Col_4,Col_5:Col_5},
		success: function(response) {
			var _dateques = response.data;
			//debugger;
			var tab = $("#schema_tab_info");
			tab.empty(); 
			//第一列显示的信息
			var title = ['模式名','模式中文名','开发负责人','模式描述','已用大小','总表数','总对象数'];
			//1.添加第一列
			addTabTitle(tab,title);
			var c1 = _dateques.sch1;
			var c2 = _dateques.sch2;
			var c3 = _dateques.sch3;
			var c4 = _dateques.sch4;
			var c5 = _dateques.sch5;
			//debugger;
			try {
				if(Col_1==''){//不比对
					addColSchInfo(c1,c1);
					addColSchInfo(c2,c2);
					addColSchInfo(c3,c3);
					addColSchInfo(c4,c4);
					addColSchInfo(c5,c5);
				}else{
					addColSchInfo(c1,c1);
					if(Col_2==''){
						addColSchInfo(c2,c2);
					}else{
						addColSchInfo(c2,c1);
					}
					if(Col_3==''){
						addColSchInfo(c3,c3);
					}else{
						addColSchInfo(c3,c1);
					}
					if(Col_4==''){
						addColSchInfo(c4,c4);
					}else{
						addColSchInfo(c4,c1);
					}
					if(Col_5==''){
						addColSchInfo(c5,c5);
					}else{
						addColSchInfo(c5,c1);
					}
				}
			} catch (e) {
				
			}
			
		},fail : function(){
			alert("load fail!");
		}
	});
}

function addColSchInfo(obj,obj2){
	$("#schema_tab_info tr").each(function(no){
		//debugger;
		var trHtml = $(this).html();
		var tdText = '';
		var bgColor = bgcolor1;
		if(no==0){
			tdText = obj.schname;
			if(tdText!=obj2.schname){
				bgColor=bgcolor2;
			}
		}else if(no==1){
			tdText = obj.schChName;
			if(tdText!=obj2.schChName){
				bgColor=bgcolor2;
			}
		}else if(no==2){
			tdText = obj.devloper;
			if(tdText!=obj2.devloper){
				bgColor=bgcolor2;
			}
		}else if(no==3){
			tdText = obj.remark;
			if(tdText!=obj2.remark){
				bgColor=bgcolor2;
			}
		}else if(no==4){
			tdText = obj.usedSize;
			if(tdText!=obj2.usedSize){
				bgColor=bgcolor2;
			}
		}else if(no==5){
			tdText = obj.tabCnt;
			if(tdText!=obj2.tabCnt){
				bgColor=bgcolor2;
			}
		}else if(no==6){
			tdText = obj.objCnt;
			if(tdText!=obj2.objCnt){
				bgColor=bgcolor2;
			}
		}
		trHtml += "<td class=\"tab_tr_width_02\" bgcolor=\""+bgColor+"\">"+tdText+"</td>";
		$(this).html(trHtml);
	});
}


/**************************************比较模式schema end*************************/



/**************************************比较模块信息 begin*************************/
/**
 * 模块列表初始化
 */
function initCompModule(){
	$.ajax({
		url:context+"/metadataVer.do?method=initCompModule",
		method: 'POST',
		//async:false,
		dataType :'json',
		data:{dbId:initDbId,Col_1:Col_1,Col_2:Col_2,Col_3:Col_3,Col_4:Col_4,Col_5:Col_5},
		success: function(response) {
			var _dateques = response.data;
			if(_dateques.col.length==0){
				return;
			}
			//第一列
			var tab = $("#module_tab_list");
			tab.empty(); 
			for(var j=0;j<_dateques.col.length;j++){
				var rows = "<tr>" +
/*						"<td class=\"tab_tr_width_01\">"+_dateques.col[j].schname+"/"+_dateques.col[j].modName+"</td>" +
*/						"<td class=\"tab_tr_width_01\">"+_dateques.col[j].modName+"</td>" +
						"<td>"+_dateques.col[j].modName+"</td>" +
						"<td>"+_dateques.col[j].schname+"</td>" +
						"</tr>";
				tab.append(rows);
			}
			initModuleSearch(_dateques.col);
			$("#module_tab_list tr td:nth-child(2)").hide(); //隐藏第二列 
			$("#module_tab_list tr td:nth-child(3)").hide(); //隐藏第三列 
			//debugger;
			var col = _dateques.col;
			var col1 = _dateques.col1;
			var col2 = _dateques.col2;
			var col3 = _dateques.col3;
			var col4 = _dateques.col4;
			var col5 = _dateques.col5;
			var tabId = "#module_tab_list";
			//debugger;
			try {
				if(col1.length==0){//不用比较，第一列被移除
					compCol(tabId,col,col1,true);
					compCol(tabId,col,col2,true);
					compCol(tabId,col,col3,true);
					compCol(tabId,col,col4,true);
					compCol(tabId,col,col5,true);
				}
				else{
					compCol(tabId,col,col1,true);
					compCol(tabId,col,col2,false);
					compCol(tabId,col,col3,false);
					compCol(tabId,col,col4,false);
					compCol(tabId,col,col5,false);
				}
			
			} catch (e) {
				// TODO: handle exception
			}
			//双击行触发事件
			$(".moduleList table tr").click(function() {   
		         $('.moduleList').hide();
		         $('.moduleInfo').show();
		         var module = $(this).children("td:first").html();
		         var moduleName = $(this).children("td:eq(1)").html();
		         var schema = $(this).children("td:eq(2)").html();
		         $("#module_title_name").html("<a href=\"javascript:changeTitle('module_title_name','模块信息','moduleList','moduleInfo')\">模块信息</a>&nbsp|&nbsp<font class=\"cmp_font_08\">"+module+"</font>");
			     loadModDetail(moduleName,schema);
			});
		     mouseTabRowsColor('moduleList');
		},fail : function(){
			alert("load fail!");
		}
	});

}

/**
 * 模块对比详细
 */
function loadModDetail(modName,schema){
	$.ajax({
		url:context+"/metadataVer.do?method=getMetaCompModDetail",
		method: 'POST',
		async:false,
		dataType :'json',
		data:{dbId:dbId,modName:modName,schemaName:schema,Col_1:Col_1,Col_2:Col_2,Col_3:Col_3,Col_4:Col_4,Col_5:Col_5},
		success: function(response) {
			var _dateques = response.data;
			//debugger;
			var tab = $("#module_tab_info");
			tab.empty();//初始化
			//第一列显示的信息
			var title = ['模块名','模块中文名称','所属模式','所属机构','分管室经理','开发负责联系人','SA','模块识别表达式','使用状态','模式描述','已用大小','总表数','总对象数'];
			//1.添加第一列
			addTabTitle(tab,title);
			var c1 = _dateques.col1;
			var c2 = _dateques.col2;
			var c3 = _dateques.col3;
			var c4 = _dateques.col4;
			var c5 = _dateques.col5;
			//debugger;
			try {
				if(Col_1==''){//不比对
					addColModInfo(c1,c1);
					addColModInfo(c2,c2);
					addColModInfo(c3,c3);
					addColModInfo(c4,c4);
					addColModInfo(c5,c5);
				}else{
					addColModInfo(c1,c1);
					if(Col_2==''){
						addColModInfo(c2,c2);
					}else{
						addColModInfo(c2,c1);
					}
					if(Col_3==''){
						addColModInfo(c3,c3);
					}else{
						addColModInfo(c3,c1);
					}
					if(Col_4==''){
						addColModInfo(c4,c4);
					}else{
						addColModInfo(c4,c1);
					}
					if(Col_5==''){
						addColModInfo(c5,c5);
					}else{
						addColModInfo(c5,c1);
					}
				}
				
			
			} catch (e) {
				
			}
			
		},fail : function(){
			alert("load fail!");
		}
	});

}
function addColModInfo(obj,obj2){
	$("#module_tab_info tr").each(function(no){
		//debugger;
		var trHtml = $(this).html();
		var tdText = '';
		var bgColor = bgcolor1;
		if(no==0){
			tdText = obj.modName;
			if(tdText!=obj2.modName){
				bgColor=bgcolor2;
			}
		}else if(no==1){
			tdText = obj.modChName;
			if(tdText!=obj2.modChName){
				bgColor=bgcolor2;
			}
		}else if(no==2){
			tdText = obj.schname;
		}else if(no==3){
			tdText = obj.dept;
			if(tdText!=obj2.dept){
				bgColor=bgcolor2;
			}
		}else if(no==4){
			tdText = obj.deptcharger;
			if(tdText!=obj2.deptcharger){
				bgColor=bgcolor2;
			}
		}else if(no==5){
			tdText = obj.devloper;
			if(tdText!=obj2.devloper){
				bgColor=bgcolor2;
			}
		}else if(no==6){
			tdText = obj.sa;
			if(tdText!=obj2.sa){
				bgColor=bgcolor2;
			}
		}else if(no==7){
			tdText = obj.modPtn;
			if(tdText!=obj2.modPtn){
				bgColor=bgcolor2;
			}
		}else if(no==8){
			tdText = obj.status;
			if(tdText!=obj2.status){
				bgColor=bgcolor2;
			}
		}else if(no==9){
			tdText = obj.remark;
			if(tdText!=obj2.remark){
				bgColor=bgcolor2;
			}
		}else if(no==10){
			tdText = obj.usedSize;
			if(tdText!=obj2.usedSize){
				bgColor=bgcolor2;
			}
		}else if(no==11){
			tdText = obj.tabCnt;
			if(tdText!=obj2.tabCnt){
				bgColor=bgcolor2;
			}
		}else if(no==12){
			tdText = obj.objCnt;
			if(tdText!=obj2.objCnt){
				bgColor=bgcolor2;
			}
		}
		trHtml += "<td class=\"tab_tr_width_02\" bgcolor=\""+bgColor+"\">"+tdText+"</td>";
		$(this).html(trHtml);
	});
}
/**************************************比较模块信息 end***************************/


/**************************************比较表信息 begin***************************/
/**
 * 表信息列表初始化
 */
function initCompTable(schemaId,moduleId,table,noSameTable,start,limit){
	$.ajax({
		url:context+"/metadataVer.do?method=initCompTable",
		method: 'POST',
		dataType :'json',
		data:{
			dbId:initDbId,
			start:start,
			limit:limit,
			schemaId:schemaId,
			moduleId:moduleId,
			table:table,
			noSameTable:noSameTable,
			Col_1:Col_1,
			Col_2:Col_2,
			Col_3:Col_3,
			Col_4:Col_4,
			Col_5:Col_5
			},
		beforeSend: function(){
			$("#table_tab_list").empty();
			//正在请求的图片
			$('#tabLoading').html("<img src="+context+"/page/mp/imgs/loding.gif>");
		},
		success: function(response) {
			$('#tabLoading').html("");
			$(".tab_total_page").html(response.totalPages);//显示总页数
	        $(".tab_current_page").html(response.currPage);//当前的页数
			//$('#tablePages').html(response.currPage+"\/"+response.totalPages);
			var _dateques = response.data;
			if(_dateques.col.length==0){
				return;
			}
			//第一列
			var tab = $("#table_tab_list");
			//debugger;
			tab.empty();
			for(var j=0;j<_dateques.col.length;j++){
				var tabName = _dateques.col[j].TABNAME;
				var showTabName = (tabName.length > 22? tabName.toString().substring(0,22)+"..." : tabName);

				var rows = "<tr>" +
/*						"<td class=\"tab_tr_width_01\">"+_dateques.col[j].schname+"/"+_dateques.col[j].modName+"/"+_dateques.col[j].tabName+"</td>" +
*/						"<td class=\"tab_tr_width_01\"><font title=\""+tabName+"\">"+showTabName+"</font></td>" +
						"<td>"+_dateques.col[j].MODNAME+"</td>" +
						"<td>"+_dateques.col[j].SCHNAME+"</td>" +
						"<td>"+tabName+"</td>" +
						"</tr>";
				tab.append(rows);
			}
			$("#table_tab_list tr td:nth-child(2)").hide(); //隐藏第二列 
			$("#table_tab_list tr td:nth-child(3)").hide(); //隐藏第三列 
			$("#table_tab_list tr td:nth-child(4)").hide(); //隐藏第四列 
			//debugger;
			var col = _dateques.col;
			var col1 = _dateques.col1;
			var col2 = _dateques.col2;
			var col3 = _dateques.col3;
			var col4 = _dateques.col4;
			var col5 = _dateques.col5;
			//debugger;
			var tabId = "#table_tab_list";
			try {
			
				if(col1.length==0){//不用比较，第一列被移除
					compCol(tabId,col,col1,true);
					compCol(tabId,col,col2,true);
					compCol(tabId,col,col3,true);
					compCol(tabId,col,col4,true);
					compCol(tabId,col,col5,true);
				}
				else{
					compCol(tabId,col,col1,true);
					compCol(tabId,col,col2,false);
					compCol(tabId,col,col3,false);
					compCol(tabId,col,col4,false);
					compCol(tabId,col,col5,false);
				}
			
			} catch (e) {
				// TODO: handle exception
			}
			//双击行触发事件
			 $(".tableList table tr").click(function() {   
		         $('.tableList').hide();
		         $('#tablePageDiv').hide();
		         $('.tableInfo').show();
		         var table = $(this).children("td:eq(3)").html();
		         $("#table_title_name").html("<a href=\"javascript:changeTitle('table_title_name','表信息','tableList','tableInfo','tablePageDiv')\">表信息</a>&nbsp|&nbsp<font class=\"cmp_font_08\">"+table+"</font>");
		         var moduleName = $(this).children("td:eq(1)").html();
		         var schema = $(this).children("td:eq(2)").html();
		         var table = $(this).children("td:eq(3)").html();
		         loadTabDetail(moduleName,schema,table);
			 });
		        mouseTabRowsColor('tableList');
		},fail : function(){
			alert("load fail!");
		}
	});

}

/**
 * 表对比详细
 */
function loadTabDetail(modName,schema,table){
	$.ajax({
		url:context+"/metadataVer.do?method=getMetaCompTabDetail",
		method: 'POST',
		async:false,
		dataType :'json',
		data:{
			dbId:dbId,
			modName:modName,
			schemaName:schema,
			tabName:table,
			Col_1:Col_1,Col_3:Col_3,Col_2:Col_2,Col_4:Col_4,Col_5:Col_5},
		success: function(response) {
			var _dateques = response.data;
			var tab = $("#table_tab_info");
			tab.html("");//初始化
			//第一列显示的信息
			var title = ['表名','表中文名','所属表空间','主键字段','外键字段','外键关联表','记录数',
			             '是否关键表','描述','压缩描述','生命周期说明','分区数','表大小','创建日期','最后更新日期'];
			//1.添加第一列
			addTabTitle(tab,title);
		//	debugger;
			var c1 = _dateques.col1;
			var c2 = _dateques.col2;
			var c3 = _dateques.col3;
			var c4 = _dateques.col4;
			var c5 = _dateques.col5;
			try {
				if(Col_1==''){//不比对
					addColTabInfo(c1,c1);
					addColTabInfo(c2,c2);
					addColTabInfo(c3,c3);
					addColTabInfo(c4,c4);
					addColTabInfo(c5,c5);
				}else{
					addColTabInfo(c1,c1);
					if(Col_2==''){
						addColTabInfo(c2,c2);
					}else{
						addColTabInfo(c2,c1);
					}
					if(Col_3==''){
						addColTabInfo(c3,c3);
					}else{
						addColTabInfo(c3,c1);
					}
					if(Col_4==''){
						addColTabInfo(c4,c4);
					}else{
						addColTabInfo(c4,c1);
					}
					if(Col_5==''){
						addColTabInfo(c5,c5);
					}else{
						addColTabInfo(c5,c1);
					}
				}
			
			} catch (e) {
				
			}
			
		},fail : function(){
			alert("load fail!");
		}
	});

}

function addColTabInfo(obj,obj2){
	$("#table_tab_info tr").each(function(no){
		//debugger;
		var trHtml = $(this).html();
		var tdText = '';
		var bgColor = bgcolor1;
		if(no==0){
			tdText = obj.tabName;
			if(tdText!=obj2.tabName){
				bgColor=bgcolor2;
			}
		}else if(no==1){
			tdText = obj.tabChName;
			if(tdText!=obj2.tabChName){
				bgColor=bgcolor2;
			}
		}else if(no==2){
			tdText = obj.tabspaceName;
			if(tdText!=obj2.tabspaceName){
				bgColor=bgcolor2;
			}
		}else if(no==3){
			tdText = obj.pkCols;
			if(tdText!=obj2.pkCols){
				bgColor=bgcolor2;
			}
		}else if(no==4){
			tdText = obj.fkCols;
			if(tdText!=obj2.fkCols){
				bgColor=bgcolor2;
			}
		}else if(no==5){
			tdText = obj.fkTableName;
			if(tdText!=obj2.fkTableName){
				bgColor=bgcolor2;
			}
		}else if(no==6){
			tdText = obj.rowCount;
			if(tdText!=obj2.rowCount){
				bgColor=bgcolor2;
			}
		}else if(no==7){
			tdText = obj.impFlag;
			if(tdText!=obj2.impFlag){
				bgColor=bgcolor2;
			}
		}else if(no==8){
			tdText = obj.remark;
			if(tdText!=obj2.remark){
				bgColor=bgcolor2;
			}
		}else if(no==9){
			tdText = obj.zipDesc;
			if(tdText!=obj2.zipDesc){
				bgColor=bgcolor2;
			}
		}else if(no==10){
			tdText = obj.lcycDesc;
			if(tdText!=obj2.lcycDesc){
				bgColor=bgcolor2;
			}
		}else if(no==11){
			tdText = obj.pCnt;
			if(tdText!=obj2.pCnt){
				bgColor=bgcolor2;
			}
		}
		else if(no==12){
			tdText = obj.tSize;
			if(tdText!=obj2.tSize){
				bgColor=bgcolor2;
			}
		}
		else if(no==13){
			tdText = obj.crtDate;
			if(tdText!=obj2.crtDate){
				bgColor=bgcolor2;
			}
		}else if(no==14){
			tdText = obj.modiyDate;
			if(tdText!=obj2.modiyDate){
				bgColor=bgcolor2;
			}
		}
		trHtml += "<td class=\"tab_tr_width_02\" bgcolor=\""+bgColor+"\">"+tdText+"</td>";
		$(this).html(trHtml);
	});
}


/**************************************比较表信息 end*****************************/



/**************************************比较字段信息 bedin*****************************/

/**
 * 字段列表初始化
 */
function initCompColumn(schemaId,moduleId,table,column,noSameCol,start,limit){
	$.ajax({
		url:context+"/metadataVer.do?method=initCompColumn",
		method: 'POST',
		dataType :'json',
		beforeSend: function(){
			$("#column_tab_list").empty();
			//正在请求的图片
			$('#colLoading').html("<img src="+context+"/page/mp/imgs/loding.gif>");
			//debugger;
		},
		data:{
			dbId:initDbId,
			Col_1:Col_1,
			Col_3:Col_3,
			Col_2:Col_2,
			Col_4:Col_4,
			Col_5:Col_5,
			schemaId:schemaId,
			moduleId:moduleId,
			table:table,
			column:column,
			noSameCol:noSameCol,
			start:start,
			limit:limit
			},
		success: function(response) {
			$('#colLoading').html("");
			$(".col_total_page").html(response.totalPages);//显示总页数
	        $(".col_current_page").html(response.currPage);//当前的页数
			var _dateques = response.data;
			if(_dateques.col.length==0){
				return;
			}
			//第一列
			var tab = $("#column_tab_list");
			tab.empty();
			for(var j=0;j<_dateques.col.length;j++){
				//debugger;
				var colName = _dateques.col[j].COLNAME;
				var showColName = (colName.length > 22? colName.toString().substring(0,22)+"..." : colName);
				var rows = "<tr>" +
/*						"<td class=\"tab_tr_width_01\">"+_dateques.col[j].schname+"/"+_dateques.col[j].modName+"/"+
						_dateques.col[j].tabName+"/"+_dateques.col[j].colName+"</td>" +*/
						"<td class=\"tab_tr_width_01\"><font title=\""+colName+"\">"+showColName+"</font></td>" +
						"<td>"+_dateques.col[j].MODNAME+"</td>" +
						"<td>"+_dateques.col[j].SCHNAME+"</td>" +
						"<td>"+_dateques.col[j].TABNAME+"</td>" +
						"<td>"+_dateques.col[j].COLNAME+"</td>" +
						"</tr>";
				tab.append(rows);
			}
			$("#column_tab_list tr td:nth-child(2)").hide(); //隐藏第二列 
			$("#column_tab_list tr td:nth-child(3)").hide(); //隐藏第三列 
			$("#column_tab_list tr td:nth-child(4)").hide(); //隐藏第四列 
			$("#column_tab_list tr td:nth-child(5)").hide(); //隐藏第五列 
			//debugger;
			var col1 = _dateques.col1;
			var col2 = _dateques.col2;
			var col3 = _dateques.col3;
			var col4 = _dateques.col4;
			var col5 = _dateques.col5;
			var col = _dateques.col;
			var tabId = "#column_tab_list";
			
			try {
				if(col1.length==0){//不用比较，第一列被移除
					compCol(tabId,col,col1,true);
					compCol(tabId,col,col2,true);
					compCol(tabId,col,col3,true);
					compCol(tabId,col,col4,true);
					compCol(tabId,col,col5,true);
				}
				else{
					compCol(tabId,col,col1,true);
					compCol(tabId,col,col2,false);
					compCol(tabId,col,col3,false);
					compCol(tabId,col,col4,false);
					compCol(tabId,col,col5,false);
				}
			
				} catch (e) {
				// TODO: handle exception
			}
			   //双击行触发事件：字段信息
		    $(".columnList table tr").click(function() {   
		         $('.columnList').hide();
		         $('#columnPageDiv').hide();
		         $('.columnInfo').show();
		         var column = $(this).children("td:eq(4)").html();
		         $("#column_title_name").html("<a href=\"javascript:changeTitle('column_title_name','字段信息','columnList','columnInfo','columnPageDiv')\">字段信息</a>&nbsp|&nbsp<font class=\"cmp_font_08\">"+column+"</font>");
		         var moduleName = $(this).children("td:eq(1)").html();
		         var schema = $(this).children("td:eq(2)").html();
		         var table = $(this).children("td:eq(3)").html();
		         var column = $(this).children("td:eq(4)").html();
		         loadColumnDetail(moduleName,schema,table,column);  
		    });
		     mouseTabRowsColor('columnList');//
			 
		},fail : function(){
			alert("load fail!");
		}
	});

}

/**
 * 字段对比详细
 */
function loadColumnDetail(modName,schema,table,column){
	//alert(table);
	$.ajax({
		url:context+"/metadataVer.do?method=getMetaCompColumnDetail",
		method: 'POST',
		async:false,
		dataType :'json',
		data:{
			dbId:dbId,
			modName:modName,
			schemaName:schema,
			tabName:table,
			colName:column,
			Col_1:Col_1,Col_3:Col_3,Col_2:Col_2,Col_4:Col_4,Col_5:Col_5
			},
		success: function(response) {
			var _dateques = response.data;
			var tab = $("#column_tab_info");
			tab.html("");//初始化
			//第一列显示的信息
			var title = ['字段英文名','字段中文名','字段类型','字段序号','主键','是否允许空值','是否代码字段',
			             '是否有索引','引用代码表','字段描述'];
			//1.添加第一列
			addTabTitle(tab,title);
			//debugger;
			
			var c1 = _dateques.col1;
			var c2 = _dateques.col2;
			var c3 = _dateques.col3;
			var c4 = _dateques.col4;
			var c5 = _dateques.col5;
			try {
				if(Col_1==''){//不比对
					addColColumnInfo(c1,c1);
					addColColumnInfo(c2,c2);
					addColColumnInfo(c3,c3);
					addColColumnInfo(c4,c4);
					addColColumnInfo(c5,c5);
				}else{
					addColColumnInfo(c1,c1);
					if(Col_2==''){
						addColColumnInfo(c2,c2);
					}else{
						addColColumnInfo(c2,c1);
					}
					if(Col_3==''){
						addColColumnInfo(c3,c3);
					}else{
						addColColumnInfo(c3,c1);
					}
					if(Col_4==''){
						addColColumnInfo(c4,c4);
					}else{
						addColColumnInfo(c4,c1);
					}
					if(Col_5==''){
						addColColumnInfo(c5,c5);
					}else{
						addColColumnInfo(c5,c1);
					}
				}
			
			} catch (e) {
				
			}
		},fail : function(){
			alert("load fail!");
		}
	});

}

function addColColumnInfo(obj,obj2){
	$("#column_tab_info tr").each(function(no){
		//debugger;
		var trHtml = $(this).html();
		var tdText = '';
		var bgColor = bgcolor1;
		if(no==0){
			tdText = obj.colName;
			if(tdText!=obj2.colName){
				bgColor=bgcolor2;
			}
		}else if(no==1){
			tdText = obj.colChName;
			if(tdText!=obj2.colChName){
				bgColor=bgcolor2;
			}
		}else if(no==2){
			tdText = obj.colType;
			if(tdText!=obj2.colType){
				bgColor=bgcolor2;
			}
		}else if(no==3){
			tdText = obj.colSeq;
			if(tdText!=obj2.colSeq){
				bgColor=bgcolor2;
			}
		}else if(no==4){
			tdText = obj.pkFlag;
			if(tdText!=obj2.pkFlag){
				bgColor=bgcolor2;
			}
		}else if(no==5){
			tdText = obj.nvlFlag;
			if(tdText!=obj2.nvlFlag){
				bgColor=bgcolor2;
			}
		}else if(no==6){
			tdText = obj.ccolFlag;
			if(tdText!=obj2.ccolFlag){
				bgColor=bgcolor2;
			}
		}else if(no==7){
			tdText = obj.indxFlag;
			if(tdText!=obj2.indxFlag){
				bgColor=bgcolor2;
			}
		}else if(no==8){
			tdText = obj.codeTab;
			if(tdText!=obj2.codeTab){
				bgColor=bgcolor2;
			}
		}else if(no==9){
			tdText = obj.remark;
			if(tdText!=obj2.remark){
				bgColor=bgcolor2;
			}
		}
		trHtml += "<td class=\"tab_tr_width_02\" bgcolor=\""+bgColor+"\">"+tdText+"</td>";
		$(this).html(trHtml);
	});
}




/**************************************比较字段信息 end*****************************/

/**
 * 初始化schema查询添加下拉
 */
function initSchmaSearch(schs){
	//debugger;
	if(schs.length!=0){
		$("#tab_schema_selectId").html('');
		$("#col_schema_selectId").html('');
		$("#tab_schema_selectId").append("<option value=''>全部</option>");
		$("#col_schema_selectId").append("<option value=''>全部</option>");
		for(var i=0;i<schs.length;i++){
			$("#tab_schema_selectId").append("<option value='"+schs[i].schname+"'>"+schs[i].schname+"</option>");
			$("#col_schema_selectId").append("<option value='"+schs[i].schname+"'>"+schs[i].schname+"</option>");
		}
	}
}
/**
 * 初始化模块查询添加下拉
 */
function initModuleSearch(schs){
	//debugger;
	if(schs.length!=0){
		$("#tab_module_selectId").html('');
		$("#col_module_selectId").html('');
		$("#tab_module_selectId").append("<option value=''>全部</option>");
		$("#col_module_selectId").append("<option value=''>全部</option>");
		for(var i=0;i<schs.length;i++){
			$("#tab_module_selectId").append("<option value='"+schs[i].modName+"'>"+schs[i].modName+"</option>");
			$("#col_module_selectId").append("<option value='"+schs[i].modName+"'>"+schs[i].modName+"</option>");
		}
	}
}



/*****************************公共部分******************************************/

/**
 * 模式对比列表
 * @param tabId 表格Id
 * @param sch 全部 
 * @param sch1 各个列
 * @param flag 是否需要对比
 */
function compCol(tabId,sch,sch1,flag){
	var c1 = new Array();
	var bgcolor = new Array();
	if(sch1.length!=0){
		for(var i=0;i<sch1.length;i++){
			if(sch1[i].has=='0'){
				c1.push("<img src=\""+context+"/page/mp/imgs/gc_06-2.png\"/>");
			}else{
				c1.push("<img src=\""+context+"/page/mp/imgs/gc_03.png\"/>");
			}
			if(flag){//不比较
				bgcolor.push(bgcolor1);
			}else{
				if(sch1[i].mod=='0'){
					bgcolor.push(bgcolor1);
				}else{
					bgcolor.push(bgcolor2);
				}
			}
		}
	}else{
		for(var j=0;j<sch.length;j++){
				c1.push("&nbsp;");
				bgcolor.push(bgcolor1);
		}
	}
	
	addColumnList(tabId,c1,bgcolor);

}
/**
 * 表格部分查询
 * @param type
 * @returns {Boolean}
 */
function searchTable(type){
	//debugger;
	var schemaId = $("#tab_schema_selectId").val();
	var moduleId = $("#tab_module_selectId").val();
	var table = $("#ser_cmp_tab_searchTable").val();
	var noSameTable = $(".isTabChange").prop("checked");
	//debugger;
	var current_page = $(".tab_current_page").text();//当前页数
	var totle = $(".tab_total_page").text();//总页数
	var start = 0;
	var limit = 15;
	if(type=='prev'){//上一页
		if(current_page==1){
			return;
		}
	   start = limit* (current_page-2);//起始范围
	}
	if(type=="next"){
		if(current_page==totle){
            return false;//如果大于总页数就禁用下一页
        }
		 ++current_page;
		 start = limit* (current_page-1);//起始范围
	}
	initCompTable(schemaId,moduleId,table,noSameTable,start,limit);
	
	}

/**
 * 字段部分查询
 * @param type
 * @returns {Boolean}
 */
function searchColumn(type){
	//debugger;
	var schemaId = $("#col_schema_selectId").val();
	var moduleId = $("#col_module_selectId").val();
	var table = $("#ser_cmp_col_searchTable").val();
	var column = $("#search_colId").val();
	var noSameCol = $(".isColChange").prop("checked");
	//debugger;
	var current_page = $(".col_current_page").text();//当前页数
	var totle = $(".col_total_page").text();//总页数
	var start = 0;
	var limit = 15;
	if(type=='prev'){//上一页
		if(current_page==1){
			return;
		}
	   start = limit* (current_page-2);//起始范围
	}
	if(type=="next"){
		if(current_page==totle){
            return false;//如果大于总页数就禁用下一页
        }
		 ++current_page;
		 start = limit* (current_page-1);//起始范围
	}
	initCompColumn(schemaId,moduleId,table,column,noSameCol,start,limit);
	
	}

/**
 * tab动态添加列
 * @param tabId
 * @param tds
 */
function addColumnList(tabId,tds,bgColor){
	var id = tabId+" tr";
	$(id).each(function(no){
		var trHtml = $(this).html();
		var tdText = tds[no];
		var color = bgColor[no];
		trHtml += "<td class=\"tab_tr_width_02\" bgcolor=\""+color+"\">"+tdText+"</td>";
		$(this).html(trHtml);
	});
}



/**
 * 双击详情时的表格头
 */
function addTabTitle(tab,titleList){
	for(var j=0;j<titleList.length;j++){
		var rows = "<tr><td class=\"tab_tr_width_01\">"+titleList[j]+"</td></tr>";
		tab.append(rows);
	}
}

/**
 * 鼠标放上去表格行的颜色改变
 * @param divClass
 */
function mouseTabRowsColor(divClass){
	   $("."+divClass+" table tr").mouseover(function(){  //."+divClass+" table tr:gt(0)
	        //如果鼠标移到class为stripe的表格的tr上时，执行函数  
	        $(this).addClass("over");}).mouseout(function(){  
	            //给这行添加class值为over，并且当鼠标一出该行时执行函数  
	            $(this).removeClass("over");
	            }); //移除该行的class 
}


/**
 * 展开收起
 */
function toggle(){
	 $("#toggle").click(function() {
         $(this).html($(".dataBaseInfo").is(":hidden") ? "<img src=\""+context+"/page/mp/imgs/gf_vcomp_shouqi.png\"/>&nbsp;收起"+"" : "<img src=\""+context+"/page/mp/imgs/gf_vcomp_zhankai.png\"/>&nbsp;展开"+"");
         $(".dataBaseInfo").slideToggle("slow");
     });
     $("#toggle2").click(function() {
        $(this).html($(".schema").is(":hidden") ? "<img src=\""+context+"/page/mp/imgs/gf_vcomp_shouqi.png\"/>&nbsp;收起"+"" : "<img src=\""+context+"/page/mp/imgs/gf_vcomp_zhankai.png\"/>&nbsp;展开"+"");
     	$(".schema").slideToggle("slow");
     });
     $("#toggle3").click(function() {
        $(this).html($(".module").is(":hidden") ? "<img src=\""+context+"/page/mp/imgs/gf_vcomp_shouqi.png\"/>&nbsp;收起"+"" : "<img src=\""+context+"/page/mp/imgs/gf_vcomp_zhankai.png\"/>&nbsp;展开"+"");
     	$(".module").slideToggle("slow");
     });
     $("#toggle4").click(function() {
        $(this).html($(".table").is(":hidden") ? "<img src=\""+context+"/page/mp/imgs/gf_vcomp_shouqi.png\"/>&nbsp;收起"+"" : "<img src=\""+context+"/page/mp/imgs/gf_vcomp_zhankai.png\"/>&nbsp;展开"+"");
     	$(".table").slideToggle("slow");
     });
     $("#toggle5").click(function() {
        $(this).html($(".column").is(":hidden") ? "<img src=\""+context+"/page/mp/imgs/gf_vcomp_shouqi.png\"/>&nbsp;收起"+"" : "<img src=\""+context+"/page/mp/imgs/gf_vcomp_zhankai.png\"/>&nbsp;展开"+"");
     	$(".column").slideToggle("slow");
     });
}
/**
 * 【请选择版本】提示信息
 */
function tip(id){
	  $(id) .each (function(){
      	$(this).val($(this).attr("focucmsg"));
      	$(this).val($(this).attr("focucmsg")).css("color","#979393");
      	$(this).focus(function(){
      	if($(this).val() == $(this).attr("focucmsg"))
      	{
      	$(this).val('');
      	$(this).val('').css("color","#6b6969");
      	}
      	});
      	$(this).blur(function(){
      	if(!$(this).val()){
      	$(this).val($(this).attr("focucmsg"));
      	$(this).val($(this).attr("focucmsg")).css("color","#979393");
      	}
      	});
      	});
}

/**
 * 动态改变现实内容
 * @param div
 * @param name
 * @param show
 * @param hide
 */
function changeTitle(div,name,show,hide,search){
	//debugger;
	 $("#"+div).html(name);//头部
	 $('.'+show).show();//列表
     $('.'+hide).hide();//详细信息
     $('#'+search).show();//查询条件
}


/**
 * 生成分割线
 * @param No 序号
 * @param name 文字
 * @returns {String}
 */
function getTitle(No,name){
	return '<div class="version_type">'+
	'<div class="xd-name">'+name+'</div>'+
	'<div class="apDiv-line"></div> '+
	'</div>';
}
/**
 * 返回系统版本信息
 * @param obj
 * @returns {String}
 */
function getSysAllVersion(obj){
	var data = getEveryVer(obj) ;
	//debugger;
    var ver =  '<div class="version_choose">'+
			'<div style="overflow: hidden;">'+
			'<div class="version_view_sysName">　'+obj.sys.sysName+
			'</div>'+
			'</div>'+
			'<div style="margin-left: 8px">'+
				'<table width="100%" class="version_view_table" align="left">'+
					//<!-- 版本基本信息 -->
				data[1]+
				'</table>'+
			'</div>'+
				//<!-- 更多版本信息 -->
			 '<div class="more_ver">'+
				 '<table width="100%" id="more_ver_01">'+
				  data[0]+
				'</table>'+
			'</div>'+
		'</div>';
	return ver;
}

function getEveryVer(_dateques){
	var more_ver_1 = "";
	if(_dateques.pastList.length>2){
		 more_ver_1 = '<tr height="20">';
	}
	var pastV = '';
	if(_dateques.pastList.length>0){
		for(var i=0;i<_dateques.pastList.length;i++){
			var name = _dateques.pastList[i].VER_NAME;
			var date = _dateques.pastList[i].ALT_VER_DATE_NO;
			if(i<2){//默认显示两个历史
				pastV += "<a href=\"javascript:void(0);\" class=\"version-item\" ver-id=\""+date+"\" ver-name=\""+name+"\">"+name+"【"+date+"】</a>&nbsp;&nbsp;";
			}else{
				more_ver_1 +="<td><a href=\"javascript:void(0);\" class=\"version-item\" ver-id=\""+date+"\" ver-name=\""+name+"\">"+name+"【"+date+"】</a></td>";
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
	
	/*if(_dateques.farVer.length>0){
		var size = _dateques.farVer.length;
		for (var i = 0; i < size; i++) {
			var data = _dateques.farVer[i];
			if(i==0){
				devVer = "<a href=\"javascript:void(0);\" class=\"version-item\" ver-id=\""+data.ALT_VER_DATE_NO+"\" ver-name=\""+data.VER_NAME+"\">"+data.VER_NAME+"【"+data.ALT_VER_DATE_NO+"】</a>&nbsp;&nbsp;";
			}else{
				farVer += "<a href=\"javascript:void(0);\" class=\"version-item\" ver-id=\""+data.ALT_VER_DATE_NO+"\" ver-name=\""+data.VER_NAME+"\">"+data.VER_NAME+"【"+data.ALT_VER_DATE_NO+"】</a>&nbsp;&nbsp;";
			}
		}
	}*/
	var currId = "";
	var currName = "";
	var showInfo = "";
	var morePast = "";
	var showUatInfo = '';
	var uatTime = "";
	var uatName = "";
	var preName = "";
	var preTime = "";
	var predbcode = "";
	/*if(_dateques.curlist.length!=0){
		currId = _dateques.curlist[0].OPT_ID;
		currName = _dateques.curlist[0].VER_NAME;
		dbcode = _dateques.curlist[0].DB_CODE;
		showInfo = currName+"【"+currId+"】";
	}*/
	//debugger;
	if(_dateques.uatList.length!=0){//UAT快照
		//debugger;
		uatTime = _dateques.uatList[0].VER_CREATE_TIME;
		uatName = _dateques.uatList[0].VER_NAME;
		showUatInfo = uatName+"【"+uatTime+"】";
	}
	if(_dateques.devVerList.length>0){//当前开发
		var data = _dateques.devVerList[0];
		currId = data.ALT_VER_DATE_NO;
		currName = data.VER_NAME;
		showInfo = currName+"【"+currId+"】";
	}
	if(_dateques.farVerList.length>0){//远期
		var size = _dateques.farVerList.length;
		for (var i = 0; i < size; i++) {
			var data = _dateques.farVerList[i];
			farVer += "<a href=\"javascript:void(0);\" class=\"version-item\" ver-id=\""+data.ALT_VER_DATE_NO+"\" ver-name=\""+data.VER_NAME+"\">"+data.VER_NAME+"【"+data.ALT_VER_DATE_NO+"】</a>&nbsp;&nbsp;";
		}
	}
	var showpreInfo = "";
	if(_dateques.proVer.length!=0){//生产
		//debugger;
		preTime = _dateques.proVer[0].VER_CREATE_TIME;
		preName = _dateques.proVer[0].VER_NAME;
		predbcode = _dateques.proVer[0].DB_CODE;
		showpreInfo = preName+"【"+preTime+"】";
	}
	if(more_ver_1!=""){
		morePast = '<td height="29" colspan="2" align="right"><a href="javascript:showHideMoreVer()" class="moreVer">更多历史版本</a></td>';
	}

	var tr = 
		'<tr >'+
	    '<td class="version_view_table_font">当前开发版本：</td>'+
	    "<td>&nbsp;<a href=\"javascript:void(0);\" class=\"version-item\" ver-id=\""+currId+"\" ver-name=\""+currName+"\">"+showInfo+"</a>"+'</td>'+
	    '</tr>'+
		'<tr >'+
	    '<td class="version_view_table_font">生产快照：</td>'+
	    "<td>&nbsp;<a href=\"javascript:void(0);\" class=\"version-item\" ver-id=\""+dbId+"_PRO"+"\"' ver-name=\""+preName+"\">"+showpreInfo+"</a>"+'</td>'+
	    '</tr>'+
	    '<tr>'+
	    '<td class="version_view_table_font">UAT快照：</td>'+
	    "<td>&nbsp;<a href=\"javascript:void(0);\" class=\"version-item\" ver-id=\""+dbId+"_UAT"+"\"' ver-name=\""+uatName+"\">"+showUatInfo+"</a>"+'</td>'+
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
	    	+morePast+
	    ' </tr>';
	var json = [];
	json.push(more_ver_1);
	json.push(tr);
	return json;
}
function adjustDbId(p_dbId){
	if(p_dbId==null){
		return "";
	}
	if(p_dbId.length<=4){
		return p_dbId;
	}
	var endStr=p_dbId.substring(p_dbId.length-4);
	if(endStr=="_UAT"||endStr=="_PRO"){
		return p_dbId.substring(0,p_dbId.length-4);
	}else{
		return  p_dbId;
	}
	
}

function showHideMoreVer(){
	$(".moreVer").html($(".more_ver").is(":hidden") ? "收起"+"" : "更多历史版本"+"");
    $(".more_ver").slideToggle("slow");
}

/*function noSameTable(objCheck){
	alert(objCheck);
}*/

$(function(){
	 if ($.support) {
	  $('input:checkbox').click(function () {
	   this.blur();  
	   this.focus();
	  });  
	 };
	 
	 $(".isTabChange").change(function() {
		 /*if(Col_1==''){
			 alert('请选择需要比较的版本!');
			 if(this.checked){ this.checked=false; } 
			 return;
		 }*/
			 searchTable();
	 });
	 $(".isColChange").change(function() {
		 searchColumn();
	 });
	}); 