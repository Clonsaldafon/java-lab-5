<%@ page import="java.io.File" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>File Manager</title>
</head>
<body>
<h2>Файловый менеджер</h2>
<p>Время генерации: <%= request.getAttribute("timestamp") %></p>
<hr>
<h3>Текущая директория: <%= request.getAttribute("basePath") %></h3>
<ul>
    <%
        String path = ((String)request.getAttribute("basePath"));
        String parentPath = new File(path).getParent();
        if (parentPath == null) {
            parentPath = "/";
        }

    %>
    <li><a href="files?path=<%= URLEncoder.encode(parentPath, "UTF-8") %>">Вернуться</a></li>
    <%
        File[] files = (File[]) request.getAttribute("files");
        if (files != null) {
            for (File file : files) {
                String encodePath = URLEncoder.encode(file.getAbsolutePath(), "UTF-8");
                if (file.isDirectory()) {
    %>
    <li>[D] <a href="files?path=<%= encodePath %>"><%= file.getName() %></a></li>
    <%
    } else {
    %>
    <li>[F] <a href="download?path=<%= encodePath %>"><%= file.getName() %></a></li>
    <%
                }
            }
        }
    %>
</ul>
</body>
</html>