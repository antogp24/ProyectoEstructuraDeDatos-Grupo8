package ec.edu.espol;

import java.util.Scanner;

import java.util.Arrays;

public class Main {
    static MyList<Contacto> contactos = new MyList<>();
    static int indice_contacto_activo = 0;
    static boolean running_app = true;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (running_app){
            System.out.print("Ingresa un comando: ");
            String comando = scanner.nextLine();
            process_command(comando, scanner);
        }
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
        new Cmd_Desc_Pair("ayuda",              "Imprimir esta ayuda"),
        new Cmd_Desc_Pair("contacto nuevo",     "Crear un contacto nuevo"),
        new Cmd_Desc_Pair("contacto lista",     "Visualizar la lista de contactos"),
        new Cmd_Desc_Pair("contacto anterior",  "Mover el cursor al contacto anterior"),
        new Cmd_Desc_Pair("contacto siguiente", "Mover el cursor al contacto siguiente"),
        new Cmd_Desc_Pair("cerrar",             "Cerrar la aplicación"),
    };

    static void process_command(String command, Scanner scanner) {
        String[] parts = command.split(" ");
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        switch (parts[0]) {
            case "ayuda": command_ayuda(args); break;
            case "cerrar": command_cerrar(args); break;
            case "contacto": command_contacto(args, scanner); break;
            default: {
                System.out.println("Comando inválido: \"" + parts[0]+ '"');
                 System.out.println("    Escribe \"ayuda\" para ver los comandos válidos");
            }
        }
    }

    static void command_contacto(String[] args, Scanner scanner) {
        if (args.length != 1) {
            System.out.println("Se esperaba 1 argumento para este comando.");
            System.out.println("Intentalo de nuevo.");
            return;
        }
        switch (args[0]) {
            case "nuevo": {
                Contacto nuevo = Contacto.next(scanner);
                contactos.add(nuevo);
            } break;

            case "lista": {
                visualize_commands();
            } break;

            case "siguiente": {
                indice_contacto_activo = (indice_contacto_activo + 1) % contactos.size();
                visualize_commands();
            } break;

            case "anterior": {
                indice_contacto_activo = mod(indice_contacto_activo - 1, contactos.size());
                visualize_commands();
            } break;

            default: {
                System.out.println("Argumento inválido: \"" + args[0] + '"');
                 System.out.println("    Escribe \"ayuda\" para ver los argumentos válidos");
            }
        }
    }

    static void command_cerrar(String[] args) {
        if (args.length != 0) {
            System.out.println("Se esperaba 0 argumentos para este comando.");
            System.out.println("Intentalo de nuevo.");
            return;
        }
        running_app = false;
    }

    static void command_ayuda(String[] args) {
        if (args.length != 0) {
            System.out.println("Se esperaba 0 argumentos para este comando.");
            System.out.println("Intentalo de nuevo.");
            return;
        }
        for (int i = 0; i < comandos.length; i++) {
            System.out.printf("| %-20s | %-45s |\n", comandos[i].command, comandos[i].description);
        }
    }

    static void visualize_commands() {
        for (int i = 0; i < contactos.size(); i++) {
            String nombre = contactos.get(i).nombre;
            String cursor = (i == indice_contacto_activo) ? "->" : "  ";

            System.out.printf("%s contacto #%d: '%s'\n", cursor, i+1, nombre);
        }
    }

    static int mod(int a, int b) {
        return (a % b + b) % b;
    }
}
