$(document).ready(function(){
	paging();
	
	loadFlowInfo(decId);
	
	loadMyAltMeta(decId,0,limit);
	
	loadCheckHistory(decId);
	
	loadAltertionImpact(0,limit,decId);
	
	//loadMetaIntColCodeDec(0,limit,decId);
	
	//loadMetaIntFileDec(0,limit,decId);
	
	$('#submit').click(function(){
		var checkOpinion = $('textarea').val();
		layer.load('正在提交，请稍后...');
		$("#submit").attr("disabled",true);
		$.ajax({
			url: context+'/flow.do',
			type: 'post',
			data: {method:'complete', taskId:taskId, decId:decId, checkOpinion:checkOpinion, assignee:assignee},
			dataType: 'json',
			success: function(){
				layer.alert("操作成功！",1);
				window.parent.loadTaskList();
				//window.parent.initWaitToDo();
				setTimeout(close,2000);
			},
			error: function(){
				layer.alert("操作失败！",3);
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