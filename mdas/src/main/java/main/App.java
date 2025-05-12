package main;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        int opc;
        do{
            System.out.println("Selecciona la acción que desees realizar:" +
                                "\n1.- Registrarse" +
                                "\n2.- Iniciar Sesión");

            opc = aux.Scanf.scanInt();

            switch (opc) {
                case 1:
                    //Funcion de registrarse
                    break;
                case 2:
                    //Funcion de iniciar sesion
                    break;
                default:
                    System.out.println("La opción seleccionada no existe");
                    break;
            }
        }while(opc != 1 && opc != 2);
        
    }
}
