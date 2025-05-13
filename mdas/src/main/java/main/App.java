package main;

import dto.*;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        int opc;
        Usuario usuario = new Usuario();
        String dni = "";
        do{
            System.out.println("\nSelecciona la acción que desees realizar:" +
                                "\n1.- Registrarse" +
                                "\n2.- Iniciar Sesión" +
                                "\n0.- Salir");

            opc = aux.Scanf.scanInt();
            
            //Para hacer un clear de la terminal
            System.out.print("\033[H\033[2J");
            System.out.flush();

            switch (opc) {
                case 0:
                    System.out.println("\nSaliendo de la aplicación");
                    break;
                case 1:
                    System.out.println("\nIntroduce tu correo:");
                    usuario.setCorreo(aux.Scanf.scanString());
                    System.out.println("\nIntroduce tu contraseña:");
                    usuario.setContrasena(aux.Scanf.scanString());
                    System.out.println("\nIntroduce tu nombre y apellidos:");
                    usuario.setNombreCompleto(aux.Scanf.scanString());
                    System.out.println("\nIntroduce tu numero de teléfono:");
                    usuario.setTelefono(aux.Scanf.scanInt());
                    System.out.println("\nIntroduce tu DNI:");
                    dni = aux.Scanf.scanDni();
                    


                    //Funcion de registrarse
                    break;
                case 2:
                    //Funcion de iniciar sesion
                    break;
                default:
                    System.out.println("\nLa opción seleccionada no existe");
                    break;
            }
        }while(opc != 0);
        
    }

    /*
     * Lo mismo hay que pasar como argumento el correo
     */
    public static void interfazUsuario(){
        int opc;
        do{
            System.out.println("\nSelecciona la acción que desees realizar:" +
                                "\n1.- Comprar Entrada" +
                                "\n2.- Publicar Reventa" +
                                "\n3.- Recargar Monedero" +
                                "\n4.- Ver mis entradas" +
                                "\n5.- Valorar Usuario" +
                                "\n6.- Reclamar/Consultar" +
                                "\n0.- Cerrar Sesión");

            opc = aux.Scanf.scanInt();

            //Para hacer un clear de la terminal
            System.out.print("\033[H\033[2J");
            System.out.flush();

            switch (opc) {
                case 0: 
                    System.out.println("\nCerrando sesión");
                    break;
                case 1:
                    //Funcion de comprar entradas
                    break;
                case 2:
                    //Funcion de publicar reventa
                    break;
                case 3:
                    //Funcion de recargar monedero
                    break;
                case 4:
                    //Funcion de ver mis entradas
                    break;
                case 5:
                    System.out.println("\nLo sentimos, esta función no se ha implementado todavia");
                    break;
                case 6:
                    System.out.println("\nLo sentimos, esta función no se ha implementado todavia");
                    break;
                default:
                    System.out.println("\nLa opción seleccionada no existe");
                    break;
            }
        }while(opc != 0);
    }

    public static void interfazOrganizador(){
        int opc;
        do{
            System.out.println("\nSelecciona la acción que desees realizar:" +
                                "\n1.- Publicar Evento" +
                                "\n2.- Modificar Evento" +
                                "\n3.- Cancelar Evento" +
                                "\n4.- Ver mis eventos" +
                                "\n0.- Cerrar Sesión");

            opc = aux.Scanf.scanInt();

            //Para hacer un clear de la terminal
            System.out.print("\033[H\033[2J");
            System.out.flush();

            switch (opc) {
                case 0: 
                    System.out.println("\nCerrando sesión");
                    break;
                case 1:
                    //Funcion de publicar evento
                    break;
                case 2:
                    //Funcion de modificar evento
                    break;
                case 3:
                    //Funcion de cancelar evento
                    break;
                case 4:
                    //Funcion de ver mis eventos
                    break;
                default:
                    System.out.println("\nLa opción seleccionada no existe");
                    break;
            }
        }while(opc != 0);
    }
}
