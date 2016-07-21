    
   /**
    * 创建一个闭包,编写一个jquery 插件，支持选项卡翻页，关闭等功能
    * 命名为 sunlineTab
    * 版本号：v1.0
    * 作者：chenbo
    * 时间：2015-10-16 
    * @param $
    */
;!(function($) {
	
    "use strict";//声明为严格模式
    
	var SunlineTab = function(element, options){
		 this.$element  = $(element)
		 this.options   = $.extend({}, SunlineTab.DEFAULTS, options)
		 this.isLoading = false
	}
	
	SunlineTab.VERSION  = '1.0';

	SunlineTab.DEFAULTS = {
		renderTo: $(document.body),
		isClose:true,
		defaultIcon:'fa fa-database',
		defaultClose:false,//默认无关闭
		closeIcon:'closeTab fa fa-remove',
		hoverCloseIcon:'closeTab fa fa-times-circle',
		cls:"closeTab",
		currentTabId:'',
		maxSize:20,//设置默认支持的最大打开个数
		off_left:0,//偏移量 向左
		off_right:0,//偏移量 向右
		step_rang:20,//移动量，默认20，单位px
		size:1
	}
	
	SunlineTab.prototype = {
		initTabs:function(){
			this.$element.parent().prepend('<div class="f-tabstrip-scroller f-tabstrip-scroller-start"><i class="fa fa-chevron-left"></i></div>');
			this.$element.on("click", "a", function (e) {
			    e.preventDefault();
			    $(this).tab('show');
			 });
			this.$element.parent().append('<div class="f-tabstrip-scroller f-tabstrip-scroller-end"><i class="fa fa-chevron-right"></i></div>');
			this.registerScrollerEvent();
		},
		getTab:function(tabId){
			return this.$element.find('a[href="#' + tabId + '"]');
		},
	    addTab:function(tabId,tabUrl,tabTitle,tabIcon){
	    	var tabIconClass = this.options.defaultIcon;
	    	if(tabIcon!=null && tabIcon!=""){
	    		tabIconClass = tabIcon;
	    	}
	    	var isClose = this.options.defaultClose;//如果未设置关闭标识，则使用默认值
	    	this.insertTab(tabId,tabUrl,tabTitle,tabIconClass,isClose);
	    },
	    showTab:function(tabId){
	    	this.getTab(tabId).tab('show');;
	    },
	    insertTab:function(tabId,tabUrl,tabTitle,tabIconClass,closeFlag){
	    	if(closeFlag){
	    		var tabCloseClass = this.options.closeIcon;
	    		$('.nav-tabs').append('<li><a href="#' + tabId + '"><i class="'+tabIconClass+'"></i> '+tabTitle+'&nbsp;&nbsp; <i class="'+tabCloseClass+'" ></i></a></li>');
				this.registerCloseEvent(tabId);
				this.registerRightMenuEvent(tabId);
	    	}else{
	    		$('.nav-tabs').append('<li><a href="#' + tabId + '"><i class="'+tabIconClass+'"></i> '+tabTitle+'&nbsp;&nbsp;</a></li>');
	    	}
	    	$('.tab-content').append('<div class="tab-pane" id="' + tabId + '"></div>');
			if(tabUrl && tabUrl!=""){
				 $("#" + tabId).html("<div style='tab_top'><iframe id='" + tabId + "_f' name='" + tabId + "_f' frameborder='0' height='500px' scrolling='yes' width='100%' src='"+tabUrl+"'></iframe></div>");
			}
			this.changeTabSyle();
			$(this).tab('show');
			this.showTab(tabId);
	    },
	    changeTabSyle:function(){
	    	//调整tab样式
			var p_width = this.$element.parent().width();//获取父元素宽度
			var child_width = 0;
			var childElem = this.$element.children();
			for(var i=0;i<childElem.length;i++){
				child_width += $(childElem[i]).width()+2; 
			}
			this.options.off_left = p_width-child_width;//存储偏移数据
			if(p_width<child_width){//如果子元素占满
				this.$element.css('left',this.options.off_left);
				this.$element.css('width',child_width);
				this.$element.parent().find(".f-tabstrip-scroller").addClass('f-tabstrip-scroller-display');
			}else{
				this.$element.css('left',0);
				this.$element.css('width',p_width);
				this.$element.parent().find(".f-tabstrip-scroller").removeClass('f-tabstrip-scroller-display');
			}
	    },
	    getCurrSize: function () {
	    	return this.$element.children().length;
	    },
	    closeSelf:function(tabId){
	    	//alert("关闭本身："+tabId);
	    	$("#sunlinetabRMenu").hide();
	    	this.$element.find('a:last').tab('show'); // 显示最后一个tab
	    	var tabContentId = this.getTab(tabId).attr("href");
	    	this.getTab(tabId).parent().remove();//删除标题
	    	$(tabContentId).remove(); //删除内容页面
	    	this.$element.find('a:last').tab('show'); // 显示最后一个tab
	  	    this.changeTabSyle();
	    },
	    closeOther:function(tabId){
	    	//alert("关闭Other："+tabId);
	    	$("#sunlinetabRMenu").hide();
	    	var childElem = this.$element.children();
			for(var i=0;i<childElem.length;i++){
				var aElem =  $(childElem[i]).find('a[href="#' + tabId + '"]');
				if(aElem.length==0){//不为本身，则关闭
					var aElems = $(childElem[i]).find('a'),
					   closeIcon = this.options.closeIcon,
					   closeElem = $(childElem[i]).find("i[class='"+closeIcon+"']");
					if(aElems.length>0 && closeElem.length>0){//如果存在可关闭的标签，则关闭
						var tabContentId = $(aElems[0]).attr("href");
						$(childElem[i]).remove();
						$(tabContentId).remove(); //删除内容页面 
					}
				}
			}
			this.showTab(tabId);
			this.changeTabSyle();//重新设置插件样式
	    },
	    closeAll:function(tabId){
	    	//alert("关闭all："+tabId);
	    	$("#sunlinetabRMenu").hide();
	    	this.closeOther(tabId);
	    	this.closeSelf(tabId);
	    },
	    //获取tab支持的最大打开个数
	    getTabMaxSize:function(){
	    	var size = this.options.maxSize;
			if(size<=1) size= 1;
			return size;
	    },
	    registerCloseEvent:function(tabId){
	    	var that = this,
	    	    hoverCloseIcon = this.options.hoverCloseIcon,
	    	    closeIcon = this.options.closeIcon;
	    	var closeElem = this.getTab(tabId).find("i[class='"+closeIcon+"']");

	    	closeElem.on('mouseover',function(){
	  	    	closeElem.attr("class", hoverCloseIcon);
	    	});
	    	closeElem.on('mouseout',function(){
	  	    	closeElem.attr("class", closeIcon);
	    	});
	    	closeElem.on('click',function(){
	    		 var tabContentId = closeElem.parent().attr("href");
		  		    closeElem.parent().parent().remove(); //remove li of tab
		  	        that.$element.find('a:last').tab('show'); // 显示最后一个tab
		  	        $(tabContentId).remove(); //remove respective tab content
		  	        that.changeTabSyle();
	    	});
	    	
	    	/*closeElem.click(function(){
	  		    var tabContentId = closeElem.parent().attr("href");
	  		    closeElem.parent().parent().remove(); //remove li of tab
	  	        that.$element.find('a:last').tab('show'); // 显示最后一个tab
	  	        $(tabContentId).remove(); //remove respective tab content
	  	    });
	  	    closeElem.mouseover(function(){
	  	    	closeElem.attr("class", hoverCloseIcon);
	  	    });
	  	    closeElem.mouseout(function(){
	  	    	closeElem.attr("class", closeIcon);
	  	    });*/
	    },
	    registerRightMenuEvent:function(tabId){
	    	var that = this,
    	        tabTitleElem = this.getTab(tabId);
	    	
	    	tabTitleElem.on('mousedown',function(e){
	    		 if(3 == e.which){ 
		                //alert('这 是右键单击事件'+tabId); 
		                var y = e.pageY +15;
		                var x = e.pageX -10;
		                if ($("#sunlinetabRMenu").length > 0 ) {
		                	$("#sunlinetabRMenu").remove();//存在则销毁
		                	//$("#tabRMenu").css({position:'absolute',height:'30px',width:'60px',top:y+'px',left:x+'px',display:'block'}).appendTo('body');
		                }
			             var rMenuHTMT = '<div id="sunlinetabRMenu" style="position: absolute; z-index: 500; left: '+x+'px; top:'+y+'px; display: block;">'
							                	+'<ul class="sunline-rmenu">'
							        		         +'<li id="closeSelf"><a href="javascript:void(0)"><i class="fa fa-circle-o icolor"></i> 关 闭</a></li>'
							        		         +'<li id="closeOther"><a href="javascript:void(0)"><i class="fa fa-dot-circle-o icolor"></i> 关闭其他</a></li>'
							        		         +'<li id="closeAll"><a href="javascript:void(0)"><i class="fa fa-circle icolor"></i> 关闭所有</a></li>'
							        	         +'</ul>'
							        	      +'</div>';
			                $(rMenuHTMT).appendTo('body');
			                that.options.currentTabId = tabId;
			                $("#closeSelf").on('click',function(){
			                	that.closeSelf(tabId);
			   	    	    });
			                $("#closeOther").on('click',function(){
			                	that.closeOther(tabId);
			   	    	    });
			                $("#closeAll").on('click',function(){
			                	that.closeAll(tabId);
			   	    	    });
			            $("body").bind("mousedown", that.registerBodyDownEvent);  
		           }else if(1 == e.which){ 
		                //alert('这 是左键单击事件'); 
		          } 
	    	}).bind('contextmenu',function(e){
	    	    e.preventDefault();
	    	    return false;
	    	});
	    	
	    },
	    registerBodyDownEvent:function(e){
	    	if($(event.target).parents("#sunlinetabRMenu").length>0){
	    		if($(event.target).parents("#closeSelf").length>0){
	    			this.closeSelf(this.options.currentTabId);
	    		}else if($(event.target).parents("#closeOther").length>0){
	    			that.closeOther(this.options.currentTabId);
	    		}else if($(event.target).parents("#closeAll").length>0){
	    			that.closeAll(this.options.currentTabId);
	    		}
	    	}else{ //点击到其他元素，去掉右键弹窗的关闭菜单内容，后期优化
	    		/*if($(event.target).parents(this.$element.id).length>0){
		    		alert(1);//$("#sunlinetabRMenu").hide();
	    		}else{
		    		$("#sunlinetabRMenu").hide();
	    		}*/
	    	}
	    	$("body").unbind("mousedown", this.registerBodyDownEvent);
	    	/*if (!(event.target.id == "metadataType" || event.target.id == "mdpTreeContent" || $(event.target).parents("#mdpTreeContent").length>0)) {
	    		hideMdpTreeMenu();
	    		$("body").unbind("mousedown", onBodyDown);
	    	}*/
	    },
	    registerScrollerEvent:function(){
	    	var that = this,
	    	    scrollerElem = this.$element.parent().find(".f-tabstrip-scroller");
	    	scrollerElem.click(function(){
	    		if($(this).hasClass("f-tabstrip-scroller-start")) {
	    			if(that.options.off_left<=0){
	    				that.options.off_left += that.options.step_rang;
	    				if(that.options.off_left>0){
	    					that.options.off_left = 20;//去掉间隙
	    				}
	    			}else{
	    				//往前移的按钮变色
	    				return ;
	    			}
	    		}else if($(this).hasClass("f-tabstrip-scroller-end")){
	    			var pw = that.$element.parent().width();
	    			var domW = that.$element.width();
	    			if(that.options.off_left+domW<pw+20){//加上间隙，如果已经移动到最后一个，则不再移动
	    				return ;
	    			}
	    			that.options.off_left -= that.options.step_rang;
	    		}
    			that.$element.css('left',that.options.off_left);
    			return;
	    	});
	     }
	}
	
	 var oldTab = $.fn.sunlineTab;
	 
	 // sunline tab  
	 $.fn.sunlineTab = function(option) {
		 var args = Array.apply(null, arguments);
		 args.shift();//去掉第一个参数
	     var internal_return;
		 this.each(function() {
			    debug(this); //调试打印控制台信息，发布该插件后建议关闭该语句   
			    var $this = $(this); 
		        var data =  $this.data('sunlineTab');
		        // 如果没有初始化过, 就初始化它
		       //var options = typeof option == 'object' && option
	           if (!data) $this.data('sunlineTab',(data = new SunlineTab(this, typeof option == 'object'?option:null)));
	           if (typeof option === 'string' && typeof data[option] === 'function'){
	        	   //data[option](); 
	        	   internal_return = data[option].apply(data, args);
	           }
		    });
		 if (
			internal_return === undefined ||
			internal_return instanceof SunlineTab 
		 )
		 return this;
		 if (this.length > 1)
			throw new Error('Using only allowed for the collection of a single element (' + option + ' function)');
		 else
			return internal_return;
	 }
	 
	 // 私有函数：debugging    
	  function debug($obj) {
	    if (window.console && window.console.log)    
	      window.console.log('sunlineTab debug ...');    
	  };
	  
	  /**
	   * 暴露类名, 支持后期可以通过这个为插件做自定义扩展
       * 扩展方式: $.fn.sunlineTab.Constructor.newMethod = function(){}
       * 使用: $btn.sunlineTab("newMethod");
	   */
	 $.fn.sunlineTab.Constructor = SunlineTab;
	 
	 //无冲突处理
	 $.fn.sunlineTab.noConflict = function () {
	    $.fn.sunlineTab = oldTab;
	    return this
	 };
// 闭包结束    
})(jQuery);  
 
	