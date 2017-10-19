<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ant Media Server</title>
 
<!-- 1. skin -->
<link rel="stylesheet" href="//releases.flowplayer.org/7.1.2/skin/skin.css">
 
<!-- 2. jquery library - required for video tag based installs -->
<script src="//code.jquery.com/jquery-1.12.4.min.js"></script>
 
<!-- 3. flowplayer -->
<script src="//releases.flowplayer.org/7.1.2/flowplayer.min.js"></script>

<script src="//releases.flowplayer.org/hlsjs/flowplayer.hlsjs.min.js"></script>


</head>
<body>
<!-- 
data-rtmp="rtmp://127.0.0.1/vod" 
-->
<div class="flowplayer">
	<video>
	<%
		String name = request.getParameter("name");
	    if (name == null) 
	    {
	        out.println("There is no stream identifier.");
	    } else if (name.endsWith("m3u8"))
	    {
	        out.println("<source type='application/x-mpegurl' src='streams/"+name+"'>");
	    }
	    else if (name.endsWith("mp4")) {
	    	out.println("<source type='video/mp4' src='streams/"+name+"'>");
	    }
	%>
	</video>
</div>

</body>
</html>