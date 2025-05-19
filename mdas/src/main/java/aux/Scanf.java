package aux;

import java.util.Scanner;		
import java.text.SimpleDateFormat;
import java.sql.Date;


/**
 * Clase Scanf que proporciona métodos estáticos para leer entradas del usuario.
 * 
 * Esta clase contiene varios métodos estáticos para leer diferentes tipos de datos, como:
 * - Cadenas de texto (String)
 * - Enteros (int)
 * - Fechas (Date)
 * 
 * Los métodos de esta clase incluyen validación básica para asegurar que el usuario ingresa un valor válido.
 */
public class Scanf{
	
	/**
	 * Constructor de la clase Scanf.
	 * 
	 * Este es un constructor vacío que no realiza ninguna operación al ser llamado.
	 */
	public Scanf(){
		
	}
	
	/**
	 * Lee una línea de texto desde la entrada estándar (teclado).
	 * 
	 * Este método solicita al usuario que ingrese una línea de texto a través de la consola 
	 * y devuelve esa línea como un string.
	 * 
	 * @return La línea de texto ingresada por el usuario.
	 */
	public static String scanString() {
		String result;
		Scanner sc = new Scanner(System.in);
		
		result = sc.nextLine();
		
		return result;
	}
	
	/**
	 * Lee un número entero desde la entrada estándar (teclado).
	 * 
	 * Este método solicita al usuario que ingrese un número entero. Si el usuario ingresa 
	 * un valor que no es un número entero, el método captura la excepción y solicita al usuario
	 * que vuelva a introducir un valor válido.
	 * 
	 * @return El número entero ingresado por el usuario.
	 */
	public static int scanInt() {
		int result = 0, aux = 0;
		Scanner sc = new Scanner(System.in);
		do {
			try {
				result = sc.nextInt();
				aux = 1;
			}
			catch(Exception e) {
				System.out.println("Error, no se ha introducido un numero entero, vuelva a introducir el valor");
				sc.nextLine();
			}
		}while(aux == 0);
		
		return result;
	}

	public static int scanIntPositivo() {
		int result = 0;
		do{
			result = scanInt();
			if(result < 0){
				System.out.println("Error, el numero no puede ser negativo");
			}

		} while (result < 0);
		
		return result;
	}

	public static float scanFloat() {
		float result = 0; 
		int aux = 0;
		Scanner sc = new Scanner(System.in);
		do {
			try {
				result = sc.nextFloat();
				aux = 1;
			}
			catch(Exception e) {
				System.out.println("Error, no se ha introducido un numero entero, vuelva a introducir el valor");
				sc.nextLine();
			}
		}while(aux == 0);
		
		return result;
	}

	public static float scanFloatPositivo() {
		float result = 0;
		do{
			result = scanFloat();
			if(result < 0){
				System.out.println("Error, el numero no puede ser negativo");
			}

		} while (result < 0);
		
		return result;
	}

	public static float scanFloatEntre0y1() {
		float result = 0;
		do{
			result = scanFloat();
			if(result < 0 || result > 1){
				System.out.println("Error, el numero no puede ser menor que 0 o mayor que 1");
			}
		} while (result < 0 || result > 1);
		
		return result;
	}

	
	public static String scanDni() {
		String result = "";
		Boolean valido = true;
		Scanner sc = new Scanner(System.in);
		do{
			result = sc.nextLine();
			valido = true;
			result = result.toUpperCase();

			if(result.matches("[0-9]{8}[A-Z]") == false) {
				System.out.println("Error, el dni no tiene el formato correcto");
				valido = false;
			}
		}while (valido == false);
		
		return result;
	}
	
	public static Date scanFecha() {
		int aux = 0;
		String fecha = "";
		Date result = null;
		Scanner sc = new Scanner(System.in);
		do {
			try {
				fecha = sc.nextLine();
				result = Date.valueOf(fecha);
				aux = 1;
			}
			catch(Exception e) {
				System.out.println("Error, formato de fecha incorrecto, vuelva a introducir la fecha");
			}
		}while(aux == 0);
		
		return result;
	}

	public static Date scanFechaFutura(){
		Date fecha = null;
		Boolean valido = false;
		do{
			fecha = scanFecha();
			valido = fecha.after(new Date(System.currentTimeMillis()));
			if(valido == false){
				System.out.println("Error, la fecha no puede ser anterior a la actual");
			}
		}while(valido == false);
		return fecha;
	}
	
}