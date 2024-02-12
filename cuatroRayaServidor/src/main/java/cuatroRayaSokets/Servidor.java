package cuatroRayaSokets;
import java.io.*;
import java.net.*;
import java.util.Scanner;
/**
 * Servidor tiene su hilo principal para siempre estar escuchando a clientes,
 * que lanza un hilo para siempre escuchar a un cliente y este lanza un hilo para escribir las peticiones
 */
class Servidor {
	static final int Puerto=2069;
	Scanner sc=new Scanner(System.in);
	
	public static void main(String[] arg) {
	    ServerSocket conexion=null;
        try{
           conexion = new ServerSocket(Puerto);
           //Solicitamos al sistema operativo que abra un puerto de escucha de conexiones.
        }catch(IOException ex){
            System.err.println("No se ha podido abrir el puerto de escucha.");
            System.err.println(ex.toString());
        }
        if (conexion != null) { //Si hemos podido abrir el puerto
        	while(true) {
	            try{
	                System.out.println("Esperando la conexion de clientes...");
	                Socket canal = conexion.accept();
	                Thread serverThread = new Thread(new Leer(canal));
	        		serverThread.start();
	            }catch(Exception ex){
	                System.err.println("Servidor cierra");
	                 System.err.print(ex.toString());
	            }
        	}
        }
	}
}