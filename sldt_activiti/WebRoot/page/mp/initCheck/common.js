function loadInitDec(initDecId){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getInitDec',initDecId:initDecId},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			html = "<li class=\"cl1\">系统中文名称：</li>"+
		   	       "<li class=\"cl2\">"+data.dbChName+"</li>"+
		   	       "<li class=\"cl3\">系统负责人：</li>"+
		   	       "<li class=\"cl4\">"+data.dbUser+"</li>"+
		   	       "<li class=\"cl1\">系统描述：</li>"+
		   	       "<li class=\"cl2\">"+data.dbDesc+"</li>"+
		   	       "<li class=\"cl3\"></li>"+
		   	       "<li class=\"cl4\"></li>"+
		   	       "<li class=\"cl1\">申请理由：</li>"+
		   	       "<li class=\"cl2\" style='width:83%;'>"+data.altReason+"</li>";
			$('#list2').html(html);
		},
		error: function(){
			
		}
	});
}

function loadInitFlowInfo(initDecId){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getInitDeclareInfo',initDecId:initDecId},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var html = "";
			$.each(data,function(i,val){
				html = "<li class=\"cl1\">流程名称：</li>"+
				   	   "<li class=\"cl2\">版本初始化申请审核流程</li>"+
				   	   "<li class=\"cl3\">流程状态：</li>"+
				   	   "<li class=\"cl4\">"+val.name+"</li>"+
				   	   "<li class=\"cl1\">申请人：</li>"+
				   	   "<li class=\"cl2\">"+val.altUserName+"</li>"+
				   	   "<li class=\"cl3\">申请时间：</li>"+
				   	   "<li class=\"cl4\">"+val.altOperDate+"</li>";
			});
			$('#list1').html(html);
		},
		error: function(){
			
		}
	});
}

function loadInitCheckHistory(initDecId){
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getInitCheckHistory',initDecId:initDecId},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var value = "";
			$.each(data,function(i,val){
				value += val.userName+"【"+val.department+"】审核任务，审核时间："+val.auditTime+"，审核结果："+val.auditResult+"，审核意见："+val.auditOpinion+"<br/>";
			});
			$('#checkInfo').html(value);
		},
		error: function(){
			
		}
	});
}

function loadMoreInitCheckHistory(initDecId){
	$('#moreBtn').click(function(){
		$('#moreBtnDiv').hide();
		$.ajax({
			url: context+'/flow.do',
			type: 'post',
			data: {method:'getMoreInitCheckHistory',initDecId:initDecId},
			dataType: 'json',
			success: function(response){
				var data = response.data;
				var divs = "";
				$.each(data,function(i,val){
					var div = "<div><table class='msg_table'><tr><td class='msg_table_odd'>审核人</td><td class='msg_table_even'>"+val.auditor+"</td></tr>" +
						"<tr><td class='msg_table_odd'>所在部门</td><td class='msg_table_even'>"+val.department+"</td></tr>" +
						"<tr><td class='msg_table_odd'>审核时间</td><td class='msg_table_even'>"+val.auditTime+"</td></tr>" +
						"<tr><td class='msg_table_odd'>审核结果</td><td class='msg_table_even'>"+val.auditResult+"</td></tr>" +
						"<tr><td class='msg_table_odd'>审核意见</td><td class='msg_table_even'>"+val.auditOpinion+"</td></tr>" +
						"</table></div>";
					divs += div;
				});
				$('#handle').append(divs);
			},
			error: function(){
				
			}
		});
	});
}

function close(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}