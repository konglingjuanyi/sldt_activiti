function sysTabContextMenu() {
	 	$.contextMenu({
            selector: '#verTableData tr',  
            callback: function (key, options) {  
                var m = "global: " + key;  
                window.console && console.log(m) || alert(m);  
            },  
            items: {  
                "dispatchImpact": {  
                    name: "影响分析",  
                    icon: "edit",  
                    callback: function (key, options) {  
                    	analyseAltMeta('',$(this).attr('abbr'),key);
                    }  
                },
                "sep1": "---------",
                "dispatchLineage": { name: "血缘分析", icon: "cut",
                	callback: function (key, options) {  
                    	analyseAltMeta('',$(this).attr('abbr'),key);
                    }  
                }
            }  
        });
};

function sysColContextMenu() {
 	$.contextMenu({
        selector: '#verColumnData tr',  
        callback: function (key, options) {  
            var m = "global: " + key;  
            window.console && console.log(m) || alert(m);  
        },  
        items: {  
            "dispatchImpact": {  
                name: "影响分析",  
                icon: "edit",  
                callback: function (key, options) {  
                	analyseAltMeta('',$(this).attr('abbr'),key);
                }  
            },
            "sep1": "---------",
            "dispatchLineage": { name: "血缘分析", icon: "cut",
            	callback: function (key, options) {  
                	analyseAltMeta('',$(this).attr('abbr'),key);
                }  
            },
            "sep2": "---------",
            "dsRef": { name: "标准引用", icon: "cut",
            	callback: function (key, options) {  
            		showDSItem($(this).attr('abbr'),$(this).attr('refDs'));
                }  
            }
        }  
    });
};

function showDSItem(metaContext,dsRef){
	var html = "";
	html = html+"<div class=\"showDivAltDetail\" style='width:380px;height:185px;font-size:12px;'>";
	html = html+"<div class=\"okAltTile\" style='padding-top:10px;'>";
	html = html+"<b>数据标准引用</b>";
	html = html+"</div>";
	html = html+"<div style='padding-bottom:20px;'>";
	html = html+"<hr style='background-color:#ed1a2e;padding-top:2px;'>";
	html = html+"</div>";
	
	html = html+"<div style='padding-top:0px;height:80px;' id='impMsg'>";
	html = html+="<div><b>上下文：</b>"+metaContext+"</div>";
	html = html+="<div style='padding-top:10px;'><b>标准引用说明：</b>"+dsRef+"</div>";
	html = html+"</div>";
	
	html = html+"<div class='showDivBut' style='padding-top:0px;'>";
	html = html+"<div style='padding-left:150px;float:left;'>";
	html = html+"<button id=\"pagebtn\" class='but' onclick=\"\">&nbsp;取 消  </button>";
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
		page: {
		    html: html
		}
	});
	
	//自设关闭
	$('#pagebtn').on('click', function(){
		layer.close(pageii);
	});
}

function analyseAltMeta(classifer,metaContext,type){
	$.ajax({
		url:context+'/' + 'analyse.do?method=getInstanceId',
		method: 'POST',
		data:{context:metaContext,classifer:classifer,dbCode:dbId},
		async:false,
		dataType :'json',
		success: function(response) {
			var objdata = response.data;
			if(objdata.length>0){
				var mmid = objdata[0].INSTANCE_ID;
				var url = "";
				var name = "";
				if(type=='dispatchLineage'){
					url = "/metamanager/analyseMCommand.do?invoke=dispatchLineage&MDID="+mmid;
					name = "元数据血统分析";
				}else{
					url = "/metamanager/analyseMCommand.do?invoke=dispatchImpact&MDID="+mmid;
					name = "元数据影响分析";
				}
				$.layer({
				    type: 2,
				    border: [2, 1, '#ccc'],
				    title: "<b>"+name+"</b>",
				    shadeClose: false,
				    closeBtn: [0,true],
				    iframe: {src : url},
				    area: ['98%', '507px']
				});
			}else{
				layer.alert("分析失败或未找到映射关系！", 3);
			}
		},fail : function(){
			alert("cards load fail!");
		}
	});
}