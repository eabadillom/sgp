package mx.com.ferbo.util;

public class FormatUtil {
	
	public static String capitalizarPrimeraLetra(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto; // Devuelve el texto original si está vacío o es nulo
        }

        // Convierte la primera letra a mayúscula y el resto permanece igual
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
	
	public static String convertirAOrdinal(int numero) {
		
        if (numero < 1 || numero > 100) {
            return "número fuera de rango (1-100)";
        }

        String[] unidades = {"", "primer", "segundo", "tercer", "cuarto", "quinto", "sexto", "séptimo", "octavo", "noveno"};
        String[] decenas = {"", "décimo", "vigésimo", "trigésimo", "cuadragésimo", "quincuagésimo", 
                            "sexagésimo", "septuagésimo", "octogésimo", "nonagésimo"};

        // Caso especial: 100
        if (numero == 100) {
            return "centésimo";
        }

        int unidad = numero % 10;
        int decena = numero / 10;

        // Generar el ordinal dependiendo de las decenas y unidades
        if (decena == 0) {
            return unidades[unidad]; // Del 1 al 9
        } else if (unidad == 0) {
            return decenas[decena]; // 10, 20, 30, ..., 90
        } else {
            return decenas[decena] + " " + unidades[unidad]; // Combinación, por ejemplo, 21 -> "vigésimo primer"
        }
    }
}
