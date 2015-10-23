<%--
  Created by IntelliJ IDEA.
  User: clark
  Date: 10/23/2015
  Time: 1:47 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:javascript src="dashboard.js"/>
</head>

<body>

<h1>Files Queued</h1>

<ul id="queued">
    <% files.each { file -> %>
    <li id="<%="file_${file.id}"%>">
        <%="${file.file} - ${file.time}"%>
    </li>
    <% } %>
</ul>

<h1>Files Processing</h1>
<ul id="processing"></ul>

<h1>Files Complete</h1>
<ul id="complete"></ul>

</body>
</html>