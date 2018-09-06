package udpsharescreen;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.imageio.ImageIO;


public class UDPClient extends Thread{
	
	private Robot robot;
	private Rectangle rect;
	private BufferedImage buffimage;
	private ByteArrayOutputStream baoup;
	private   long speed1;
	private   long speed2;
	private   int transportbytesnum;
	private String serverip;
	private DatagramPacket pak;
	private int transport;
	 public static void main(String[] args) throws Exception { // 主方法
		   UDPClient client = new UDPClient(); // 创建本例对象
		   client.speed1=20;
		   client.speed2=1;
		   client.transportbytesnum=4096*4+6;
		   client.serverip="localhost";//这里是服务端的iP。
		   client.transport=12588;
		   client.robot=new Robot();// 创建Robot对象
		   client.rect = new Rectangle(0, 0, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());// 创建Rectangle对象
	       client.start();
	   }
	 
	 @Override
	 public void run(){
		while (true) {
			try {
				this.sendScreenByteArray(this.captureScreenToByteArray());
				Thread.sleep(speed1);
			} catch (Exception e) {

			}
		}
			
	 }

     public byte[] captureScreenToByteArray(){
        try{
            buffimage = robot.createScreenCapture(rect);// 获得缓冲图像对象
		    baoup=new ByteArrayOutputStream();
		    ImageIO.write(buffimage,"png",baoup);
		    return baoup.toByteArray();
	     }catch (Exception e) {
        	 
	     }
      	 return null;
      
      	
    }
   //5B   +   1B  + 20KB
   //总字节数+当前帧序号+20KB
    public void sendScreenByteArray(byte []imgsrcbyte) throws Exception{
    	if(imgsrcbyte==null){
    		throw new RuntimeException("获取屏幕截图数组出问题啦。。");
    	}
    	  DatagramSocket udp=new DatagramSocket();
  	      InetAddress ip=InetAddress.getByName(serverip);
  	     	 int filelength=imgsrcbyte.length;
  			 int jishu=transportbytesnum-6;
  			 int looptime=filelength/jishu;
  		     int lastis=filelength%jishu;
  		     byte []B1=UTils.IntTo7BitBytes(filelength);
  		     int i=0;
  		     for(i=0;i<looptime;i++){
  				 byte []B2=new byte[1];
  				 B2[0]=(byte)(i+1);
  				 byte [] sendbyte=UTils.concatByte(B1, B2, imgsrcbyte, i*jishu, jishu);
  				 pak=new DatagramPacket(sendbyte,0,sendbyte.length,ip,transport);
  				 udp.send(pak);
  			     Thread.sleep(speed2);
  			   
  			 }	
  			 if(lastis>0){
  				 byte []B2=new byte[1];
  				 B2[0]=(byte)(i+1);
  				 byte [] sendbyte=UTils.concatByte(B1, B2, imgsrcbyte, i*jishu, lastis);
  				 pak=new DatagramPacket(sendbyte,0,sendbyte.length,ip,transport);
  				 udp.send(pak);
  			   
  			 }
  	  udp.close();
  	 }
   
}
