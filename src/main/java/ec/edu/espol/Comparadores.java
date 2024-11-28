package ec.edu.espol;

import java.time.LocalDate;
import java.util.Comparator;

public class Comparadores {
    public static final Comparator<Contacto> porNombreCompleto = (c1, c2) -> {
        return c1.nombre.compareToIgnoreCase(c2.nombre);
    };

    public static final Comparator<Contacto> porCantidadDeTelefonos = (c1, c2) -> {
        return Integer.compare(c1.numeros_de_telefono.size(), c2.numeros_de_telefono.size());
    };

    public static final Comparator<Contacto> porIdentificadorRedesSociales = (c1, c2) -> {
        return Integer.compare(
            c1.identificadores_de_redes_sociales.size(),
            c2.identificadores_de_redes_sociales.size()
        );
    };

    public static final Comparator<Contacto> porCumpleanos = (c1, c2) -> {
        LocalDate fechaNacimiento1 = c1.obtenerFechaNacimiento();
        LocalDate fechaNacimiento2 = c2.obtenerFechaNacimiento();

        // Si ambos son nulos, son iguales
        if (fechaNacimiento1 == null && fechaNacimiento2 == null) {
            return 0;
        }
        // Si solo uno es nulo, el que es nulo va después
        if (fechaNacimiento1 == null) {
            return 1; // c1 va después
        }
        if (fechaNacimiento2 == null) {
            return -1; // c2 va después
        }

        // Comparar las fechas de nacimiento (LocalDate implementa Comparable)
        return fechaNacimiento1.compareTo(fechaNacimiento2);
    };

    public static final Comparator<Contacto> porTipoDeContacto = (c1, c2) -> {
        // Primero, comprobamos si ambos objetos son de la misma clase
        if (c1 instanceof ContactoEmpresa && c2 instanceof ContactoPersonal) {
            return -1; // Las empresas primero
        }
        if (c1 instanceof ContactoPersonal && c2 instanceof ContactoEmpresa) {
            return 1; // Las personas después
        }
        return 0;
    };
}
