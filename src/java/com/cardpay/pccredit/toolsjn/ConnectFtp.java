/*package com.cardpay.pccredit.toolsjn;









import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import sun.net.ftp.FtpProtocolException;




@Service
public class ConnectFtp {
	private static String url="10.96.1.2";
	private static String username="yq";
	private static String password="yq";
	private static int port=21;
	public static String ftpFile="/"; 
	public static String localFile="/xwd31/"; 			// 解析压缩包所放运行服务器目录
	private static FTPClient ftpClient = null;
	
	public static void connectFTP() throws FtpProtocolException, IOException {  
      
            //创建地址  
            SocketAddress addr = new InetSocketAddress(url, port);  
            //连接  
            ftpClient = FtpClient. 
            ftpClient.connect(addr);  
            //登陆  
            ftpClient.login(username, password.toCharArray());  
            ftpClient.setBinaryType();
            System.out.println("连接成功");
      
    }  



	public static void download(String ftpFile, String localFile) throws FtpProtocolException, IOException {  
        InputStream is = null;  
        FileOutputStream fos = null;  
       
            // 获取ftp上的文件  
            is = ftpClient.getFileStream(ftpFile);  
        
            File f = new File(localFile);
            byte[] bytes = new byte[1024];  
            int i;  
            fos = new FileOutputStream(f+"/"+ftpFile);  
            while((i = is.read(bytes)) != -1){  
                fos.write(bytes, 0, i);  
            }  
            System.out.println("download success!!");  
        
  
      
            try {  
                if(fos!=null) {  
                    fos.close();  
                }  
                if(is!=null){  
                    is.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
         
    }
	
	public static void disconnectFtp() {  
        if (ftpClient == null) {  
            return;  
        }  
  
        if (!ftpClient.isConnected()) {  
            return;  
        }  
  
        try {  
        	ftpClient.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	

}*/