<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload File Request Page</title>
</head>
<body>
	<h3>App Upload</h3><br><br>
	<form method="POST" action="uploadFile" enctype="multipart/form-data">
		Upload File : <input type=file name=appFile style="margin-left: 50px" /><br><br>
		File Name : <input type=text name=fileName style="margin-left: 57px"/><br><br>
		File Version * : <input type=text name=fileVersion style="margin-left: 35px; required"/><br><br>
		File Type * : <select name = fileType style="margin-left: 52px; padding-right: 70px"><option value=apk>APK</option><option value=ipa>IPA</option></select><br><br>
		Upload Type * : <select name = uploadType style="margin-left: 30px; padding-right: 45px"><option value=create>CREATE</option><option value=Update>UPDATE</option></select><br><br>
		Download URL : <input type = text name=downloadUrl style="margin-left: 22px"><br><br>
		Application : <input type = text name=application style="margin-left: 50px"><br><br><br>
		<input type="submit" value="Upload File">
	</form>
</body>
</html>