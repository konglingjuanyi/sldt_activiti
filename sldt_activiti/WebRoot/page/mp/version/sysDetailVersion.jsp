<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统版本</title>
<link href="<%=request.getContextPath()%>/page/mp/css/frame.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/form.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/version.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/common.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.contextMenu.js"></script>

</head>
<body style="background-color: #F5F5F5;">
    <!-- 目录 -->
    <div id="sys_ver_list_Div">
        <div class="sys_verDiv4">
            <div class="catalogue1">
                <div class="catalogue2">
                    <div class="catalogue21">目录</div>
                </div>
                <div class="catalogue3">
                    <ul class="ulverstyle">
                        <li class="dl1">1 模式信息</li>  
                        <li class="dl1">2 模块信息</li>  
                        <li class="dl1">3 表级信息</li>  
                        <li  class="dl1">4 字段信息</li>
                        <li class="dl1">5 字段落地代码</li> 
                        <li class="dl1">6 文件/程序接口</li>
                        <li class="dl2"> &nbsp;<a href="#schemaInfoId"> 模式基本信息</a></li> 
                        <li  class="dl2">&nbsp;<a href="#moduleInfoId"> 模块基本信息</a></li>
                        <li class="dl2">&nbsp;<a href="#tableInfoId"> 表级基本信息</a></li> 
                        <li class="dl2">&nbsp;<a href="#columnInfoId"> 字段基本信息</a></li> 
                        <li class="dl2"> &nbsp;<a href="#colCodefoId"> 字段落地基本信息</a></li> 
                        <li class="dl2"> &nbsp;<a href="javascript:void(0)" onclick="showFileIntPage(dbId,optId)"> 接口（文件）使用关系</a></li> 
                    </ul>
                </div>
            </div>
        </div>
        <!-- 版本概要 -->
        <div class="sysDiv5" id="schemaInfoId">
            <div class="xd">
                <div class="xd-1">1</div>
            </div>
            <div class="xd-name">模式信息</div>
            <div class="apDiv-line"></div>
        </div>
        <div class="sys_verDiv6" id="verSchemaData">
            <div class="apDiv-bg">
                <table class="x-bg" style="width:100%">
                    <tr>
                        <th scope="row">元数据代码</th>
                    </tr>
                    <tr>
                        <th scope="row">元数据名称</th>
                    </tr>
                </table>
            </div>
        </div>
        <!-- 版本概要 -->
        <div class="sysDiv5" id="moduleInfoId">
            <div class="xd">
                <div class="xd-1">2</div>
            </div>
            <div class="xd-name">模块信息</div>
            <div class="apDiv-line"></div>
        </div>
        <div class="sys_verDiv7">
            <div class="sys_verDiv7_2">
                <table class="data_table" style="width:100%">
                    <thead>
                        <tr>
                            <th>模块名</th>
                            <th>模块中文名称</th>
                            <th>开发负责联系人</th>
                            <th>SA</th>
                            <th>模块识别表达式</th>
                            <th>模式描述</th>
                            <th>总表数</th>
                        </tr>
                    </thead>
                    <tbody id="verModuleData"></tbody>
                </table>
            </div>
        </div>
        <!-- 版本概要 -->
        <div class="sysDiv5" id="tableInfoId">
            <div class="xd">
                <div class="xd-1">3</div>
            </div>
            <div class="xd-name">表级信息</div>
            <div class="apDiv-line"></div>
        </div>
        <div class="sys_verDiv8">
            <div class="sys_verDiv8_1">
                <div id="search">
                    <div id="tablePageDiv">
                        <div class="search_tab_schema_div">模式选择：</div>
                        <div class="search_schema_bg_div">
                            <div><select name="search_value" class="search_select_input"></select></div>
                        </div>
                        <div class="search_tab_module_div">模块选择：</div>
                        <div class="search_module_bg_div">
                            <div><select name="search_value" class="search_select_input"></select></div>
                        </div>
                        <div class="search_table_col_div">表级数据查询：</div>
                        <div class="search_table_bg_div">
                            <div><input type="text" name="search_value" class="search_input"></div>
                            <div class="search_table_btn_div" style="cursor: pointer;" id="submit"></div>
                        </div>
                        <div class="search_page_left" style="cursor:pointer;" id="tableLastPage"></div>
                        <div class="search_page_right" id="tableNextPage" style="cursor: pointer;"></div>
                        <div class="search_page_mid" id="tablePages">0/0</div>
                    </div>
                </div>
            </div>
            <div class="sys_verDiv8_2">
                <table class="data_table" style="width:100%">
                    <thead>
                        <tr>
                            <th style="width: 15%">表英文名</th>
                            <th style="width: 10%">表中文名</th>
                            <th style="width: 10%">所属表空间</th>
                            <th style="width: 15%">主键字段</th>
                            <th style="width: 10%">外键字段</th>
                            <th style="width: 5%">是否关键表</th>
                            <th style="width: 10%">生命周期说明</th>
                            <th style="width: 15%">描述</th>
                            <th style="width: 10%">上下文</th>
                        </tr>
                    </thead>
                    <tbody id="verTableData"></tbody>
                </table>
            </div>
        </div>
        <!-- 版本概要 -->
        <div class="sysDiv5" id="columnInfoId">
            <div class="xd">
                <div class="xd-1">4</div>
            </div>
            <div class="xd-name">字段级信息</div>
            <div class="apDiv-line"></div>
        </div>
        <div class="sys_verDiv9" style="padding:0 0 0 0 ;">
            <div class="sys_verDiv9_1">
                <div id="search">
                    <div id="columnPageDiv" name="columnPageDiv">
                        <div class="search_col_schema_div">模式选择：</div>
                        <div class="search_col_schema_bg_div">
                            <div><select name="search_value" class="search_col_select_input"></select></div>
                        </div>
                        <div class="search_col_module_div">模块选择：</div>
                        <div class="search_col_module_bg_div">
                            <div><select name="search_value" class="search_col_select_input"></select></div>
                        </div>
                        <div class="search_col_table_div">表级数据查询：</div>
                        <div class="search_col_table_bg_div">
                            <div><input type="text" name="search_value" class="search_input"></div>
                        </div>
                        <div class="search_col_div">字段级数据查询：</div>
                        <div class="search_bg_div">
                            <div><input type="text" id="sysVerColumnSearch" name="sysVerColumnSearch" class="search_input"></div>
                            <div class="search_btn_div" style="cursor: pointer;" id="submit"></div>
                        </div>
                        <div class="search_col_page_left" style="cursor:pointer;"></div>
                        <div class="search_col_page_right" style="cursor:pointer;"></div>
                        <div class="search_col_page_mid">0/0</div>
                    </div>
                </div>
            </div>
            <div class="sys_verDiv9_2" style="padding:50px 0 0 0 ;">
                <table class="data_table" style="width:100%">
                    <thead>
                        <tr>
                           <th>字段英文名</th>
                           <th>字段中文名</th>
                           <th>字段类型</th>
                           <th width="50px">字段序号</th>
                           <th width="50px">主键</th>
                           <th width="50px">是否允许空值</th>
                           <th width="50px">是否代码字段</th>
                           <th width="50px">是否有索引</th>
                           <th>引用代码表</th>
                           <th>字段描述</th>
                           <th width="20%">上下文</th>   
                        </tr>
                    </thead>
                    <tbody id="verColumnData"></tbody>
                </table>
            </div>
        </div>
        <div class="sysDiv5" id="colCodefoId">
            <div class="xd">
                <div class="xd-1">5</div>
            </div>
            <div class="xd-name">字段落地信息</div>
            <div class="apDiv-line"></div>
        </div>
        <div class="sys_verDiv9" style="padding: 0 0 40px 0 ;">
            <div class="sys_verDiv9_1">
                <div id="search">
                    <div id="colCodePageDiv">
                        <div class="search_col_schema_div" style="left: 7px;">模式选择：</div>
                        <div class="search_col_schema_bg_div" style="left:134px;">
                            <div><select name="search_value" class="search_col_select_input"></select></div>
                        </div>
                        <div class="search_col_module_div" style="left:500px;">模块选择：</div>
                        <div class="search_col_module_bg_div" style="left:623px;">
                            <div><select name="search_value" class="search_col_select_input"></select></div>
                        </div>
                        <div id="colCodeTabSearDiv" style="padding-top: 40px;">
                            <div class="search_tab_schema_div">表查询：</div>
                            <div class="search_schema_bg_div" style="padding-left: 10px;">
                                <div><input type="text" name="search_value" style="width: 150px;" class="search_input"></div>
                            </div>
                        </div>
                        <div class="search_tab_module_div" style="padding-left: 30px;">字段查询：</div>
                        <div class="search_module_bg_div" style="padding-left: 40px;">
                            <div><input type="text" name="sysVerColumnSearch" style="width: 150px;" class="search_input"></div>
                        </div>
                        <div class="search_table_col_div" style="padding-left: 60px;">代码值查询：</div>
                        <div class="search_table_bg_div" style="padding-left: 10px;">
                            <div><input type="text" name="sysVerColumnSearch" style="width: 150px;" class="search_input"></div>
                            <div class="search_table_btn_div" style="cursor: pointer;" id="submit"></div>
                        </div>
                        <div style="border:0px solid red;overflow: hidden;float:right;margin-right:10px;">
                            <div class="search_page_left" style="cursor: pointer;" id="tableLastPage"></div>
                            <div class="search_page_mid" id="tablePages">0/0</div>
                            <div class="search_page_right" id="tableNextPage" style="cursor: pointer;"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="sys_verDiv9_2" style="padding: 50px 0 0 0 ;">
                <table class="data_table" style="width: 100%">
                    <thead>
                        <tr>
                            <th style="width: 15%">字段英文名</th>
                            <th style="width: 10%">代码值</th>
                            <th style="width: 10%">代码中文名</th>
                            <th style="width: 15%">代码描述</th>
                            <th style="width: 10%">是否有效</th>
                            <th style="width: 5%">生效日期</th>
                            <th style="width: 10%">无效日期</th>
                            <th style="width: 10%">上下文</th>
                        </tr>
                    </thead>
                    <tbody id="verColCodeData"></tbody>
                </table>
            </div>
        </div>
    </div>
</body>
    <script type="text/javascript" src="<%=request.getContextPath()%>/page/mp/version/sysMenu.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/version/sysDetailVersion.js"></script>
</html>