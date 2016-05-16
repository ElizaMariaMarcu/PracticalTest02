package ro.pub.cs.systems.eim.practicaltest02.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import android.util.Log;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
                    String line = bufferedReader.readLine();
                    if (line != null) {
                    	String[] tokens = line.split(",");
                    	int result = 0;
                    	Log.v(Constants.TAG, "tokens: " + tokens[0] + " " +tokens[1] + " " + tokens[2]);
                    	if (tokens[0].equals("add")) {
                    		result = Integer.parseInt(tokens[1]) + Integer.parseInt(tokens[2]);
                    	} else if (tokens[0].equals("mul")){
                    		result = Integer.parseInt(tokens[1]) * Integer.parseInt(tokens[2]);
                    	}
                    	
                    	 String resultString = "" ;
                    	 if (result <= Integer.MAX_VALUE) {
                    		 resultString  = result + ""; 
                    	 } else {
                    		 resultString = "overflow";
                    	 }
                    	 Log.v(Constants.TAG, "result:" + resultString);
                    	 
                    	 if (tokens[0].equals("mul"))
                    		 Thread.sleep(2000);
                         printWriter.println(resultString);
                         printWriter.flush();
                    }
                     
                 
                } else {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
                }
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
        }
    }

}
