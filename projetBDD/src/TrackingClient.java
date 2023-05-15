import java.io.*;
import java.net.*;

public class TrackingClient {

	private String host;
	private int port;

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

	public void run() {
		String data = this.makeData();
		boolean success = this.sendData(data);
		if ( success ) {
			this.validateData();
		} else {
			this.invalidateData();
		}
	}

	static public void main( String[] args ) throws IOException {
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
		System.out.println("submission ok.");
		/* Soumission OK */
	}
	public void invalidateData() {
		System.out.println("submission ko.");
		/* Echec de la soumission */
	}

	public String makeData() {
		String data = "";

		/* Construction du contenu XML (utilisation de JDBC)*/
		return data;
	}


}
