package accesoDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import auxiliares.LeeProperties;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class AccesoJDBC implements I_Acceso_Datos {

	private String driver, urlbd, user, password; // Datos de la conexion
	private Connection conn1;

	public AccesoJDBC() {
		System.out.println("ACCESO A DATOS - Acceso JDBC");

		try {
			HashMap<String, String> datosConexion;

			LeeProperties properties = new LeeProperties("Ficheros/config/accesoJDBC.properties");
			datosConexion = properties.getHash();

			driver = datosConexion.get("driver");
			urlbd = datosConexion.get("urlbd");
			user = datosConexion.get("user");
			password = datosConexion.get("password");
			conn1 = null;

			Class.forName(driver);
			conn1 = DriverManager.getConnection(urlbd, user, password);
			if (conn1 != null) {
				System.out.println("Conectado a la base de datos");
			}

		} catch (ClassNotFoundException e1) {
			System.out.println("ERROR: No Conectado a la base de datos. No se ha encontrado el driver de conexion");
			// e1.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		} catch (SQLException e) {
			System.out.println("ERROR: No se ha podido conectar con la base de datos");
			System.out.println(e.getMessage());
			// e.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		}
	}

	public int cerrarConexion() {
		try {
			conn1.close();
			System.out.println("Cerrada conexion");
			return 0;
		} catch (Exception e) {
			System.out.println("ERROR: No se ha cerrado corretamente");
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {
		HashMap<Integer, Deposito> depositosCreados = new HashMap<Integer, Deposito>();
		try {
			String sql = "SELECT * FROM depositos";
			PreparedStatement sentence = conn1.prepareStatement(sql);
			ResultSet rs = sentence.executeQuery();
			while (rs.next()) {
				Deposito d = new Deposito();
				d.setId(rs.getInt(1));
				d.setNombreMoneda(rs.getString(2));
				d.setValor(rs.getInt(3));
				d.setCantidad(rs.getInt(4));
				depositosCreados.put(d.getId(), d);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return depositosCreados;
	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> dispensadoresCreados = new HashMap<String, Dispensador>();
		try {
			String sql = "SELECT * FROM dispensadores";
			PreparedStatement sentence = conn1.prepareStatement(sql);
			ResultSet rs = sentence.executeQuery();
			while (rs.next()) {
				Dispensador d = new Dispensador();
				d.setId(rs.getInt(1));
				d.setClave(rs.getString(2));
				d.setNombreProducto(rs.getString(3));
				d.setPrecio(rs.getInt(4));
				d.setCantidad(rs.getInt(5));
				dispensadoresCreados.put(d.getClave(), d);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return dispensadoresCreados;
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean todoOK = true;
		clearTable("depositos");
		try {
			for (Deposito d : depositos.values()) {
				String sql = "INSERT INTO depositos (id, nombre, valor, cantidad) VALUES (?, ?, ?, ?)";
				PreparedStatement sentence = conn1.prepareStatement(sql);
				sentence.setInt(1, d.getId());
				sentence.setString(2, d.getNombreMoneda());
				sentence.setInt(3, d.getValor());
				sentence.setInt(4, d.getCantidad());
				int rs = sentence.executeUpdate();
			}
		} catch (Exception e) {
			todoOK = false;
			System.out.println(e);
		}
		return todoOK;
	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		boolean todoOK = true;
		clearTable("dispensadores");
		try {
			for (Dispensador d : dispensadores.values()) {
				String sql = "INSERT INTO dispensadores (id, clave, nombre, precio, cantidad) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement sentence = conn1.prepareStatement(sql);
				sentence.setInt(1, d.getId());
				sentence.setString(2, d.getClave());
				sentence.setString(3, d.getNombreProducto());
				sentence.setInt(4, d.getPrecio());
				sentence.setInt(5, d.getCantidad());
				int rs = sentence.executeUpdate();
			}
		} catch (Exception e) {
			todoOK = false;
			System.out.println(e);
		}
		return todoOK;
	}

	private void clearTable(String table) {
		try {
			String sql = "DELETE FROM " + table;
			PreparedStatement sentence = conn1.prepareStatement(sql);
			int rs = sentence.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

} // Fin de la clase