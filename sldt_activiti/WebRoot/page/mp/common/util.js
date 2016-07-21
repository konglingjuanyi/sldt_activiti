/**
 * 表格背景颜色，每隔一行就显示不同的颜色
 * @param tableId 表格Id
 */
function changeTableBackground(tableId){
	$("#"+tableId+" tr").each(function(i, item){
		if(i%2==0){
			$(this).css("backgroundColor","#F5F5F5");
		}
	});
}

/**
 * 鼠标放上去表格行的颜色改变,还需要在表格的css文件加上样式，divid是表格的id或者class
 * divid tr.over td{
	        background:#e1e1e1;
 *}
 * @param divid
 */
function mouseTabRowsColorId(divId){
	$(divId+" tr").mouseover(function(){  //."+divClass+" table tr:gt(0)
		//如果鼠标移到class为stripe的表格的tr上时，执行函数  
		$(this).addClass("over");}).mouseout(function(){  
			//给这行添加class值为over，并且当鼠标一出该行时执行函数  
			$(this).removeClass("over");
		}); //移除该行的class 
}


function hidedetails(){
	$("#details").hide();
}

function showdetails(thisObj,orderid){
	var d = $(thisObj);
	var pos = d.offset();
	var t = pos.top + d.height() - 230; // 弹出框的上边位置
	            var l = pos.left + d.width() - 100;  // 弹出框的左边位置
	            $("#details").css({ "top": t, "left": l }).show();
	$("#details").html($("#box"+orderid).html());
}

/**
 * 日期格式化
 * @param format
 * @returns
 * new Date().format("MM/yyyy/dd")
 */
Date.prototype.format = function(format){
	var o = {
	"M+" : this.getMonth()+1, //month
	"d+" : this.getDate(), //day
	"h+" : this.getHours(), //hour
	"m+" : this.getMinutes(), //minute
	"s+" : this.getSeconds(), //second
	"q+" : Math.floor((this.getMonth()+3)/3), //quarter
	"S" : this.getMilliseconds() //millisecond
	};
	
	if(/(y+)/.test(format)) {
	format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}
	
	for(var k in o) {
	if(new RegExp("("+ k +")").test(format)) {
	format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
	}
	}
	return format;
} ;


/***************************************************界面输入框弹层*************************************/
function searchDiv(searchId,searchTextId,divSubmit,ajaxUrl){
	$(function(){
		//取得div层 
		var $search = $("#"+searchId+""); 
		//取得输入框JQuery对象 
		var $searchInput = $search.find("#"+searchTextId+""); 
		//关闭浏览器提供给输入框的自动完成 
		$searchInput.attr('autocomplete','off'); 
		//创建自动完成的下拉列表，用于显示服务器返回的数据,插入在搜索按钮的后面，等显示的时候再调整位置 
		var $autocomplete = $('<div class="autocomplete"></div>').hide().insertAfter("#"+divSubmit+""); 
		//清空下拉列表的内容并且隐藏下拉列表区 
		var clear = function(){ 
			$autocomplete.empty().hide(); 
		}; 
		//注册事件，当输入框失去焦点的时候清空下拉列表并隐藏 
		$searchInput.blur(function(){ 
			setTimeout(clear,500); 
		}); 
		//下拉列表中高亮的项目的索引，当显示下拉列表项的时候，移动鼠标或者键盘的上下键就会移动高亮的项目，想百度搜索那样 
		var selectedItem = null; 
		//timeout的ID 
		var timeoutid = null; 
		//设置下拉项的高亮背景 
		var setSelectedItem = function(item){ 
			//更新索引变量 
			selectedItem = item ; 
			//按上下键是循环显示的，小于0就置成最大的值，大于最大值就置成0 
			if(selectedItem < 0){ 
				selectedItem = $autocomplete.find('li').length - 1; 
			} 
			else if(selectedItem > $autocomplete.find('li').length-1 ) { 
				selectedItem = 0; 
			} 
			//首先移除其他列表项的高亮背景，然后再高亮当前索引的背景 
			$autocomplete.find('li').removeClass('highlight').eq(selectedItem).addClass('highlight'); 
		}; 
		var ajax_request = function(){ 
			//ajax服务端通信 
			$.ajax({ 
				'url':ajaxUrl, //服务器的地址 
				'data':{'likeName':$searchInput.val()}, //参数 
				'dataType':'json', //返回数据类型 
				'type':'POST', //请求类型 
				'success':function(data){ 
				if(data.data.length>0) { 
					//遍历data，添加到自动完成区 
					$.each(data.data, function(index,term) { 
						var searchValue = "";
						if(searchId=='columnPageDiv'){
							searchValue = term.COLNAME;
						}else{
							searchValue = term.TABNAME;
						}
					//创建li标签,添加到下拉列表中 
					$('<li></li>').text(searchValue).appendTo($autocomplete).addClass('clickable') 
					.hover(function(){ 
						//下拉列表每一项的事件，鼠标移进去的操作 
						$(this).siblings().removeClass('highlight'); 
						$(this).addClass('highlight'); 
						selectedItem = index; 
					},function(){ 
						//下拉列表每一项的事件，鼠标离开的操作 
						$(this).removeClass('highlight'); 
						//当鼠标离开时索引置-1，当作标记 
						selectedItem = -1; 
					}) 
					.click(function(){ 
						//鼠标单击下拉列表的这一项的话，就将这一项的值添加到输入框中 
						$searchInput.val(searchValue); 
						//清空并隐藏下拉列表 
						$autocomplete.empty().hide(); 
					}); 
					});//事件注册完毕 
					//设置下拉列表的位置，然后显示下拉列表 
					var ypos = $searchInput.position().top; 
					var xpos = $searchInput.position().left; 
					$autocomplete.css('width',($searchInput.width()+4)+"px"); 
					$autocomplete.css({'position':'relative','left':xpos-1 + "px",'top':ypos +"px"}); 
					setSelectedItem(0); 
					//显示下拉列表 
					$autocomplete.show(); 
				} 
			} 
			}); 
		}; 
		//对输入框进行事件注册 
		$searchInput.keyup(function(event) { 
			//字母数字，退格，空格 
			if(event.keyCode > 40 || event.keyCode == 8 || event.keyCode ==32) { 
				//首先删除下拉列表中的信息 
				$autocomplete.empty().hide(); 
				clearTimeout(timeoutid); 
				timeoutid = setTimeout(ajax_request,500); 
			} 
			else if(event.keyCode == 38){ 
				//上 
				//selectedItem = -1 代表鼠标离开 
				if(selectedItem == -1){ 
					setSelectedItem($autocomplete.find('li').length-1); 
				} 
				else { 
					//索引减1 
					setSelectedItem(selectedItem - 1); 
				} 
				event.preventDefault(); 
			} 
			else if(event.keyCode == 40) { 
				//下 
				//selectedItem = -1 代表鼠标离开 
				if(selectedItem == -1){ 
					setSelectedItem(0); 
				} 
				else { 
				//索引加1 
					setSelectedItem(selectedItem + 1); 
				} 
				event.preventDefault(); 
			} 
		}) 
		.keypress(function(event){ 
			//enter键 
			if(event.keyCode == 13) { 
				//列表为空或者鼠标离开导致当前没有索引值 
				if($autocomplete.find('li').length == 0 || selectedItem == -1) { 
					return; 
				} 
				$searchInput.val($autocomplete.find('li').eq(selectedItem).text()); 
				$autocomplete.empty().hide(); 
				event.preventDefault(); 
			} 
		}) 
		.keydown(function(event){ 
			//esc键 
			if(event.keyCode == 27 ) { 
				$autocomplete.empty().hide(); 
				event.preventDefault(); 
			} 
		})
		.focus(function(event){
			//setTimeout(ajax_request,100); 
		})
		; 
		//注册窗口大小改变的事件，重新调整下拉列表的位置 
		$(window).resize(function() { 
			try {
				var ypos = $searchInput.position().top; 
				var xpos = $searchInput.position().left; 
				$autocomplete.css('width',$searchInput.css('width')); 
				$autocomplete.css({'position':'relative','left':xpos + "px",'top':ypos +"px"}); 
			} catch (e) {
				// TODO: handle exception
			}
			
		}); 
	}); 
}


/**
 * 
 * 鼠标放上去出现提示,需要有title属性
 * 使用例：$(function(){
 *	sweetTitles.setTipElements('.version_view_compare img');//显示的位置：id或者class名称
 *	sweetTitles.init();
 *	});
 * <img  src="" title="显示的信息"/>
 */
var sweetTitles = {
	x : 10,	
	y : 20,	
	tipElements : "",
	getTipElements : function() {
	  return this.tipElements;
    },
    setTipElements : function(v) {
        this.tipElements = v;
    },
	init : function() {
		$(this.tipElements).mouseover(function(e){
			this.myTitle = this.title;
			//debugger;
			//this.myHref = this.href;
			//this.myHref = (this.myHref.length > 200 ? this.myHref.toString().substring(0,200)+"..." : this.myHref);
			this.title = "";
			var tooltip = "";
			if(this.myTitle == "")
			{
			    tooltip = "";
			}
			else
			{
			    tooltip = "<div id='tooltip'><p>"+this.myTitle+"</p></div>";
			}
			$('body').append(tooltip);
			$('#tooltip')
				.css({
					"opacity":"0.8",
					"top":(e.pageY+20)+"px",
					"left":(e.pageX+10)+"px"
				}).show('fast');	
		}).mouseout(function(){
			this.title = this.myTitle;
			$('#tooltip').remove();
		}).mousemove(function(e){
			$('#tooltip')
			.css({
				"top":(e.pageY+20)+"px",
				"left":(e.pageX+10)+"px"
			});
		});
	}
};
