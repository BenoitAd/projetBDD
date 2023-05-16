import BD.ErreurSQL;
import BD.SessionOracle;
import oracle.jdbc.OracleTypes;
import oracle.xdb.XMLType;

import java.io.*;
import java.net.*;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TrackingClient {

	private String host;
	private int port;

	private SessionOracle sessionOracle = new SessionOracle("i10a10a", "i10a10a");

	public TrackingClient( String host, int port ) {
		this.host = host;
		this.port = port;
	}


	public boolean sendData( String data ) {
		try {
			Socket s = new Socket(this.host, this.port);

			/* 1. send data */
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			out.write(data);
			out.newLine();
			out.flush();
			s.shutdownOutput();

			/* 2. read response */
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String r = in.readLine();
			System.out.println("first line received: " + r);

			s.close();

			return "OK.".equals(r);

		} catch ( IOException e ) {
			e.printStackTrace();
			return false;
		}
	}

	public void run() throws SQLException {
		String data = this.makeData();
		boolean success = this.sendData(data);
		if ( success ) {
			this.validateData();
		} else {
			this.invalidateData();
		}
	}

	static public void main( String[] args ) throws IOException, SQLException {
		String host = "localhost";
		int port = 9876;
		if ( args.length >= 1 ) {
			host = args[1];
			if ( args.length >= 2 ) {
				port = Integer.valueOf(args[1]);
			}
		}
		TrackingClient tc = new TrackingClient(host, port);
		tc.run();
	}

	/**
	 * MODIFIER ICI
	 * (vous pouvez ajouter des attributs)
	 */
	public void validateData() {

		List<String> listIsbn = new ArrayList<String>();
		try {
			Statement statement = this.sessionOracle.getConnection().createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM tracking WHERE FLAG = 1");
			while (result.next()) {
				listIsbn.add(result.getString("isbn"));
			}
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}

		for (String isbn : listIsbn) {
			try {
				CallableStatement cs = this.sessionOracle.getConnection().prepareCall("{call tracker.set_flag_2(?)");
				cs.setString(1, isbn);
				cs.execute();
			}
			catch (SQLException e) {
				System.out.println(new ErreurSQL(e.getErrorCode(), e.getMessage()));
			}
		}
		System.out.println("submission ok.");
		/* Soumission OK */
	}
	public void invalidateData() throws SQLException {
		System.out.println("submission ko.");
		/* Echec de la soumission */
		List<String> listIsbn = new ArrayList<String>();
		try {
			Statement statement = this.sessionOracle.getConnection().createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM tracking WHERE FLAG = 1");
			while (result.next()) {
				listIsbn.add(result.getString("isbn"));
			}
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}

		for (String isbn : listIsbn) {
			try {
				CallableStatement cs = this.sessionOracle.getConnection().prepareCall("{call tracker.set_flag_0(?)");
				cs.setString(1, isbn);
				cs.execute();
			}
			catch (SQLException e) {
				System.out.println(new ErreurSQL(e.getErrorCode(), e.getMessage()));
			}
		}

	}

	public String makeData() {
		String data = "";
		try {
			CallableStatement cs = sessionOracle.getConnection().prepareCall("{call tracker.tracking_xml(?)}");

			cs.registerOutParameter(1, OracleTypes.CURSOR);
			cs.execute();
			ResultSet rs = (ResultSet) cs.getObject(1);
			while (rs.next()) {
				XMLType xml = (XMLType) rs.getObject(1);
				data += xml.getStringVal();
			}
			System.out.println("Data : " + data);
		}
		catch (SQLException e) {
			System.out.println(new ErreurSQL(e.getErrorCode(), e.getMessage()));
		}
		/* Construction du contenu XML (utilisation de JDBC)*/
		return data;

	}


}
