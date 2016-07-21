<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<div id="columnPageDiv" style="margin-top: 5px;margin-bottom: 5px; overflow: hidden;">
            <div style="overflow: hidden;border: 0px solid red;">
            <div class="searchTable_front" style="border: 0px solid red;float: left;">模式选择：</div>
            <div class="" style="float: left;margin-left: 30px;">
                <div><select name="search_value" id="col_schema_selectId" class="comp_search_select_input" style="width: 220px;"></select></div>
            </div>
            <div class="searchTable_front" style="border: 0px solid red;float: left;margin-left: 70px;margin-right: 28px;">模块选择：</div>
            <div class="" style="float: left;margin-left: 8px;">
                      <div><select name="search_value" id="col_module_selectId"  class="comp_search_select_input" style="width: 220px;"></select></div>
            </div>
            </div>
            <div style="overflow: hidden;border: 0px solid red;">
            <div id = "columnTablePageDiv">
            <div class="searchTable_front" style="border: 0px solid red;float:left;">表信息查询：</div>
            <div class="ver_cmp_search_table_bg_div2">
            <input type="text" name="search_value" class="ser_cmp_search_input " id="ser_cmp_col_searchTable">
            <div id="coltabsubmit" style="border:0px solid red;margin-top: 35px;"></div>
            </div>
            </div>
            <div id="columnPageDivs"> 
            <div class="searchTable_front" style="border: 0px solid red;float:right;margin-right: 600px;overflow: hidden;">字段信息查询：</div>
            <div class="ver_cmp_search_table_bg_div3">
                <input type="text" name="search_value" class="ser_cmp_search_input" id="search_colId">
                <div id="colsubmit" style="border:0px solid red;margin-top: 35px;"></div>
            </div> 
            </div>
            </div>
            <div class="ser_cmp_search_column_btn_div" onclick="searchColumn()"><img src="<%=request.getContextPath() %>/page/mp/imgs/gf_search.png"></div>
            <div class="comp_col_page_2">
                <div class="ver_cmp_search_page_left" style="cursor: pointer;border: 0px solid red;" id="columnLastPage" onclick="searchColumn('prev')"></div>
                <div class="ver_cmp_search_page_mid" id="columnPages"><span class="col_current_page">0</span><span style="padding:0 3px;">/</span><span class="col_total_page">0</span></div> 
                <div class="ver_cmp_search_page_right" id="columnNextPage" style="cursor: pointer;" onclick="searchColumn('next')"></div> 
            </div>
        </div>
        <div class="columnList">
             <div id = "colLoading" align="center"></div>
        <table width="100%" border="1" class="data_table_list" id="column_tab_list">
            </table>
        </div>
        <div class="columnInfo" style=" display:none">
            <table width="100%" border="1" id="column_tab_info">
             
            </table>
        </div>
</html>