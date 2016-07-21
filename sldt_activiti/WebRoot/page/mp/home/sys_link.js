    function getSystemItem(){
        //===========================================================
        $.ajax({
            url:context + '/sysMgr.do?method=links',
            method: 'POST',
            data:({'sysClass':'1',start:0,limit:10}),
            dataType :'json',
            success: function(response) {
                var html='<table class="sys_link_table" width="400" >';
                
                var _dateques = response.data;
                
                for(var i=0; i<_dateques.length; i++) {
                    if(i%2==0) {
                        html+="<tr>";
                    }
                    html += '<td class="odd" style="width:50px;">' +  _dateques[i].DB_CODE+'</td>';
                    html += '<td class="even" id="dbchnameId'+i+'" title="'+_dateques[i].DBCHNAME+'">';
                    if(_dateques[i].ISNULL=='1'){
                        html += "<a>"+ splitSysName(_dateques[i].DBCHNAME) + "</a>";
                    }else{
                        html += "<a style=\"cursor: pointer;\" onclick=\"reqSysDetailPage('"+_dateques[i].DB_CODE+"','')\"  target=\"_blank\"  >"+ splitSysName(_dateques[i].DBCHNAME) + "</a>";
                    }
                    html += '</td>';
                    
                    if(i%2==1) {
                        html+="</tr>";
                    }
                }
                if(_dateques.length%2==1){
                    html+="</tr>";
                }
                
                html+="</table>";
                document.getElementById("sys_list_1_div").innerHTML=html;
            }
        });
        
                
        $.ajax({
            url:context + '/sysMgr.do?method=links',
            method: 'POST',
            data:({'sysClass':'2',start:0,limit:10}),
            dataType :'json',
            success: function(response) {
                var html='<table class="sys_link_table">';
                
                var _dateques = response.data;
                
                for(var i=0; i<_dateques.length; i++) {
                    
                    html+="<tr>";

                    html += '<td class="odd" style="width:50px;">' +  _dateques[i].DB_CODE+'</td>';
                    html += '<td class="even" id="dbchnameId'+i+'" title="'+_dateques[i].DBCHNAME+'">';
                    if(_dateques[i].ISNULL=='1'){
                        html += "<a>"+ splitSysName(_dateques[i].DBCHNAME) + "</a>";
                    }else{
                        html += "<a style=\"cursor: pointer;\" onclick=\"reqSysDetailPage('"+_dateques[i].DB_CODE+"','')\"  target=\"_blank\"  >"+ splitSysName(_dateques[i].DBCHNAME) + "</a>";
                    }
                    html += '</td>';
                    
                    html+="</tr>";
                }
                
                html+="</table>";
                
                document.getElementById("sys_list_2_div").innerHTML=html;
            }
        });

        $.ajax({
            url:context + '/sysMgr.do?method=links',
            method: 'POST',
            data:({'sysClass':'3',start:0,limit:10}),
            dataType :'json',
            success: function(response) {
                var html='<table class="sys_link_table" >';
                
                var _dateques = response.data;
                
                for(var i=0; i<_dateques.length; i++) {
                    if(i%2==0) {
                        html+="<tr>";
                    }
                    
                    html += '<td class="odd" style="width:50px;">' + _dateques[i].DB_CODE+'</td>';
                    html += '<td class="even" id="dbchnameId'+i+'" title="'+_dateques[i].DBCHNAME+'">';
                    if(_dateques[i].ISNULL=='1'){
                        html += "<a>"+ splitSysName(_dateques[i].DBCHNAME) + "</a>";
                    }else{
                        html += "<a style=\"cursor: pointer;\" onclick=\"reqSysDetailPage('"+_dateques[i].DB_CODE+"','')\"  target=\"_blank\"  >"+ splitSysName(_dateques[i].DBCHNAME) + "</a>";
                    }
                    html += '</td>';
                    
                    if(i%2==1) {
                        html+="</tr>";
                    }
                }
                
                if(_dateques.length%2==1){
                    html+="</tr>";
                }
                
                html+="</table>";
            //  alert(html);
                
                document.getElementById("sys_list_3_div").innerHTML=html;
            }
        });

        function splitSysName(sysName) {
            if(sysName.length <= 14){
                return sysName;
            }else {
                return sysName.substring(0, 11) + '...';
            }
        }
        
        
    }

function reqSysDetailPage(dbId,optId){
	parent.changeMainPage("page/mp/version/sysVersion.jsp?dbId="+dbId+"&optId="+optId,"nav_4");

}
