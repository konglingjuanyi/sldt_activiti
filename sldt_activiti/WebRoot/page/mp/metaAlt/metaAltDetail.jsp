<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
var context = '<%=request.getContextPath()%>';
var dbId = '<%=request.getParameter("dbId")%>';
var dbName = '<%=request.getParameter("dbName")%>';
var optId = '<%=request.getParameter("optId")%>';
var altSts = '<%=request.getParameter("altSts")%>';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据管控应用门户-元数据变更明细</title>

<link href="<%=request.getContextPath()%>/page/mp/css/metadataChange.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/page/mp/css/metaAlteration.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/page/mp/css/common.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/page/mp/css/frame.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/page/mp/common/util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>
<!-- 调整表头样式 -->
<style type="text/css">
.xubox_shade, .xubox_layer{
position: fixed;
}
.xubox_layer{
top:300px !important;
}
</style>
</head>
<body style="background-color: #F5F5F5;">
     <div class="gf_all">
        <div class="center">
            <!-- 左边 -->
            <div class="center_left_content">
                <div id="alt_title">
                    <div id="alt_tile_1">
                        <b>元数据变更明细</b>
                    </div>
                </div>
                <div id="alt_hr">
                    <hr color="#ed1a2e">
                </div>
                <!-- 全部表格div -->
                <div id="change_table_column_info">
                    <div id="change_table_info">
                        <div id="change_16">
                            <div style="padding-top:20px;">
                                <table class="data_table_alt" style="width: 790px; padding-top: 50px;">
                                    <colgroup>
                                        <col width=5%></col>
                                        <col width=15%></col>
                                        <col width=12%></col>
                                        <col width=12%></col>
                                        <col width=5%></col>
                                        <col width=5%></col>
                                        <col width=7%></col>
                                        <col width=11%></col>
                                        <col width=10%></col>
                                        <col width=8%></col>
                                        <col width=8%></col>
                                    </colgroup>
                                    <tr>
                                        <td colspan="12">
                                            <div id="c" style="float: left;">
                                                <div id="sysName"
                                                    style="float: left; padding-left: 10px; width: 520px; font-size: 20px; text-align: left;">无</div>
                                                <div style="float: left; padding-left: 120px;">目标版本：</div>
                                                <div id="targetVerDate"
                                                    style="float: left; padding-left: 10px;">无</div>
                                                <div style="clear: both"></div>
                                                <!--这个层很有用，必须要，否则可能不兼容。-->
                                            </div>
                                         </td>
                                    </tr>
                                    <tr>
                                        <td colspan="12"
                                            style="text-align: left; padding: 0 0 0 10px;">
                                            <div id="c" style="float: left;">
                                                <div id="a" style="float: left;">近期已投产版本：</div>
                                                <div id="jqVerDate" style="float: left;">
                                                </div>
                                                <div style="float: left; padding-left: 40px;">开发中版本：</div>
                                                <div id="deveVerDate" style="float: left;">无</div>
                                                <div id="a" style="float: left; padding-left: 40px;">远期版本：</div>
                                                <div id="forVerDate" style="float: left;">无</div>
                                                <div style="clear: both"></div>
                                                <!--这个层很有用，必须要，否则可能不兼容。-->
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="12"
                                            style="text-align: left; padding: 0 0 0 10px;">
                                            <div style="float: left;font-size: 20px;">
                                                <b>变更历史</b>
                                            </div>
                                            <div style="float: right;padding-right: 20px;border: 0px solid red;position:relative;">
                                                <a style="cursor: pointer;" onclick="reqDecPage(dbId,optId)"><font color="#ffffff" style="position:absolute;left: 2px;top: 2px">变更申报</font><img src="<%=request.getContextPath()%>/page/mp/imgs/gf_button_img.png"></a>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr >
                                        <td colspan="12"
                                            style="text-align: left; padding: 10px 0 0 10px;">
                                            <div id="c" style="float: left;">
                                                <div id="a" class="findWhere0">变更前：</div>
                                                <div id="b" class="findWhere2">
                                                    <select id="lastAltOptId" style="width: 110px;"><option
                                                            value=''>--不限--</option></select>
                                                </div>
                                                <div id="a" class="findWhere1">模型分类：</div>
                                                <div id="b" class="findWhere2">
                                                    <select id="classId" style="width: 110px;"><option value=''>--不限--</option></select>
                                                </div>
                                                <div id="a" class="findWhere1">变更类型：</div>
                                                <div id="b" class="findWhere2">
                                                    <select id="altType" style="width: 110px;"><option
                                                            value=''>--不限--</option></select>
                                                </div>
                                                <div id="a" class="findWhere1">变更方式：</div>
                                                <div id="b" class="findWhere2">
                                                    <select id="altMode" style="width: 110px;"><option
                                                            value=''>--不限--</option></select>
                                                </div>
                                                <div style="clear: both"></div>
                                                <!--这个层很有用，必须要，否则可能不兼容。-->
                                            </div>
                                            <div id="altLike" style="float: left; padding: 10px 0 0 0;">
                                                <div id="a" style="float: left;">变更批次：</div>
                                                <div id="b" class="findWhere2">
                                                    <select id="altBatch" style="width: 110px;"><option
                                                            value=''>--不限--</option></select>
                                                </div>
                                                <div id="a" class="findWhere1">变更状态：</div>
                                                <div id="b" style="float: left;">
                                                    <select id="altSts" class="findWhere2">
                                                        <option value=''>--不限--</option>
                                                        <option value='0'>已生效</option>
                                                        <option value='1'>申报中</option>
                                                        <option value='2'>未申报</option>
                                                        <option value='3'>未确认</option>
                                                    </select>
                                                </div>
                                                <div id="a" class="findWhere1">变更项目：</div>
                                                <div id="search_alt_table_div"
                                                    style="float: left; width: 150px;">
                                                    <input type="text" style="width: 147px;" id="altObj">
                                                </div>
                                                <div id="tabSubmit"></div>
                                                <div style="clear: both"></div>
                                                <!--这个层很有用，必须要，否则可能不兼容。-->
                                            </div>
                                            <div id="c"
                                                style="float: left; padding: 10px 0 10px 0; width: 791px;">
                                                <div id="a" style="float: left; padding-left: 340px;">
                                                    <!-- <input type="button" onclick="sysVerSearchByType()"
                                                        value="&nbsp;&nbsp;查询&nbsp;&nbsp;" /> -->
                                                        <img onclick="sysVerSearchByType()" style="cursor: pointer;" src="<%=request.getContextPath()%>/page/mp/imgs/gf_search.png">
                                                </div>
                                                <div id="a" style="float: left; padding-left: 20px;">
<!--                                                    <input type="button" value="&nbsp;&nbsp;重置&nbsp;&nbsp;" />
 -->                                                <img  style="cursor: pointer;" src="<%=request.getContextPath()%>/page/mp/imgs/gf_reset.png">
                                                </div>
                                                <div style="padding-right: 20px;" id="alt_search">
                                                    <div class="alt_search_page_right" style="cursor: pointer;"></div>
                                                    <div class="alt_search_page_mid"
                                                        style="padding-left: 15px;">0/0</div>
                                                    <div class="alt_search_page_left" style="cursor: pointer;"></div>
                                                </div>
                                                <div style="clear: both"></div>
                                                <!--这个层很有用，必须要，否则可能不兼容。-->
                                            </div>
                                        </td>
                                    </tr>
                                    <tbody>
                                        <tr>
                                            <th>序号</th>
                                            <th>变更项目</th>
                                            <th>变更前版本</th>
                                            <th>变更后版本</th>
                                            <th>方式</th>
                                            <th>分类</th>
                                            <th>类型</th>
                                            <th>变更批次</th>
                                            <th>变更时间</th>
                                            <th>变更人</th>
                                            <th>状态</th>
                                            <!-- <th>操作</th> -->
                                        </tr>
                                    </tbody>
                                    <tbody id="metaAltDetailItem">
                                        <tr class="data_table_odd">
                                            <td rowspan=2>1</td>
                                            <td>PD_S04_TX_INT_RATE_SWAP</td>
                                            <td>20141222</td>
                                            <td>20150115</td>
                                            <td>在线</td>
                                            <td>表</td>
                                            <td>新增</td>
                                            <td>20141222001</td>
                                            <td>2014-12-23 12:45:24</td>
                                            <td>admin</td>
                                            <td>已生效</td>
                                            </tr><tr><td colspan=10>删除 | 确认 | 详情</td></tr>
                                    </tbody>
                                    <tr>
                                        <td colspan="12" style="height: 25px;"></td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
           <div class="center_right_content">
                <div id="change_42">
                    <div id="change_43">
                        <img src="<%=request.getContextPath()%>/page/mp/imgs/ny19.png">
                    </div>
                    <div id="change_44"></div>
                </div>
                <div id="change_45">
                    <div id="change_46">系统统计</div>
                    <div id="change_47"></div>
                </div>
            </div> 
        </div>
     </div>

</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/page/mp/metaAlt/metaAltDetail.js"></script>
</html>