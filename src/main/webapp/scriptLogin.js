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

    $('#introduceHuella').dialog({
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
function myAlert(mensaje) {
    dialogos(3);
    jQuery("#dialog-message").text(mensaje);
    $("#dialog-message").dialog({title: "Aviso del sistema..."});
    $("#dialog-message").dialog("open");
}

function dialogos(st) {

    switch (st) {
        case 1:
            $('#introduceHuella').dialog('open');
            $('#procesando').dialog('close');
            break;

        case 2:
            $('#introduceHuella').dialog('close');
            $('#procesando').dialog('open');
            break;

        case 3:
            $('#introduceHuella').dialog('close');
            $('#procesando').dialog('close');
            break;
    }
}

function tipoHuella(caso) {

    switch (caso) {
        case 1:
            $('#escanea').show();
            $('#coloca').show();
            $('#valida').hide();
            $('#acepta').hide();
            $('#invalida').hide();
            $('#niega').hide();
            $('#mensajeHuella').html("Coloca tu huella en el lector");
            break;
        case 2:
            $('#escanea').hide();
            $('#coloca').hide();
            $('#valida').show();
            $('#acepta').show();
            $('#invalida').hide();
            $('#niega').hide();
            $('#mensajeHuella').html("Huella capturada correctamente");
            break;
        case 3:
            $('#escanea').hide();
            $('#coloca').hide();
            $('#valida').hide();
            $('#acepta').hide();
            $('#invalida').show();
            $('#niega').show();
            $('#mensajeHuella').html("Huella invalida. Intenta de nuevo");
            break;
    }
}

function delay(time) {
    return new Promise(resolve => setTimeout(resolve, time));
}


async function freeze(t, valor1, valor2, token, appPath) {

    if (t) {
        dialogos(valor1);
        tipoHuella(valor2);
        await delay(3000);
        dialogos(3);
    } else {
        dialogos(valor1);
        tipoHuella(valor2);
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
        myAlert("Debe indicar un numero de empleado.");
        $("#inoutES").prop("disabled", false);
        $("#inoutP").prop("disabled", false);
        return;
    }

    $("#accion").val(accion);
    var obj = new Object();
    obj.tpAccion = "Capture";
    var jsonString = JSON.stringify(obj);
    var botonES = $("#inoutES").prop("disabled", false);

    $('#introduceHuella').dialog({title: "Lectura de huella"});
    dialogos(1);
    tipoHuella(1);

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
            console.log("Entrando a funcion validar.....")
            validar(jsonObj.biometricData1, appPath);
            console.log("Saliendo de funcion lectura...................")
            $("#inoutES").prop("disabled", false);
            $("#inoutP").prop("disabled", false);
        },
        error: function (jsonObj) {
            myAlert("No hay respuesta del lector de huella.");
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
                $('#introduceHuella').dialog({title: "Exito"});
                freeze(false, 1, 2, token, appPath);

            } else {
                $('#introduceHuella').dialog({title: "Error"});
                freeze(true, 1, 3, null, null);
            }

        },
        error: function (jsonObj) {
            var jsonText = jsonText = jsonObj.responseText;
            var respuesta = null;
            if (jsonText === undefined || jsonText === null) {
                respuesta = {"lastMessageError": "No hay respuesta de Facturama."};
            } else {
                respuesta = JSON.parse(jsonText);
            }

            myAlert(respuesta.lastMessageError);
        }
    });
    console.log("Saliendo de funcion validar.............")
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
            console.log("function jsonObj..........")
            console.log("Registo exitoso");
            location.href = myUrl;
        },
        error: function (jsonObj) {
            myAlert("No hay respuesta del lector de huella.");
        }
    });
    var prueba = new Arrar(3);
}