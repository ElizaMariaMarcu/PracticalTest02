package ro.pub.cs.systems.eim.practicaltest02.graphicuserinterface;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends Activity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText operator1EdiText = null;
    private EditText operator2EditText = null;
    private EditText cityEditText = null;
    private Button addButton = null;
    private Button mulButton = null;
    private TextView operationResultTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }

        }
    }

    private OperationButtonClickListener operationButtonClickListener = new OperationButtonClickListener();
    private class OperationButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String  clientPort = serverPortEditText.getText().toString();
            if (clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            String operator1 = operator1EdiText.getText().toString();
            String operator2 = operator2EditText.getText().toString();
            Button op = (Button)view;
            String operation =  op.getText().toString();
            Log.v(Constants.TAG, operation);
            if (operator1 == null || operator1.isEmpty() ||
                    operator2 == null || operator2.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Parameters from client (city / information type) should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            operationResultTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    Constants.ADDRESS,
                    Integer.parseInt(clientPort),
                    operator1,
                    operator2,
                    operation,
                    operationResultTextView);
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        operator1EdiText = (EditText)findViewById(R.id.operator1_edit_text);
        operator2EditText = (EditText)findViewById(R.id.operator2_edit_text);
        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        addButton = (Button)findViewById(R.id.add_button);
        addButton.setText("add");
        mulButton = (Button)findViewById(R.id.mul_button);
        mulButton.setText("mul");
        connectButton.setOnClickListener(connectButtonClickListener);
        addButton.setOnClickListener(operationButtonClickListener);
        mulButton.setOnClickListener(operationButtonClickListener);
        operationResultTextView = (TextView)findViewById(R.id.operation_result_text_view);
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
