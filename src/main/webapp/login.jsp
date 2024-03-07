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
		<style type="text/css">
			.overlay{
			    display: none;
			    position: fixed;
			    width: 100%;
			    height: 100%;
			    top: 0;
			    left: 0;
			    z-index: 999;
			    background: rgba(120, 120, 120,0.8);
			}
			
			/* Turn off scrollbar when body element has the loading class */
			body.loading{
			    overflow: hidden;   
			}
			/* Make spinner image visible when body element has the loading class */
			body.loading .overlay{
			    display: block;
			}
		</style>
		<script type="text/javascript">
			$(document).ready(function (){
				jQuery("#dialog-message" ).dialog({
					autoOpen: false,
					modal: true,
					buttons: {
						Aceptar: function() {
							$( this ).dialog( "close" );
						}
					}
				});
				
				$(document).on({
				    ajaxStart: function(){
				        $("body").addClass("loading"); 
				    },
				    ajaxStop: function(){ 
				        $("body").removeClass("loading"); 
				    },
				    ajaxError: function() {
				    	$("body").removeClass("loading");
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
			
			function lectura(accion){
				console.log("Entrando a funcion lectura..................")
				$("#inoutES").prop("disabled", true);
				$("#inoutP").prop("disabled", true);
				var num = $("#numero").val();
				
				if(num.length == 0) {
					myAlert("Debe indicar un número de empleado.");
					$("#inoutES").prop("disabled", false);
					$("#inoutP").prop("disabled", false);
					return;
				}
				
				$("#accion").val(accion);
				
				var obj = new Object();
				obj.tpAccion = "Capture";
				var jsonString = JSON.stringify(obj);
				var botonES = $("#inoutES").prop("disabled", true)
				$.ajax({
					async: false,
					type : "POST",
					dataType : 'json',
					data : jsonString,
					contentType :"application/json;charset=utf-8",
					url : "http://localhost:8090/finger",
					timeout: 60000,
					success : function(jsonObj) {
						jsonObj.biometricData1;
						console.log("Entrando a funcion validar.....")
						validar(jsonObj.biometricData1);
						console.log("Saliendo de funcion lectura...................")
						$("#inoutES").prop("disabled", false);
						$("#inoutP").prop("disabled", false);
					},
					error : function(jsonObj) {
						myAlert("No hay respuesta del lector de huella.");
						$("#inoutES").prop("disabled", false);
						$("#inoutP").prop("disabled", false);
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
					async: false,
					type : "POST",
					dataType : 'json',
					data : jsonString,
					contentType :"application/json;charset=utf-8",
					url : "http://localhost:8090/finger",
					timeout: 60000,
					success : function(jsonObj) {
						console.log("function jsonObj..........")
						var token = jsonObj.token; 
						var verificacion = jsonObj.verifyBiometricData;
						if(verificacion){
							registryServlet(token);
						}else{
							myAlert("Las huellas no coinciden");
							
						}
						
					},
					error : function(jsonObj) {
						var jsonText = jsonText = jsonObj.responseText;
						var respuesta = null;
						if(jsonText == undefined || jsonText == null) {
							respuesta = {"lastMessageError" : "No hay respuesta de Facturama."};
						} else {
							respuesta = JSON.parse(jsonText);
						}
						
						myAlert(respuesta.lastMessageError);
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
					async: false,
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
			  width: 30rem;
			  margin: 0px auto;
			  background: #fff;
			  padding: 35px 25px;
			  text-align: center;
			  box-shadow: 0px 5px 5px -0px rgba(0, 0, 0, 0.3);
			  border-radius: 5px;
			}
		
			.login-body {
				height: 100vh;
				font-family: "latoregular", "Trebuchet MS", Arial, Helvetica, sans-serif;
				font-size: 16px;
				margin: 0;
				padding: 0 0 0 0;
				background-image: linear-gradient(to top, #6b77a1, #737ea5 3%, #9599b3 15%, #b1b0bf 28%,
					#c7c1c8 41%, #d6cdcf 57%, #dfd5d3 74%, #e2d7d4);
				background-image: -ms-linear-gradient(bottom, #6B77A1 0%, #737EA5 3%, #9599B3 15%, #B1B0BF
					28%, #C7C1C8 41%, #D6CDCF 57%, #DFD5D3 74%, #E2D7D4 100%);
				background-image: -moz-linear-gradient(bottom, #6B77A1 0%, #737EA5 3%, #9599B3 15%,
					#B1B0BF 28%, #C7C1C8 41%, #D6CDCF 57%, #DFD5D3 74%, #E2D7D4 100%);
				background-image: -o-linear-gradient(bottom, #6B77A1 0%, #737EA5 3%, #9599B3 15%, #B1B0BF
					28%, #C7C1C8 41%, #D6CDCF 57%, #DFD5D3 74%, #E2D7D4 100%);
				background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0, #6B77A1),
					color-stop(3, #737EA5), color-stop(15, #9599B3),
					color-stop(28, #B1B0BF), color-stop(41, #C7C1C8),
					color-stop(57, #D6CDCF), color-stop(74, #DFD5D3),
					color-stop(100, #E2D7D4));
				background-image: -webkit-linear-gradient(bottom, #6B77A1 0%, #737EA5 3%, #9599B3 15%,
					#B1B0BF 28%, #C7C1C8 41%, #D6CDCF 57%, #DFD5D3 74%, #E2D7D4 100%);
				filter: progid:DXImageTransform.Microsoft.gradient( startColorstr="#E2D7D4",
					endColorstr="#6B77A1", GradientType=0);
				box-sizing: border-box;
				background-repeat: no-repeat;
				background-attachment: fixed;
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
			
			.btnfos {
			 background: #2196f3;			 
			  color: white;
			  border-radius: 10px;
			  cursor: pointer;
			  font-size: 1rem;
			  font-weight: 400;
			  line-height: 25px;
			  max-width: 11rem;
			  margin: 1rem;
			  padding: 0.5rem;
			  text-transform: uppercase;
			  width: 100%;
			}
			
			.btnfos-5 {
			  border: 0 solid;
			  box-shadow: inset 0 0 20px rgba(255, 255, 255, 0);
			  outline: 1px solid;
			  outline-color: rgba(255, 255, 255, 0);
			  outline-offset: 0px;
			  text-shadow: none;
			  -webkit-transition: all 1250ms cubic-bezier(0.19, 1, 0.22, 1);
			          transition: all 1250ms cubic-bezier(0.19, 1, 0.22, 1);
			  outline-color: rgba(255, 255, 255, 0.5);
			  outline-offset: 0px;
			}

			.btnfos-5:hover {
			  box-shadow: inset 0 0 20px rgba(255, 255, 255, 0.5), 0 0 20px rgba(255, 255, 255, 0.2);
			  outline-offset: 15px;
			  outline-color: rgba(255, 255, 255, 0);
			  text-shadow: 1px 1px 2px #427388;
			}
			
		
		</style>
	</head>
	<body class="login-body" >
	
		<div id="dialog-message" class="dialog-box" style="background-color: #FFFFFF; color: #000000; display: none;"></div>
		
		<div align="center">
			<img src="resources/recursos/images/login.png" width="400" height="170" >
		</div>
		
		<div id="pinpad" align="center">
	  		<form>
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
			    <input type="button" value="&#10006;" id="clear" class="pinButton clear"/>
			    <input type="button" value="0" id="0 " class="pinButton calc"/>
			    <input type="button" value="" id="enter" class="pinButton enter"/>
			    
				<div align="center">
					<input type="hidden" id="accion" value=""  style="display:none">
					<input type="button" id="inoutES" value="Entrada / Salida" name="" onclick="lectura('registro');" class=" btnfos btnfos-5" />
					<input type="button" id="inoutP" value="Mi Perfil" onclick="lectura('perfil');" class=" btnfos btnfos-5"  />
				</div>
			</form>
		</div>
	</body>
</html>