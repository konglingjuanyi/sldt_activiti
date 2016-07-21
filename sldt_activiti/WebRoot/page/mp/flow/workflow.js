/**
 * 获取元素的outerHTML
 */
$.fn.outerHTML = function() {

    // IE, Chrome & Safari will comply with the non-standard outerHTML, all others (FF) will have a fall-back for cloning
    return (!this.length) ? this : (this[0].outerHTML ||
    (function(el) {
        var div = document.createElement('div');
        div.appendChild(el.cloneNode(true));
        var contents = div.innerHTML;
        div = null;
        return contents;
    })(this[0]));
    
};




function graphTrace(options){
	var actSts = $(this).attr('actSts');
	if(actSts != 1){
		var actId = $(this).attr('pid');
		$.ajax({
			url: context+'/flow.do',
			type: 'post',
			data: {method:'getHistory',actId:actId},
			dataType: 'json',
			success: function(response){
				var data = response.data;
				var html = "";
				html = html+"<div class=\"showDivAltDetail\">";
				html = html+"<div style='padding:0 0 10px 0;'>";
				html = html+"<table class=\"data_table_alt_detail\" style=\"width: 590px;\">";
				html = html+"<colgroup>";
				html = html+"<col width=15% ></col>";
				html = html+"<col width=25%></col>";
				html = html+"<col width=15%></col>";
				html = html+"<col width=45%></col>";
				
				html = html+"</colgroup>";
				html = html+"<tbody>";
				html = html+"<tr>";
				html = html+"<th>审核人</th>";
				html = html+"<th>审核时间</th>";
				html = html+"<th>审核结果</th>";
				html = html+"<th>审核意见</th>";
				html = html+"</tr>";
				$.each(data,function(i,val){
					html = html+"<tr>";
					html = html+"<td>"+val.auditor+"</td>";
					html = html+"<td>"+val.auditTime+"</td>";
					html = html+"<td>"+val.auditResult+"</td>";
					html = html+"<td>"+val.auditOpinion+"</td>";
					html = html+"</tr>";
				})
				html = html+"</tbody>";
				html = html+"</table>";
				html = html+"</div>";
				html = html+"</div>";
				var pageii = $.layer({
					type: 1,
					title: '<b>审核日志</b>',
					area: ['auto', 'auto'],
					border: [0], //去掉默认边框
					shade: [0], //去掉遮罩
					closeBtn: [0, true], //去掉默认关闭按钮
					page: {
					    html: html
					}
				});
				//自设关闭
				$('#pagebtn').on('click', function(){
					layer.close(pageii);
				});
			}
		});
	}else{
		var _defaults = {
		        srcEle: this,
		        pid: $(this).attr('pid')
		    };
		var opts = $.extend(true, _defaults, options);
		
		 var imageUrl = context + "/flow.do?method=process-instance&pid=" + opts.pid + "&type=image";
		 
		/* if ($('#workflowTraceDialog').length == 0) {
	         $('<div/>', {
	             id: 'workflowTraceDialog',
	            // title: '查看流程（按ESC键可以关闭）<button id="changeImg">如果坐标错乱请点击这里</button>',
	             title: '查看流程-----------',
	             html: "<iframe frameBorder='0' width='100%' height='100%' src='" + imageUrl + "' style='left:0px; top:0px;' />"
	         }).appendTo('body');
	     } else {
	         $('#workflowTraceDialog iframe').attr('src', imageUrl);
	     }
		 // 打开对话框
	    $('#workflowTraceDialog').dialog({
	        modal: true,
	        resizable: false,
	        dragable: false,
	        open: function() {
	           // $('#workflowTraceDialog').dialog('option', 'title', '查看流程（按ESC键可以关闭）<button id="changeImg">如果坐标错乱请点击这里</button>');
	        	 $('#workflowTraceDialog').dialog('option', 'title', '查看流程');
	        	 $('#workflowTraceDialog .ui-accordion-content').height(800);
	        	//$('#workflowTraceDialog').css('padding', '0.2em');
	           // $('#workflowTraceDialog .ui-accordion-content').css('padding', '0.2em').height($('#workflowTraceDialog').height() - 75);

	        },
	        close: function() {
	            $('#workflowTraceDialog').remove();
	        },
	        width: document.documentElement.clientWidth * 0.65,
	        height: document.documentElement.clientHeight * 0.85
	    });*/
		 var height = $(window).height();
		 $.layer({
	 		type : 2,
	 		shade : [0.5 , '#000' , true],
	 		shadeClose : true,
	 		border : [!0],
	 		title : false,
	 		offset : ['25px',''],
	 		area : ['60%', (height - 50)+'px'],
	 		iframe : {src : imageUrl}
	 	});
	}
}

function graphTrace_1(options) {
    var _defaults = {
        srcEle: this,
        pid: $(this).attr('pid')
    };
    var opts = $.extend(true, _defaults, options);

   /* // 处理使用js跟踪当前节点坐标错乱问题
    $('#changeImg').live('click', function() {
        $('#workflowTraceDialog').dialog('close');
        if ($('#imgDialog').length > 0) {
            $('#imgDialog').remove();
        }
        $('<div/>', {
            'id': 'imgDialog',
            title: '此对话框显示的图片是由引擎自动生成的，并用红色标记当前的节点',
            html: "<img src='" + context + '/workflow/process/trace/auto/' + opts.pid + "' />"
        }).appendTo('body').dialog({
            modal: true,
            resizable: false,
            dragable: false,
            width: document.documentElement.clientWidth * 0.95,
            height: document.documentElement.clientHeight * 0.95
        });
    });*/

    // 获取图片资源
    var imageUrl = context + "/flow.do?method=process-instance&pid=" + opts.pid + "&type=image";
    $.getJSON(context + '/flow.do?method=trace&pid=' + opts.pid, function(infos) {

        var positionHtml = "";

        // 生成图片
        var varsArray = new Array();
        $.each(infos, function(i, v) {
            var $positionDiv = $('<div/>', {
                'class': 'activity-attr'
            }).css({
                position: 'absolute',
                left: (v.x - 1),
                top: (v.y - 1),
                width: (v.width - 2),
                height: (v.height - 2),
                backgroundColor: 'black',
                opacity: 0,
                zIndex: $.fn.qtip.zindex - 1
            });

            // 节点边框
            var $border = $('<div/>', {
                'class': 'activity-attr-border'
            }).css({
                position: 'absolute',
                left: (v.x - 1),
                top: (v.y - 1),
                width: (v.width - 4),
                height: (v.height - 3),
                zIndex: $.fn.qtip.zindex - 2
            });

            if (v.currentActiviti) {
                $border.addClass('ui-corner-all-12').css({
                  //  border: '3px solid red'
                });
            }
            positionHtml += $positionDiv.outerHTML() + $border.outerHTML();
            varsArray[varsArray.length] = v.vars;
        });

        if ($('#workflowTraceDialog').length == 0) {
            $('<div/>', {
                id: 'workflowTraceDialog',
               // title: '查看流程（按ESC键可以关闭）<button id="changeImg">如果坐标错乱请点击这里</button>',
                title: '查看流程-----------',
                html: "<div><img src='" + imageUrl + "' style='left:0px; top:0px;' />" +
                "<div id='processImageBorder'>" +
                positionHtml +
                "</div>" +
                "</div>"
            }).appendTo('body');
        } else {
            $('#workflowTraceDialog img').attr('src', imageUrl);
            $('#workflowTraceDialog #processImageBorder').html(positionHtml);
        }

        // 设置每个节点的data
        $('#workflowTraceDialog .activity-attr').each(function(i, v) {
            $(this).data('vars', varsArray[i]);
        });

        // 打开对话框
        $('#workflowTraceDialog').dialog({
            modal: true,
            resizable: false,
            dragable: false,
            open: function() {
               // $('#workflowTraceDialog').dialog('option', 'title', '查看流程（按ESC键可以关闭）<button id="changeImg">如果坐标错乱请点击这里</button>');
            	 $('#workflowTraceDialog').dialog('option', 'title', '查看流程');
            	$('#workflowTraceDialog').css('padding', '0.2em');
                $('#workflowTraceDialog .ui-accordion-content').css('padding', '0.2em').height($('#workflowTraceDialog').height() - 75);

                // 此处用于显示每个节点的信息，如果不需要可以删除
                $('.activity-attr').qtip({
                    content: function() {
                        var vars = $(this).data('vars');
                        var tipContent = "<table class='need-border'>";
                        $.each(vars, function(varKey, varValue) {
                            if (varValue) {
                                tipContent += "<tr><td class='label'>" + varKey + "</td><td>" + varValue + "<td/></tr>";
                            }
                        });
                        tipContent += "</table>";
                        return tipContent;
                    },
                    position: {
                        at: 'bottom left',
                        adjust: {
                            x: 3
                        }
                    }
                });
                // end qtip
            },
            close: function() {
                $('#workflowTraceDialog').remove();
            },
            width: document.documentElement.clientWidth * 0.95,
            height: document.documentElement.clientHeight * 0.95
        });

    });
}
