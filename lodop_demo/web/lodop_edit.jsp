<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>新增、修改lodop</title>
</head>
<%
String modelName = request.getParameter("modelName");
boolean editFlag = false;
HashMap<String, String> formMap = (HashMap<String, String>) request.getServletContext().getAttribute("formMap");
if(modelName != null){
    if (formMap != null) {
        //修改记录
        editFlag = true;
    }
}
%>
<script language="javascript" src="LodopFuncs.js"></script>
<script language="javascript" type="text/javascript">
    window.onload = function() {
        setTimeout("initLodop()",500);
    };

    var LODOP; //声明为全局变
    function initLodop(){
        LODOP = getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
        LODOP.PRINT_INITA(4,10,600,400,"lodop设置");

        setFrom();  //载入模板

        LODOP.SET_SHOW_MODE("DESIGN_IN_BROWSE",1);
        LODOP.PRINT_DESIGN();
    }

    function getFormDetail(){
        //获取模板描述
        var detail;
        if (LODOP.CVERSION){
            LODOP.On_Return = function(TaskID,Value){
                detail=Value;
            };
        }
        detail = LODOP.GET_VALUE("ProgramData",0);
        return detail;
    }

    function saveForm(){
        var modelName = document.getElementById("modelName").value;
        var modelDesc = getFormDetail();

        window.location = "<%=request.getContextPath()%>/listlodop?method=save&modelName=" + encodeURIComponent(modelName) + "&modelDesc=" + encodeURIComponent(modelDesc);
    }

    function setFrom(){
        <%if(editFlag){
        String detail = formMap.get(modelName).replaceAll("\n","").replaceAll("\t","").replaceAll("\r","");
        %>
        //装载模板
        document.getElementById("modelName").value = "<%=modelName%>";

        //模板详情
        LODOP.ADD_PRINT_DATA("ProgramData","<%=detail%>");
        <%}%>
    }

    function alertForm(){
        alert(getFormDetail());
    }
</script>
<body>
<h1>
    模板名称：<input type="text" id="modelName" value="表单模板">&nbsp;&nbsp;
    <input type="button" value="保存模板" onclick="saveForm()">&nbsp;&nbsp;
    <input type="button" value="获取模板" onclick="alertForm()">&nbsp;&nbsp;
</h1>
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width="600" height="400">
    <embed id="LODOP_EM" type="application/x-print-lodop" width="600" height="400" pluginspage="install_lodop32.exe"></embed>
</object>
</body>
</html>
