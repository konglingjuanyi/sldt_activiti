$(document).ready(function(){
	paging();
	
	loadFlowInfo(decId);
	
	loadMyAltMeta(decId,0,limit);
	
	loadCheckHistory(decId);
	
	loadAltertionImpact(0,limit,decId);
	
	//loadMetaIntColCodeDec(0,limit,decId);
	
	//loadMetaIntFileDec(0,limit,decId);
	
	$('#submit').click(function(){
		var node = "approver";
		var checkResult = $('input:radio:checked').val();
		var checkOpinion = $('textarea').val();
		if(checkResult == undefined){
			layer.alert("请给出您的审批结果！",0);
			return false;
		}
		layer.load('正在提交，请稍后...');
		$("#submit").attr("disabled",true);
		$.ajax({
			url: context+'/flow.do',
			type: 'post',
			data: {method:'complete', taskId:taskId, decId:decId, node:node, checkResult:checkResult, checkOpinion:checkOpinion,
				assignee:assignee},
			dataType: 'json',
			success: function(){
				layer.alert('办理成功！',1);
				window.parent.loadTaskList();
				//window.parent.initWaitToDo();
				setTimeout(close,2000);
			},
			error: function(){
				layer.alert('办理失败！',3);
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