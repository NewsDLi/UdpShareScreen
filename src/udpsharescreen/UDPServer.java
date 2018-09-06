package udpsharescreen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;
import javax.imageio.stream.IIOByteBuffer;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

//如果使用Myeclipse请放开这两个。eclipse请关闭。因为Eclipse插件包里有。所以不需要手动倒入。
//import com.sun.image.codec.jpeg.JPEGCodec;  
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class UDPServer extends JFrame implements Runnable{
    private Image receiveImg = null; // 声明图像对象
    private ReceiveImagePanel receiveImagePanel = null; // 声明图像面板对象
    private DatagramSocket udp; // 声明Socket对象socket
    private DatagramPacket pak;  
    public UDPServer() {
        super();
         setTitle("服务器端程序");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 379, 260);
        
        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        receiveImagePanel = new ReceiveImagePanel();
       
        final JPanel panel_1 = new JPanel();
        panel_1.setLayout(new BorderLayout());
        getContentPane().add(panel_1, BorderLayout.CENTER);
       
        final JPanel imgPanel = new JPanel();
        final GridLayout gridLayout = new GridLayout(1, 0);
        gridLayout.setVgap(10);
        imgPanel.setLayout(gridLayout);
        panel_1.add(imgPanel, BorderLayout.CENTER);
        imgPanel.add(receiveImagePanel);
        receiveImagePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
    }
    
    
    public static void main(String[] args) throws Exception { // 主方法
    	UDPServer frame = new UDPServer(); // 创建本类对象
        frame.setVisible(true);
        Thread t=new Thread(frame);
        t.start();
        
    }
    @Override
	public void run() {
		while (true) {
			try {
				receive();
			} catch (Exception e) {
				System.out.println("有异常");
				break;
			}
		}
	}
    private void receive() throws Exception {
    	  udp=new DatagramSocket(12588);
    	  int jishu=4096*4;//一帧中有效的图片字节数。
		  int receivenum=jishu+6;
		  byte []everyzhen=new byte[0];
		  //5B   +   1B  + 20KB
		   //总字节数+当前帧序号+20KB
		  PICBean picbean=new PICBean();
		  int  temp=0;
		  while(true){
			   everyzhen=new byte[receivenum];
			   pak=new DatagramPacket(everyzhen, 0,everyzhen.length);
			   udp.receive(pak);
			   everyzhen=pak.getData();
			   picbean.setCurr_file_length(UTils._7BitBytesToInt(everyzhen));
			   picbean.setCurr_zhen(everyzhen[5]);
			   picbean.setCurr_totalzhen(jishu);
			   if(picbean.getLast_file_length()==-1){//第一次进来  
				   //1刚好是头帧。
				   if(picbean.Isfirstzhen()){
					   picbean.updateCurrToLast();
					   picbean.setImgByteArray(new byte[picbean.getCurr_file_length()]);
					    if(picbean.isTheEnd()){ //既是头帧又是尾帧   传输完毕
					    	System.out.println("这里");
					    	 int lastlength=(picbean.getCurr_file_length()%jishu==0 ? jishu :picbean.getCurr_file_length()%jishu );
					    	 UTils.copyArray(everyzhen, 6,lastlength,picbean.getImgByteArray(), temp*jishu);
					    	 break;
					    }else{//一般不是   
					    	UTils.copyArray(everyzhen, 6,everyzhen.length-6,picbean.getImgByteArray(), temp*jishu);
					    }
					    temp++;
				   }else{//2不是头帧。继续等待直到头帧为止。
					   picbean.reInit();
					   continue;
				   }
			   }else{//2 已经确保头帧正确的情况下继续接收。
				     if(picbean.getCurr_file_length()!=picbean.getLast_file_length()){//
				    	 picbean.reInit();
				    	 System.out.println("传输异常，没有传输完毕就跨节1");
				    	 continue;
				     }
				     if(picbean.getCurr_totalzhen()!=picbean.getLast_totalzhen()){//
				    	 picbean.reInit();
				    	 System.out.println("传输异常，没有传输完毕就跨节2");
				    	 continue;
				     }
				     //当前帧是最后一帧吗  
				     if(picbean.isTheEnd()){//是
				    	 int lastlength=(picbean.getCurr_file_length()%jishu==0 ? jishu :picbean.getCurr_file_length()%jishu );
				    	 UTils.copyArray(everyzhen, 6,lastlength,picbean.getImgByteArray(), temp*jishu);
				    	 break;
				     }else{//否
				    	 UTils.copyArray(everyzhen, 6,everyzhen.length-6,picbean.getImgByteArray(),temp*jishu);
				    	 picbean.updateCurrToLast();
				      }
				     temp++;
				   
			   }
			
		  }
		  udp.close();
		  receiveImg = new ImageIcon(picbean.getImgByteArray()).getImage();// 创建图像对象
          receiveImagePanel.repaint();// 重新绘制图像
          picbean=null;
     
   }
    
    // 创建面板类
    class ReceiveImagePanel extends JPanel {
        public void paint(Graphics g) {
          if (receiveImg != null) {
                g.clearRect(0, 0, this.getWidth(), this.getHeight());// 清除绘图上下文的内容
                g.drawImage(receiveImg, 0, 0, this.getWidth(),
                        this.getHeight(), this);// 绘制指定大小的图片
            }
        }
    }

	
}
