<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<div id="tablePageDiv" style="overflow: hidden;margin-top: 5px;margin-bottom: 8px;">
            <div  class="searchTable_front" style="border: 0px solid red;float: left;">模式选择：</div>
            <div class="" style="float: left;">
                      <div><select name="search_value" id="tab_schema_selectId"  class="comp_search_select_input"></select></div>
            </div>
            <div class="searchTable_front" style="border: 0px solid red;float: left;">模块选择：</div>
            <div class="" style="float: left;">
                      <div><select name="search_value" id="tab_module_selectId" class="comp_search_select_input"></select></div>
            </div>
            <div class="searchTable_front" style="border: 0px solid red;float: left;">表信息查询：</div>
            <div class="ver_cmp_search_table_bg_div"> 
            <div><input type="text" name="search_value" class="ser_cmp_search_input" id="ser_cmp_tab_searchTable"></div>
            <div id="tabsubmit" style="border:0px solid red;margin-top: 35px;"></div>
<!--            <div class="autocomplete" style="border:1px solid red;position: relative; width: 226px; top: 10px; left: -1px;" jQuery1421285005126="208"/>
 -->            </div>
            <div class="ser_cmp_search_table_btn_div"style="cursor: pointer;"  onclick="searchTable()"></div>
            <div style="border:0px solid red;overflow: hidden;">
            <div class="ver_cmp_search_page_left" style="cursor: pointer;" id="tableLastPage" onclick="searchTable('prev')"></div>
            <div class="ver_cmp_search_page_mid" id="tablePages"><span class="tab_current_page">0</span><span style="padding:0 3px;">/</span><span class="tab_total_page">0</span></div> 
            <div class="ver_cmp_search_page_right" id="tableNextPage" style="cursor: pointer;" onclick="searchTable('next')"></div>
            </div>
        </div>
        <div class="tableList">
             <div id = "tabLoading" align="center"></div>
        <table width="100%" border="1" class="data_table_list" id="table_tab_list">
            </table>
        </div>
        <div class="tableInfo" style="display:none">
            <table width="100%" border="1" id="table_tab_info">
            </table>

        </div>
</html>