$(document).ready(function(){
	paging();
	
	$('#listTr').hide();
	
	$('input:radio:eq(0)').click(function(){
		$('#listTr').show();
	});
	
	$('input:radio:eq(1)').click(function(){
		$('#listTr').hide();
	});
	
	loadFlowInfo(decId);
	
	loadMyAltMeta(decId,0,limit);
	
	loadCheckHistory(decId);
	
	loadAltertionImpact(0,limit,decId);
	
	//loadMetaIntColCodeDec(0,limit,decId);
	
	//loadMetaIntFileDec(0,limit,decId);
	
	$('#submit').click(function(){
		var node = "analyzer";
		var checkResult = $('input:radio:checked').val();
		var checkOpinion = $('textarea').val();
		var feedbacUsers = $('#listValInput').val();
		if(checkResult == undefined){
			layer.alert("请给出您的分析结果！",0);
			return false;
		}
		if(checkResult == '1'){
			if(feedbacUsers == ''){
				layer.alert("请选择会签人员！",0);
				return false;
			}
		}
		layer.load('正在提交，请稍后...');
		$("#submit").attr("disabled",true);
		$.ajax({
			url: context+'/flow.do',
			type: 'post',
			data: {method:'completeForSign', taskId:taskId, decId:decId, node:node, checkResult:checkResult, 
				checkOpinion:checkOpinion, feedbacUsers:feedbacUsers, assignee:assignee},
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
		$('#listTr').hide();
	});
	
	$('#close').click(function(){
		close();
	});
	
	$('#choose').click(function(){
		var url = context+'/frame.do?method=reqSignPage';
		/*var width = 850; //弹出窗口的宽度;
		var height = 350; //弹出窗口的高度;
		var top = (window.screen.availHeight-30-height)/2; //获得窗口的垂直位置;
		var left = (window.screen.availWidth-10-width)/2; //获得窗口的水平位置;
		var config = 'width='+width+', height='+height+', top='+top+', left='+left;
		window.open(url,'sign',config);*/
		$.layer({
		    type: 2,
		    border: [1, 1, '#ccc'],
		    title: false,
		    shadeClose: false,
		    closeBtn: false,
		    iframe: {src : url},
		    area: ['780px', '369px']
		});
	});
});