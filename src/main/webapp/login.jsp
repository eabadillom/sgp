<%@page import="mx.com.ferbo.dto.DetEmpleadoDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="mx.com.ferbo.controller.LoginBean" %>
<%@ page import="mx.com.ferbo.dto.DetEmpleadoDTO" %>
<%@ page import="javax.faces.context.FacesContext" %>    
<%DetEmpleadoDTO empleado = (DetEmpleadoDTO) session.getAttribute("empleado"); %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;

if(empleado != null){
	request.getSession().invalidate();
}

String numEmpleado = "";

%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<title></title>
		<link rel="shortcut icon" href="#">
		<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.js"></script>
		<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.13.0/jquery-ui.min.js"></script>
		<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.13.0/jquery-ui.css">
		<script type="text/javascript">
			$(document).ready(function (){
				jQuery("#dialog-message" ).dialog({
					autoOpen: false,
					buttons: {
						Aceptar: function() {
							$( this ).dialog( "close" );
						}
					}
				});

				const input_value = $("#numero");
				const accion = $("#accion");
				
				//disable input from typing
				$("#numero").keypress(function () {
				  return false;
				});
				
				//add password
				$(".calc").click(function () {
				  let value = $(this).val();
				  field(value);
				});
				
				function field(value) {
				  input_value.val(input_value.val() + value);
				}
				
				$("#clear").click(function () {
				  input_value.val("");
				});
				
				$("#enter").click(function () {
				  myAlert("Your password " + input_value.val() + " added");
				});
				
				$("#inoutES").click(function() {
					accion.val("registro"); 
				});
				
				$("#inoutP").click(function() {
					accion.val("perfil"); 
				});
				
			});
			
			function myAlert(mensaje) {
				jQuery("#dialog-message").text(mensaje);
				$( "#dialog-message" ).dialog({ title: "Aviso del sistema..." });
				$( "#dialog-message" ).dialog( "open" );
			}
			
			function lectura(){
				console.log("Entrando a funcion lectura..................")
				var num = $("#numero").val();
				
				var obj = new Object();
				obj.tpAccion = "Capture";
				var jsonString = JSON.stringify(obj);
				
				$.ajax({
					type : "POST",
					dataType : 'json',
					data : jsonString,
					contentType :"application/json;charset=utf-8",
					url : "http://localhost:8090/finger",
					timeout: 60000,
					success : function(jsonObj) {
						jsonObj.biometricData1;
						console.log("Entrando a fuincion validar.....")
						validar(jsonObj.biometricData1);

						console.log("Saliendo de funcion lectura...................")
						
					},
					error : function(jsonObj) {
						myAlert("No hay respuesta del lector de huella.");
					}
				});
			}
			
			function validar(captura){
				console.log("Entrando a funcion validar..........")
				var numeroEmp = $("#numero").val();
				var obj =  new Object();
				obj.tpAccion = "Validate";
				obj.captura = captura; 
				obj.numeroEmpleado = numeroEmp;
				
				var jsonString = JSON.stringify(obj);
				
				$.ajax({
					type : "POST",
					dataType : 'json',
					data : jsonString,
					contentType :"application/json;charset=utf-8",
					url : "http://localhost:8090/finger",
					timeout: 60000,
					success : function(jsonObj) {
						console.log("function jsonObj..........")
						//myAlert("Respuesta del lector de huella: " + jsonObj.token);
						var token = jsonObj.token; 
						var verificacion = jsonObj.verifyBiometricData;
						if(verificacion){
							registryServlet(token);
						}else{
							location.href = "<%=basePath%>/" + "login.jsp";
						}
						
					},
					error : function(jsonObj) {
						myAlert("No hay respuesta del lector de huella.");
					}
				});
				console.log("Saliendo de funcion validar.............")
			}
			
			
			function registryServlet(objeto1){
				console.log("Entrando a registryServlet");
				var accion = $("#accion").val();
				var numEmpleado = $("#numero").val();
				var parametros = {
						numero: numEmpleado,
						token: objeto1,
						accion: accion
				};
				
				var path = "<%=basePath%>/registry?" + $.param(parametros);
				$.ajax({
					type : "GET",
					dataType : 'json',
					contentType :"application/json;charset=utf-8",
					url : path,
					timeout: 60000,
					success : function(jsonObj) {
						var url = jsonObj.url;
						var myUrl = "<%=basePath%>/" + url;
						
						console.log("function jsonObj..........")
						console.log("Registo exitoso");
						location.href = myUrl;

					},
					error : function(jsonObj) {
						myAlert("No hay respuesta del lector de huella.");
					}
				});
			}
			
			
		</script>
		<style type="text/css">
			form {
			  width: 390px;
			  margin: 50px auto;
			  background: #fff;
			  padding: 35px 25px;
			  text-align: center;
			  box-shadow: 0px 5px 5px -0px rgba(0, 0, 0, 0.3);
			  border-radius: 5px;
			}
		
			input[type="password"] {
			  padding: 0 40px;
			  border-radius: 5px;
			  width: 300px;
			  margin: auto;
			  border: 1px solid rgb(228, 220, 220);
			  outline: none;
			  font-size: 60px;
			  color: transparent;
			  text-shadow: 0 0 0 rgb(71, 71, 71);
			  text-align: center;
			}
			 
			input:focus {
			  outline: none;
			}
			 
			.pinButton {
			  border: none;
			  background: none;
			  font-size: 1.5em;
			  border-radius: 50%;
			  height: 60px;
			  font-weight: 550;
			  width: 60px;
			  color: transparent;
			  text-shadow: 0 0 0 rgb(102, 101, 101);
			  margin: 7px 20px;
			}
			 
			.clear,
			.enter {
			  font-size: 1em !important;
			}
			 
			.pinButton:hover {
			  box-shadow: #506ce8 0 0 1px 1px;
			}
			.pinButton:active {
			  background: #506ce8;
			  color: #fff;
			}
			 
			.clear:hover {
			  box-shadow: #ff3c41 0 0 1px 1px;
			}
			 
			.clear:active {
			  background: #ff3c41;
			  color: #fff;
			}
			 
			.enter:hover {
			  box-shadow: #47cf73 0 0 1px 1px;
			}
			 
			.enter:active {
			  background: #47cf73;
			  color: #fff;
			}
		</style>
	</head>
	<body >
	
		<div id="dialog-message" class="dialog-box" style="background-color: #3366CC; color: white; display: none;"></div>
		<div id="pinpad">
	  		<form >
			    <input type="password" id="numero" name="numero" /><br>
			    <input type="button" value="1" id="1" class="pinButton calc"/>
			    <input type="button" value="2" id="2" class="pinButton calc"/>
			    <input type="button" value="3" id="3" class="pinButton calc"/><br>
			    <input type="button" value="4" id="4" class="pinButton calc"/>
			    <input type="button" value="5" id="5" class="pinButton calc"/>
			    <input type="button" value="6" id="6" class="pinButton calc"/><br>
			    <input type="button" value="7" id="7" class="pinButton calc"/>
			    <input type="button" value="8" id="8" class="pinButton calc"/>
			    <input type="button" value="9" id="9" class="pinButton calc"/><br>
			    <input type="button" value="clear" id="clear" class="pinButton clear"/>
			    <input type="button" value="0" id="0 " class="pinButton calc"/>
			    <input type="button" value="enter" id="enter" class="pinButton enter"/>
			</form>
		</div>
		<div align="center">
			<input type="button" id="accion" value=""  style="display:none">
			<input type="button" id="inoutES" value="Entrada/Salida" name="" onclick="lectura();" />
			<input type="button" id="inoutP" value="Mi Perfil" onclick="lectura();" />
		</div>
	</body>
</html>