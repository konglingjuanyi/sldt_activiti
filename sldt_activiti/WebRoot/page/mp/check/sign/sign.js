$(document).ready(function(){
	loadUsers();
	
	$('#searchUser').click(function(){
		var userId = $.trim($('#userId').val());
		if(userId == ""){
			return false;
		}
		loadUsers();
	});
	
	$('#add').click(function(){
		var $options = $('#selectable option:selected');
		/*$.each($options,function(i,val){
			var userId = $(val).val();
			var flag = existed(userId);
			if(flag != true){
				$(val).appendTo('#selected');
			}
		})*/
		$options.appendTo('#selected');
	});
	$('#addAll').click(function(){
		var $options = $('#selectable option');
		/*$.each($options,function(i,val){
			var userId = $(val).val();
			var flag = existed(userId);
			if(flag != true){
				$(val).appendTo('#selected');
			}
		})*/
		$options.appendTo('#selected');
	});
	$('#remove').click(function(){
		var $options = $('#selected option:selected');
		$options.appendTo('#selectable');
	});
	$('#removeAll').click(function(){
		var $options = $('#selected option');
		$options.appendTo('#selectable');
	});
	$('#selectable').dblclick(function(){
		var $options = $("option:selected", this);
		/*$.each($options,function(i,val){
			var userId = $(val).val();
			var flag = existed(userId);
			if(flag != true){
				$(val).appendTo('#selected');
			}
		})*/
		$options.appendTo("#selected");
	});
	$('#selected').dblclick(function(){
		var $options = $("option:selected", this);
		$options.appendTo("#selectable");
	});
	$('#cancel').click(function(){
		close();
	});
	$('#submit').click(function(){
		var options = $('#selected option');
		var selected = "";
		var selectedVal = "";
		if(options.length == 0){
			layer.alert("请选择用户！",0);
			return false;
		}
		for(var i=0;i<options.length;i++){
			selected += options[i].text+",";
			selectedVal += options[i].value+",";
		}
		/*var listInput = window.opener.document.getElementById("listInput");
		var listValInput = window.opener.document.getElementById("listValInput");
		var $listInput = $(listInput);
		var $listValInput = $(listValInput);
		$listInput.val(selected);
		$listValInput.val(selectedVal);*/
		parent.$('#listInput').val(selected);
		parent.$('#listValInput').val(selectedVal);
		close();
	});
});

function loadUsers(){
	var userId = $.trim($('#userId').val());
	$.ajax({
		url: context+'/flow.do',
		type: 'post',
		data: {method:'getUsers',userId:userId},
		dataType: 'json',
		success: function(response){
			var data = response.data;
			var html = "";
			$.each(data,function(i,val){
				//debugger;
				var userId = val.USERID;
				var flag = existed(userId);
				if(flag != true){
					html += "<option value="+val.USERID+">"+val.USERNAME+"</option>";
				}
			});
			$('#selectable').html(html);
		}
	});
}

function existed(userId){
	var flag = false;
	var options = $('#selected option');
	options.each(function(i,val){
		var userId_ = val.value;
		if(userId == userId_){
			flag = true;
		}
	});
	return flag;
}