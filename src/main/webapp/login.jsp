<%@page import="mx.com.ferbo.dto.DetEmpleadoDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="mx.com.ferbo.controller.LoginBean" %>
<%@ page import="mx.com.ferbo.model.DetEmpleado" %>
<%@ page import="javax.faces.context.FacesContext" %>    
<%DetEmpleado empleado = (DetEmpleado) session.getAttribute("empleado"); %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    String imagesPath = basePath + "/resources/recursos/images/dialog";

    if (empleado != null) {
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
        <link rel="stylesheet" href="<%= basePath%>/styleLogin.css">
        <script src="<%= basePath%>/scriptLogin.js"></script>
    </head>
    <body  class="login-body">
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
                    <input type="button" id="inoutES" value="Entrada / Salida" name="" onclick="lectura('registro', '<%=basePath%>');" class=" btnfos btnfos-5" />
                    <input type="button" id="inoutP" value="Mi Perfil" onclick="lectura('perfil', '<%=basePath%>');" class=" btnfos btnfos-5"  />
                </div>
            </form>
        </div>
        <div id="dialogSystem" title="un titulo" style="display: none;">
            <table id="tab-introduce">
                <tr>
                    <td id="aviso" align="center" valign="center">
                        <img id="reporte" src="<%= imagesPath%>/administrador.png">
                        <img id="escanea" src="<%= imagesPath%>/finger_print_01s.png">
                        <img id="valida" src="<%= imagesPath%>/finger_print_02s.png">
                        <img id="invalida" src="<%= imagesPath%>/finger_print_03s.png">
                    </td>
                </tr>
                <tr>
                    <td id="estado">
                        <img id="coloca" src="<%= imagesPath%>/huella.gif">
                        <img id="acepta" src="<%= imagesPath%>/pasa.png">
                        <img id="niega" src="<%= imagesPath%>/nopasa.png">
                        <label id="mensajeUsuario">unTexto</label>
                    </td>
                </tr>

            </table>
        </div>
        <div id="procesando" title="Procesando" style="display: none;">
            <table id="tab-procesando">
                <tr>
                    <td id="espere" align="center" valing="center"> Espere un momento </td>
                </tr>
                <tr>
                    <td><img src="<%= imagesPath%>/cargando.gif"></td>
                </tr>
            </table>
        </div>
        <div id="registro" title="Registro" style="display: none;">
            <table id="tab-Registro">
                <tr>
                    <td align="left" valign="center">
                        <label id="LEmpleado">Empleado: </label>
                        <label id="empleado">unTexto</label>
                    </td>
                </tr>
                <tr>
                    <td align="left" valign="center">
                        <label id="LEntrada">Hora de entrada: </label>
                        <label id="entrada">unTexto</label>
                    </td>
                </tr>
                <tr>
                    <td align="left" valign="center">
                        <label id="LSalida">Hora de salida: </label>
                        <label id="salida">unTexto</label>
                    </td>
                </tr>
                <tr>
                    <td>
                        <img id="acepta" src="<%= imagesPath%>/pasa.png">
                        <label id="mensajeRegistro">unTexto</label>
                    </td>
                </tr>
            </table>
        </div>         
    </body>
</html>