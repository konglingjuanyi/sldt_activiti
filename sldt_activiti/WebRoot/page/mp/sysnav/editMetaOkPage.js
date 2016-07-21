function loadData(){
	var obj = parent.obj;
	var tabStr = "";
	var moduleStr = "";
	var columnStr = "";
	var dbStr = "";
	for(var i = 0 ; i < obj.length ; i ++){
		if(obj[i].db!=null){
			dbStr+=setDbItem(obj[i].db);
		}
		if(obj[i].column!=null){
			columnStr+=setColumnItem(obj[i].column);
		}
		if(obj[i].table!=null){
			tabStr += setTableItem(obj[i].table);
		}
		if(obj[i].module!=null){
			moduleStr+=setModuelItem(obj[i].module);
		}
	}
	$("#change_04").html(dbStr);
	$("#altColumnItem").html(columnStr);
	$("#altTableItem").html(tabStr);
	$("#altModuleItem").html(moduleStr);
	changeTableBackground('altColumnItem');
	mouseTabRowsColorId('#altColumnItem');
	changeTableBackground('altTableItem');
	mouseTabRowsColorId('#altTableItem');
	changeTableBackground('altModuleItem');
	mouseTabRowsColorId('#altModuleItem');
}

function setColumnItem(columnObj){
	var str = "";
	for(var k = 0 ; k < columnObj.length ; k++){
		var _item = eval('(' + columnObj[k].item + ')');
		var columnItemObj = JSON.stringify(_item);
		columnItemObj = columnItemObj.replace(new RegExp("\"","gm"),"'");
		var colChName = _item.colChName;
		var colType = _item.colType;
		var colSeq = _item.colSeq;
		var pkFlag = _item.pkFlag;
		var nvlFlag = _item.nvlFlag;
		var ccolFlag = _item.ccolFlag;
		var indxFlag = _item.indxFlag;
		var codeTab = _item.codeTab;
		var remark = _item.remark;
		str = str + "<tr class=\"data_table_odd\" id=\""+columnItemObj+"\">";
		for(var j = 0 ; j < columnObj[k].alt.length ; j ++){
			if(columnObj[k].alt[j].id=='COLCHNAME'){
				colChName = columnObj[k].alt[j].val;
			}else if(columnObj[k].alt[j].id=='COLTYPE'){
				colType = columnObj[k].alt[j].val;
			}else if(columnObj[k].alt[j].id=='COLSEQ'){
				colSeq = columnObj[k].alt[j].val;
			}else if(columnObj[k].alt[j].id=='PKFLAG'){
				pkFlag = columnObj[k].alt[j].val;
			}else if(columnObj[k].alt[j].id=='NVLFLAG'){
				nvlFlag = columnObj[k].alt[j].val;
			}else if(columnObj[k].alt[j].id=='CCOLFLAG'){
				ccolFlag = columnObj[k].alt[j].val;
			}else if(columnObj[k].alt[j].id=='INDEXFLAG'){
				indxFlag = columnObj[k].alt[j].val;
			}else if(columnObj[k].alt[j].id=='COLDETAB'){
				codeTab = columnObj[k].alt[j].val;
			}else if(columnObj[k].alt[j].id=='REMARK'){
				remark = columnObj[k].alt[j].val;
			}
		}
		str = str + "<td>"+_item.colName+"</td>";
		str = str + "<td id='COLCHNAME' >"+colChName+"</td>";
		str = str + "<td id='COLTYPE'>"+colType+"</td>";
		str = str + "<td id='COLSEQ' >"+colSeq+"</td>";
		str = str + "<td id='PKFLAG' >"+pkFlag+"</td>";
		str = str + "<td id='NVLFLAG' >"+nvlFlag+"</td>";
		str = str + "<td id='CCOLFLAG' >"+ccolFlag+"</td>";
		str = str + "<td id='INDEXFLAG' >"+indxFlag+"</td>";
		str = str + "<td id='COLDETAB' >"+codeTab+"</td>";
		str = str + "<td id='REMARK' >"+remark+"</td>";
		str = str + "<td>/"+_item.dbId+"/"+_item.schname+"/"+_item.modName+"/"+_item.tabName+"</td>";
		str = str + "</tr>";
	}
	return str;
}

function setTableItem(tableObj){
	var str = "";
	for(var k = 0 ; k < tableObj.length ; k++){
		var _item = eval('(' + tableObj[k].item + ')');
		var tableItemObj = JSON.stringify(_item);
		tableItemObj = tableItemObj.replace(new RegExp("\"","gm"),"'");
		var tabChName = _item.tabChName;
		var pkCols = _item.pkCols;
		var fkCols = _item.fkCols;
		var tabspaceName = _item.tabspaceName;
		var impFlag = _item.impFlag;
		var lcycDesc = _item.lcycDesc;
		var remark = _item.remark;
		str = str + "<tr class=\"data_table_odd\" id=\""+tableItemObj+"\">";
		for(var j = 0 ; j < tableObj[k].alt.length ; j ++){
			if(tableObj[k].alt[j].id=='TABCHNAME'){
				tabChName = tableObj[k].alt[j].val;
			}else if(tableObj[k].alt[j].id=='PKCOLS'){
				pkCols = tableObj[k].alt[j].val;
			}else if(tableObj[k].alt[j].id=='FKCOLS'){
				fkCols = tableObj[k].alt[j].val;
			}else if(tableObj[k].alt[j].id=='TABSPACENAME'){
				tabspaceName = tableObj[k].alt[j].val;
			}else if(tableObj[k].alt[j].id=='IMPFLAG'){
				impFlag = tableObj[k].alt[j].val;
			}else if(tableObj[k].alt[j].id=='LCYCDESC'){
				lcycDesc = tableObj[k].alt[j].val;
			}else if(tableObj[k].alt[j].id=='REMARK'){
				remark = tableObj[k].alt[j].val;
			}
		}
		str = str + "<td>"+_item.tabName+"</td>";
		str = str + "<td id='TABCHNAME'>"+tabChName+"</td>";
		str = str + "<td id='TABSPACENAME'>"+tabspaceName+"</td>";
		str = str + "<td id='PKCOLS'>"+pkCols+"</td>";
		str = str + "<td id='FKCOLS'>"+fkCols+"</td>";
		str = str + "<td id='IMPFLAG' >"+impFlag+"</td>";
		str = str + "<td id='LCYCDESC' >"+lcycDesc+"</td>";
		str = str + "<td id='REMARK' >"+remark+"</td>";
		str = str + "<td>/"+_item.dbId+"/"+_item.schname+"/"+_item.modName+"</td>";
		str = str + "</tr>";
	}
	return str;
}

function setModuelItem(moduleObj){
	var str = "";
	for(var k = 0 ; k < moduleObj.length ; k++){
		var _item = eval('(' + moduleObj[k].item + ')');
		var moduleItemObj = JSON.stringify(_item);
		moduleItemObj = moduleItemObj.replace(new RegExp("\"","gm"),"'");
		var modChName = _item.modChName;
		var devloper = _item.devloper;
		var sa = _item.sa;
		var modPtn = _item.modPtn;
		var remark = _item.remark;
		//alert("module----"+obj[i].module[k].alt.length);
		str = str + "<tr class=\"data_table_odd\" id=\""+moduleItemObj+"\">";
		for(var j = 0 ; j < moduleObj[k].alt.length ; j ++){
			if(moduleObj[k].alt[j].id=='MODCHNAME'){
				modChName = moduleObj[k].alt[j].val;
			}else if(moduleObj[k].alt[j].id=='DEVLOPER'){
				devloper = moduleObj[k].alt[j].val;
			}else if(moduleObj[k].alt[j].id=='SA'){
				sa = moduleObj[k].alt[j].val;
			}else if(moduleObj[k].alt[j].id=='MODPTN'){
				modPtn = moduleObj[k].alt[j].val;
			}else if(moduleObj[k].alt[j].id=='REMARK'){
				remark = moduleObj[k].alt[j].val;
			}
		}
		str = str + "<td>"+_item.modName+"</td>";
		str = str + "<td id=\"MODCHNAME"+"\">"+modChName+"</td>";
		str = str + "<td id=\"DEVLOPER"+"\">"+devloper+"</td>";
		str = str + "<td id=\"SA"+"\" >"+sa+"</td>";
		str = str + "<td id=\"MODPTN"+"\" >"+modPtn+"</td>";
		str = str + "<td id=\"REMARK"+"\">"+remark+"</td>";
		str = str + "<td>"+_item.tabCnt+"</td>";
		str = str + "</tr>";
	}
	return str;
}


function setDbItem(dbObjItem){
	var str = "";
	str = str + " <div id=\"change_04\">";
	str = str + "<div class=\"wb\">";
	str = str + "<ul class=\"list1\" style='margin-left: 20px;'>";
	for(var i = 0 ; i < dbObjItem.length ; i++){
		var _item = eval('(' + dbObjItem[i].item + ')');
		var dbChName = _item.DBCHNAME;
		var dept = _item.DEPT;
		var dba = _item.DEPT;
		var proMFac = _item.PRO_M_FAC;
		var devloper = _item.DEVLOPER;
		var remark = _item.REMARK;
		for(var j = 0 ; j < dbObjItem[i].alt.length ; j++){
			if(dbObjItem[i].alt[j].id=='DBCHNAME'){
				dbChName = dbObjItem[i].alt[j].val;
			}else if(dbObjItem[i].alt[j].id=='DEPT'){
				dept = dbObjItem[i].alt[j].val;
			}else if(dbObjItem[i].alt[j].id=='DBA'){
				dba = dbObjItem[i].alt[j].val;
			}else if(dbObjItem[i].alt[j].id=='PRO_M_FAC'){
				proMFac = dbObjItem[i].alt[j].val;
			}else if(dbObjItem[i].alt[j].id=='DEVLOPER'){
				devloper = dbObjItem[i].alt[j].val;
			}else if(dbObjItem[i].alt[j].id=='REMARK'){
				remark = dbObjItem[i].alt[j].val;
			}
		}
		str = str + " <li class=\"cl1\" >元数据代码：</li> ";
		str = str + " <li class=\"cl2\" >"+_item.DB_CODE+"</li> ";
		str = str + "  <li class=\"cl3\"  >元数据名称：</li> ";
		str = str + " <li  class=\"cl4\" >"+dbChName+"&nbsp;</li>";
		str = str + " <li  class=\"cl3\" >生效时间：</li> ";
		str = str + "  <li  class=\"cl4\" >"+_item.curVerOptId+"</li> ";
		str = str + "  <li  class=\"cl1\" >所属开发科室：</li>  ";
		str = str + " <li  class=\"cl2\" >"+dept+"&nbsp;</li>";
		str = str + " <li  class=\"cl3\" >DBA：</li>";
		str = str + " <li  class=\"cl4\" >"+dba+"&nbsp;</li>";
		str = str + "  <li  class=\"cl1\" >开发商：</li>  ";
		str = str + " <li  class=\"cl2\" >"+proMFac+"&nbsp;</li>";
		str = str + " <li  class=\"cl3\" >开发负责人：</li>";
		str = str + " <li  class=\"cl4\" >"+devloper+"&nbsp;</li>";
		str = str + " <li class=\"cl3\" >元数据类型：</li>  ";
		str = str + " <li  class=\"cl4\" >系统</li> ";
		str = str + " <li  class=\"cl1\" >功能简介：</li>  ";
		str = str + "  <li  class=\"tiledcl5\">"+remark+"&nbsp;</li>";
	}
	str = str + "  </ul>  ";
	str = str + "  </div>  ";
	str = str + "  </div>  ";
	return str;
};

