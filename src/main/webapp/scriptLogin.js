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

function dialogos(st) {

    switch (st) {
        case 1:
            $('#dialogSystem').dialog('open');
            $('#procesando').dialog('close');
            break;

        case 2:
            $('#dialogSystem').dialog('close');
            $('#procesando').dialog('open');
            break;

        case 3:
            $('#dialogSystem').dialog('close');
            $('#procesando').dialog('close');
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

async function freeze(isFreeze, st, numDialog, message, token, appPath) {

    if (isFreeze) {
        dialogos(st);
        tipoMensaje(numDialog, message);
        await delay(3000);
        dialogos(3);
    } else {
        dialogos(st);
        tipoMensaje(numDialog, message);
        await delay(1000);
        registryServlet(token, appPath);
    }
}

function lectura(accion, appPath) {
    console.log("Entrando a funcion lectura..................");
    $("#inoutES").prop("disabled", true);
    $("#inoutP").prop("disabled", true);
    var num = $("#numero").val();

    if (num.length === 0) {
        $('#dialogSystem').dialog({title: "Aviso del sistema"});
        freeze(true, 1, 4, "Debe ingresar un numero de usuario", null, null);
        $("#inoutES").prop("disabled", false);
        $("#inoutP").prop("disabled", false);
        return;
    }

    $("#accion").val(accion);
    var obj = new Object();
    obj.tpAccion = "Capture";
    var jsonString = JSON.stringify(obj);
    var botonES = $("#inoutES").prop("disabled", false);

    $('#dialogSystem').dialog({title: "Lectura de huella"});
    dialogos(1);
    tipoMensaje(1,"Coloca tu huella en el lector");

    $.ajax({
        async: true,
        type: "POST",
        dataType: 'json',
        data: jsonString,
        contentType: "application/json;charset=utf-8",
        url: "http://localhost:8090/finger",
        timeout: 60000,

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
            freeze(true, 1, 4, "Lector de huella no detectado", null, null);
            $("#inoutES").prop("disabled", false);
            $("#inoutP").prop("disabled", false);
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
        timeout: 60000,
        success: function (jsonObj) {
            console.log("function jsonObj..........");
            var token = jsonObj.token;
            var verificacion = jsonObj.verifyBiometricData;
            if (verificacion) {
                $('#dialogSystem').dialog({title: "Exito"});
                freeze(false, 1, 2, "Huella capturada correctamente",token, appPath);

            } else {
                $('#dialogSystem').dialog({title: "Error"});
                freeze(true, 1, 3, "Huella invalida. Intente de nuevo.", null, null);
            }

        },
        error: function (jsonObj) {
            var jsonText = jsonText = jsonObj.responseText;
            var respuesta = null;
            if (jsonText === undefined || jsonText === null) {
                //respuesta = {"lastMessageError": "No hay respuesta de Facturama."};
            } else {
               $('#dialogSystem').dialog({title: "Aviso del sistema"});
                freeze(true, 1, 4, "El numero " + numeroEmp + " no esta registrado.", null, null);
                respuesta = JSON.parse(jsonText);
            }
            $('#dialogSystem').dialog({title: "Error"});
                freeze(true, 1, 4, "No hay respuesta de facturama.", null, null);
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
        timeout: 60000,
        success: function (jsonObj) {
            var url = jsonObj.url;
            var myUrl = appPath + url;
            console.log("function jsonObj..........");
            console.log("Registo exitoso");
            location.href = myUrl;
        },
        error: function (jsonObj) {
            $('#dialogSystem').dialog({title: "Aviso del sistema"});
            freeze(true, 1, 4, "Notifique al admistrador de sistemas.", null, null);
        }
    });
    var prueba = new Arrar(3);
}