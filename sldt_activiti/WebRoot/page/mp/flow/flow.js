$(document).ready(function(){
	loadTaskList();
});

function claim(taskId){
	$.layer({
		shade: [0],
		area: ['auto','auto'],
		dialog: {
			msg: '是否签收此任务？',
			btns: 2,
			type: 4,
			yes: function(){
				layer.load('正在签收，请稍后...');
				$.ajax({
					url: context+'/flow.do',
					type: 'post',
					data: {method:'claim',taskId:taskId},
					dataType: 'json',
					success: function(){
						layer.alert("签收成功！",1);
						loadTaskList();
					},
					error: function(){
						layer.alert("签收失败！",3);
					}
				});
			},
			no: function(){
				
			}
		}
	});
};

function handle_dec(taskId,decId,name,assignee){
	var node = "";
	if(name == "变更申请审核"){
		node = "firsthecker";
	}else if(name == "变更申请单复审<架构办>"){
		node = "rechecker";
	}else if(name == "影响方分析<架构办>"){
		node = "analyzer";
	}else if(name == "受影响方评估反馈<会签>"){
		node = "feedbac";
	}else if(name == "架构办审批<架构办>"){
		node = "approver";
	}else if(name == "全行评审"){
		node = "appraisal";
	}
	var url = context+'/frame.do?method=handle&taskId='+taskId+'&decId='+decId+'&node='+node+'&assignee='+assignee;
	/*var width = 1050; //弹出窗口的宽度;
	var height = 550; //弹出窗口的高度;
	var top = (window.screen.availHeight-30-height)/2; //获得窗口的垂直位置;
	var left = (window.screen.availWidth-10-width)/2; //获得窗口的水平位置;
	var config = 'width='+width+', top='+top+', left='+left+', scrollbars=yes';
	name = name.replace("<","").replace(">","");
	window.open(url,name,config);*/
	$.layer({
	    type: 2,
	    border: [1, 1, '#ccc'],
	    title: false,
	    shadeClose: false,
	    closeBtn: false,
	    iframe: {src : url},
	    area: ['940px', '587px']//,
	    /*end: function(){
	    	loadTaskList();
	    }*/
	});
};

function handle_init_dec(taskId,initDecId,name,assignee){
	var node = "";
	if(name == "版本初始化初审"){
		node = "firstCheck";
	}else if(name == "版本初始化复审"){
		node = "rechecker";
	}
	var url = context+'/frame.do?method=handleForInit&taskId='+taskId+'&initDecId='+initDecId+'&node='+node+'&assignee='+assignee;
	$.layer({
	    type: 2,
	    border: [1, 1, '#ccc'],
	    title: false,
	    shadeClose: false,
	    closeBtn: false,
	    iframe: {src : url},
	    area: ['955px', '550px']
	});
};

function loadTaskList(){
	$('#flowTable_tBody tr').remove();
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'taskList'},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var tBody = "";
			var length = data.length;
			$.each(data,function(i, val){
				var tr = "";
				var name = val.name.replace("<","&lt;").replace(">","&gt;");
				if(i % 2 == 0){
					tr = "<tr class='data_table_even'><td style='display:none'>"+val.decId+"</td>" +
						 "<td style=\"width: 82px;\">"+(i+1)+"</td><td style=\"width: 123px;\">变更申报审核流程</td>" +
						 "<td style=\"width: 122px;\">"+val.decName+"</td>" +
						 "<td style=\"width: 103px;\">"+val.altUserName+"</td><td style=\"width: 153px;\">"+val.altOperDate+"</td>" +
						 "<td style=\"width: 152px;\">"+name+"</td><td style=\"width: 274px;\">"+val.decDesc+"</td>";
					tr +="<td style=\"width: 82px;\"><a style=\"cursor: pointer;color:blue;\" onclick=\"handle_dec('"+val.taskId+"','"+val.decId+"','"+name+"','"+val.assignee+"')\" >办理</a><td></tr>"
				}else{
					tr = "<tr class='data_table_odd'><td style='display:none'>"+val.decId+"</td>" +
					 	 "<td style=\"width: 82px;\">"+(i+1)+"</td><td style=\"width: 123px;\">变更申报审核流程</td>" +
					 	 "<td style=\"width: 122px;\">"+val.decName+"</td>" +
					 	 "<td style=\"width: 103px;\">"+val.altUserName+"</td><td style=\"width: 153px;\">"+val.altOperDate+"</td>" +
					 	 "<td style=\"width: 152px;\">"+name+"</td><td style=\"width: 274px;\">"+val.decDesc+"</td>";
					tr +="<td style=\"width: 82px;\"><a style=\"cursor: pointer;color:blue;\" onclick=\"handle_dec('"+val.taskId+"','"+val.decId+"','"+name+"','"+val.assignee+"')\" >办理</a><td></tr>"
				}
				tBody += tr;
			});
			$.ajax({
				url: context+'/flow.do',
				type: 'post',
				data: {method:'initTaskList'},
				dataType: 'json',
				success: function(response){
					var data_ = response.data;
					$.each(data_,function(i, val){
						var tr = "";
						var count = length+i;
						if(count % 2 == 0){
							tr = "<tr class='data_table_even'><td style='display:none'>"+val.initDecId+"</td>" +
							 	 "<td style=\"width: 82px;\">"+(count+1)+"</td><td style=\"width: 123px;\">版本初始化申请审核流程</td>" +
							 	 "<td style=\"width: 122px;\">"+val.initDecName+"</td>" +
							 	 "<td style=\"width: 103px;\">"+val.altUserName+"</td><td style=\"width: 153px;\">"+val.altOperDate+"</td>" +
							 	 "<td style=\"width: 152px;\">"+val.name+"</td><td style=\"width: 274px;\">"+val.altReason+"</td>";
							tr +="<td style=\"width: 82px;\"><a style=\"cursor: pointer;color:blue;\" onclick=\"handle_init_dec('"+val.taskId+"','"+val.initDecId+"','"+val.name+"','"+val.assignee+"')\" >办理</a><td></tr>"
						}else{
							tr = "<tr class='data_table_odd'><td style='display:none'>"+val.initDecId+"</td>" +
						 	 	 "<td style=\"width: 82px;\">"+(count+1)+"</td><td style=\"width: 123px;\">版本初始化申请审核流程</td>" +
						 	 	 "<td style=\"width: 122px;\">"+val.initDecName+"</td>" +
						 	 	 "<td style=\"width: 103px;\">"+val.altUserName+"</td><td style=\"width: 153px;\">"+val.altOperDate+"</td>" +
						 	 	 "<td style=\"width: 152px;\">"+val.name+"</td><td style=\"width: 274px;\">"+val.altReason+"</td>";
							tr +="<td style=\"width: 82px;\"><a style=\"cursor: pointer;color:blue;\" onclick=\"handle_init_dec('"+val.taskId+"','"+val.initDecId+"','"+val.name+"','"+val.assignee+"')\" >办理</a><td></tr>"
						}
						tBody += tr;
					});
					$('#flowTable_tBody').append(tBody);
					mouseTabRowsColorId('#flowTable_tBody');
				},
				error: function(){
					
				}
			});
		},
		error: function(){
			
		}
	});
}

/******************************获取待办事项******************************/
/*function loadTaskList(start,limit){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'toDoList',start:start,limit:limit},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var totalPages = response.totalPages;
			var currPage = response.currPage;
			var tbody = "";
			$.each(data,function(i,val){
				var tr = "";
				var name = val.name.replace("<","&lt;").replace(">","&gt;");
				if(i % 2 == 0){
					tr = "<tr class='data_table_even'><td style='display:none'>"+val.decId+"</td><td>"+(i+1)+"</td><td>"+val.type+"</td><td>"+val.decName+"</td>" +
							"<td>"+val.altUser+"</td><td>"+val.altOperDate+"</td>" +
							"<td>"+name+"</td><td>"+val.decDesc+"</td>";
					if(val.assignee == ""){
						tr += "<td><input class='claim' type='button' value='签收' onclick=claim('"+val.taskId+"') /></td></tr>";
					}else{
						tr += "<td><input class='handle' type='button' value='办理' onclick=handle('"+val.taskId+"','"+val.decId+"','"+name+"') /></td></tr>";
					};
				}else{
					tr = "<tr class='data_table_odd'><td style='display:none'>"+val.decId+"</td><td>"+(i+1)+"</td><td>"+val.type+"</td><td>"+val.decName+"</td>" +
							"<td>"+val.altUser+"</td><td>"+val.altOperDate+"</td>" +
							"<td>"+name+"</td><td>"+val.decDesc+"</td>";
					if(val.assignee == ""){
						tr += "<td><input class='claim' type='button' value='签收' onclick=claim('"+val.taskId+"') /></td></tr>";
					}else{
						tr += "<td><input class='handle' type='button' value='办理' onclick=handle('"+val.taskId+"','"+val.decId+"','"+name+"') /></td></tr>";
					};
				}
				tbody += tr;
				var next = currPage*limit;
				var last = (currPage-2)*limit;
				var pagesDiv = " <div class=\"search_page_right_f\" onclick=\"page('"+limit+"','"+next+"')\" style=\"cursor: pointer;\" ></div>"
					+" <div class=\"search_page_mid_f\" >"+currPage+"/"+totalPages+"</div>"
					+"<div class=\"search_page_left_f\" onclick=\"page('"+limit+"','"+last+"')\" style=\"cursor: pointer;\" ></div>";
				$("#search_page").html(pagesDiv);
				$('#flowTable_tBody').append(tbody);
			});
		}
	});
}*/