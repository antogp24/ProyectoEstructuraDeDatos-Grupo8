package ec.edu.espol;

import java.time.LocalDate;

public class BusquedaFiltrada {
    public static CircularLinkedListIterator<Contacto> iterator = null;

    public static void setContactos(CircularLinkedList<Contacto> contactos) {
        iterator = contactos.iterator();
    }

    private static MyList<Contacto> crearFiltrado() {
        iterator.reset();
        return new MyList<>(iterator.getList().size());
    }

    public static MyList<Contacto> porMesDeNacimiento(int mes) {
        if (mes < 1 || mes > 12) return null;
        MyList<Contacto> filtrado = crearFiltrado();
        while (iterator.hasNext() && !iterator.hasLooped()) {
            Contacto contacto = iterator.next();
            LocalDate fecha = contacto.obtenerFechaNacimiento();
            if (fecha != null && fecha.getMonthValue() == mes) {
                filtrado.add(contacto);
            }
        }
        return filtrado;
    }

    public static MyList<Contacto> porPais(String pais) {
        MyList<Contacto> filtrado = crearFiltrado();
        while (iterator.hasNext() && !iterator.hasLooped()) {
            Contacto contacto = iterator.next();
            if (contacto.pais.equalsIgnoreCase(pais)) {
                filtrado.add(contacto);
            }
        }
        return filtrado;
    }

    public static MyList<Contacto> porPaisCiudad(String pais, String ciudad) {
        MyList<Contacto> filtrado = crearFiltrado();
        while (iterator.hasNext() && !iterator.hasLooped()) {
            Contacto contacto = iterator.next();
            if (contacto.pais.equalsIgnoreCase(pais) && contacto.ciudad.equalsIgnoreCase(ciudad)) {
                filtrado.add(contacto);
            }
        }
        return filtrado;
    }

    public static MyList<Contacto> tipoPersonal() {
        MyList<Contacto> filtrado = crearFiltrado();
        while (iterator.hasNext() && !iterator.hasLooped()) {
            Contacto contacto = iterator.next();
            if (contacto instanceof ContactoPersonal) {
                filtrado.add(contacto);
            }
        }
        return filtrado;
    }

    public static MyList<Contacto> tipoEmpresa() {
        MyList<Contacto> filtrado = crearFiltrado();
        while (iterator.hasNext() && !iterator.hasLooped()) {
            Contacto contacto = iterator.next();
            if (contacto instanceof ContactoEmpresa) {
                filtrado.add(contacto);
            }
        }
        return filtrado;
    }
}
