package accesoDatos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

/*
 * Todas los accesos a datos implementan la interfaz de Datos
 */

public class FicherosTexto implements I_Acceso_Datos {

	File fDis; // FicheroDispensadores
	File fDep; // FicheroDepositos

	public FicherosTexto() {
		fDis = new File("Ficheros/datos/dispensadores.txt");
		fDep = new File("Ficheros/datos/depositos.txt");
		System.out.println("ACCESO A DATOS - FICHEROS DE TEXTO");
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {
		HashMap<Integer, Deposito> depositosCreados = new HashMap<Integer, Deposito>();
		try {
			FileReader fr = new FileReader(fDep.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);
			String line;
			int id = 1;
			while ((line = br.readLine()) != null) {
				String[] aux = line.split(";");
				Deposito d = new Deposito();
				d.setId(id);
				d.setNombreMoneda(aux[0]);
				d.setValor(Integer.valueOf(aux[1]));
				d.setCantidad(Integer.valueOf(aux[2]));
				depositosCreados.put(id, d);
				id++;
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return depositosCreados;
	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> dispensadoresCreados = new HashMap<String, Dispensador>();
		try {
			FileReader fr = new FileReader(fDis.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);
			String line;
			int id = 1;
			while ((line = br.readLine()) != null) {
				String[] aux = line.split(";");
				Dispensador d = new Dispensador();
				d.setId(id);
				d.setClave(aux[0]);
				d.setNombreProducto(aux[1]);
				d.setPrecio(Integer.valueOf(aux[2]));
				d.setCantidad(Integer.valueOf(aux[3]));
				dispensadoresCreados.put(aux[0], d);
				id++;
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return dispensadoresCreados;
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean todoOK = true;
		try {
			String content = "";
			for (Deposito d : depositos.values()) {
				content += d.getNombreMoneda() + ";" + d.getValor() + ";" + d.getCantidad() + "\n";
			}
			FileWriter fw = new FileWriter(fDep.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			todoOK = false;
			System.out.println(e);
		}
		return todoOK;
	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		boolean todoOK = true;
		try {
			String content = "";
			for (Dispensador d : dispensadores.values()) {
				content += d.getClave() + ";" + d.getNombreProducto() + ";" + d.getPrecio() + ";" + d.getCantidad()
						+ "\n";
			}
			FileWriter fw = new FileWriter(fDis.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			todoOK = false;
			System.out.println(e);
		}
		return todoOK;
	}

} // Fin de la clase