<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
String dbName = request.getParameter("dbName");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<table  width="100%" border="1" align="center" bordercolor="#E0E0E0">
              <tr>
                <td height="37" colspan="6" bgcolor="#EFEFEF"><font class="cmp_font_01">版本比对</font><font class="cmp_font_02">| <%=dbName%></font></td>
              </tr>
              <tr>
                <td height="" colspan="6">
                <hr color="#ed1a2e">
              </td>
              </tr>
              <tr class="top_tab_tr_01">
                <td class="tab_tr_width_01"><font class="cmp_font_03">版本信息</font></td>
                <!-- 第二栏 -->
                <td class="tab_tr_width_02">
                    <div id="hideTxt">
<!--                        <div class="cmp_font_04">7月版</div>
                        <div class="cmp_font_04">2014/07/01</div>
                        <div class="cmp_font_05"> <a href="javascript:deleteVersionInfo('#fullTxt','#hideTxt')"><font class="cmp_font_05">移除</font></a></div> -->
                    </div>
                   <div id="fullTxt" style="display:none;">
                   <div >
                        <input type="text" class="stext" name="school" id="ver1" onclick="chooseVer('#ver1')" focucmsg="请选择版本" />
                   </div>
                    <div class="cmp_font_06"><a href="javascript:addVersionInfo('#hideTxt','#fullTxt')"><img src="<%=request.getContextPath() %>/page/mp/imgs/gf_add.png" /></a></div>
                    </div>
             </td>
                
              <!-- 第三列 -->
              <td class="tab_tr_width_02">
                <div id="hideTxt2" style="display:none;">
                    <div class="cmp_font_04" id="vname2"><!-- 8月版 --></div>
                    <div class="cmp_font_04" id="vdate2"><!-- 2014/08/01 --></div>
                    <div class="cmp_font_05"> <a href="javascript:deleteVersionInfo('#fullTxt2','#hideTxt2')"><font class="cmp_font_05">移除</font></a></div> 
                </div>
               <div id="fullTxt2" style="display:none;">
               <div>
                    <input type="text" class="stext" name="school" id="ver2" onclick="chooseVer('#ver2')" focucmsg="请选择版本" />
               </div>
                    <div class="cmp_font_06"><a href="javascript:addVersionInfo('#hideTxt2','#fullTxt2')"><img src="<%=request.getContextPath() %>/page/mp/imgs/gf_add.png" /></a></div>
             </div>
            </td>
            
             <!-- 第四列 -->
             <td class="tab_tr_width_02">
                <div id="hideTxt3" style="display:none;">
                    <div class="cmp_font_04" id="vname3"><!-- 9月版 --></div>
                    <div class="cmp_font_04" id="vdate3"><!-- 2014/09/01 --></div>
                    <div class="cmp_font_05"> <a href="javascript:deleteVersionInfo('#fullTxt3','#hideTxt3')"><font class="cmp_font_05">移除</font></a></div> 
                </div>
               <div id="fullTxt3" style="display:none;">
               <div>
                    <input type="text" class="stext" name="school" id="ver3" onclick="chooseVer('#ver3')" focucmsg="请选择版本" />
               </div>
                    <div class="cmp_font_06"><a href="javascript:addVersionInfo('#hideTxt3','#fullTxt3')"><img src="<%=request.getContextPath() %>/page/mp/imgs/gf_add.png" /></a></div>
             </div>
           </td>
                
                 <!-- 第五列 -->
             <td class="tab_tr_width_02">
                <div id="hideTxt4" style="display:none;">
                    <div class="cmp_font_04" id="vname4"><!-- 备用板 --></div>
                    <div class="cmp_font_04" id="vdate4"><!-- 2014/09/21 --></div>
                    <div class="cmp_font_05"> <a href="javascript:deleteVersionInfo('#fullTxt4','#hideTxt4')"><font class="cmp_font_05">移除</font></a></div>
               </div>
               <div id="fullTxt4" style="display:block;">
               <div>
                <div >
                <input type="text" class="stext" name="school" id="ver4" onclick="chooseVer('#ver4')" focucmsg="请选择版本" />
                </div>
<%--                <div style="float: left"><img src="<%=request.getContextPath() %>/page/mp/imgs/gf_cmp_2_03.png"/>
 --%>               </div>
                    <div class="cmp_font_06"><a href="javascript:addVersionInfo('#hideTxt4','#fullTxt4')"><img src="<%=request.getContextPath() %>/page/mp/imgs/gf_add.png" /></a></div>
               </div>
             </div>
           </td>
            <!-- 第六列 -->
             <td class="tab_tr_width_02">
                <div id="hideTxt5" style="display:none;">
                    <div class="cmp_font_04" id="vname5"></div>
                    <div class="cmp_font_04" id="vdate5"></div>
                    <div class="cmp_font_05"> <a href="javascript:deleteVersionInfo('#fullTxt5','#hideTxt5')"><font class="cmp_font_05">移除</font></a></div>
               </div>
               <div id="fullTxt5" style="display:block;">
               <div >
                <input type="text" class="stext" name="school" id="ver5" onclick="chooseVer('#ver5')" focucmsg="请选择版本" />
                </div>
                    <div class="cmp_font_06"><a href="javascript:addVersionInfo('#hideTxt5','#fullTxt5')"><img src="<%=request.getContextPath() %>/page/mp/imgs/gf_add.png" /></a></div>
             </div>
           </td>
              </tr>
             
            </table>
</html>