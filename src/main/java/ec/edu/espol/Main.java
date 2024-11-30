package ec.edu.espol;

import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;

public class Main {
    static CircularLinkedList<Contacto> contactos;
    static CircularLinkedListIterator<Contacto> cursor_contactos;
    static boolean running_app = true;

    static final boolean AUTOGUARDADO = true;

    public static void main(String[] args) {
        contactos = Contacto.cargarContactos();
        cursor_contactos = contactos.iterator();
        BusquedaFiltrada.setContactos(contactos);
        cursor_contactos.next();

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
        imprimirTablaComandos();
        while (running_app) {
            System.out.print("\nIngresa un comando: ");
            String comando = scanner.nextLine();
            procesarComando(comando, scanner);
        }
        guardarProgreso();
    }

    static final class Cmd_Desc_Pair {
        String command;
        String description;

        public Cmd_Desc_Pair(String command, String description) {
            this.command = command;
            this.description = description;
        }
    }

    static final Cmd_Desc_Pair[] comandos = {
        new Cmd_Desc_Pair("ayuda",           "Imprimir esta ayuda"),
        new Cmd_Desc_Pair("cerrar",          "Cerrar la aplicación"),
        new Cmd_Desc_Pair("lista",           "Visualizar la lista de contactos"),
        new Cmd_Desc_Pair("lista_detallada", "Visualizar la lista de contactos"),
        new Cmd_Desc_Pair("anterior",        "Mover el cursor al contacto anterior"),
        new Cmd_Desc_Pair("siguiente",       "Mover el cursor al contacto siguiente"),
        new Cmd_Desc_Pair("nuevo",           "Crear un contacto nuevo"),
        new Cmd_Desc_Pair("editar",          "Editar el contacto del cursor"),
        new Cmd_Desc_Pair("#editar",         "Editar un contacto a partir de su número"),
        new Cmd_Desc_Pair("eliminar",        "Eliminar el contacto del cursor"),
        new Cmd_Desc_Pair("#eliminar",       "Eliminar un contacto a partir de su número"),
        new Cmd_Desc_Pair("filtrar",         "Buscar los contactos que cumplen un criterio"),
        new Cmd_Desc_Pair("ordenar",         "Ordenar la lista de contactos en base a un criterio"),
    };

    static void procesarComando(String command, Scanner scanner) {
        String[] parts = command.split(" ");
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        switch (parts[0]) {
            case "ayuda": comandoAyuda(args); break;
            case "cerrar": comandoCerrar(args); break;

            case "lista": imprimirListaContactos(false); break;
            case "lista_detallada": imprimirListaContactos(true); break;

            case "siguiente": {
                cursor_contactos.next();
                imprimirListaContactos(false);
            } break;

            case "anterior": {
                cursor_contactos.prev();
                imprimirListaContactos(false);
            } break;

            case "nuevo": {
                Contacto nuevo = Contacto.next(scanner);
                contactos.add(nuevo);
                cursor_contactos.reset();
                if (AUTOGUARDADO) guardarProgreso();
            } break;

            case "editar": editar_contacto_del_cursor(scanner); break;
            case "#editar": editar_contacto_por_numero(scanner); break;

            case "eliminar": eliminar_contacto_del_cursor(); break;
            case "#eliminar": eliminar_contacto_por_numero(scanner); break;

            case "filtrar": comandoFiltrar(scanner); break;
            case "ordenar": comandoOrdenar(scanner); break;

            default: {
                System.out.println("Comando inválido: \"" + parts[0]+ '"');
                System.out.println("    Escribe \"ayuda\" para ver los comandos válidos");
            }
        }
    }

    static void comandoCerrar(String[] args) {
        if (args.length != 0) {
            System.out.println("Se esperaba 0 argumentos para este comando.");
            System.out.println("Intentalo de nuevo.");
            return;
        }
        running_app = false;
    }

    static void comandoAyuda(String[] args) {
        if (args.length != 0) {
            System.out.println("Se esperaba 0 argumentos para este comando.");
            System.out.println("Intentalo de nuevo.");
            return;
        }
        imprimirTablaComandos();
    }

    static void imprimirTablaComandos() {
        System.out.printf("+---------------------------------------------------------------------------+\n");
        System.out.printf("|                          Comandos Generales                               |\n");
        System.out.printf("+-----------------+---------------------------------------------------------+\n");
        for (int i = 0; i < comandos.length; i++) {
            System.out.printf("| %-15s | %-55s |\n", comandos[i].command, comandos[i].description);
        }
        System.out.printf("+-----------------+---------------------------------------------------------+\n");
    }

    static void imprimirListaContactos(final boolean detallado) {
        int i = 0;
        CircularLinkedListIterator<Contacto> it = contactos.iterator();
        while (it.hasNext() && !it.hasLooped()) {
            Contacto contacto = it.next();
            String cursor = (it.getCurrentNode() == cursor_contactos.getCurrentNode()) ? "->" : "  ";
            char tipo = (contacto instanceof ContactoPersonal) ? 'P' : 'E';
            System.out.printf("%s [%c] contacto #%d: ", cursor, tipo, i+1);
            if (detallado) {
                System.out.println(contacto.toString());
            } else {
                System.out.println(contacto.nombre);
            }
            i++;
        }
    }

    static int opcionAtributoLista(Scanner scanner, String nombreAtributo) {
        System.out.println("¿Qué deseas hacer en \"" + nombreAtributo + "\"?");
        System.out.println("    1. Añadir uno a la lista");
        System.out.println("    2. Cambiar uno de la lista");
        System.out.println("    3. Borrar uno de la lista");
        return nextNumero(scanner, "Escoge una opción (número): ", 1, 3);
    }

    @FunctionalInterface
    private static interface Funcion_Scan_Contacto<T> {
        T next(Scanner scanner);
    }

    private static <T> void cambiarAtributoLista(Scanner scanner, MyList<T> list, int subopcion, Funcion_Scan_Contacto<T> funcion_scan) {
        switch (subopcion) {
            case 1 -> { // Añadir
                System.out.print("Valor: ");
                list.add(funcion_scan.next(scanner));
            }
            case 2 -> { // Cambiar
                if (list.isEmpty()) {
                    System.out.println("No hay elementos que cambiar.");
                    return;
                }
                list.imprimirNumerado("    ");
                int indice = nextNumero(scanner, "Escoge uno (índice): ", 1, list.size());
                System.out.print("Valor: ");
                list.set(indice-1, funcion_scan.next(scanner));
            }
            case 3 -> { // Borrar
                if (list.isEmpty()) {
                    System.out.println("No hay elementos que borrar.");
                    return;
                }
                list.imprimirNumerado("    ");
                int indice = nextNumero(scanner, "Escoge uno (índice): ", 1, list.size());
                list.removeAt(indice-1);
            }
        }
    }

    static void editarContacto(Scanner scanner, Contacto contacto) {
        final boolean es_personal = (contacto instanceof ContactoPersonal);
        System.out.println("Escoge que atributo quieres cambiar");
        System.out.println("     1. Nombre");
        System.out.println("     2. Foto");
        System.out.println("     3. Dirección");
        System.out.println("     4. Emails");
        System.out.println("     5. Números de teléfono");
        System.out.println("     6. Identificadores de redes sociales");
        System.out.println("     7. Fechas de interes");
        System.out.println("     8. Contactos relacionados");
        System.out.println("     9. País");
        System.out.println("    10. Ciudad");
        System.out.println("    11. " + (es_personal ? "Apellido" : "Sucursales"));

        final int opcion = nextNumero(scanner, "Escoge uno (número): ", 1, 10);
        switch (opcion) {
            case 1 -> {
                System.out.print("Nuevo nombre: ");
                contacto.nombre = scanner.nextLine();
            }
            case 2 -> {
                System.out.print("Nuevo path de la foto: ");
                contacto.foto = new Foto(scanner.nextLine());
            }
            case 3 -> {
                System.out.print("Nueva dirección: ");
                contacto.direccion = scanner.nextLine();
            }
            case 4 -> {
                final int subopcion = opcionAtributoLista(scanner, "emails");
                cambiarAtributoLista(scanner, contacto.emails, subopcion, new Funcion_Scan_Contacto<String>() {
                    @Override
                    public String next(Scanner scanner) {
                        return scanner.nextLine();
                    }
                });
            }
            case 5 -> {
                final int subopcion = opcionAtributoLista(scanner, "números de teléfono");
                cambiarAtributoLista(scanner, contacto.numeros_de_telefono, subopcion, new Funcion_Scan_Contacto<String>() {
                    @Override
                    public String next(Scanner scanner) {
                        return scanner.nextLine();
                    }
                });
            }
            case 6 -> {
                final int subopcion = opcionAtributoLista(scanner, "identificadores de redes sociales");
                cambiarAtributoLista(scanner, contacto.identificadores_de_redes_sociales, subopcion, new Funcion_Scan_Contacto<String>() {
                    @Override
                    public String next(Scanner scanner) {
                        return scanner.nextLine();
                    }
                });
            }
            case 7 -> {
                final int subopcion = opcionAtributoLista(scanner, "fechas de interes");
                cambiarAtributoLista(scanner, contacto.fechas_de_interes, subopcion, new Funcion_Scan_Contacto<FechaDeInteres>() {
                    @Override
                    public FechaDeInteres next(Scanner scanner) {
                        System.out.println(); // Salto de línea antes del primer prompt.
                        return FechaDeInteres.next(scanner, "    ");
                    }
                });
            }
            case 8 -> {
                final int subopcion = opcionAtributoLista(scanner, "contactos relacionados");
                cambiarAtributoLista(scanner, contacto.contactos_relacionados, subopcion, new Funcion_Scan_Contacto<String>() {
                    @Override
                    public String next(Scanner scanner) {
                        return scanner.nextLine();
                    }
                });
            }
            case 9 -> {
                System.out.print("País: ");
                contacto.pais = scanner.nextLine();
            }
            case 10 -> {
                System.out.print("Ciudad: ");
                contacto.ciudad = scanner.nextLine();
            }
            case 11 -> {
                if (contacto instanceof ContactoPersonal contacto_personal) {
                    System.out.print("Apellido: ");
                    contacto_personal.apellido = scanner.nextLine();
                }
                else if (contacto instanceof ContactoEmpresa contacto_empresa) {
                    final int subopcion = opcionAtributoLista(scanner, "sucursales");
                    cambiarAtributoLista(scanner, contacto_empresa.sucursales, subopcion, new Funcion_Scan_Contacto<String>() {
                        @Override
                        public String next(Scanner scanner) {
                            return scanner.nextLine();
                        }
                    });
                }
            }
        }
    }

    static void editar_contacto_del_cursor(Scanner scanner) {
        if (contactos.isEmpty()) {
            System.out.println("No hay contactos en la lista de contactos.");
            return;
        }
        editarContacto(scanner, cursor_contactos.getCurrent());
        System.out.println("Contacto editado exitosamente");
        if (AUTOGUARDADO) guardarProgreso();
    }

    static void editar_contacto_por_numero(Scanner scanner) {
        System.out.print("Ingrese el teléfono del contacto a editar: ");
        String telefono_a_editar = scanner.nextLine();

        // Recorrer todos los contactos
        CircularLinkedListIterator<Contacto> it = contactos.iterator();
        while (it.hasNext() && !it.hasLooped()) {
            Contacto actual = it.next();

            // Buscar el que se va a editar
            for (String telefono: actual.numeros_de_telefono) {
                if (telefono_a_editar.equals(telefono)) {
                    editarContacto(scanner, it.getConsumed());
                    System.out.println("Contacto editado exitosamente");
                    if (AUTOGUARDADO) guardarProgreso();
                    return;
                }
            }        
        }
        System.out.println("Contacto no encontrado");
    }

    static void eliminar_contacto_del_cursor() {
        if (contactos.isEmpty()) {
            System.out.println("No hay contactos que eliminar.");
            return;
        }
        cursor_contactos.remove();
        System.out.println("Contacto eliminado exitosamente");
        if (AUTOGUARDADO) guardarProgreso();
        imprimirListaContactos(false);
    }

    static void eliminar_contacto_por_numero(Scanner scanner) {
        System.out.print("Ingrese el teléfono del contacto a eliminar: ");
        String telefono_a_eliminar = scanner.nextLine();

        // Recorrer todos los contactos
        CircularLinkedListIterator<Contacto> it = contactos.iterator();
        while (it.hasNext() && !it.hasLooped()) {
            Contacto actual = it.next();

            // Buscar el que se va a editar
            for (String telefono: actual.numeros_de_telefono) {
                if (telefono_a_eliminar.equals(telefono)) {
                    it.removeConsumed();
                    System.out.println("Contacto eliminado exitosamente");
                    if (AUTOGUARDADO) guardarProgreso();
                    cursor_contactos.reset();
                    return;
                }
            }        
        }
        System.out.println("Contacto no encontrado");
    }

    static void comandoFiltrar(Scanner scanner) {
        System.out.println("Criterios de busqueda:");
        System.out.println("    1. Por mes de nacimiento.");
        System.out.println("    2. Por país.");
        System.out.println("    3. Por país y ciudad.");
        System.out.println("    4. Todos los contactos personales.");
        System.out.println("    5. Todos los contactos de empresa.");

        final int opcion = nextNumero(scanner, "Escoge uno (número): ", 1, 5);

        MyList<Contacto> filtrado = switch (opcion) {
            case 1 -> {
                int mes = nextNumero(scanner, "Ingresa el mes (1-12): ", 1, 12);
                yield BusquedaFiltrada.porMesDeNacimiento(mes);
            }

            case 2 -> {
                System.out.print("País: ");
                String pais = scanner.nextLine();
                yield BusquedaFiltrada.porPais(pais);
            }

            case 3 -> {
                System.out.print("País: ");
                String pais = scanner.nextLine();
                System.out.print("Ciudad: ");
                String ciudad = scanner.nextLine();
                yield BusquedaFiltrada.porPaisCiudad(pais, ciudad);
            }

            case 4 -> BusquedaFiltrada.tipoPersonal();
            case 5 -> BusquedaFiltrada.tipoEmpresa();

            default -> null;
        };

        if (filtrado == null || filtrado.isEmpty()) {
            System.out.println("No hubieron resultados con este criterio.");
            return;
        }
        for (Contacto contacto: filtrado) {
            System.out.println(contacto);
        }
    }

    static void comandoOrdenar(Scanner scanner) {
        System.out.println("Criterios de ordenamiento:");
        System.out.println("    1. Por nombre completo.");
        System.out.println("    2. Por cantidad de telefonos.");
        System.out.println("    3. Por identificadores de redes sociales.");
        System.out.println("    4. Por cumpleaños.");
        System.out.println("    5. Por tipo de contacto.");

        final int opcion = nextNumero(scanner, "Escoge uno (número): ", 1, 5);

        Comparator<Contacto> comparador = switch (opcion) {
            case 1 -> Comparadores.porNombreCompleto;
            case 2 -> Comparadores.porCantidadDeTelefonos;
            case 3 -> Comparadores.porIdentificadorRedesSociales;
            case 4 -> Comparadores.porCumpleanios;
            case 5 -> Comparadores.porTipoDeContacto;
            default -> null;
        };

        if (comparador == null) {
            throw new IllegalStateException("No se esperaba que el comparador fuera null. Revisa si cubriste todos los casos.");
        }

        contactos.sort(comparador);
        cursor_contactos.reset();
        cursor_contactos.next();
        imprimirListaContactos(false);
    }

    public static int nextNumero(Scanner scanner, String prompt, int a, int b) {
        int numero = a - 1;
        do {
            try {
                System.out.print(prompt);
                numero = scanner.nextInt();
                scanner.nextLine();
                if (numero < a || numero > b) {
                    System.out.printf("Debe estar entre %d y %d. Intenta de nuevo.\n", a, b);
                }
            } catch (InputMismatchException e) {
                System.out.println("Debe ser un entero. Intenta de nuevo.");
                scanner.nextLine();
            }
        } while (numero < a || numero > b);

        return numero;
    }

    static void guardarProgreso() {
        try {
            Contacto.guardarContactos(contactos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
