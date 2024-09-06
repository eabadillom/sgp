$(document).ready(function () {
    jQuery("#dialog-message").dialog({
        autoOpen: false,
        modal: true,
        buttons: {
            Aceptar: function () {
                $(this).dialog("close");
            }
        }
    });

    $('#dialogSystem').dialog({
        autoOpen: false,
        modal: true,
        width: 'auto',
        height: 'auto',
        'max-height': '100%',
        'max-width': '100%'
    });

    $('#procesando').dialog({
        autoOpen: false,
        modal: true,
        width: 'auto',
        height: 'auto',
        'max-height': '100%',
        'max-width': '100%'
    });

    $('#registro').dialog({
        autoOpen: false,
        modal: true,
        width: 'auto',
        height: 'auto',
        'max-height': '100%',
        'max-width': '100%'
    });

    $(document).on({
        ajaxStart: function () {
            $("body").addClass("loading");
        },
        ajaxStop: function () {
            $("body").removeClass("loading");
        },
        ajaxError: function () {
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
    $("#inoutES").click(function () {
        accion.val("registro");
    });
    $("#inoutP").click(function () {
        accion.val("perfil");
    });
});

function dialogos(stD) {

    switch (stD) {
        case 1:
            $('#dialogSystem').dialog('open');
            $('#procesando').dialog('close');
            $('#registro').dialog('close');
            break;

        case 2:
            $('#dialogSystem').dialog('close');
            $('#procesando').dialog('open');
            $('#registro').dialog('close');
            break;

        case 3:
            $('#dialogSystem').dialog('close');
            $('#procesando').dialog('close');
            $('#registro').dialog('close');
            break;

        case 4:
            $('#dialogSystem').dialog('close');
            $('#procesando').dialog('close');
            $('#registro').dialog('open');
            break;

    }
}

function tipoMensaje(numDialog, message) {

    $('#mensajeUsuario').html(message);

    switch (numDialog) {
        case 1:
            $('#reporte').hide();
            $('#escanea').show();
            $('#coloca').show();
            $('#valida').hide();
            $('#acepta').hide();
            $('#invalida').hide();
            $('#niega').hide();
            break;

        case 2:
            $('#reporte').hide();
            $('#escanea').hide();
            $('#coloca').hide();
            $('#valida').show();
            $('#acepta').show();
            $('#invalida').hide();
            $('#niega').hide();
            break;

        case 3:
            $('#reporte').hide();
            $('#escanea').hide();
            $('#coloca').hide();
            $('#valida').hide();
            $('#acepta').hide();
            $('#invalida').show();
            $('#niega').show();
            break;

        case 4:
            $('#reporte').show();
            $('#escanea').hide();
            $('#coloca').hide();
            $('#valida').hide();
            $('#acepta').hide();
            $('#invalida').hide();
            $('#niega').show();
            break;
    }
}

function delay(time) {
    return new Promise(resolve => setTimeout(resolve, time));
}

async function freeze(tF, stD, numDialog, message, token, appPath, jsonObj) {

    if (tF === 1) {
        dialogos(stD);
        tipoMensaje(numDialog, message);
        await delay(3000);
        dialogos(3);
    } else if (tF === 2) {
        dialogos(stD);
        tipoMensaje(numDialog, message);
        await delay(1000);
        registryServlet(token, appPath);
    } else {
        descifrar(jsonObj);
        dialogos(4);
        await delay(5000);
        dialogos(3);
    }
}

function formatDate(unafecha){
    const fecha = new Date(unafecha);
    
    const anio = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2,'0');
    const dia = String(fecha.getDay()).padStart(2,'0');
    const horas = String(fecha.getHours()).padStart(2,'0');
    const minutos = String(fecha.getMinutes()).padStart(2,'0');
    const segundos = String(fecha.getSeconds()).padStart(2,'0');
    
    return `${anio}-${mes}-${dia} ${horas}:${minutos}:${segundos}`;
}

function descifrar(jsonObj){
  
    const nuevaEntrada = formatDate(jsonObj.responseJSON.horaEntrada);
    const nuevaSalida = formatDate(jsonObj.responseJSON.horaSalida);
     
    $('#empleado').html(jsonObj.responseJSON.numero);
    $('#entrada').html(nuevaEntrada);
    $('#salida').html(nuevaSalida);
    $('#mensajeRegistro').html("Tu registro se guardo con exito");
    
    if(jsonObj.responseJSON.horaSalida === null){
        $('#LEmpleado').show();
        $('#empleado').show();
        $('#LEntrada').show();
        $('#entrada').show();
        $('#LSalida').hide();
        $('#salida').hide();
    }else{
        $('#LEmpleado').show();
        $('#empleado').show();
        $('#LEntrada').show();
        $('#entrada').show();
        $('#LSalida').show();
        $('#salida').show();
    }
}

function lectura(accion, appPath, tP) {
    console.log("Entrando a funcion lectura..................");
    $("#inoutES").prop("disabled", true);
    $("#inoutP").prop("disabled", true);
    var num = $("#numero").val();


    if (num.length === 0) {
        $('#dialogSystem').dialog({title: "Aviso del sistema"});
        freeze(1, 1, 4, "Debe ingresar un numero de empleado.", null, null, null);
        $("#inoutES").prop("disabled", false);
        $("#inoutP").prop("disabled", false);
        return;
    }
    
    if(num.length != tP){
        $('#dialogSystem').dialog({title: "Aviso del sistema"});
        freeze(1, 1, 4, "El numero de empleado debe ser de " + tP + " digitos.", null, null, null);
        $("#inoutES").prop("disabled", false);
        $("#inoutP").prop("disabled", false);
        return;
    }

    $("#accion").val(accion);
    var obj = new Object();
    obj.captureTimeout = 15000;
    obj.tpAccion = "Capture";
    var jsonString = JSON.stringify(obj);
    var botonES = $("#inoutES").prop("disabled", false);

    $('#dialogSystem').dialog({title: "Lectura de huella"});
    dialogos(1);
    tipoMensaje(1, "Coloca tu huella en el lector.");
    $("#inoutES").prop("disabled", false);
    $("#inoutP").prop("disabled", false);

    $.ajax({
        async: true,
        type: "POST",
        dataType: 'json',
        data: jsonString,
        contentType: "application/json;charset=utf-8",
        url: "http://localhost:8090/finger",
        timeout: 15000,

        success: function (jsonObj) {
            jsonObj.biometricData1;
            console.log("Entrando a funcion validar.....");
            validar(jsonObj.biometricData1, appPath);
            console.log("Saliendo de funcion lectura...................");
            $("#inoutES").prop("disabled", false);
            $("#inoutP").prop("disabled", false);
        },
        error: function (jsonObj) {
            $('dialogSystem').dialog({title: 'Aviso del sistema'});
            freeze(1, 1, 4, "No hay respuesta en el lector.", null, null, null);
            $("#inoutES").prop("disabled", false);
            $("#inoutP").prop("disabled", false);
            $('#numero').val('');
        }
    });
}

function validar(captura, appPath) {
    console.log("Entrando a funcion validar..........");
    var numeroEmp = $("#numero").val();
    var obj = new Object();
    obj.tpAccion = "Validate";
    obj.captura = captura;
    obj.numeroEmpleado = numeroEmp;
    var jsonString = JSON.stringify(obj);

    dialogos(2);

    $.ajax({
        async: true,
        type: "POST",
        dataType: 'json',
        data: jsonString,
        contentType: "application/json;charset=utf-8",
        url: "http://localhost:8090/finger",
        timeout: 15000,
        success: function (jsonObj) {
            console.log("function jsonObj..........");
            var token = jsonObj.token;
            var verificacion = jsonObj.verifyBiometricData;
            if (verificacion) {
                $('#dialogSystem').dialog({title: "Exito"});
                freeze(2, 1, 2, "Huella capturada correctamente.", token, appPath, null);

            } else {
                $('#dialogSystem').dialog({title: "Error"});
                freeze(1, 1, 3, "Huella invalida. Intente de nuevo.", null, null, null);
            }
        },
        error: function (jsonObj) {
            if (jsonObj.responseJSON.message === "Hubo algun problema con la base de datos") {
                $('#dialogSystem').dialog({title: "Error"});
                freeze(1, 1, 3, "Huella invalida. Intente de nuevo.", null, null, null);
                $('#numero').val('');
            } else if (jsonObj.responseJSON.horaEntrada !== undefined && jsonObj.responseJSON.horaEntrada !== null){
                freeze(3, 1, 4, "", null, null, jsonObj);
                $('#numero').val('');
            }else{
                $('#dialogSystem').dialog({title: "Error"});
                freeze(1, 1, 3, "Huella invalida. Intente de nuevo.", null, null, null);
                $('#numero').val('');
            }
        }
    });
    console.log("Saliendo de funcion validar.............");
}

function registryServlet(objeto1, appPath) {
    console.log("Entrando a registryServlet");
    var accion = $("#accion").val();
    var numEmpleado = $("#numero").val();
    var parametros = {
        numero: numEmpleado,
        token: objeto1,
        accion: accion
    };
    var path = appPath + "/registry?" + $.param(parametros);
    $.ajax({
        async: false,
        type: "GET",
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        url: path,
        timeout: 15000,
        success: function (jsonObj) {
            var url = jsonObj.url;
            var myUrl = appPath + url;
            console.log("function jsonObj..........");
            console.log("Registo exitoso");
            location.href = myUrl;
        },
        error: function (jsonObj) {
            $('#dialogSystem').dialog({title: "Aviso del sistema"});
            freeze(1, 1, 4, "Notifique al admistrador de sistemas.", null, null, null);
        }
    });
    var prueba = new Array(3);
}