package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String operator1;
    private String operator2;
    private String operation;
    private TextView operationResultTextView;

    private Socket socket;

    public ClientThread(
            String address,
            int port,
            String operator1,
            String operator2,
            String operation,
            TextView operationResultTextView) {
        this.address = address;
        this.port = port;
        this.operator1 = operator1;
        this.operator2 = operator2;
        this.operation = operation;
        this.operationResultTextView = operationResultTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
            	String request = operation+","+operator1+","+operator2;
                printWriter.println(request);
                printWriter.flush();
 
                String line="";
                while ((line = bufferedReader.readLine()) != null) {
                    final String finalizedWeatherInformation = line;
                    operationResultTextView.post(new Runnable() {
                        @Override
                        public void run() {
                        	operationResultTextView.append(finalizedWeatherInformation + "\n");
                        }
                    });
                }
            } else {
                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
