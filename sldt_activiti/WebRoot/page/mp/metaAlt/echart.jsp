
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>ECharts</title>


<script type="text/javascript" src="<%=request.getContextPath()%>/page/mp/metaAlt/echart/esl.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/public/jquery/jquery.js"></script>
</head>

<body>
</div>
	<div id="main<%=request.getParameter("num") %>"
		style="height: 150px; width: 400px; border: 1px solid #ccc;">
	</div>

<script type="text/javascript">
var context = '<%=request.getContextPath()%>';

var num = '<%=request.getParameter("num") %>';
var unc = '<%=request.getParameter("unc") %>';
var notDec = '<%=request.getParameter("notDec") %>';
var dec = '<%=request.getParameter("dec") %>';
var valid = '<%=request.getParameter("valid") %>';
if(num==null){
	num = 0;
}
if(unc==null){
	unc = 0;
}
if(notDec==null){
	notDec = 0;
}
if(dec==null){
	dec = 0;
}
if(valid==null){
	valid = 0;
}
var data = [];
if(unc==0&&notDec==0&&dec==0&&valid==0){
	data = [
            {value: 1, name:'无变更数据',
            	itemStyle : {
	                normal : {
	                	color:'#32CD00'
	                }
	            }
            }];	
}else{
	data = [
	            {value: unc, name:'未确认变更',
	            	itemStyle : {
		                normal : {
		                	color:'#ED1A2E'
		                }
		            }
	            },
	            {value:notDec, name:'未申报变更',
	            	itemStyle : {
		                normal : {
		                	color:'#FF6702'
		                }
		            }
	            },
	            {value:dec, name:'申报中变更',
	            	itemStyle : {
		                normal : {
		                	color:'#0EA3C7'
		                }
		            }
	            },
	            {value:valid, name:'已生效变更',
	            	itemStyle : {
		                normal : {
		                	color:'#32CD00'
		                }
		            }
	            }];	
}
//按需加载
//Step:3 conifg ECharts's path, link to echarts.js from current page.
//Step:3 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径
require.config({
	paths : {
		echarts : context+'/page/mp/metaAlt/echart/echarts', //echarts.js的路径
		'echarts/chart/pie' : context+'/page/mp/metaAlt/echart/chart/pie' 
	}
});
//Step:4 require echarts and use it in the callback.
//Step:4 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
require([ 'echarts', 'echarts/chart/pie' ],
	//回调函数
	DrawEChart
);
//渲染ECharts图表
function DrawEChart(ec) {
	  // 基于准备好的dom，初始化echarts图表
	  myChart = ec.init(document.getElementById('main'+num)); 
	  var option = {
	      title : {
	        text: '',
	        subtext: '',
	        x:'center'
	      },
	      tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	      },
	      legend: {
	        orient : 'vertical',
	        x : 'left',
	        data:['未确认变更','未申报变更','申报中变更','已生效变更'],
	        show:false
	      },
	      calculable : false,
	      series : [
	        {
	          name:'变更统计饼图',
	          type:'pie',
	          radius : '100%',
				    center: ['30%', '50%'],
	          itemStyle : {
	                normal : {
	                    label : {
	                        position : 'inner',
                       	show : true,
	                        formatter: '{d}%' 
	                    },
	                    labelLine : {
	                        show : false
	                    }
	                }
	            },
	          data:data
	        }
	      ]
	    };
	  
	  // 为echarts对象加载数据 
	  myChart.setOption(option); 
}

</script>
</html>
