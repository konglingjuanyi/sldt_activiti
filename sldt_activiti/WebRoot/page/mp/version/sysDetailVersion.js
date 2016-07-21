$(document).ready(function(){
	//隔行变色
changeTableBackground('verModuleData');
changeTableBackground('verTableData');
changeTableBackground('verColumnData');
});
/*********************************************通过系统ID、版本ID查询模式信息***********************************/
var getVerSchemaCode = function(_item){
	var str = "";
	str = str + "<td style=\"width:100px;\">"+_item.schname+"</td>";
	return str;
};
var getVerSchemaName = function(_item){
	var str = "";
	str = str + "<td style=\"width:100px;\">"+_item.schChName+"</td>";
	return str;
};

$.ajax({
	url:context+'/' + 'metadataVer.do?method=verMetaSchemaItem&isnews=true&dbId='+dbId+'&optId='+optId,
	method: 'POST',
	async:false,
	dataType :'json',
	success: function(response) {
		var verMetaItem = response.data;
		var str = "";
		str += "<div class=\"apDiv-bg\">";
		str += "<table class=\"x-bg\";width=\"100%\">";
		
		var code = "";
		code += "<tr>";
		code += "<th scope=\"row\" style=\"width:50px;\">元数据代码</th>";
		
		var name = "";
		name += "<tr>";
		name += "<th scope=\"row\">元数据名称</th>";
        
		for(var i = 0;i<verMetaItem.length;i++){
			var item = verMetaItem[i];
			code = code +  getVerSchemaCode(item);
			name = name +  getVerSchemaName(item);
		}
		code += "</tr>";
		name += "</tr>";
		str += code;
		str += name;
		str += "</table>";
		str += "</div>";
		$("#verSchemaData").html(str);
	},fail : function(){
		alert("cards load fail!");
	}
});

/*********************************************通过系统ID、模式ID查理模块数据***********************************/
var getVerModule = function(_item){
	var str = "";
	str = str + "<tr class=\"\">";
	//str = str + "<td><div style=\"width:50px; height:22px;line-height:22px; white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\"> "+_item.modName+"</div></td>";
	str = str + "<td>"+_item.modName+"</td>";
	str = str + "<td>"+_item.modChName+"</td>";
	str = str + "<td>"+_item.devloper+"</td>";
	str = str + "<td>"+_item.sa+"</td>";
	str = str + "<td>"+_item.modPtn+"</td>";
	str = str + "<td>"+_item.remark+"</td>";
	str = str + "<td>"+_item.tabCnt+"</td>";
	//str = str + "<td><a href="+"javascript:getVerTableData('"+_item.modName+"','"+_item.schname+"',0,15)"+">查看模块表</a></td>";
	str = str + "</tr>";
	return str;
};

$.ajax({
	url:context+'/' + 'metadataVer.do?method=verMetaModuleItem&isnews=true&dbId='+dbId+'&optId='+optId,
	method: 'POST',
	async:false,
	dataType :'json',
	success: function(response) {
		var verMetaItem = response.data;
		var str = "";
		for(var i = 0;i<verMetaItem.length;i++){
			var item = verMetaItem[i];
			str = str +  getVerModule(item);
		}
		$("#verModuleData").html(str);
		mouseTabRowsColorId("#verModuleData");
	},fail : function(){
		alert("cards load fail!");
	}
});


/*********************************************通过系统ID、模式ID、模块ID或许表级数据***********************************/
getVerTableData('', '', 0, 15, '');
function getVerTableData(modName,schemaId,start,limit,searchText){
	var getVerTable = function(_item,i){
		var str = "";
		str = str + "<tr class=\"\" id='list_table"+i+"' abbr=\"/"+_item.dbId+"/"+_item.schname+"/"+_item.modName+"/"+_item.tabName+"\">";
		str = str + "<td><a href=\"#columnInfoId\" onclick="+"getVerColumnData('"+_item.modName+"','"+_item.schname+"','"+_item.tabName+"','','"+_item.tabName+"',0,15)"+">"+_item.tabName+"</a></td>";
		str = str + "<td>"+_item.tabChName+"</td>";
		str = str + "<td>"+_item.tabspaceName+"</td>";
		str = str + "<td>"+_item.pkCols+"</td>";
		str = str + "<td>"+_item.fkCols+"</td>";
		str = str + "<td>"+_item.impFlag+"</td>";
		str = str + "<td>"+_item.lcycDesc+"</td>";
		str = str + "<td>"+_item.remark+"</td>";
		str = str + "<td>/"+_item.dbId+"/"+_item.schname+"/"+_item.modName+"</td>";
		
		str = str + "</tr>";
		return str;
	};
	if(searchText==null){
		searchText="";
	}
	$.ajax({
		url:context+'/' + 'metadataVer.do?method=verMetaTableItem&isnews=true&dbId='+dbId+'&optId='+optId+'&moduleId='+modName+'&schemaId='+schemaId+'&searchText='+searchText+'&start='+start+'&limit='+limit,
		method: 'POST',
		async:false,
		dataType :'json',
		success: function(response) {
			var verMetaItem = response.data.tabData;
			var schemaData =  response.data.schemaData;
			var moduleData =  response.data.moduleData;
			var str = "";
			for(var i = 0;i<verMetaItem.length;i++){
				var item = verMetaItem[i];
				str = str +  getVerTable(item,i);
			}
			$("#verTableData").html(str);
			sysTabContextMenu();
			mouseTabRowsColorId('#verTableData');
			changeTableBackground('verTableData');
			var next = (response.currPage*limit);
			var last = ((response.currPage-2)*limit);
			var pagesDiv = "";
			
				pagesDiv = pagesDiv+" <div class=\"search_tab_schema_div\">模式选择：</div> <div class=\"search_schema_bg_div\">"
			      +" <div><select id=\"schemaSelectId\" class=\"search_select_input\"><option value=''>请选择...</option></select></div>"
			      +" </div>";
				
				pagesDiv = pagesDiv+" <div class=\"search_tab_module_div\">模块选择：</div> <div class=\"search_module_bg_div\">"
			      +" <div><select id=\"moduleSelectId\" class=\"search_select_input\"><option value=''>请选择...</option></select></div>"
			      +" </div>";
				
				pagesDiv = pagesDiv+" <div class=\"search_table_col_div\">表级数据查询：</div> <div class=\"search_table_bg_div\">"
			      +" <div><input type=\"text\" id=\"sysVerTableSearch\" value='"+searchText+"' class=\"search_input\"></div>"
			      +" <div class=\"search_table_btn_div\" id=\"tabsubmit\" style=\"cursor: pointer;\" onclick=\"sysVerSearchByType('table','"+schemaId+"','"+modName+"')\"></div>"
			      +" </div>";
			
			
			pagesDiv = pagesDiv+"<div style=\"border:0px solid red;overflow: hidden;float:right;margin-right:10px;\"><div class=\"search_page_left\" onclick=\"sysVerPageByType('table',"+last+","+response.totalPages+",'','"+searchText+"','"+schemaId+"','"+modName+"')\" style=\"cursor: pointer;\" ></div>"
					    	+" <div class=\"search_page_mid\">"+response.currPage+"/"+response.totalPages+"</div>"
					    	+" <div class=\"search_page_right\" onclick=\"sysVerPageByType('table',"+next+","+response.totalPages+",'','"+searchText+"','"+schemaId+"','"+modName+"')\" style=\"cursor: pointer;\" ></div></div>";
			
			$("#tablePageDiv").html(pagesDiv);
			
			for(var i = 0;i<schemaData.length;i++){
				var item = schemaData[i];
				$("#schemaSelectId").append("<option value='" + item.schname + "'>" + item.schChName + "</option>");
			}
			
			if(schemaId!=''&&schemaId!=null){
				$("#schemaSelectId").find("option[value='"+schemaId+"']").attr("selected",true);
			}
			
			for(var i = 0;i<moduleData.length;i++){
				var item = moduleData[i];
				$("#moduleSelectId").append("<option value='" + item.modName + "'>" + item.modChName + "</option>");
			}
			
			if(modName!=''&&modName!=null){
				$("#moduleSelectId").find("option[value='"+modName+"']").attr("selected",true);
			}
			var ajaxUrl = context+'/' + 'metadata.do?method=tableAutoComplete&dbId='+dbId+'&moduleId='+modName+'&schemaId='+schemaId+'&start='+start+'&limit=5';
			searchDiv("tablePageDiv", "sysVerTableSearch", "tabsubmit",ajaxUrl);
		},fail : function(){
			alert("cards load fail!");
		}
	});
}


/*********************************************通过系统ID、模式ID、模块ID、表ID获字段数据***********************************/
getVerColumnData('', '', '','','', 0, 15);
function getVerColumnData(modName,schemaId,tableId,searchText,tabSearcharText,start,limit){
	var getVerTable = function(_item,i){
		var str = "";
		str = str + "<tr class=\"\" id='list_col"+i+"' refDs='"+_item.dsRef+"' abbr=\"/"+_item.dbId+"/"+_item.schname+"/"+_item.modName+"/"+_item.tabName+"/"+_item.colName+"\">";
		str = str + "<td><a href=\"#colCodefoId\" onclick=\"getVerColCodeData('"+_item.schname+"','"+_item.modName+"','"+_item.tabName+"','"+_item.colName+"','',0,15)\">"+_item.colName+"</a></td>";
		str = str + "<td>"+_item.colChName+"</td>";
		str = str + "<td>"+_item.colType+"</td>";
		str = str + "<td>"+_item.colSeq+"</td>";
		str = str + "<td>"+_item.pkFlag+"</td>";
		str = str + "<td>"+_item.nvlFlag+"</td>";
		str = str + "<td>"+_item.ccolFlag+"</td>";
		str = str + "<td>"+_item.indxFlag+"</td>";
		str = str + "<td>"+_item.codeTab+"</td>";
		str = str + "<td>"+_item.remark+"</td>";
		str = str + "<td>/"+_item.dbId+"/"+_item.schname+"/"+_item.modName+"/"+_item.tabName+"</td>";
		str = str + "</tr>";
		return str;
	};

	if(searchText==null){
		searchText="";
	}
	if(tabSearcharText==null){
		tabSearcharText="";
	}
	$.ajax({
		url:context+'/' + 'metadataVer.do?method=verMetaColumnItem&isnews=true&dbId='+dbId+'&optId='+optId+'&moduleId='+modName+'&schemaId='+schemaId+'&tableId='+tableId+'&searchText='+searchText+'&tabSearcharText='+tabSearcharText+'&start='+start+'&limit='+limit,
		method: 'POST',
		async:false,
		dataType :'json',
		success: function(response) {
			var verMetaItem = response.data.colData;
			var schemaData =  response.data.schemaData;
			var moduleData =  response.data.moduleData;
			var str = "";
			for(var i = 0;i<verMetaItem.length;i++){
				var item = verMetaItem[i];
				str = str +  getVerTable(item,i);
			}
			$("#verColumnData").html(str);
			sysColContextMenu();
			mouseTabRowsColorId('#verColumnData');
			changeTableBackground('verColumnData');
			var next = (response.currPage*limit);
			var last = ((response.currPage-2)*limit);
			
			var pagesDiv = "";
				
				pagesDiv = pagesDiv+" <div class=\"search_col_schema_div\">模式选择：</div> <div class=\"search_col_schema_bg_div\">"
					      +" <div><select id=\"colSchemaSelectId\" class=\"search_col_select_input\"><option value=''>请选择...</option></select></div>"
					      +" </div>";
				
				pagesDiv = pagesDiv+" <div class=\"search_col_module_div\">模块选择：</div> <div class=\"search_col_module_bg_div\">"
					      +" <div><select id=\"colModuleSelectId\" class=\"search_col_select_input\"><option value=''>请选择...</option></select></div>"
					      +" </div>";
				
				
				pagesDiv = pagesDiv +" <div id=\"colTabSearDiv\"><div class=\"search_col_table_div\" >表级数据查询：</div> <div class=\"search_col_table_bg_div\">"
					      +" <div><input type=\"text\" id=\"colSysVerTableSearch\"  value='"+tabSearcharText+"' class=\"search_input\"></div>"
					      +" <div  id=\"colTabsubmit\" ></div>"
					      +" </div></div>";
				
				
				pagesDiv = pagesDiv +" <div class=\"search_col_div\">字段级数据查询：</div> <div class=\"search_bg_div\">"
					      +" <div><input type=\"text\" id=\"sysVerColumnSearch\"  value='"+searchText+"' class=\"search_input\"></div>"
					      +" <div class=\"search_btn_div\" style=\"cursor: pointer;\" id=\"colsubmit\" onclick=\"sysVerSearchByType('column','"+schemaId+"','"+modName+"','"+tableId+"')\"></div>"
					      +" </div>";
				
					pagesDiv = pagesDiv + "<div class=\"pageDiv\" style=\"\"><div class=\"search_col_page_left\" onclick=\"sysVerPageByType('column',"+last+","+response.totalPages+",'"+searchText+"','"+tabSearcharText+"','"+schemaId+"','"+modName+"','"+tableId+"')\" style=\"cursor: pointer;\" ></div>"
					    	+"<div class=\"search_col_page_mid\">"+response.currPage+"/"+response.totalPages+"</div>"
							+" <div class=\"search_col_page_right\" onclick=\"sysVerPageByType('column',"+next+","+response.totalPages+",'"+searchText+"','"+tabSearcharText+"','"+schemaId+"','"+modName+"','"+tableId+"')\" style=\"cursor: pointer;\" ></div>"
					    	+" </div>";
					
					
			$("#columnPageDiv").html(pagesDiv);
			
			for(var i = 0;i<schemaData.length;i++){
				var item = schemaData[i];
				$("#colSchemaSelectId").append("<option value='" + item.schname + "'>" + item.schChName + "</option>");
			}
			
			if(schemaId!=''&&schemaId!=null){
				$("#colSchemaSelectId").find("option[value='"+schemaId+"']").attr("selected",true);
			}
			
			for(var i = 0;i<moduleData.length;i++){
				var item = moduleData[i];
				$("#colModuleSelectId").append("<option value='" + item.modName + "'>" + item.modChName + "</option>");
			}
			
			if(modName!=''&&modName!=null){
				$("#colModuleSelectId").find("option[value='"+modName+"']").attr("selected",true);
			}
			
			var ajaxUrl = context+'/' + 'metadata.do?method=columnAutoComplete&dbId='+dbId+'&moduleId='+modName+'&schemaId='+schemaId+'&tableId='+tableId+'&start='+start+'&limit=5';
			
			var tabUrl = context+'/' + 'metadata.do?method=tableAutoComplete&dbId='+dbId+'&moduleId='+modName+'&schemaId='+schemaId+'&start='+start+'&limit=5';
			
			searchDiv("columnPageDiv", "sysVerColumnSearch", "colsubmit",ajaxUrl);

			searchDiv("colTabSearDiv", "colSysVerTableSearch", "colTabsubmit",tabUrl);
			
		},fail : function(){
			alert("cards load fail!");
		}
	});
}

/*********************************************通过系统ID、模式ID、模块ID、表ID获字段数据***********************************/
getVerColCodeData('','','','','',0, 15);
function getVerColCodeData(schemaId,modName,tableId,colSearchar,codeSearchar,start,limit){
	var getVerColCode = function(_item,i){
		var str = "";
		str = str + "<tr class=\"\" id='list_code_"+i+"'>";
		str = str + "<td>"+_item.COLNAME+"</td>";
		str = str + "<td>"+_item.CODE_VAL+"</td>";
		str = str + "<td>"+_item.CODE_CH_NAME+"</td>";
		str = str + "<td>"+_item.CODE_DESC+"</td>";
		str = str + "<td>"+_item.CODE_STS+"</td>";
		str = str + "<td>"+_item.CODE_START_DATE+"</td>";
		str = str + "<td>"+_item.CODE_END_DATE+"</td>";
		str = str + "<td>/"+_item.DB_CODE+"/"+_item.SCHNAME+"/"+_item.MODNAME+"/"+_item.TABNAME+"/"+_item.COLNAME+"</td>";
		str = str + "</tr>";
		return str;
	};

	$.ajax({
		url:context+'/' + 'metadataVer.do?method=verColCodeItem&dbId='+dbId+'&optId='+optId+'&start='+start+'&limit='+limit,
		method: 'POST',
		async:false,
		data:{schema:schemaId,module:modName,tableName:tableId,colName:colSearchar,codeVal:codeSearchar},
		dataType :'json',
		success: function(response) {
			var verMetaItem = response.data.codeData;
			var schemaData =  response.data.schemaData;
			var moduleData =  response.data.moduleData;
			var str = "";
			for(var i = 0;i<verMetaItem.length;i++){
				var item = verMetaItem[i];
				str = str +  getVerColCode(item,i);
			}
			$("#verColCodeData").html(str);
			//sysColContextMenu();
			mouseTabRowsColorId('#verColCodeData');
			changeTableBackground('verColCodeData');
			
			var next = (response.currPage*limit);
			var last = ((response.currPage-2)*limit);
			
			var pagesDiv = "";
			
			pagesDiv +="<div class=\"search_col_schema_div\" style=\"left: 7px;\">模式选择：</div>";
			pagesDiv +="<div class=\"search_col_schema_bg_div\"  style=\"left: 134px;\">";
			pagesDiv +=" <div><select id=\"colCodeSchema\"  class=\"search_col_select_input\" ></select></div>";
			pagesDiv +="</div>";
    		
			pagesDiv +="<div class=\"search_col_module_div\" style=\"left: 500px;\">模块选择：</div>";
			pagesDiv +="<div class=\"search_col_module_bg_div\" style=\"left: 623px;\">";
			pagesDiv +=" <div><select id=\"colCodeModule\"  class=\"search_col_select_input\"></select></div>";
			pagesDiv +="</div>";
			
			pagesDiv +="<div id=\"colCodeTabSearDiv\"  style=\"padding-top: 40px;\"><div class=\"search_tab_schema_div\">表查询：</div>";
			pagesDiv +="<div class=\"search_schema_bg_div\" style=\"padding-left: 10px;\">";
			pagesDiv +="<div><input type=\"text\" id=\"colColdeTable\" value='"+tableId+"' style=\"width: 150px;\" class=\"search_input\"></div>";
			pagesDiv +=" <div id=\"colCodeTabsubmit\" style='left:-20px;'></div>";
			pagesDiv +="</div></div>";
    		
			pagesDiv +="<div id=\"colCodeColSearDiv\"><div class=\"search_tab_module_div\" style=\"padding-left: 30px;\">字段查询：</div>";
			pagesDiv +="<div class=\"search_module_bg_div\" style=\"padding-left: 40px;\">";
			pagesDiv +=" <div><input type=\"text\" id=\"colCodeCol\" value='"+colSearchar+"' style=\"width: 150px;\" class=\"search_input\"></div>";
			pagesDiv +=" <div  id=\"colCodesubmit\" ></div>";
			pagesDiv +=" </div></div>";
		    
			pagesDiv +="<div class=\"search_table_col_div\" style=\"padding-left: 60px;\">代码值查询：</div>";
			pagesDiv +="<div class=\"search_table_bg_div\" style=\"padding-left: 10px;\">";
			pagesDiv +="<div><input type=\"text\" name=\"colcode\" style=\"width: 150px;\" class=\"search_input\"></div>";//schemaId,modName,tableId,colSearchar,codeSearchar
			pagesDiv +="<div class=\"search_table_btn_div\"style=\"cursor: pointer;\" onclick=\"sysVerSearchByType('code','"+schemaId+"','"+modName+"','"+tableId+"','"+colSearchar+"','"+codeSearchar+"')\" id=\"codesubmit\"></div>";
			pagesDiv +="</div>";
			
			
			pagesDiv +="<div style=\"border:0px solid red;overflow: hidden;float:right;margin-right:10px;\">";
			pagesDiv +="<div class=\"search_page_left\" style=\"cursor: pointer;\" id=\"codeLastPage\" onclick=\"sysVerPageByType('code',"+last+","+response.totalPages+",'"+schemaId+"','"+modName+"','"+tableId+"','"+colSearchar+"','"+codeSearchar+"')\"></div>";
			pagesDiv +="<div class=\"search_page_mid\" id=\"codePages\">"+response.currPage+"/"+response.totalPages+"</div>";
			pagesDiv +="<div class=\"search_page_right\" id=\"codeNextPage\" style=\"cursor: pointer;\" onclick=\"sysVerPageByType('code',"+next+","+response.totalPages+",'"+schemaId+"','"+modName+"','"+tableId+"','"+colSearchar+"','"+codeSearchar+"')\"></div>";
			pagesDiv +="</div>";
			
			var ajaxUrl = context+'/' + 'metadata.do?method=columnAutoComplete&dbId='+dbId+'&moduleId='+modName+'&schemaId='+schemaId+'&tableId='+tableId+'&start='+start+'&limit=5';
			
			var tabUrl = context+'/' + 'metadata.do?method=tableAutoComplete&dbId='+dbId+'&moduleId='+modName+'&schemaId='+schemaId+'&start='+start+'&limit=5';
			
			searchDiv("colCodeTabSearDiv", "colColdeTable", "colCodeTabsubmit",tabUrl);
			
			searchDiv("colCodeColSearDiv", "colCodeCol", "colCodesubmit",ajaxUrl);

			
			$("#colCodePageDiv").html(pagesDiv);
			
			
			for(var i = 0;i<schemaData.length;i++){
				var item = schemaData[i];
				$("#colCodeSchema").append("<option value='" + item.schname + "'>" + item.schChName + "</option>");
			}
			
			if(schemaId!=''&&schemaId!=null){
				$("#colCodeSchema").find("option[value='"+schemaId+"']").attr("selected",true);
			}
			
			for(var i = 0;i<moduleData.length;i++){
				var item = moduleData[i];
				$("#colCodeModule").append("<option value='" + item.modName + "'>" + item.modChName + "</option>");
			}
			
			if(modName!=''&&modName!=null){
				$("#colCodeModule").find("option[value='"+modName+"']").attr("selected",true);
			}
			
		},fail : function(){
			alert("cards load fail!");
		}
	});
}


/*********************************************我的版本界面分页方法***********************************/
function sysVerPageByType(type,start,totalPages,colSearchText,tabSearchText,schemaId,moduleId,tableId){
	if(start<0){
		return;
	}
	if(start/15>=totalPages){
		return;
	}
	if(type=='table'){
		getVerTableData(moduleId, schemaId, start, 15,tabSearchText);
	}else if(type=='column'){
		getVerColumnData(moduleId, schemaId, tableId,colSearchText,tabSearchText, start, 15);
	}else if(type=='code'){
		//schemaId,modName,tableId,colSearchar,codeSearchar,start,limit
		getVerColCodeData(colSearchText,tabSearchText,schemaId,moduleId,tableId,start,15);
	}
}
/*********************************************我的版本界面查询方法***********************************/
function sysVerSearchByType(type,schemaId,moduleId,tableId){
	var searchText = "";
	var tabSearchText = "";
	if(type=='table'){
		if($("#sysVerTableSearch").val() != 'undefined'&&$("#sysVerTableSearch").val() !=null){
			searchText = $("#sysVerTableSearch").val();
		}
		getVerTableData($("#moduleSelectId").val(), $("#schemaSelectId").val(), 0, 15,searchText);
	}else if(type=='column'){
		if($("#sysVerColumnSearch").val() != 'undefined'&&$("#sysVerColumnSearch").val() !=null){
			searchText = $("#sysVerColumnSearch").val();
		}
		
		if($("#colSysVerTableSearch").val() != 'undefined'&&$("#colSysVerTableSearch").val() !=null){
			tabSearchText = $("#colSysVerTableSearch").val();
		}
		getVerColumnData($("#colModuleSelectId").val(), $("#colSchemaSelectId").val(), tableId,searchText,tabSearchText, 0, 15);
	}else if(type=='code'){
		var colColdeTable = "";
		var colCodeCol = "";
		var colcode = "";
		if($("#colColdeTable").val() != 'undefined'&&$("#colColdeTable").val() !=null){
			colColdeTable = $("#sysVerColumnSearch").val();
		}
		
		if($("#colCodeCol").val() != 'undefined'&&$("#colCodeCol").val() !=null){
			colCodeCol = $("#colCodeCol").val();
		}
		if($("#colcode").val() != 'undefined'&&$("#colcode").val() !=null){
			colcode = $("#colCodeCol").val();
		}
		getVerColCodeData($("#colCodeSchema").val(), $("#colCodeModule").val(), colColdeTable,colCodeCol,colcode, 0, 15);
	}
}

function showFileIntPage(dbId,optId){
	parent.changeMainPage("page/mp/version/pFileIntPage.jsp?dbId="+dbId+"&optId="+optId);

}
