package communication;

import java.net.DatagramPacket;
import java.net.InetAddress;

import communication.actions.StandardAction;
import communication.util.SyncVector;


public class ClientSend extends Thread{
	private CommClient commClient;
	private SyncVector packets;
	private boolean dead = false;
	
	public ClientSend(CommClient commClient, SyncVector packets) {
		this.commClient = commClient;
		this.packets = packets;
		this.setPriority(Thread.NORM_PRIORITY);
	}
	
    private byte[] concat(byte[] a, byte[] b){
    	byte[] cat=new byte[a.length+b.length];

    	try {
    		for (int i=0;i<a.length;i++)
    			cat[i]=a[i];
    		for (int i=0;i<b.length;i++)
    			cat[a.length+i]=b[i]; 
    		}catch (Exception e){
    			System.err.println("Exception :" + e);   		
    		}
    	return cat;
    }

    public void run() {
	    while(!dead) {
	    	synchronized(packets) {
	    		while(packets.isEmpty());
	    		
		    	while(!packets.isEmpty()) {
		    		StandardAction action = (StandardAction)packets.get(0);
		    		packets.remove(0);
		    		
		    		byte[] entete = new byte[2];
		    		entete[0] = action.getType();
		    		entete[1] = 0; // CRC Non encore utilis�
		    		byte[] data;
		    		if(action.getObjet()!=null) {
		    			data = concat(entete, action.getObjet());
		    		} else {
		    			data = entete;
		    		}

		    		try {
		    			InetAddress server_inet = InetAddress.getByName(action.getAddress());
		    			// create an object InetAddress (IP)
		    			
		    			DatagramPacket datagram =
		    				new DatagramPacket(data, data.length, server_inet, action.getPort());
		    			// create a datagram packet
		    			commClient.getSocket().send(datagram);
		    			// send the datagram packet to server
		    		} catch (Exception e) {
		    			return;
		    		}
		    		yield();
		    	}
	    	}
	    }
    }
   
   public void stopThread(){
	   dead = true;
   }
}
