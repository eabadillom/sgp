<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<title></title>
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
			});
			
			function myAlert(mensaje) {
				jQuery("#dialog-message").text(mensaje);
				$( "#dialog-message" ).dialog({ title: "Aviso del sistema..." });
				$( "#dialog-message" ).dialog( "open" );
			}
			
			
			function registry() {
				var num = $("#numero").val();
				var parametros = {
						numero: num
				};
				var path = "<%=basePath%>/registry?" + $.param(parametros);
				$.ajax({
					type: "GET",
					dataType: "json",
					url: path,
					success:  function (jsonObj) {
						var respuesta = jsonObj;
						console.log("Solicitando peticion al agente FP...");
						invokeScan(respuesta.f1, respuesta.f2);
					},
					error: function (jsonObj) {
						var respuesta = JSON.parse(jsonObj.responseText);
						myAlert(respuesta);
					}
				});
				
			}
			
			function invokeScan(f1, f2) {
				var parametros = {
						"TpAccion": "VerifyFingerprint",
					    "FingerPrinToVerify": [f1, f2]
				}
				
				var obj = new Object();
                obj.TpAccion = "VerifyFingerprint";
                obj.FingerPrinToVerify = listado(f1, f2);
                var jsonString = JSON.stringify(obj);
				
				$.ajax({
					type: "POST",
					dataType: 'json',
					data:  jsonString,
					url : "http://localhost:23106",
					success:  function (jsonObj) {
						myAlert("Respuesta del lector de huella: " + jsonObj.Message);
						myAlert("Respuesta del lector de huella: " + jsonObj.VerifyBiometricData);
					},
			    	error: function(jsonObj) {
			    		myAlert("No hay respuesta del lector de huella.");
					}
				});
			}
			
			function listado(f1, f2) {
                const arreglo = [];
                arreglo.push(f1);
                arreglo.push(f2);
                return arreglo;
            };
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
	<body>
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
			<input type="button" id="inout" value="Entrada / Salida" onclick="registry();"/>
		</div>
	</body>
</html>