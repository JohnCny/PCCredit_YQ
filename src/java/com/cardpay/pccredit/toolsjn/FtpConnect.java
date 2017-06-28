package com.cardpay.pccredit.toolsjn;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;

public class FtpConnect {
	 private static final Log logger = LogFactory.getLog(FtpConnect.class);   
    private String userName="yq";         //FTP 登录用户名   
    private String password="yq";          //FTP 登录密码   
    private String ip="10.96.1.2" ;                    //FTP 服务器地址IP地址   
    private int port=21;                        //FTP 端口   

    private FTPClient ftpClient = null; //FTP 客户端代理   
    public static String ftpFile="/"; 
	public static String localFile="/xwd31/"; 	
   
  
    /**  
     * 连接到服务器  
     *  
     * @return true 连接服务器成功，false 连接服务器失败  
     * @throws IOException 
     * @throws SocketException 
     */   
    public void connectServer() throws SocketException, IOException {   
      
       
                    ftpClient = new FTPClient();   
                  
                    ftpClient.setControlEncoding("GBK");
                    FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
                    conf.setServerLanguageCode("zh");
                    ftpClient.connect(ip,port);   
                    ftpClient.login(userName, password); 
                    ftpClient.changeWorkingDirectory(ftpFile);  
                 System.out.println("链接成功");
        
    }  
    
    /**  
     * 下载文件  
     *  
     * @param remoteFileName             --服务器上的文件名  
     * @param localFileName--本地文件名  
     * @return true 下载成功，false 下载失败  
     * @throws IOException 
     */   
    public void loadFile(String remoteFileName, String localFileName) {   
    	System.out.println(remoteFileName);
    	 try {
			FTPFile[] ftpFiles = ftpClient.listFiles();
			 for (FTPFile file : ftpFiles) {  
	    		 System.out.println();
	    	 }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
        // 下载文件   
        BufferedOutputStream buffOut = null;   
       
            try {
				buffOut = new BufferedOutputStream(new FileOutputStream(localFileName+"/"+remoteFileName));   
        ftpClient.retrieveFile(new String(remoteFileName.getBytes("GBK"), "IS0-8859-1"), buffOut);   
        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
            if (buffOut != null)
				try {
					buffOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           
       
    }   
    /**  
     * 设置传输文件的类型[文本文件或者二进制文件]  
     *  
     * @param fileType--BINARY_FILE_TYPE、ASCII_FILE_TYPE  
     *  
     */   
    public void setFileType(int fileType) {   
        try {   
            ftpClient.setFileType(fileType);   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
    }   
    
    public static void main(String[] args) throws SocketException, IOException {  
    	FtpConnect ftpClient = new FtpConnect();  
        ftpClient.connectServer();
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateString = format.format(new Date());
		 File url = new File(ftpClient.localFile+dateString);
		 //本地创建当日数据文件夹
        if(!url.exists()){ 
        	url.mkdirs();  
        }
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);// 设置传输二进制文件   
		 for (int i = 0; i < OdsTools_jn.fileName.length; i++) {
			 ftpClient.loadFile(OdsTools_jn.fileName[i], ftpClient.localFile+dateString);
	        }
      
         
         
        }  
    }  

