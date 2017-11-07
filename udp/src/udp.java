

import java.io.File;
import java.io.FileInputStream;
import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class udp {

    public static void main(String[] args) {
        data(args);
    }
    
    public static void data(String[] args)
    {  
        try {
        	int buffLen = (Integer.parseInt(args[3]) != 0) ? Integer.parseInt(args[3]) : 1024;
        	int transDelay = (Integer.parseInt(args[4]) != 0) ? Integer.parseInt(args[4]) : 13;
        	int packetSize = (Integer.parseInt(args[5]) != 0) ? Integer.parseInt(args[5]) : 256;
        	int port = Integer.parseInt(args[1]);
        	int bytesRead = 0;
        	int bytesSent = 0;
        	int counter = 1;
        	String ipAdd = args[0];
        	double packIter = (double) buffLen / packetSize;
        	byte[] buffer= new byte[buffLen];
            FileInputStream fs = new FileInputStream(new File(args[2]));

            DatagramSocket socket = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(ipAdd);
//            System.out.println("Ip: " + ip);

            bytesRead = fs.read(buffer, 0, buffer.length);
            System.out.println("Sending PACKET: "+ (counter++));
            DatagramPacket packet = new DatagramPacket(buffer, 0, packetSize, ip, port);
            socket.send(packet);
            bytesSent = packetSize;
            sleep(transDelay);
            
            while (packIter > 1.0) {
            	if (packIter < 2.0) {
            		packet.setData(buffer, bytesSent, bytesRead % packetSize);
            		bytesSent += bytesRead % packetSize;
            	}
            	else {
            		packet.setData(buffer, bytesSent, packetSize);
            		bytesSent += packetSize;
            	}
            	System.out.println("Sending PACKET: "+ (counter++));
            	socket.send(packet);
            	packIter -= 1.0;
            	sleep(transDelay);
            }
            
            
            while (bytesRead != -1)
            {          
                bytesRead = fs.read(buffer, 0, buffer.length);
                System.out.println("Sending PACKET: "+ (counter++));
                packet.setData(buffer, 0, packetSize);
                socket.send(packet); 
                bytesSent = packetSize;
                packIter = (double) buffLen / packetSize;
                sleep(transDelay);
                
                while (packIter > 1.0) {
                	if (packIter < 2.0) {
                		packet.setData(buffer, bytesSent, bytesRead % packetSize);
                		bytesSent += bytesRead % packetSize;
                	}
                	else {
                		packet.setData(buffer, bytesSent, packetSize);
                		bytesSent += packetSize;
                	}
                	System.out.println("Sending PACKET: "+ (counter++));
                	socket.send(packet);
                	packIter -= 1.0;
                	sleep(transDelay);
                }
            }
        socket.close(); 
        fs.close();
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
}
}