<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<script type="text/javascript" src="<%=request.getContextPath()%>/public/slider/power-slider-debug.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/slider/power-slider.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/slider/power-slider.min.js"></script>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/sys_nav.css">
<SCRIPT type="text/javascript">
    var context = '<%=request.getContextPath()%>';

/*     !function(){
      var winWidth = window.innerWidth || document.documentElement && document.documentElement.clientWidth || 0,
        pageWidth = 'SMALL';

      // 获取 body 宽度设定。
          var pageWidthDef = {
      SMALL: 980,
      LARGE: 1180
    };

    if (winWidth >= 1280) {    // @media screen and (min-width: 1280px)
      pageWidth = 'LARGE';
    } else {                  // Default width.
      pageWidth = 'SMALL';
    }
  

      document.body.className += (' w-' + pageWidth.toLowerCase());
    }(); */
</SCRIPT>
</head>
<body>
<DIV class="publicizing-wrapper" alog-group="publicizing">
        <DIV id="publicizing">
            <DIV class="background">
                <UL></UL>
            </DIV>
            <DIV class="layout">
                <div class="slider" style="padding-left:0px; padding-top: 0px;"
                    id="car-img-left">
                    <ul class="sliderbox">
                        <div id="card_location">

                        </div>
                    </ul>
                    <ul class="slidernav"></ul>
                <div class="prev">&lt;</div>
                <div class="next">&gt;</div>
                </div>
            </DIV>
        </DIV>
    </DIV>
</body>
<script type="text/javascript">
function reqSysDetailPage(dbId,optId){
    parent.changeMainPage("page/mp/version/sysVersion.jsp?dbId="+dbId+"&optId="+optId,"nav_4");

}
//加载卡片信息
var getCarInfoStr = function(_item,i){
  var str = "";
  var bgcolor="#e62e31";
  var right=0;
  var padding_left = 10;
  if(((i)%4)==0){
      bgcolor = "#2e84e6";
  }else if(((i)%4)==1){
      padding_left = 10;
      bgcolor="#e62e31";
  }else if(((i)%4)==2){
      bgcolor="#ffad33";
      padding_left = 10;
  }else if(((i)%4)==3){
      bgcolor="#333333";
      padding_left = 10;
      right = 0;
  }
  var add = 0 ;
  var del = 0;
  var upd = 0;
  str = str + "<li class=\"card\" style=\"\">";
  str = str + "<div class='altDiv' >";
  str = str + "<table border=\"0\" width=285>";
  str = str + "<tr>";
  str = str + "<td bgcolor=\""+bgcolor+"\" style='position:relative'>";
  if(_item.isSnaCode=='1'){
      str = str + "<div title='"+_item.dbChName+"' class='altTile' >"+_item.title+"</div>";
  }else{
      str = str + "<div style=\"cursor: pointer;\" title='"+_item.dbChName+"' class='altTile' onclick=\"javascript:reqSysDetailPage('"+_item.dbId+"','') \">"+_item.title+"</div>";
  }
  if(_item.me){
      str = str +"<div class='altTitleImg' style='right: "+right+"px;'><img src=\"../imgs/card/sanjiao_03.png\"/></div>";
  }
  str = str + "</td>";
  str = str + "</tr>";
  str = str + "<tr>";
  str = str + "<td bgcolor=\""+bgcolor+"\" style=\"text-align: left;\">";
  if(_item.isSnaCode=='1'){
      if(_item.me){
          str = str + "<div class='altDes'>请点击<span style=\"cursor: pointer;text-decoration: underline;\" onclick=\"reqObjWin('reqAllSysVer')\"> 这里 </span>前往元数据版本功能中申请初始化当前系统元数据！</div>";
      }else{
          str = str + "<div class='altDes'>无初始化系统元数据！</div>";
      }
  }else{
      str = str + "<div class='altDes'>" + _item.remark + "</div>";
  }
  str = str + "</td>";
  str = str + "</tr>";
  str = str + "<tr>";
  str = str + "<td >";
  str = str + "<div class='altTwoSolid'>";
  str = str + "<div class=\"altDivType\">元数据类型</div>";
  str = str + "<div class=\"altDivSum\">新增</div>";
  str = str + "<div class=\"altDivSum\">修改</div>";
  str = str + "<div class=\"altDivSum\">删除</div>";
  str = str + "</div>";
  str = str + "</td>";
  str = str + "</tr>";
          
  str = str + "<tr>";
  str = str + "<td class='td_clas' abbr=\"<div style='font-size:14px;'>模式总数量</div><div style='font-size:20px;'>"+_item.altMap.schemaCount+"</div>\">";
  str = str + "<div style=\"padding-left: 10px;padding-right: 10px;\">";
  str = str + "<div class='altSolid'>";
  str = str + "<div class='altDivType'><img src='../imgs/Schema.gif' style='padding-top:2px;'><div style='float: right;margin-right:25px;'>模式</div></div>";
  add = 0 ;
  del = 0;
  upd = 0;
  if(_item.altMap.SCHEMA!=null){
      if(_item.altMap.SCHEMA.U!=null){
          upd = _item.altMap.SCHEMA.U;
      }
      if(_item.altMap.MODULE.D!=null){
          del = _item.altMap.SCHEMA.D;
      }
      if(_item.altMap.MODULE.I!=null){
          add = _item.altMap.SCHEMA.I;
      }
  }
  str = str + "<div class=\"altDivSumGreen\" >"+add+"</div>";
  str = str + "<div class=\"altDivSumYellow\">"+upd+"</div>";
  str = str + "<div class=\"altDivSumRed\" >"+del+"</div>";
  str = str + "</div>";
  str = str + "</div>";
  str = str + "</td>";
  str = str + "</tr>";
          
  str = str + "<tr>";
  str = str + "<td class='td_clas' abbr=\"<div style='font-size:14px;'>模块总数量</div><div style='font-size:20px;'>"+_item.altMap.moduleCount+"<div>\">";
  str = str + "<div style=\"padding-left: 10px;padding-right: 10px;\">";
  str = str + "<div class='altSolid'>";
  str = str + "<div class='altDivType'><img src='../imgs/ERModel.gif' style='padding-top:2px;'><div style='float: right;margin-right:25px;'>模块</div></div>";
  add = 0 ;
  del = 0;
  upd = 0;
  if(_item.altMap.MODULE!=null){
      if(_item.altMap.MODULE.U!=null){
          upd = _item.altMap.MODULE.U;
      }
      if(_item.altMap.MODULE.D!=null){
          del = _item.altMap.MODULE.D;
      }
      if(_item.altMap.MODULE.I!=null){
          add = _item.altMap.MODULE.I;
      }
  }
  str = str + "<div class=\"altDivSumGreen\" >"+add+"</div>";
  str = str + "<div class=\"altDivSumYellow\">"+upd+"</div>";
  str = str + "<div class=\"altDivSumRed\" >"+del+"</div>";
  str = str + "</div>";
  str = str + "</div>";
  str = str + "</td>";
  str = str + "</tr>";
  str = str + "<tr>";
  str = str + "<td class='td_clas' abbr=\"<div style='font-size:14px;'>字段级总数量</div><div style='font-size:20px;'>"+_item.altMap.tableCount+"</div>\">";
  str = str + "<div style=\"padding-left: 10px;padding-right: 10px;\">";
  str = str + "   <div class='altSolid' >";
  add = 0 ;
  del = 0;
  upd = 0;
  if(_item.altMap.TABLE!=null){
      if(_item.altMap.TABLE.U!=null){
          upd = _item.altMap.TABLE.U;
      }
      if(_item.altMap.TABLE.D!=null){
          del = _item.altMap.TABLE.D;
      }
      if(_item.altMap.TABLE.I!=null){
          add = _item.altMap.TABLE.I;
      }
  }
  str = str + "<div class='altDivType'><img src='../imgs/Table.gif' style='padding-top:2px;'><div style='float: right;margin-right:25px;'>表</div></div>";
  str = str + "<div class=\"altDivSumGreen\" >"+add+"</div>";
  str = str + "<div class=\"altDivSumYellow\">"+upd+"</div>";
  str = str + "<div class=\"altDivSumRed\" >"+del+"</div>";
  str = str + "</div>";
  str = str + "</div>";
  str = str + "</td>";
  str = str + "</tr>";
  str = str + "<tr>";
  str = str + "<td class='td_clas' abbr=\"<div style='font-size:14px;'>字段级总数量</div><div style='font-size:20px;'>"+_item.altMap.columnCount+"</div>\">";
  str = str + "<div style=\"padding-left: 10px;padding-right: 10px;\">";
  str = str + "<div class='altSolid'>";
  str = str + "<div class='altDivType'><img src='../imgs/Column.gif' style='padding-top:2px;'><div style='float: right;margin-right:25px;'>字段</div></div>";
  add = 0 ;
  del = 0;
  upd = 0;
  if(_item.altMap.COLUMN!=null){
      if(_item.altMap.COLUMN.U!=null){
          upd = _item.altMap.COLUMN.U;
      }
      if(_item.altMap.COLUMN.D!=null){
          del = _item.altMap.COLUMN.D;
      }
      if(_item.altMap.COLUMN.I!=null){
          add = _item.altMap.COLUMN.I;
      }
  }
  str = str + "<div class=\"altDivSumGreen\" >"+add+"</div>";
  str = str + "<div class=\"altDivSumYellow\">"+upd+"</div>";
  str = str + "<div class=\"altDivSumRed\" >"+del+"</div>";
  str = str + "</div>";
  str = str + "</div>";
  str = str + "</td>";
  str = str + "</tr>";
  str = str + "</table>";
  str = str + "</div>";
  str = str + "</li>";
  return str;
};


$.ajax({
  url:context + '/metadata.do?method=cardMeta',
      method : 'POST',
      async : false,
      dataType : 'json',
      data:{start:0,limit:10,isnew:'true'},
      success : function(response) {
          //var _dateques = response.data;
          var count = response.length;
          var str = "";//卡片代码字符串
          for ( var i = 0; i < count; i++) {
              //每页分三个卡片
              var _item = response[i];
              str += getCarInfoStr(_item, i);
          }
          //显示
          $("#card_location").html(str);
      },
      fail : function() {
          alert("cards load fail!");
      }
  });
$("#car-img-left").powerSlider({
  handle:"left",sliderNum:4
});

$(".td_clas").hover(function(){
  $(this).css('background-color','#ccc');
  cardItemTips($(this).attr('abbr'),$(this));
},function(){
  $(this).css('background-color','#FFFFFF');
  layer.closeTips();
}
);
function cardItemTips(msg,obj){
  layer.tips(msg, obj, {
      style: ['border:1px;background-color:#78BA32;font-weight:bolder;text-align: center; font-family: 宋体;color:#fff', '#78BA32'],
      maxWidth:200
  });
}

</script>
</html>