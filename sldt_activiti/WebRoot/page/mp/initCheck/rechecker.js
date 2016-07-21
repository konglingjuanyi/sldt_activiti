$(document).ready(function(){
	loadInitFlowInfo(initDecId);
	
	loadInitDec(initDecId);
	
	loadInitCheckHistory(initDecId);
	
	//loadMoreInitCheckHistory(initDecId);
	
	$('#submit').click(function(){
		var node = "rechecker";
		var checkResult = $('input:radio:checked').val();
		var checkOpinion = $('textarea').val();
		if(checkResult == undefined){
			layer.alert("请给出您的审核结果！",0);
			return false;
		}
		$("#submit").attr("disabled",true);
		layer.load('正在提交，请稍后...');
		$.ajax({
			url: context+'/flow.do',
			type: 'post',
			data: {method:'completeForInit', taskId:taskId, initDecId:initDecId, node:node, checkResult:checkResult, 
				checkOpinion:checkOpinion, assignee:assignee},
			dataType: 'json',
			success: function(){
				layer.alert("办理成功！",1);
				window.parent.loadTaskList();
				//window.parent.initWaitToDo();
				setTimeout(close,1000);
			},
			error: function(){
				layer.alert("办理失败！",3);
			}
		});
	});
	
	$('#reset').click(function(){
		$('#checkForm').get(0).reset();
	});
	
	$('#close').click(function(){
		close();
	});
	
});