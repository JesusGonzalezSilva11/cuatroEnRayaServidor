package cuatroRayaSokets;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Leer implements Runnable{
	Socket canal=null;
	InputStream streamEntrada=null;
	Datos base;
	String usser;
	String passw;
	int partidaAct;
	
	Leer (Socket canal) {
		this.canal=canal;
		base=new Datos();
		try {
			this.streamEntrada=canal.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

        //Creamos un objeto PrintWriter a partir del Stream de salidadel socket o canal de comunicación
        //El objeto PrintWriter, nos permitirá utilizar los métodos print y write para mandar datos al proceso que está escuchando al otro lado del canal.
		DataInputStream entrada = new DataInputStream(streamEntrada);	            
        String valorEntrada=null;
        System.out.println("Conectado el usuario " + canal.getInetAddress());
		while(canal.isConnected()) {
			//recibe la accion y datos
			try {
				valorEntrada = entrada.readUTF();
				var valores=valorEntrada.split("-");
				switch(valores[0]) {
				
				case "signUp":
					signUp(valores[1],valores[2]);//usuario,contraseña
					break;	
				case "signIn":
					signIn(valores[1],valores[2]);//usuario,contraseña
					break;
				case "updateUser":
					updateUser(valores[1],valores[2],valores[3]);//usuario1,usuario2,contraseña
					break;
				case "turno":
					turno(Integer.parseInt(valores[1]),Integer.parseInt(valores[2]));//columna,fila
					break;
				case "isTerminada":
					isTerminada();
					break;
				case "getUltimoTurno":
					getUltimoTurno();
					break;
				case "dimitir":
					dimitir(Integer.parseInt(valores[1]));//partidaID
					break;
				case "dimite":
					dimitir();
					break;
				case "activos":
					getActiveUsers();
					break;
				case "desafio":
					desafiar(valores[1]);//usuario desafiado
					break;
				case "getPartidas":
					getPartidas();
					break;
				case "getPartidasAcabadas":
					getPartidasAcabadas();
					break;
				case "continuaPartida"://devuelve al cliente un sting que sea tamaño&usuario-columna-fila_usuario-columna-fila...
					continuaPartida(Integer.parseInt(valores[1]));//codigo de la partida
					break;
				case "refrescarPartida"://devuelve al cliente un sting que sea tamaño&usuario-columna-fila_usuario-columna-fila...
					refrescarPartida();
					break;
				case "replay":
					replay(Integer.parseInt(valores[1]));//partidaID del replay, lo que manda son tamaño, todos los turnos usuario&columna&fila_usuario&columna&fila
					break;
				case "replaysin":
					replay();//lo que manda son tamaño, todos los turnos usuario&columna&fila_usuario&columna&fila
					break;
				}
			
			}catch (Exception e) {
				base.setDisconect(usser);// Desconectamos cliente si se llego a conectar

				break; // Cerramos conexion
			}
		}		
	}

	void signUp(String name,String pass){
		var passw=base.getPass(name);
		if(pass.equals(passw)&&!base.isConected(name)) {
			System.out.println("Te mando un true");
			base.setConect(name);
			this.usser=name;
			this.passw=pass;
			System.out.println("Logueado user: "+this.usser);
			Thread escribir = new Thread(new Escribir(canal,"boolean"+"-"+"true"));
			escribir.start();
		}else {
			System.out.println("Te mando un false");
			Thread escribir = new Thread(new Escribir(canal,"boolean"+"-"+"false"));
			escribir.start();
		}
	}
	
	void signIn(String name,String pass) {
		var users=base.getUsers();
		if(users.contains(name)||pass.equals("")) {
			System.out.println("Te mando un false");
			Thread escribir = new Thread(new Escribir(canal,"boolean"+"-"+"false"));
			escribir.start();
		}else {
			base.setUser(name, pass);
			System.out.println("Te mando un true");
			Thread escribir = new Thread(new Escribir(canal,"boolean"+"-"+"true"));
			escribir.start();
		}
	}
	
	void updateUser(String name1,String name2,String pass2) {
		var users=base.getUsers();
		if(users.contains(name2)||pass2.equals("")) {
			if(name1.equals(name2)&&!pass2.equals("")) {
				base.updateUser(name1,name2, pass2);
				System.out.println("Te mando un true");
				this.usser=name2;
				this.passw=pass2;
				Thread escribir = new Thread(new Escribir(canal,"boolean"+"-"+"true"));
				escribir.start();
			}else {
				System.out.println("Te mando un false");
				Thread escribir = new Thread(new Escribir(canal,"boolean"+"-"+"false"));
				escribir.start();
			}
		}else {
			base.updateUser(name1,name2, pass2);
			System.out.println("Te mando un true");
			this.usser=name1;
			this.passw=pass2;
			Thread escribir = new Thread(new Escribir(canal,"boolean"+"-"+"true"));
			escribir.start();
		}
	}
	
	void getActiveUsers() {//devuelve los usuarios: usuario&usuario...
		String users=base.getActiveUsers(usser);
		Thread escribir = new Thread(new Escribir(canal,"Array"+"-"+users));
		escribir.start();
	}
	
	void desafiar(String usser2) {//devuelve el tamaño del tablero es el boton de desafio en play
		int tablero=(int) (Math.random() * (12 - 7 + 1)) + 7;
		this.partidaAct=base.newPartida(usser,usser2,tablero);
		Thread escribir = new Thread(new Escribir(canal,"Array"+"-"+tablero));
		escribir.start();
	}
	
	void getPartidas() {//mandar el id de la partida nombre del usuario si le toca mover y estado conectado o desconectado es la ventana partidas
		String datos=base.getPartidas1(this.usser);
		datos+=base.getPartidas2(this.usser);
		if(datos.equals("")) {
			datos="null";
		}
		System.out.println("getPartidas del user "+this.usser+": "+datos);
		Thread escribir = new Thread(new Escribir(canal,"Array"+"-"+datos));
		escribir.start();
	}
	
	void getPartidasAcabadas() {//partidaId&usuario2&(ultimo usuario)&(gano o dimitio)_...
		String datos=base.getPartidasAcabadas1(this.usser);
		datos+=base.getPartidasAcabadas2(this.usser);
		if(datos.equals("")) {
			datos="null";
		}
		Thread escribir = new Thread(new Escribir(canal,"Array"+"-"+datos));
		escribir.start();
	}
	
	void continuaPartida(int partida) {//devuelve el tamaño del tablero y si hay algun turno usuario-columna-fila&usuario..., es el boton de continue en partidas
		this.partidaAct=partida;
		String datos=base.getTablero(partida)+"_";
		
		datos+=base.getTurnos(partida);
		
		Thread escribir = new Thread(new Escribir(canal,"Array"+"-"+datos));
		escribir.start();
	}
	
	void refrescarPartida() {//devuelve si hay algun turno usuario-columna-fila&usuario...
		String datos="null";
		System.out.println("La partida a refrescar es: "+this.partidaAct);
		if(this.partidaAct!=0) {
			datos=base.getTurnos(this.partidaAct);
		}
		System.out.println("Los turnos son: "+datos);
		
		Thread escribir = new Thread(new Escribir(canal,"Array"+"-"+datos));
		escribir.start();
	}
	
	void turno(int columna, int fila) {
		System.out.println("La partida usuario columna fila "+partidaAct+usser+columna+fila);
		
		if(base.getUsuario1(this.partidaAct).equals(this.usser)&&base.getUltimoTurnoUsuario(this.partidaAct).equals("null")) {	
			
			base.insertPosicion(partidaAct, usser, columna, fila);
			
			System.out.println("todo bien coloca ficha turno 1");
		}else if(!/*<|---El ultimo turno NO es del usuario*/base.getUltimoTurnoUsuario(this.partidaAct).equals(this.usser)
				&&base.isTerminada(partidaAct)==false//si no esta acabada claro
				&&!base.getUltimoTurnoUsuario(this.partidaAct).equals("null")) {//tampoco puede ser el turno 1
			
			if(base.ocupado(partidaAct, columna, fila)==false) {//si no esta ocupado coloca
				
				base.insertPosicion(partidaAct, usser, columna, fila);
				
				System.out.println("todo bien coloca ficha");
				
				isGanador();
			}else {
				
				System.out.println("esta ocupado");
			}
		}else {
			System.out.println("no le toca o no es el jugador 1");
		}
	}
	
	void isGanador() {
	    int tamaño = base.getTablero(partidaAct);
	    String turnos = base.getTurnos(partidaAct);
	    String[] divididos = turnos.split("_"); // usuario&columna&fila
	    String[] lastUser=divididos[divididos.length-1].split("&");
	    
	    // Crear una matriz para representar el tablero
	    String[][] tablero = new String[tamaño][tamaño];

	    // Inicializar el tablero con casillas vacías
	    for (int i = 0; i < tamaño; i++) {
	        for (int j = 0; j < tamaño; j++) {
	            tablero[i][j] = "vacio";
	        }
	    }

	    // Llenar el tablero con las jugadas realizadas
	    for (String jugada : divididos) {
	        String[] infoJugada = jugada.split("&");
	        int columna = Integer.parseInt(infoJugada[1]);
	        int fila = Integer.parseInt(infoJugada[2]);
	        String estado = infoJugada[0];

	        // Actualizar el estado de la casilla en el tablero
	        tablero[fila][columna] = estado;
	    }
	    
	    if(compruebaLineas(tablero, Integer.parseInt(lastUser[2]), Integer.parseInt(lastUser[1]), lastUser[0])) {
	    	base.ganar(partidaAct);
	    	System.out.println("Gano la partida el jugador: "+lastUser[0]);
	    }
	    
	    
	}
	
	boolean compruebaLineas(String[][] tablero, int fila, int columna, String usuario) {//comprobar si hay un 4 en raya en alguna direccion
	    Boolean cuatroEnRaya=false;
		String secuenciaGanadora=usuario+"-"+usuario+"-"+usuario+"-"+usuario;
		String secuenciaGanadoraDos=usuario+"-"+usuario+"-"+usuario;
		
		// Construir la línea horizontal completa a partir del tablero
	    String getFila = "";
	    for (int i = 0; i < tablero[0].length; i++) {
	    	getFila+=tablero[fila][i]+"-";
	    }
	    // Verificar si la línea horizontal contiene la secuencia de 4 fichas del mismo usuario
	    if(getFila.contains(secuenciaGanadora)) {
	    	cuatroEnRaya=true;
	    }
	    
	    // Verificar en dirección vertical
	    String getColumna = "";
	    for (int i = 0; i < tablero.length; i++) {
	    	getColumna+=tablero[i][columna]+"-";
	    }
	    if(getColumna.contains(secuenciaGanadora)) {
	    	cuatroEnRaya=true;
	    }
	    
	    // Verificar en dirección diagonal superior-izquierda a inferior-derecha
	    String getDiagonalI = "";
	    int i = fila;
	    int j = columna;
	    while (i != 0 && j != 0) {
	        i--;
	        j--;
	    }
	    while (i < tablero.length && j < tablero[0].length) {
	    	getDiagonalI+=tablero[i][j]+"-";
	        i++;
	        j++;
	    }
	    if (getDiagonalI.contains(secuenciaGanadoraDos)) {
	        cuatroEnRaya = true;
	    }

	    // Verificar en dirección diagonal superior-derecha a inferior-izquierda
	    String getDiagonalD = "";
	    i = fila;
	    j = columna;
	    while (i != 0 && j != tablero[0].length - 1) {
	        i--;
	        j++;
	    }
	    while (i < tablero.length && j >= 0) {
	    	getDiagonalD+=tablero[i][j]+"-";
	        i++;
	        j--;
	    }
	    if (getDiagonalD.contains(secuenciaGanadoraDos)) {
	        cuatroEnRaya = true;
	    }
	    
	    return cuatroEnRaya;
	}
	
	private void isTerminada() {
		if(base.isTerminada(this.partidaAct)==true) {
			Thread escribir = new Thread(new Escribir(canal,"boolean"+"-"+"true"));
			escribir.start();
		}else {
			Thread escribir = new Thread(new Escribir(canal,"boolean"+"-"+"false"));
			escribir.start();
		}
	}
	
	void getUltimoTurno() {//devuelve usuario&columna&fila
		String turnos=base.getTurnos(this.partidaAct);
		String[] divididos=turnos.split("_");
		Thread escribir = new Thread(new Escribir(canal,"Array"+"-"+divididos[divididos.length-1]));
		escribir.start();
	}
	
	void dimitir(int partida) {
		if(partida!=0) {
			System.out.println("La partida es: "+partida);
			if(base.isTerminada(partida).equals(false)) {
				base.dimitir(partida);
			}
		}
		
	}
	
	void dimitir() {
		if(base.isTerminada(this.partidaAct)==false) {
			System.out.println("Dimitio el jugador: "+this.usser+this.partidaAct);
			base.dimitir(this.partidaAct);
		}
	}
	
	void replay(int partida) {//tamaño_usuario&columna&fila_...
		int tamaño=base.getTablero(partida);
		String datos=tamaño+"_"+base.getTurnos(partida);
		
		Thread escribir = new Thread(new Escribir(canal,"Array"+"-"+datos));
		escribir.start();
	}
	void replay() {//tamaño_usuario&columna&fila_...
		int tamaño=base.getTablero(this.partidaAct);
		String datos=tamaño+"_"+base.getTurnos(this.partidaAct);
		
		Thread escribir = new Thread(new Escribir(canal,"Array"+"-"+datos));
		escribir.start();
	}
}
