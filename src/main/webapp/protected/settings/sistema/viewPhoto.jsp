<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<script type="text/javascript">
			function cargaFoto() {
				var txtFoto = document.getElementById("txtFoto");
				var foto = document.getElementById("foto");
				foto.src = txtFoto.value;
			}
		</script>
	</head>
	<body>
		<div align="center">
			<input type="text" id="txtFoto" name="txtFoto">
		</div>
		<div align="center">
			<input type="reset" value="Borrar datos"/>
			<input type="button" value="Cargar foto" onclick="cargaFoto();">
		</div>
		<div align="center">
			<img alt="Foto" id="foto" name="foto">
		</div>
	</body>
</html>