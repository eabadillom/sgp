package mx.com.ferbo.enums;

import java.math.BigDecimal;
import java.util.Arrays;

public enum ValoresBD {
	
	CERO(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)),
	QUINCE(new BigDecimal("15.00").setScale(2, BigDecimal.ROUND_HALF_UP)),
    TREINTA(new BigDecimal("30.00").setScale(2, BigDecimal.ROUND_HALF_UP)),
    CIEN(new BigDecimal("100.00").setScale(2, BigDecimal.ROUND_HALF_UP));

    private final BigDecimal valor;

    ValoresBD(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public static ValoresBD fromString(String nombre) {
        return Arrays.stream(ValoresBD.values())
                .filter(e -> e.name().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No existe el valor: " + nombre));
    }

    public static void main(String[] args) {
        System.out.println(ValoresBD.QUINCE.getValor()); // 15.00
        System.out.println(ValoresBD.fromString("CIEN").getValor()); // 100.00
    }
}
