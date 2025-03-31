package mx.com.ferbo.enums;

import java.math.BigDecimal;
import java.util.Arrays;

public enum ValoresBD {
	
	_CERO(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)),
	_15(new BigDecimal("15.00").setScale(2, BigDecimal.ROUND_HALF_UP)),
    _30(new BigDecimal("30.00").setScale(2, BigDecimal.ROUND_HALF_UP)),
    _30_4(new BigDecimal("30.40").setScale(2, BigDecimal.ROUND_HALF_UP)),
    _100(new BigDecimal("100.00").setScale(2, BigDecimal.ROUND_HALF_UP)),
    _DIAS_ANIO(new BigDecimal("365").setScale(2, BigDecimal.ROUND_HALF_UP))
    ;

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
        System.out.println(ValoresBD._15.getValor()); // 15.00
        System.out.println(ValoresBD.fromString("CIEN").getValor()); // 100.00
    }
}
