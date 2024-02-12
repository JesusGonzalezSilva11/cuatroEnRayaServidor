package cuatroRayaSokets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Escribir implements Runnable{
	Socket canal=null;
	OutputStream streamSalida=null;
	String operaciones;
	
	Escribir(Socket canal,String datos) {
		this.canal=canal;
		try {
			this.streamSalida=canal.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		operaciones=datos;
	}
	@Override
	public void run() {
		try {//manda
			var salida = new DataOutputStream(streamSalida);
			var accion=operaciones.split("-");
			
			switch(accion[0]) {
			
			case "boolean":
				if(accion[1].equals("true")) {
					salida.writeBoolean(true);
					salida.flush();
				}else {
					salida.writeBoolean(false);
					salida.flush();
				}
				break;
				
			case "Array":
				salida.writeUTF(accion[1]);
				salida.flush();
				break;
				
			case "":
			}
			
	    }catch(Exception ex){
	        System.err.println("El cliente cerro");
	        System.err.print(ex.toString());
	    }
	}
}
