package ec.edu.espol;

import java.util.Scanner;
import java.util.Map;
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

	static final Map<String, String> comandos = Map.of(
		"ayuda",              "Imprimir esta ayuda",
		"contacto nuevo",     "Crear un contacto nuevo",
		"cerrar",             "Cerrar la aplicación"
	);

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
		comandos.forEach((comando, descripcion) -> {
			System.out.printf("| %-15s | %-25s |\n", comando, descripcion);
		});
	}
}
