package blackjack.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import blackjack.protocol.DecryptJson;

/**
 * ClientThread
 */
public class ClientThread extends Thread {

    private final Socket socket;
    private final BufferedWriter out;
    private final BufferedReader in;
    private String lastMessage;
    // private String toSend;
    // private Thread senderThread;
    private Thread listenerThread;
    private int responseCount;
    private boolean isConnected;

    public ClientThread(Socket socket) throws IOException {
        super();
        this.socket = socket;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.responseCount = 0;
    }
    
    // will save the state of the game
    public void setStatePointer() {

    }


	@Override
	public void start() {

        // listen
        listenerThread = new Thread(() -> {
            System.out.println("Starting listener Thread");
            while (this.socket.isConnected()) {
                try {
                    String serverMessage = in.readLine();
                    if (DecryptJson.isConnectedToGame(serverMessage)) {

                    }
                    System.out.println(serverMessage);
                    this.lastMessage = serverMessage;
                } catch (IOException e) {
                    
                }
            }

        });

        // send
    //     senderThread = new Thread(() -> {
    //         System.out.println("Starting sender Thread");
    //         while (this.socket.isConnected()) {
    //             try {
    //                 this.wait();
    //                 System.out.println(toSend);
    //                 out.write(toSend);
    //             } catch (IOException e) {
    //                 e.printStackTrace();
    //                 System.out.println("IO Exception occurred");
    //             } catch (InterruptedException e) {
				// 	e.printStackTrace();
    //                 System.out.println("Sender Thread interuppted");
				// }
    //         }
    //     });

        listenerThread.start();
        // senderThread.start();


	}

    private void closeEverything(Socket socket, BufferedReader in, BufferedWriter out) {
        try {
            socket.close();
            in.close();
            out.close();

            if (listenerThread.isAlive()) {
                listenerThread.interrupt();
            }

            // if (senderThread.isAlive()) {
            //     senderThread.interrupt();
            // }

            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Couldn't close the connection.");
        }
    }

    public String lastMessage() {
        return lastMessage;
    }

    public void notifyServer(String message) {
        // this.notifyAll();
        // this.toSend = message;
        try {
			out.write(message);
            out.newLine();
            out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public int getResponseCount() {
        return responseCount;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    
}
