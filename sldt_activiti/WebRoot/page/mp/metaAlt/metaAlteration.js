function getSysAltDom(_item,num){
	var str = " <div style=\"padding-top: 20px;\"> ";
		str += "<table  class=\"data_table_alt\" style=\"width: 790px;\" >";
		str += "<tr>";
		str += "<td style=\"width: 200px;height: 120px;font-family: 宋体;font-size: 16px;vertical-align:top;\" rowspan=\"3\" ><div style='padding-top: 10px; height:50px;background-color: #F5F5F5;font-weight: bolder;'>"+_item.dbChName+"<br/>("+_item.dbId+")</div><div><hr /></div><div style='padding: 5px;font-size:12px;text-align: left;font-weight: normal;'>"+_item.remark+"</div></td>";
		str += "<td style=\"text-align:left; height: 30px; padding: 0 0 0 10px;\">";
		str += "当前系统版本：<a style=\"cursor: pointer;\" onclick=\"reqSysDetailPage('"+_item.dbId+"','"+_item.curVerOptId+"')\">"+(_item.curVerOptId ==null || _item.curVerOptId =='' ? _item.fStartdate : _item.curVerOptId)+"</a>";
		str += "<div style=\"float:right; padding-right: 10px;\"><a style=\"font-family: 宋体;cursor: pointer;font-size:16px;\" onclick=\"reqSysMoreVerPage('"+_item.dbId+"','"+_item.dbChName+"')\"><img src=\""+context+"/page/mp/imgs/gf_more.png\" /></a></div>";
		str += "</td>";
		str += "</tr>";
		str += "<tr>";
		str += "<td>";
		str += "<div id=\"c\" style=\"float:left;\">";
		str += "<div class=\"img_div\"><iframe style='height: 180px;width:400px;' frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\" allowtransparency=\"yes\"  ";
		str += " src=\""+context+"/page/mp/metaAlt/echart.jsp?num="+num+"&unc="+(_item.unc == null|| _item.unc=='' ? 0 : _item.unc)+"&notDec="+(_item.notDec == null || _item.notDec == '' ? 0 : _item.notDec)+"&dec="+(_item.dec == null || _item.dec == '' ? 0 : _item.dec)+"&valid="+(_item.valid == null || _item.valid == '' ? 0 : _item.valid)+"\"></iframe></div>";
		str += "<div id=\"b\" class=\"img_content_div\">";
		str += "<div id=\"status_0\"><img alt=\"\" src=\"../imgs/status/0.png\" /> 未确认变更<a style=\"cursor: pointer;\" onclick=\"reqAltMetaDet('"+_item.dbId+"','"+_item.dbChName+"','','3')\"><b class=\"alt_num_b\" style='color:#ED1A2E;'>&nbsp;"+(_item.unc == null|| _item.unc=='' ? 0 : _item.unc)+"&nbsp;</b></a>个</div>";
		str += "<div id=\"status_0\"><img alt=\"\" src=\"../imgs/status/1.png\" /> 未申报变更<a style=\"cursor: pointer;\" onclick=\"reqAltMetaDet('"+_item.dbId+"','"+_item.dbChName+"','','2')\"><b class=\"alt_num_b\" style='color:#FF6702;'>&nbsp;"+(_item.notDec == null||_item.notDec=='' ? 0 : _item.notDec)+"&nbsp;</b></a>个</div>";
		str += "<div id=\"status_0\"><img alt=\"\" src=\"../imgs/status/2.png\" /> 申报中变更<a style=\"cursor: pointer;\" onclick=\"reqAltMetaDet('"+_item.dbId+"','"+_item.dbChName+"','','1')\"><b class=\"alt_num_b\" style='color:#0EA3C7;'>&nbsp;"+(_item.dec == null||_item.dec =='' ? 0 : _item.dec)+"&nbsp;</b></a>个</div>";
		str += "<div id=\"status_0\"><img alt=\"\" src=\"../imgs/status/3.png\" /> 已生效变更<a style=\"cursor: pointer;\" onclick=\"reqAltMetaDet('"+_item.dbId+"','"+_item.dbChName+"','','0')\"><b class=\"alt_num_b\" style='color:#32CD00;'>&nbsp;"+(_item.valid == null ||_item.valid=='' ? 0 : _item.valid)+"&nbsp;</b></a>个</div>";
		str += "</div>";
		str += "<div style=\"clear:both\"></div><!--这个层很有用，必须要，否则可能不兼容。--> ";
		str += "</div>";
		str += "</td>";
		str += " </tr>";
		str += " <tr>";
		str += "<td style=\"text-align: left; height: 30px; padding: 0 0 0 10px; \" >";
		str += "<div id=\"c\" style=\"float:left;\">";
		str += "<div id=\"a\" style=\"float:left;\">开发版本：</div>";
		str += " <div id=\"b\" style=\"float:left;\" >&nbsp;"+(_item.developAltHtml==null || _item.developAltHtml=='' ? '无' : _item.developAltHtml)+"</div>";
		str += "<div id=\"a\" style=\"float:left;padding-left: 50px;\">远期版本：</div>";
		str += " <div id=\"b\" style=\"float:left;\" >"+(_item.forwardAlt==null ||_item.forwardAlt==''  ? '无' : _item.forwardAlt )+"</div>";
		str += "<div style=\"clear:both\"></div><!--这个层很有用，必须要，否则可能不兼容。--> ";
		str += "</div>";
		str += "</td>";
		str += " </tr>";
		str += " </table>";
		str += " </div>";
		return str;
}

$.ajax({
	url:context + '/metadataAlt.do?method=altMetaItem',
	method: 'POST',
	async:false,
	data:{isnew:'true'},
	dataType :'json',
	success: function(response) {
		var sysAlt = response.data;
        var str = "";
        
		for(var i = 0;i<sysAlt.length;i++){
			var item = sysAlt[i];
			str += getSysAltDom(item,i);
		}
		$("#change_16").html(str);
		
	},fail : function(){
		alert("cards load fail!");
	}
});
/****************************************跳转系统版本变更详情*************************/
function reqAltMetaDet(dbId,dbName,optId,altSts){
	parent.changeMainPage("page/mp/metaAlt/metaAltDetail.jsp?dbId="+dbId+"&dbName="+dbName+"&optId="+optId+"&altSts="+altSts);

}
/***************************************系统版本更多******************************/
function reqSysMoreVerPage(dbId,dbName){
	parent.changeMainPage("page/mp/metaAlt/moreSysVer.jsp?dbId="+dbId+"&dbName="+dbName);
}
/*************************************点击版本跳转详情**********************************/

function reqSysDetailPage(dbId,optId){
	parent.changeMainPage("page/mp/version/sysVersion.jsp?dbId="+dbId+"&optId="+optId);
}

function reqSysVersionPage(dbCode,optId){
	var iframe = document.getElementById('formIfr').contentWindow;
	iframe.reqPageForm.target="_reqVerDetail";
	iframe.reqPageForm.dbId.value=dbCode;
	iframe.reqPageForm.optId.value=optId;
	iframe.reqPageForm.method.value='reqSysVersionPage';
	iframe.reqPageForm.submit();
}

function reqUploadPage(){
	parent.changeMainPage("page/mp/metaAlt/impMetaItemPage.jsp");

}
/*************************************************饼图***************************************/

sweetTitles.setTipElements('.uploadExcelImg img');//显示的位置：id或者class名称
sweetTitles.init();