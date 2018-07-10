<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>动态生成table</title>
      <script src="${contextPath}js/jquery-3.3.1.min.js"></script>
  </head>
  <script>
    function makeTableCell(){
        $("#tableShowDiv").html("");
        $("#tableColumnShowSpan").html("");

        var picWidth = $("#picWidth").val();
        var picHeight = $("#picHeight").val();
        $("#tableShowDiv").width(picWidth + "px");
        $("#tableShowDiv").height(picHeight + "px");

        var tableWidth = $("#tableWidth").val();
        var tableCount = $("#tableCount").val();
        var rowNum = $("#rowNum").val();
        var cellNum = $("#cellNum").val();

        var borderLine = $("#borderLine").val();
        var tableBackgroundColor = $("#tableBackgroundColor").val();

        var url = "columnshow";
        var data = "rowNum=" + rowNum + "&cellNum=" + cellNum;
        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function(data){
                $("#tableColumnShowSpan").html(data);
            }
        });

        var url = "tableshow";
        var data = "tableWidth=" + tableWidth + "&tableCount=" + tableCount + "&rowNum=" + rowNum + "&cellNum=" + cellNum
                        + "&borderLine=" + borderLine + "&tableBackgroundColor=" + tableBackgroundColor;
        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function(data){
                $("#tableShowDiv").html(data);
            }
        });
    }

    function makeTableDetail(){
        $("#tableShowDiv").html("");

        var rowNum = $("#rowNum").val();
        var cellNum = $("#cellNum").val();

        var cellArr = new Array();
        var count = 0;
        for(var i=1;i<=rowNum;i++){
            for(var j=1;j<=cellNum;j++) {
                var cell = new Object();
                cell.rowCount = i;
                cell.cellCount = j;
                cell.cellType = $("#cellType" + i + j).val();
                cell.cellTypeVal = $("#cellTypeVal" + i + j).val();
                cell.colType = $("#colType" + i + j).val();
                cell.colTypeVal = $("#colTypeVal" + i + j).val();
                cell.fontSize = $("#fontSize" + i + j).val();
                cell.widthSize = $("#widthSize" + i + j).val();
                cell.heightSize = $("#heightSize" + i + j).val();
                cell.position = $("#position" + i + j).val();
                cell.fontWeight = $("#fontWeight" + i + j).val();

                cellArr[count] = cell;
                count++;
            }
        }

        var tableWidth = $("#tableWidth").val();
        var tableCount = $("#tableCount").val();
        var rowNum = $("#rowNum").val();
        var cellNum = $("#cellNum").val();

        var borderLine = $("#borderLine").val();
        var tableBackgroundColor = $("#tableBackgroundColor").val();

        var url = "maketable?tableWidth=" + tableWidth + "&tableCount=" + tableCount + "&rowNum=" + rowNum + "&cellNum=" + cellNum
                        + "&borderLine=" + borderLine + "&tableBackgroundColor=" + tableBackgroundColor;
        $.ajax({
            type: 'POST',
            url: url,
            data: JSON.stringify(cellArr),  //转换成json字符串
            success: function(data){
                $("#tableShowDiv").html(data);
            }
        });
    }
  </script>
  <body>
  <table width="100%" border="0" cellpadding="1" cellspacing="1">
      <tr>
          <td width="15%" align="center">画布宽px</td>
          <td width="20%" align="center"><input type="text" id="picWidth" value="800" size="5"></td>
          <td width="15%" align="center">画布高px</td>
          <td width="20%" align="center"><input type="text" id="picHeight" value="400" size="5"></td>
          <td width="15%" align="center">table宽px</td>
          <td width="20%" align="center"><input type="text" id="tableWidth" value="600" size="5"></td>
      </tr>
    <tr>
        <td width="15%" align="center">table数量</td>
        <td width="20%" align="center"><input type="text" id="tableCount" value="1" size="5"></td>
        <td align="center">行数</td>
        <td align="center"><input type="text" id="rowNum" value="1" size="5"></td>
        <td align="center">列数</td>
        <td align="center"><input type="text" id="cellNum" value="2" size="5"></td>
    </tr>
      <tr>
          <td width="15%" align="center">边框px</td>
          <td width="20%" align="center">
              <input type="text" id="borderLine" value="1" size="5">
          </td>
          <td align="center">表格背景</td>
          <td align="center">
              <select id="tableBackgroundColor">
                  <option value="white">白色</option>
                  <option value="gray">灰色</option>
              </select>
          </td>
          <td align="center">画布背景</td>
          <td align="center">
            <select onchange="$('#tableShowDiv').css('background',$(this).val())">
                <option value="white">白色</option>
                <option style="color:lightgray" value="lightgray">灰色</option>
                <option style="color:deepskyblue" value="deepskyblue">淡蓝</option>
                <option style="color:greenyellow" value="greenyellow">绿色</option>
            </select>
          </td>
      </tr>
  </table>
    <input type="button" value="生成列参数" onclick="makeTableCell()">
  <br/>
  <hr>
  <span id="tableColumnShowSpan">
    <!--列控制-->
  </span>
  <br/>
  <input type="button" value="生成表单详情" onclick="makeTableDetail()">
  <br/>
<hr>
  <br/>
    <input type="button" value="获取table代码" onclick="alert($('#tableShowDiv').html());">
    <div id="tableShowDiv" style="margin:10px;"></div>
  </body>
</html>
