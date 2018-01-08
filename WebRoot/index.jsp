<%@page import="etuf.v1_0.common.EncryptHelper"%>
<%@page import="java.text.MessageFormat"%>
<%@page import="etuf.v1_0.model.base.output.OutputResult"%>
<%@page import="etuf.v1_0.test.Client"%>
<%@page import="etuf.v1_0.test.controller.Test"%>
<%@page import="etuf.v1_0.test.model.Request_Test"%>
<%@page import="etuf.v1_0.test.model.Response_Test"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    This is my JSP page. <br>
    
    <%
    
    String encryptStr=EncryptHelper.DESEncrypt("我是中国人1234564{\"}");
    System.out.println("encryptStr --  "+encryptStr);
    String decryptStr=EncryptHelper.DESDecrypt(encryptStr);
    System.out.println("decryptStr --  "+decryptStr);
        	Request_Test req=new Request_Test();
            	//req.method="Test";
            	//req.versionId=1.0;
            	req.testStr="123456";
            	req.testInt=100;
            	req.testHashMap=new HashMap<String,String[]>();
            	req.testHashMap.put("a", new String[]{"a1"});
            	req.testHashMap.put("b", new String[]{"b1","b2"});
            	req.testHashMap.put("c", new String[]{"c1","c2","c3"});
            	
            	Test tc=new Test();
            	
            	OutputResult<Response_Test, String> or= tc.ClientRequest(req, Response_Test.class);
            	
            	StringBuilder sb=new StringBuilder();
            	sb.append(MessageFormat.format("<br>or.IsSucceed:  {0}", or.IsSucceed()));
            	sb.append(MessageFormat.format("<br>or.result:  {0}", or.result));
            	if(or.IsSucceed()){
            		sb.append("<br>");
                	sb.append(MessageFormat.format("<br>or.ResultObj.retStr:  {0}", or.getResultObj().retStr));
                	sb.append(MessageFormat.format("<br>or.ResultObj.retInt:  {0}", or.getResultObj().retInt));
                	if(or.getResultObj().retHashMap!=null && or.getResultObj().retHashMap.size()>0){
                		sb.append(MessageFormat.format("<br>or.ResultObj.retHashMap-count:  {0}", or.getResultObj().retHashMap.size()));
                		sb.append(MessageFormat.format("<br>or.ResultObj.retHashMap-toString:  {0}", or.getResultObj().retHashMap.toString()));
                	}
            	}
            	//request.setCharacterEncoding("UTF-8");
            	//response.setContentType("text/html;charset=UTF-8");
        		//response.setCharacterEncoding("UTF-8");
            	response.getWriter().write(sb.toString());
            	response.getWriter().close();
            	response.getWriter().flush();
        %>
    
  </body>
</html>
