package cuatroRayaSokets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Datos {
	private static String nameDatabase = "rayas";
	private static String user = "root";
	private static String pass = "root";
	
	public Datos(){		
	}
	
	//recoge los datos que hagan falta de la base de datos
	
	static ArrayList<String> getUsers() {
		ArrayList<String> listado=new ArrayList<String>();
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT name FROM usuario;");
			while (resultSet.next()) {
				listado.add(resultSet.getString("name").trim());
			}
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return listado;
	};
	
	static String getActiveUsers(String name) {
		String listado="null";
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT name FROM usuario WHERE conectado = TRUE AND name NOT LIKE '"+name+"';");
			while (resultSet.next()) {
				listado+=resultSet.getString("name").trim()+"&";
			}
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return listado;
	};
	
	String getPass(String name) {
		String passw=null;
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT pass FROM usuario WHERE name like '"+name+"';");
			
			if (resultSet.next()) {
	            passw = resultSet.getString("pass").trim();
	        }

			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return passw;
	};
	
	static void setUser(String name,String passw) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO usuario VALUES ('"+name+"','"+passw+"',false);");
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}
	
	static void setConect(String name) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			statement.executeUpdate("UPDATE usuario SET conectado=true WHERE name like '"+name+"';");
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}
	
	static void setDisconect(String name) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			statement.executeUpdate("UPDATE usuario SET conectado=false WHERE name like '"+name+"';");
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}
	
	static Boolean isConected(String name) {
		Boolean conectado=false;
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT conectado FROM usuario WHERE name like '"+name+"';");
			
			if (resultSet.next()) {
	            String recoge = resultSet.getString("conectado").trim();
	            if(recoge.equals("1")) {
	            	conectado=true;
	            }
	        }

			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return conectado;
	};
	
	static void updateUser(String name1,String name2,String pass2) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			statement.executeUpdate("UPDATE usuario SET name = '"+name2+"', pass = '"+pass2+"' WHERE name like '"+name1+"';");
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}

	static int getUltimoTurno() {
		int id=0;	
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT MAX(turnoID) FROM turno;");
			
			if (resultSet.next()) {
				id = Integer.parseInt(resultSet.getString("MAX(turnoID)").trim());	       
	        }
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return id;
	}
	
	static String getUltimoTurnoUsuario(int partida) {
		String usuario="null";	
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT usuario FROM turno WHERE turnoID = "
					+ "(SELECT MAX(turnoID) FROM turno WHERE partidaID = "+partida+");");
			
			if (resultSet.next()) {
				usuario = resultSet.getString("usuario").trim();
	        }
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return usuario;
	}
	
	static String getTurnos(int partida) {
		String datos="null";	
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT usuario,columna,fila FROM turno WHERE partidaID="+partida+";");
			
			while (resultSet.next()) {
				datos += resultSet.getString("usuario").trim()+"&";
				datos += resultSet.getString("columna").trim()+"&";
				datos += resultSet.getString("fila").trim()+"_";
	        }
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return datos;
	}
	
	static String getUsuario2(int partida) {
		String usuario2="null";	
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT usuario2 from partida WHERE partidaID = "+partida+";");
			
			if (resultSet.next()) {
				usuario2 = resultSet.getString("usuario2").trim();
	        }
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return usuario2;
	}
	
	static String getUsuario1(int partida) {
		String usuario="null";	
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT usuario1 FROM partida WHERE partidaID = "+partida+";");
			
			if (resultSet.next()) {
				usuario = resultSet.getString("usuario1").trim();
	        }
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return usuario;
	}
	
	static int getUltimaPartida() {
		int id=0;	
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT MAX(partidaID) FROM partida;");
			
			if (resultSet.next()) {
				id = Integer.parseInt(resultSet.getString("MAX(partidaID)").trim());	       
	        }
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return id;
	}
	
	static int newPartida(String usuario1,String usuario2,int tablero) {
		Connection connection = null;
		int partida=getUltimaPartida()+1;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO partida VALUES ("+partida+",'"+usuario1+"','"+usuario2+"',null,"+tablero+");");
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return partida;
	}

	static String getPartidasAcabadas1(String usuario) {
		Connection connection = null;
		String datos = "";
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT partidaID,usuario2,ganador FROM partida WHERE usuario1 like '"+usuario+"' && ganador IS NOT NULL;");
			while (resultSet.next()) {
				datos += resultSet.getString("partidaID").trim()+"&";
				datos += resultSet.getString("usuario2").trim()+"&";
				datos += getUltimoTurnoUsuario(Integer.parseInt(resultSet.getString("partidaID")))+"&";
				datos += resultSet.getString("ganador").trim()+"_";
	        }
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return datos;
	}

	static String getPartidasAcabadas2(String usuario) {
		Connection connection = null;
		String datos = "";
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT partidaID,usuario1,ganador FROM partida WHERE usuario2 like '"+usuario+"' && ganador IS NOT NULL;");
			while (resultSet.next()) {
				datos += resultSet.getString("partidaID").trim()+"&";
				datos += resultSet.getString("usuario1").trim()+"&";
				datos += getUltimoTurnoUsuario(Integer.parseInt(resultSet.getString("partidaID")))+"&";
				datos += resultSet.getString("ganador").trim()+"_";
	        }
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return datos;
	}
	
	static String getPartidas1(String usuario) {
		Connection connection = null;
		String datos = "";
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT partidaID,usuario2 FROM partida WHERE usuario1 like '"+usuario+"' && ganador IS NULL;");
			while (resultSet.next()) {
				datos += resultSet.getString("partidaID").trim()+"&";
				String usuario2=resultSet.getString("usuario2").trim();
				datos += usuario2+"&";
				if(!getUltimoTurnoUsuario(Integer.parseInt(resultSet.getString("partidaID").trim())).equals(usuario)) {
					datos += "mueve&";
				}else {
					datos += "quieto&";
				}
				if(isConected(usuario2)) {
					datos +="conectado_";
				}else {
					datos +="desconectado_";
				}
	        }
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return datos;
	}

	static String getPartidas2(String usuario) {
		Connection connection = null;
		String datos = "";
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT partidaID,usuario1 FROM partida WHERE usuario2 like '"+usuario+"' && ganador IS NULL;");
			while (resultSet.next()) {
				datos += resultSet.getString("partidaID").trim()+"&";
				String usuario1=resultSet.getString("usuario1").trim();
				datos += usuario1+"&";
				if(!getUltimoTurnoUsuario(Integer.parseInt(resultSet.getString("partidaID").trim())).equals(usuario)&&
						(!getUltimoTurnoUsuario(Integer.parseInt(resultSet.getString("partidaID").trim())).equals("null"))) {
					datos += "mueve&";
				}else {
					datos += "quieto&";
				}
				if(isConected(usuario1)) {
					datos +="conectado_";
				}else {
					datos +="desconectado_";
				}
	        }
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return datos;
	}
	
	static int getTablero(int partida) {
		Connection connection = null;
		int tablero = 0;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT tablero FROM partida WHERE partidaID="+partida+";");
			if (resultSet.next()) {
				tablero = Integer.parseInt(resultSet.getString("tablero").trim());
	        }
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return tablero;
	}
	
	static Boolean ocupado(int partidaID,int columna,int fila) {
		Boolean ocupado=true;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT turnoID FROM turno WHERE partidaID = "+partidaID+" && columna = "+columna+" && fila = "+fila+";");
			if (!resultSet.next()) {
            	ocupado=false;
	        }
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return ocupado;
	}
	
	static void insertPosicion(int partidaID, String usuarioID, int columna, int fila) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO turno VALUES ("+(getUltimoTurno()+1)+","+partidaID+",'"+usuarioID+"',"+columna+","+fila+");");
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}
	
	static void dimitir(int partidaID) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			statement.executeUpdate("UPDATE partida SET ganador = 'dimitio' WHERE partidaID = "+partidaID+";");
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}
	
	static void ganar(int partidaID) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			statement.executeUpdate("UPDATE partida SET ganador = 'ganador' WHERE partidaID = "+partidaID+";");
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}

	static Boolean isTerminada(int partidaID) {
		Connection connection = null;
		Boolean termino=true;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT ganador FROM partida WHERE partidaID = "+partidaID+";");
			
			if (resultSet.next()) {
				String ganador = resultSet.getString("ganador");
				if(ganador==null) {
					System.out.println("No termino");
					termino=false;
				}
	        }
			System.out.println(termino);
			
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return termino;
	}
}
