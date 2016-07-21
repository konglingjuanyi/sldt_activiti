/***************************************时间轴的方法*************************************/
	$(".main .year .list").each(function (e, target) {
	    var $target=  $(target),
	        $ul = $target.find("ul");
	    $target.height($ul.outerHeight()), $ul.css("position", "absolute");
	}); 
	
	
	function showOrClose(id){
		$("#"+id).toggleClass("close");
	}
	
	function myMouseUp(num) {
		document.getElementById("version_li"+num).setAttribute("class", "cls highlight");
	}

	function myMouseOut(num) {
		document.getElementById("version_li"+num).setAttribute("class", "cls");
	}

	function reqAltMetaDet(dbId,dbName,optId,altSts){
		var iframe = document.getElementById('formIfr').contentWindow;
		iframe.reqPageForm.dbId.value=dbId;
		iframe.reqPageForm.method.value='reqMetaAltDetailPage';
		iframe.reqPageForm.dbName.value=dbName;
		iframe.reqPageForm.optId.value=optId;
		iframe.reqPageForm.altSts.value=altSts;
		iframe.reqPageForm.target="_reqAltDat";
		iframe.reqPageForm.submit();
	}
/*****************获取系统所有版本，并且按照年份进行分类***********************/
	var getAltSysMetaHis = function(_item,num){
		var str = "";
		str += "<div class=\"year\" id='year"+num+"'>";
		str += "<h2>";
		str += "<a href=\"javascript:showOrClose('year"+num+"')\">"+_item.OPT_YAR+"<i></i></a>";
		str += "</h2>";
		str += "<div class=\"list\">";
		str+="<ul>";
		for(var i = 0 ; i < _item.LIST.length; i ++){
			str+="<li class=\"cls\" style=\"cursor: pointer;\" id=\"version_li"+num+""+i+"\" onmouseover=\"myMouseUp('"+num+""+i+"')\"";
			str+="onmouseout=\"myMouseOut('"+num+""+i+"')\" onclick=\"reqAltMetaDet('"+dbId+"','"+dbName+"','"+_item.LIST[i].OPT_ID+"','')\">";
			str+="<p class=\"date\">"+_item.LIST[i].MMDD+"</p>";
			str+="<p class=\"intro\">"+_item.LIST[i].VER_DESC+"</p>";
			str+="<p class=\"version\">&nbsp;</p>";
			str+="<div class=\"more\">";
			str+="<p>"+_item.LIST[i].ALT_DESC+"</p>";
			str+="</div>";
			str+="</li>";
		}
		str+="</ul>";
		str+="</div>";
		str+="</div>";
		return str;
	};

	$.ajax({
		url:context+'/metadataAlt.do?method=altSysMetaHis&dbId='+dbId,
		method: 'POST',
		async:false,
		dataType :'json',
		success: function(response) {
			var altSysMetaHis = response.data;
			var str = "";
			for(var i = 0;i<altSysMetaHis.length;i++){
				var item = altSysMetaHis[i];
				str +=  getAltSysMetaHis(item,i);
			}
			$("#content_time").html(str);
			$('#sysTitle').html(dbName);
			for(var i = 0;i<altSysMetaHis.length;i++){
				if(i>0){
					showOrClose('year'+i);
				}
			}
		},fail : function(){
			alert("cards load fail!");
		}
	});