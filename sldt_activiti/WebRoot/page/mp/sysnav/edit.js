var altColor="#ffff66";

//需要首先通过Javascript来解决内容部分奇偶行的背景色不同
//简化的ready写法；以往写法：$(document).ready(function(){	});
function editMetaItem(){
	var defaultColor="#f5f5f5";
	//找到表格的内容区域中所有的奇数行
	//使用even是为了把通过tbody tr返回的所有tr元素中，
	//在数组里面下标是偶数的元素返回，因为这些元素，
	//实际上才是我们期望的tbody里面的奇数行
	//我们需要找到所有的td单元格
	var numTd = $("tbody td");
	//给这些单元格注册鼠标点击的事件
	numTd.dblclick(function() {	
		//找到当前鼠标点击的td,this对应的就是响应了click的那个td
		var tdObj = $(this);
		if (tdObj.children("textarea").length > 0||tdObj.context.id==null||tdObj.context.id=='') {
			//当前td中input，不执行click处理
			return false;
		}
		var text = tdObj.html(); 
		var abbr = tdObj.attr("abbr");
		//清空td中的内容
		tdObj.html("");
		//创建一个文本框
		//去掉文本框的边框
		//设置文本框中的文字字体大小是16px
		//使文本框的宽度和td的宽度相同
		//设置文本框的背景色
		//需要将当前td中的内容放到文本框中
		//将文本框插入到td中
		var inputObj = $("<textarea></textarea>").css("border-width","0")
			.css("font-size","12px").width(tdObj.width()).height(tdObj.height()).val(text).appendTo(tdObj);
		//是文本框插入之后就被选中
		inputObj.trigger("focus").trigger("select");
		inputObj.click(function() {
			return false;
		});
		//处理文本框上回车和esc按键的操作
		inputObj.keyup(function(event){
			//获取当前按下键盘的键值
			var keycode = event.which;
			//处理回车的情况
			if (keycode == 13) {
				//获取当当前文本框中的内容
				var inputtext = $(this).val();
				if(abbr==inputtext){
					tdObj.css("background-color",defaultColor);
					delObj(tdObj);
				}else{
					if(inputtext!=text){
						tdObj.css("background-color",altColor);
						setDivAltCount();
					}
				}
				//将td的内容修改成文本框中的内容
				tdObj.html(inputtext);
				setDivAltCount();
			}
			//处理esc的情况
			if (keycode == 27) {
				//将td中的内容还原成text
				tdObj.css("background-color",defaultColor);
				tdObj.html(abbr);
				delObj(tdObj);
				setDivAltCount();
			}
		});
		inputObj.blur(function(event){
			//获取当当前文本框中的内容
			var inputtext = $(this).val();
			if(abbr==inputtext){
				tdObj.css("background-color",defaultColor);
				delObj(tdObj);
			}else{
				if(inputtext!=text){
					tdObj.css("background-color",altColor);
				}
			}
			tdObj.html(inputtext);
			setDivAltCount();
		});
	});
};

//需要首先通过Javascript来解决内容部分奇偶行的背景色不同
//简化的ready写法；以往写法：$(document).ready(function(){	});
function editMetaUl(){
	var defaultColor_cor = "#FFFFFF";
	//找到表格的内容区域中所有的奇数行
	//使用even是为了把通过tbody tr返回的所有tr元素中，
	//在数组里面下标是偶数的元素返回，因为这些元素，
	//实际上才是我们期望的tbody里面的奇数行
	//我们需要找到所有的td单元格
	var numTd = $("ul li");
	//给这些单元格注册鼠标点击的事件
	numTd.dblclick(function() {	
		//找到当前鼠标点击的td,this对应的就是响应了click的那个td
		var tdObj = $(this);
		if (tdObj.children("textarea").length > 0||tdObj.context.id==null||tdObj.context.id=='') {
			//当前td中input，不执行click处理
			return false;
		}
		var text = tdObj.html(); 
		var abbr = tdObj.attr("abbr");
		if(tdObj.attr("abbr")==null){
			return;
		}
		//清空td中的内容
		tdObj.html("");
		//创建一个文本框
		//去掉文本框的边框
		//设置文本框中的文字字体大小是16px
		//使文本框的宽度和td的宽度相同
		//设置文本框的背景色
		//需要将当前td中的内容放到文本框中
		//将文本框插入到td中
		var inputObj = $("<textarea></textarea>").css("border-width","0")
			.css("font-size","12px").width(tdObj.width()).height(tdObj.height()).val(text).appendTo(tdObj);
		//是文本框插入之后就被选中
		inputObj.trigger("focus").trigger("select");
		inputObj.click(function() {
			return false;
		});
		//处理文本框上回车和esc按键的操作
		inputObj.keyup(function(event){
			//获取当前按下键盘的键值
			var keycode = event.which;
			//处理回车的情况
			if (keycode == 13) {
				//获取当当前文本框中的内容
				var inputtext = $(this).val();
				if(abbr==inputtext){
					tdObj.css("background-color",defaultColor_cor);
					delDbObjAltData(tdObj);
				}else{
					if(inputtext!=text){
						tdObj.css("background-color",altColor);
						setDivAltCount();
					}
				}
				//将td的内容修改成文本框中的内容
				tdObj.html(inputtext);
				setDivAltCount();
			}
			//处理esc的情况
			if (keycode == 27) {
				//将td中的内容还原成text
				tdObj.css("background-color",defaultColor_cor);
				tdObj.html(abbr);
				delDbObjAltData(tdObj);
				setDivAltCount();
			}
		});
		inputObj.blur(function(event){
			//获取当当前文本框中的内容
			var inputtext = $(this).val();
			if(abbr==inputtext){
				tdObj.css("background-color",defaultColor_cor);
				delDbObjAltData(tdObj);
			}else{
				if(inputtext!=text){
					setDivAltCount();
					tdObj.css("background-color",altColor);
				}
			}
			tdObj.html(inputtext);
			setDivAltCount();
		});
	});
};


getHexBackgroundColor = function(other) { 
	var rgb = other.css('background-color');
	if(!-[1,]){
	}else{
		 rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
		 function hex(x) {
		   	return ("0" + parseInt(x).toString(16)).slice(-2);
		 } 
		 rgb= "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]); 
	}
	return rgb; 
};