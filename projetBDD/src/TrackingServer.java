import java.io.*;
import java.net.*;

public class TrackingServer extends ServerSocket {

	private boolean validate;

	public TrackingServer( int port, boolean validate ) throws IOException  {
		super(port);
		this.validate = validate;
		while ( true ) {
			System.out.println("waiting client...");
			try {
				Socket s = this.accept();
				System.out.println("client connected.");

				/* 1. read data */
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String data = "";
				String r;
				do {
					System.out.println("receiving..");
					r = in.readLine();
					if ( r != null ) {
						data += r;
					}
				} while ( r != null );
				System.out.println("received : '''" + data + "'''");

				/* 2. validate data */
				boolean ok = this.validateData(data);
				System.out.println("validated: "+ ok);

				/* 3. send response */
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				out.write(ok ? "OK." : "KO.");
				out.newLine();
				out.close();

				s.close();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}


	private boolean validateData( String data ) {
		return this.validate;
	}


	static public void main( String[] args ) throws IOException {
		if ( args.length == 0 || !("1".equals(args[0]) || "0".equals(args[0])) ) {
			System.err.println("usage: java TrackingServer <0|1> [<port>]");
		}
		boolean validate = "1".equals(args[0]);
		int port = 9876;
		if ( args.length >= 2 ) {
			port = Integer.valueOf(args[1]);
		}
		new TrackingServer(port, validate);
	}

}
