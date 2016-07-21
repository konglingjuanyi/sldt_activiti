$(document).ready(function() {
	
	$(".nav-in").find("a").click(function() {
		$(".nav-in").find("a").removeClass("cur");
		$(this).attr("class","cur");
		if($(this).attr('url')) {
			var tabUrl = $(this).attr('url');
			changeMainPage(tabUrl); 
		}
	});
	
});

/**
 * 根据传入的地址，调整主体区域的页面
 * @param tabUrl
 */
function changeMainPage(tabUrl,navId){
	if(navId!=null){
		$(".nav-in").find("a").removeClass("cur");
		$("#"+navId).attr("class","cur");
	}
    if(tabUrl.indexOf('page/mp')>=0){
		tabUrl = context+app_mb+"/"+tabUrl;
	}else{
		tabUrl = context+app_mb+"/page/mp/"+tabUrl;
	}
	$('#mainArea').attr("src",tabUrl); 
}


function iFrameHeight(iframeId) {   
	var ifm = document.getElementById(iframeId);
	if(ifm){
		ifm.height = $(window).height()+2000;  
	}
}