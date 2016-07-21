var obj = [];
function altItem(){
	moduleItem();
	tableItem();
	columnItem();
	dbItem();
}
function saveAlt() {
	var showAltOkDiv = false;
	for(var i = 0 ; i < obj.length ; i ++){
		if(obj[i].db!=null){
			if(obj[i].db.length>0){
				showAltOkDiv = true;
				break;
			}
		}
		
		if(obj[i].column!=null){
			if(obj[i].column.length>0){
				showAltOkDiv = true;
				break;
			}
		}
		if(obj[i].table!=null){
			if(obj[i].table.length>0){
				showAltOkDiv = true;
				break;
			}
		}
		if(obj[i].module!=null){
			if(obj[i].module.length>0){
				showAltOkDiv = true;
				break;
			}
		}
		
	}
	
	if(showAltOkDiv){
		saveOKHtml();
	}else{
		layer.msg("无已修改元数据！", 1, 3);
	}
}


function dbItem() {
	$("#db_list_ul li").each(
			function(i){
				if($(this).attr('id')!=null&&$(this).attr('id')!=''&&$(this).attr('abbr')!=null){
					var id = $(this).attr('id');
					var abbr = $(this).attr('abbr');
					var val = $(this).html();
					if(val!=abbr){
						var item = $(this).parent().attr('id');
						var data = [];
						var flag = false;
						data.push({id:id,val:val,abbr:abbr});
						for(var i = 0 ; i < obj.length ; i ++){
							if(obj[i].db!=null){
								for(var k = 0 ; k < obj[i].db.length ; k++){
									if(item==obj[i].db[k].item){
										flag=true;
										var aflag = false;
										for(var j = 0 ; j < obj[i].db[k].alt.length ; j ++){
											if(obj[i].db[k].alt[j].id==id){
												obj[i].db[k].alt.splice(j,1);
												obj[i].db[k].alt.push({id:id,val:val,abbr:abbr});
												aflag=true;
												break;
											}
										}
										if(!aflag){
											obj[i].db[k].alt.push({id:id,val:val,abbr:abbr});
										}
										break;
									}
								}
							}
						}
						if(!flag){
							var db = [];
							db.push({item:item,alt:data});
							obj.push({db:db});
						}
					}
				}
			}
		 );
}

function columnItem() {
	$("#verColumnData td").each(
			function(i){
				if($(this).attr('id')!=null&&$(this).attr('id')!=''){
					var id = $(this).attr('id');
					var abbr = $(this).attr('abbr');
					var val = $(this).html();
					if(abbr!=val){
						var data = [];
						var item = $(this).parent().attr('id');
						var flag = false;
						data.push({id:id,val:val,abbr:abbr});
						for(var i = 0 ; i < obj.length ; i ++){
							if(obj[i].column!=null){
								for(var k = 0 ; k < obj[i].column.length ; k++){
									if(item==obj[i].column[k].item){
										flag=true;
										var aflag = false;
										for(var j = 0 ; j < obj[i].column[k].alt.length ; j ++){
											if(obj[i].column[k].alt[j].id==id){
												obj[i].column[k].alt.splice(j,1);
												obj[i].column[k].alt.push({id:id,val:val,abbr:abbr});
												aflag=true;
												break;
											}
										}
										if(!aflag){
											obj[i].column[k].alt.push({id:id,val:val,abbr:abbr});
										}
										break;
									}
								}
							}
						}
						if(!flag){
							var column = [];
							column.push({item:$(this).parent().attr('id'),alt:data});
							obj.push({column:column});
						}
					}
				}
			}
		 );
}

function tableItem() {
	$("#verTableData td").each(
			function(i){
				if($(this).attr('id')!=null&&$(this).attr('id')!=''){
					var id = $(this).attr('id');
					var abbr = $(this).attr('abbr');
					var val = $(this).html();
					if(abbr!=val){
						var data = [];
						var item = $(this).parent().attr('id');
						var flag = false;
						data.push({id:id,val:val,abbr:abbr});
						for(var i = 0 ; i < obj.length ; i ++){
							if(obj[i].table!=null){
								for(var k = 0 ; k < obj[i].table.length ; k++){
									if(item==obj[i].table[k].item){
										flag=true;
										var aflag = false;
										for(var j = 0 ; j < obj[i].table[k].alt.length ; j ++){
											if(obj[i].table[k].alt[j].id==id){
												obj[i].table[k].alt.splice(j,1);
												obj[i].table[k].alt.push({id:id,val:val,abbr:abbr});
												aflag=true;
												break;
											}
										}
										if(!aflag){
											obj[i].table[k].alt.push({id:id,val:val,abbr:abbr});
										}
										break;
									}
								}
							}
						}
						if(!flag){
							var table = [];
							table.push({item:$(this).parent().attr('id'),alt:data});
							obj.push({table:table});
						}
					}
				}
			}
		 );
}

function moduleItem() {
	$("#verModuleData td").each(
		function(i){
			if($(this).attr('id')!=null&&$(this).attr('id')!=''){
				var id = $(this).attr('id');
				var abbr = $(this).attr('abbr');
				var val = $(this).html();
				if(abbr!=val){
					var data = [];
					var item = $(this).parent().attr('id');
					var flag = false;
					data.push({id:id,val:val,abbr:abbr});
					for(var i = 0 ; i < obj.length ; i ++){
						if(obj[i].module!=null){
							for(var k = 0 ; k < obj[i].module.length ; k++){
								if(item==obj[i].module[k].item){
									flag=true;
									var aflag = false;
									for(var j = 0 ; j < obj[i].module[k].alt.length ; j ++){
										if(obj[i].module[k].alt[j].id==id){
											obj[i].module[k].alt.splice(j,1);
											obj[i].module[k].alt.push({id:id,val:val,abbr:abbr});
											aflag=true;
											break;
										}
									}
									if(!aflag){
										obj[i].module[k].alt.push({id:id,val:val,abbr:abbr});
									}
									break;
								}
							}
						}
					}
					if(!flag){
						var module = [];
						module.push({item:$(this).parent().attr('id'),alt:data});
						obj.push({module:module});
					}
				}
			}
		}
	 );
}


function delObj(tdObj){
	for(var i = 0 ; i < obj.length ; i ++){
		if(tdObj.parent().attr('lang')=='COLUMN'){
			if(obj[i].column!=null){
				for(var k = 0 ; k < obj[i].column.length ; k++){
					delObjAltData(tdObj, obj[i].column[k]);
					if(obj[i].column[k].alt.length<=0){
						obj[i].column.splice(k,1);
					}
				}
			}
		}
		if(tdObj.parent().attr('lang')=='TABLE'){
			if(obj[i].table!=null){
				for(var k = 0 ; k < obj[i].table.length ; k++){
					delObjAltData(tdObj, obj[i].table[k]);
					if(obj[i].table[k].alt.length<=0){
						obj[i].table.splice(k,1);
					}
				}
			}
		}
		if(tdObj.parent().attr('lang')=='MODULE'){
			if(obj[i].module!=null){
				for(var k = 0 ; k < obj[i].module.length ; k++){
					delObjAltData(tdObj, obj[i].module[k]);
					if(obj[i].module[k].alt.length<=0){
						obj[i].module.splice(k,1);
					}
				}
			}
		}
		
	}
}

function delObjAltData(tdObj,objItem){
	if(objItem.item==tdObj.parent().attr('id')){
		for(var j = 0 ; j < objItem.alt.length ; j ++){
			if(tdObj.attr('id')==objItem.alt[j].id){
				objItem.alt.splice(j,1);
			}
		}
	}
}

function delDbObjAltData(liObj){
	
	if(liObj.attr('id')!=null){
		for(var j = 0 ; j < obj.length ; j ++){
			if(obj[i].db!=null){
				for(var k = 0 ; k < obj[i].db.length ; k++){
					delObjAltData(liObj, obj[i].db[k]);
					if(obj[i].db[k].alt.length<=0){
						obj[i].db.splice(k,1);
					}
				}
			}
		}
	}
	
}


function insAltMeta(){
	var tarDate = $("#optSelect").val();
	if(tarDate==null||tarDate==''){
		layer.alert("请选择目标投产时间！", 3);
		return;
	}
	$("#closeInsAlt").attr("disabled",true);
	layer.load('正在提交，请稍后...');
	var altData = JSON.stringify(obj);
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否已确认提交当前变更的元数据？',
	        btns: 2,                    
	        type: 4,
	        btn: ['提交','取消'],
	        yes: function(){
	        	$.ajax({
	        		url:context+'/' + 'metadataAlt.do?method=insAltMeta',
	        		method: 'POST',
	        		async:false,
	        		data:{altData:altData,dbId:dbId,tarDate:tarDate},
	        		dataType :'json',
	        		success: function(response) {
	        			if(response.data.success=="true"){
	        				layer.alert(response.data.msg,1);
	        			}
	        			else{
	        				layer.msg(response.data.msg,3);
	        			}
	        			setTimeout(closeAll,1000);
	        		},fail : function(){
	        			layer.msg("cards load fail!", 1, 3);
	        		}
	        	});
	        }, no: function(){
	        }
	    }
	});
}
function closeAll(){
	layer.closeAll();
}
function saveOKHtml(){

	var html = "";
	html = html+"<div class=\"showDivAltDetail\" style='width:800px;height:550px;'>";
	html = html+"<div class=\"okAltTile\">";
	html = html+"<b>确认元数据</b>";
	html = html+"</div>";
	html = html+"<div>";
	html = html+"<hr color=\"#ed1a2e\">";
	html = html+"</div>";
	
	html = html+"<div style='text-align: left;padding-top:20px;'>";
	html = html+"<div style='float:left;font-size:13	px;font-family:\"宋体\";'>";
	html = html+"目标投产版本：";
	html = html+"</div>";
	html = html+"<div  style='float:left;'>";
	html = html+"<select id=\"optSelect\" style=\"width: 150px;\"><option value=''>请选择...</option></select>";
	html = html+"</div>";
	html = html+"</div>";
	
	html = html+"<div style='padding-top:30px;'>";
	html = html+"<iframe src=\""+context+"/frame.do?method=reqEditOkMetaPage\" width='800px' height='360px' frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"yes\" allowtransparency=\"yes\" ></iframe>";
	html = html+"</div>";		
	

	html = html+"<div class='showDivBut'>";
	html = html+"<div style='padding-left:300px;float:left;'>";
	html = html+"<button class='but' id='closeInsAlt' onclick=\"insAltMeta()\">&nbsp;保 存 </button>";
	html = html+"</div>";
	html = html+"<div style='padding-left:20px;float:left;'>";
	html = html+"<button id=\"pagebtn\" class='but' onclick=\"\">&nbsp;关 闭 </button>";
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
		// shift: 'left', //从左动画弹出
		page: {
		    html: html
		}
	});
	
	initOptId();
	
	//自设关闭
	$('#pagebtn').on('click', function(){
		layer.close(pageii);
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




function addTableItem(){
	var str = "";
	str = str + "<tr lang='TABLE' class=\"data_table_odd\" id=\"ADD\">";
	str = str + "<td id='TABNAME'></td>";
	str = str + "<td id='TABCHNAME' abbr=\"\"></td>";
	str = str + "<td id='TABSPACENAME' abbr=\"\"></td>";
	str = str + "<td id='PKCOLS' abbr=\"\"></td>";
	str = str + "<td id='FKCOLS' abbr=\"\"></td>";
	str = str + "<td id='IMPFLAG' abbr=\"\">"+_item.impFlag+"</td>";
	str = str + "<td id='LCYCDESC' abbr=\"\">"+_item.lcycDesc+"</td>";
	str = str + "<td id='REMARK' abbr=\"\">"+_item.remark+"</td>";
	str = str + "<td>/"+_item.dbId+"/"+_item.schname+"/"+_item.modName+"</td>";
	str = str + "</tr>";
	
	
	$("#verTableData").append("<tr><td><input id=\"sd\" type=\"checkbox\" /></td><td><input type=\"text\" /></td></tr>");
}


function setDivAltCount(){
	var dbCount=0;
	var moduleCount=0;
	var tableCount= 0;
	var columnCount=0;
	altItem();
	for(var i = 0 ; i < obj.length ; i ++){
		if(obj[i].db!=null){
			if(obj[i].db.length>0){
				dbCount++;
			}
		}
		if(obj[i].column!=null){
			if(obj[i].column.length>0){
				columnCount++;
			}
		}
		if(obj[i].table!=null){
			if(obj[i].table.length>0){
				tableCount++;
			}
		}
		if(obj[i].module!=null){
			if(obj[i].module.length>0){
				moduleCount++;
			}
		}
	}
	var str = "";
	str+= "<div style='border: 1px solid #e0e0e0;'>数据库变更："+dbCount+"个</div>";
	str+= "<div style='border: 1px solid #e0e0e0;'>模式变更："+0+"个</div>";
	str+= "	<div style='border: 1px solid #e0e0e0;'>模块变更："+moduleCount+"个 </div>";
	str+= "	<div style='border: 1px solid #e0e0e0;'>表级变更："+tableCount+"个 </div>";
	str+= "	<div style='border: 1px solid #e0e0e0;'>字段变更："+columnCount+"个</div>";
	$("#altCount").html(str);
}






