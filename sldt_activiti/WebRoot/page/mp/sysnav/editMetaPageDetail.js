$(document).ready(function(){
	//隔行变色
changeTableBackground('verModuleData');
});

var getCurVerSys = function(_item){
	var str = "";
	_item["ALT_TYPE"]="U";
	var obj = JSON.stringify(_item);
	obj = obj.replace(new RegExp("\"","gm"),"'");
	str = str + " <div id=\"change_04\">";
	str = str + "<div class=\"wb\" id='db_list_ul'>";
	str = str + "<ul class=\"list1\" id=\""+obj+"\">";
	str = str + " <li class=\"cl1\">元数据代码：</li> ";
	str = str + " <li class=\"cl2\">"+_item.DB_CODE+"</li> ";
	str = str + "  <li class=\"cl3\" >元数据名称：</li> ";
	str = str + " <li  class=\"cl4\" id='DBCHNAME' abbr='"+_item.DBCHNAME+"'>"+_item.DBCHNAME+"</li>";
	str = str + " <li  class=\"cl3\">生效时间：</li> ";
	str = str + "  <li  class=\"cl4\">"+_item.curVerOptId+"</li> ";
	str = str + "  <li  class=\"cl1\">所属开发科室：</li>  ";
	str = str + " <li  class=\"cl2\" id='DEPT' abbr='"+_item.DEPT+"'>"+_item.DEPT+"</li>";
	str = str + " <li  class=\"cl3\">DBA：</li>";
	str = str + " <li  class=\"cl4\" id='DBA' abbr='"+_item.DBA+"'>"+_item.DBA+"</li>";
	str = str + "  <li  class=\"cl1\">开发商：</li>  ";
	str = str + " <li  class=\"cl2\" id='PRO_M_FAC' abbr='"+_item.PRO_M_FAC+"'>"+_item.PRO_M_FAC+"</li>";
	str = str + " <li  class=\"cl3\">开发负责人：</li>";
	str = str + " <li  class=\"cl4\" id='DEVLOPER' abbr='"+_item.DEVLOPER+"'>"+_item.DEVLOPER+"</li>";
	str = str + " <li class=\"cl3\">元数据类型：</li>  ";
	str = str + " <li  class=\"cl4\">系统</li> ";
	str = str + " <li  class=\"cl1\" style='height:40px;'>功能简介：</li>  ";
	str = str + "  <li  class=\"tiledcl5\" style='height:40px;' id='REMARK' abbr='"+_item.REMARK+"'>"+_item.REMARK+"</li>";
	
	str = str + " <li class=\"cl1\" style='font-size:12px;color:red;'>元数据统计：</li>  ";
	str = str + " <li class=\"tiledcl5\" ></li> ";
	
	str = str + " <li class=\"cl1\">模式信息：</li>  ";
	str = str + " <li class=\"cl2\" id=\"_schemaCount\">/</li> ";
	str = str + " <li class=\"cl3\">模块信息：</li>  ";
	str = str + " <li  class=\"cl4\" id=\"_moduleCount\"></li> ";
	
	str = str + " <li class=\"cl1\">表级信息：</li>  ";
	str = str + " <li class=\"cl2\" id=\"_tableCount\"></li> ";
	str = str + " <li class=\"cl3\">字段信息：</li>  ";
	str = str + " <li  class=\"cl4\" id=\"_colCount\"></li> ";
	
	str = str + "  </ul>  ";
	str = str + " </div> ";
	str = str + "  </div>  ";

    
	return str;
};

$.ajax({
	url:context+'/' + 'metadataVer.do?method=verMetaItem&dbId='+dbId+'&optId='+optId,
	method: 'POST',
	async:false,
	dataType :'json',
	success: function(response) {
		var verMetaItem = response.data.list;
		if(verMetaItem.length>0){
			var str = "";
			for(var i = 0;i<1;i++){
				var item = verMetaItem[i];
				str = str +  getCurVerSys(item);
				$("#change_02").html(item.DBCHNAME);
				$("#change_02_desc").html(item.REMARK);
				$("#change_04").html(str);
				editMetaUl();
			}
		}
	},fail : function(){
		alert("cards load fail!");
	}
});

/*********************************************通过系统ID、版本ID查询当前版本统计信息***********************************/

$.ajax({
	url:context+'/' + 'metadataVer.do?method=curSysVerCount&dbId='+dbId+'&optId='+optId,
	method: 'POST',
	async:false,
	dataType :'json',
	success: function(response) {
		var verMetaItem = response.data;
		for(var i = 0;i<1;i++){
			var item = verMetaItem[i];
			$("#_colCount").html(item.COLCOUNT);
			$("#_moduleCount").html(item.MODCOUNT);
			$("#_schemaCount").html(item.SCHCOUNT);
			$("#_tableCount").html(item.TABCOUNT);
		}
	},fail : function(){
		alert("cards load fail!");
	}
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
	url:context+'/' + 'metadataVer.do?method=verMetaSchemaItem&dbId='+dbId+'&optId='+optId,
	method: 'POST',
	async:false,
	dataType :'json',
	success: function(response) {
		var verMetaItem = response.data;
		var str = "";
		str += "<div class=\"apDiv-bg\">";
		str += "<table class=\"x-bg\">";
		
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
	_item["ALT_TYPE"]="U";
	var obj = JSON.stringify(_item);
	obj = obj.replace(new RegExp("\"","gm"),"'");
	var str = "";
	str = str + "<tr lang='MODULE' class=\"\" id=\""+obj+"\">";
	str = str + "<td>"+_item.modName+"</td>";
	str = str + "<td id=\"MODCHNAME"+"\" abbr=\""+_item.modChName+"\">"+_item.modChName+"</td>";
	str = str + "<td id=\"DEVLOPER"+"\" abbr=\""+_item.devloper+"\" >"+_item.devloper+"</td>";
	str = str + "<td id=\"SA"+"\" abbr=\""+_item.sa+"\">"+_item.sa+"</td>";
	str = str + "<td id=\"MODPTN"+"\" abbr=\""+_item.modPtn+"\">"+_item.modPtn+"</td>";
	str = str + "<td id=\"REMARK"+"\" abbr=\""+_item.remark+"\">"+_item.remark+"</td>";
	str = str + "<td>"+_item.tabCnt+"</td>";
	str = str + "</tr>";
	return str;
};

$.ajax({
	url:context+'/' + 'metadataVer.do?method=verMetaModuleItem&dbId='+dbId+'&optId='+optId,
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
		mouseTabRowsColorId('#verModuleData');
	},fail : function(){
		alert("cards load fail!");
	}
});

/*********************************************通过系统ID、模式ID、模块ID或许表级数据***********************************/
getVerTableData('', '', 0, 15, '');
function getVerTableData(modName,schemaId,start,limit,searchText){
	var getVerTable = function(_item){
		_item["ALT_TYPE"]="U";
		var obj = JSON.stringify(_item);
		obj = obj.replace(new RegExp("\"","gm"),"'");
		var str = "";
		str = str + "<tr lang='TABLE' class=\"\" id=\""+obj+"\" abbr=\"/"+_item.dbId+"/"+_item.schname+"/"+_item.modName+"/"+_item.tabName+"\">";
		str = str + "<td><a href=\"#columnInfoId\" onclick="+"getVerColumnData('"+_item.modName+"','"+_item.schname+"','"+_item.tabName+"','','"+_item.tabName+"',0,15)"+">"+_item.tabName+"</a></td>";
		str = str + "<td id='TABCHNAME' abbr=\""+_item.tabChName+"\">"+_item.tabChName+"</td>";
		str = str + "<td id='TABSPACENAME' abbr=\""+_item.tabspaceName+"\">"+_item.tabspaceName+"</td>";
		str = str + "<td id='PKCOLS' abbr=\""+_item.pkCols+"\">"+_item.pkCols+"</td>";
		str = str + "<td id='FKCOLS' abbr=\""+_item.fkCols+"\">"+_item.fkCols+"</td>";
		str = str + "<td id='IMPFLAG' abbr=\""+_item.impFlag+"\">"+_item.impFlag+"</td>";
		str = str + "<td id='LCYCDESC' abbr=\""+_item.lcycDesc+"\">"+_item.lcycDesc+"</td>";
		str = str + "<td id='REMARK' abbr=\""+_item.remark+"\">"+_item.remark+"</td>";
		str = str + "<td>/"+_item.dbId+"/"+_item.schname+"/"+_item.modName+"</td>";
		str = str + "</tr>";
		return str;
	};
	if(searchText==null){
		searchText="";
	}
	$.ajax({
		url:context+'/' + 'metadataVer.do?method=verMetaTableItem&dbId='+dbId+'&optId='+optId+'&moduleId='+modName+'&schemaId='+schemaId+'&searchText='+searchText+'&start='+start+'&limit='+limit,
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
				str = str +  getVerTable(item);
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
			
			
			pagesDiv = pagesDiv+"<div class=\"search_page_left\" style='' onclick=\"sysVerPageByType('table',"+last+","+response.totalPages+",'','"+searchText+"','"+schemaId+"','"+modName+"')\" style=\"cursor: pointer;margin-left:  960px;\" ></div>"
							+" <div class=\"search_page_mid\">"+response.currPage+"/"+response.totalPages+"</div>"
					    	+" <div class=\"search_page_right\" id=\"tableNextPage\" onclick=\"sysVerPageByType('table',"+next+","+response.totalPages+",'','"+searchText+"','"+schemaId+"','"+modName+"')\" style=\"cursor: pointer;\" ></div>";
			
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
			editMetaItem();
		},fail : function(){
			alert("cards load fail!");
		}
	});
}


/*********************************************通过系统ID、模式ID、模块ID、表ID获字段数据***********************************/
getVerColumnData('', '', '','','', 0, 15);
function getVerColumnData(modName,schemaId,tableId,searchText,tabSearcharText,start,limit){
	var getVerTable = function(_item){
		_item["ALT_TYPE"]="U";
		var obj = JSON.stringify(_item);
		obj = obj.replace(new RegExp("\"","gm"),"'");
		var str = "";
		str = str + "<tr lang='COLUMN' class=\"\" id=\""+obj+"\"  abbr=\"/"+_item.dbId+"/"+_item.schname+"/"+_item.modName+"/"+_item.tabName+"/"+_item.colName+"\">";
		str = str + "<td>"+_item.colName+"</td>";
		str = str + "<td id='COLCHNAME' abbr=\""+_item.colChName+"\">"+_item.colChName+"</td>";
		str = str + "<td id='COLTYPE'abbr=\""+_item.colType+"\">"+_item.colType+"</td>";
		str = str + "<td id='COLSEQ' abbr=\""+_item.colSeq+"\">"+_item.colSeq+"</td>";
		str = str + "<td id='PKFLAG' abbr=\""+_item.pkFlag+"\">"+_item.pkFlag+"</td>";
		str = str + "<td id='NVLFLAG' abbr=\""+_item.nvlFlag+"\">"+_item.nvlFlag+"</td>";
		str = str + "<td id='CCOLFLAG' abbr=\""+_item.ccolFlag+"\">"+_item.ccolFlag+"</td>";
		str = str + "<td id='INDEXFLAG' abbr=\""+_item.indxFlag+"\">"+_item.indxFlag+"</td>";
		str = str + "<td id='COLDETAB' abbr=\""+_item.codeTab+"\">"+_item.codeTab+"</td>";
		str = str + "<td id='REMARK' abbr=\""+_item.remark+"\">"+_item.remark+"</td>";
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
		url:context+'/' + 'metadataVer.do?method=verMetaColumnItem&dbId='+dbId+'&optId='+optId+'&moduleId='+modName+'&schemaId='+schemaId+'&tableId='+tableId+'&searchText='+searchText+'&tabSearcharText='+tabSearcharText+'&start='+start+'&limit='+limit,
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
				str = str +  getVerTable(item);
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
				
					pagesDiv = pagesDiv + "<div class=\"pageDiv\"><div class=\"search_col_page_left\" onclick=\"sysVerPageByType('column',"+last+","+response.totalPages+",'"+searchText+"','"+tabSearcharText+"','"+schemaId+"','"+modName+"','"+tableId+"')\" style=\"cursor: pointer;\" ></div>"
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
			editMetaItem();
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
	}
}
$(function(){
	sweetTitles.setTipElements('#saveMeta img');//显示的位置：id或者class名称
	sweetTitles.init();
})

