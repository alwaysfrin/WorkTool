<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>lodop-demo</title>
  </head>

  <%
    String modelName = request.getParameter("modelName");
    boolean editFlag = false;
    HashMap<String, String> formMap = (HashMap<String, String>) request.getServletContext().getAttribute("formMap");
  %>
  <script language="javascript" src="LodopFuncs.js"></script>
  <script language="javascript" type="text/javascript">
      var LODOP; //声明为全局变
      function initLodop(){
          LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
          LODOP.PRINT_INITA(4,10,665,400,"lodop控制面板");
      }

      //页面内预览
      function prefiew(){
          initLodop();
          LODOP.SET_SHOW_MODE("PREVIEW_IN_BROWSE",1);
          LODOP.SET_SHOW_MODE("HIDE_PAPER_BOARD",1);
          LODOP.PREVIEW();
      }

      //维护
      function setup(){
          initLodop();
          LODOP.SET_SHOW_MODE("SETUP_IN_BROWSE",1);
          LODOP.PRINT_SETUP();
      }

      //设计
      function design(){
          initLodop();
          LODOP.SET_SHOW_MODE("DESIGN_IN_BROWSE",1);
          LODOP.PRINT_DESIGN();
      }

      //弹出预览
      function outpreview(){
          LODOP.PREVIEW();
      }

      function setData(){
          var paramName = document.getElementById("paramName").value;
          var paramValue = document.getElementById("paramValue").value;

          if(paramName != null && paramName.length > 0){
            LODOP.SET_PRINT_STYLEA(paramName,"CONTENT",paramValue);
          }
      }

      //获取模板详情
      function getModelData(){
          var obj = document.getElementById("modelSelect"); //定位id
          var index = obj.selectedIndex; // 选中索引
          var text = obj.options[index].text; // 选中文本
          var value = obj.options[index].value; // 选中值
          return value;
      }

      function loadModel(){
          document.getElementById("LODOP_OB").style.display="block";
          initLodop();

          //装载模板
          var value = getModelData();
          LODOP.ADD_PRINT_DATA("ProgramData",value);
          setData();
          LODOP.SET_SHOW_MODE("PREVIEW_IN_BROWSE",1);
          LODOP.SET_SHOW_MODE("HIDE_PAPER_BOARD",1);
          LODOP.PREVIEW();
      }

      function loadPrint(){
          //document.getElementById("LODOP_OB").style.display="none";
          initLodop();

          //装载模板
          var value = getModelData();
          LODOP.ADD_PRINT_DATA("ProgramData",value);
          setData();
          //LODOP.PRINTA();
          LODOP.PRINT();
      }

      function editModel(){
          var obj = document.getElementById("modelSelect"); //定位id
          var index = obj.selectedIndex; // 选中索引
          var text = obj.options[index].text; // 选中文本
          window.location = "lodop_edit.jsp?modelName=" + encodeURIComponent(text);
      }

      function deleteModel(){
          var obj = document.getElementById("modelSelect"); //定位id
          var index = obj.selectedIndex; // 选中索引
          var text = obj.options[index].text; // 选中文本
          window.location = "listlodop?method=delete&modelName=" + encodeURIComponent(text);
      }
  </script>
  <body>
  lodop列表：
    <select name="" id="modelSelect">
      <%
        if (formMap != null) {
          for(String key : formMap.keySet()){
              %>
                <option value="<%=formMap.get(key)%>"><%=key%></option>
              <%
          }
        }
      %>
    </select>
  &nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" value="加载表单后预览" onclick="loadModel()">&nbsp;&nbsp;
  <input type="button" value="加载表单后打印" onclick="loadPrint()">&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" value="新增" onclick="window.location='lodop_edit.jsp'">&nbsp;
  <input type="button" value="修改" onclick="editModel()">&nbsp;
  <input type="button" value="删除" onclick="deleteModel()">&nbsp;
  <br/><br/>
  <hr>
  <h2>
    赋值：对象名：<input type="text" id="paramName" value="">&nbsp;&nbsp;值：&nbsp;<input type="text" id="paramValue" value="">
  </h2>
  <hr>
  <h1>展示区
      <!--
    <input type="button" value="预览" onclick="prefiew()">&nbsp;
    <input type="button" value="维护" onclick="setup()">&nbsp;
    <input type="button" value="设计" onclick="design()">
    <input type="button" value="弹出预览" onclick="outpreview()">
    -->
  </h1>
  <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=820 height=450 style="display: none">
    <embed id="LODOP_EM" type="application/x-print-lodop" width=820 height=450 pluginspage="install_lodop32.exe"></embed>
  </object>

  </body>
</html>
