package com.cardpay.pccredit.toolsjn;  
  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.io.FileUtils;  
import org.apache.commons.io.IOUtils;  
import org.apache.commons.net.ftp.FTPClient;  
import org.apache.commons.net.ftp.FTPFile;  
import org.apache.commons.net.ftp.FTPReply;  
import org.apache.log4j.Logger;  
  
public class FTPUtil {  
  
    private static final Logger logger = Logger.getLogger(FTPUtil.class);  
    private static String encoding = System.getProperty("file.encoding");  
      
    static FTPClient client;  
  
    /** ftp服务器地址 */  
    private static String host="10.96.1.2";  
    /** ftp 端口号 默认21 */  
    private static int port = 21;  
    /** ftp服务器用户名 */  
    private static String username="yq";  
    /** ftp服务器密码 */  
    private static String password="yq";  
    /** ftp远程目录 */  
    private static String remoteDir="/";  
    /** 本地存储目录 */  
    private static String localDir="/xwd31/";  
    /** 文件路径通配符 默认列出所有*/  
    private static String regEx = "*";  
    /** 指定要下载的文件名 */  
    private static String[] downloadFileName={"kkh_grxx.zip","kkh_grjtcy.zip","kkh_grjtcc.zip","kkh_grscjy.zip","kkh_grxxll.zip","kkh_grgzll.zip","kkh_grrbxx.zip","kdk_yehz.zip","cxd_dkcpmc.zip","kkh_hmdgl.zip","kdk_lsz.zip","kdk_tkmx.zip","cxd_rygl.zip","kdk_jh.zip"};
	  
  
    /** 
     * 设置连接属性 
     *  
     * @param host 
     * @param username 
     * @param password 
     * @return 
     */  
    public FTPUtil setConfig(String host, String username, String password) {  
        this.host = host;  
        this.username = username;  
        this.password = password;  
        return this;  
    }  
  
    /** 
     * 设置连接属性 
     *  
     * @param host 
     * @param port 
     * @param username 
     * @param password 
     */  
    public FTPUtil setConfig(String host, int port, String username,String password) {  
        this.host = host;  
        this.port = port;  
        this.username = username;  
        this.password = password;  
        return this;  
    }  
  
    /** 
     * 连接FTP服务器 
     */  
    private static  void  connectServer() {  
        client = new FTPClient();  
        //设置超时时间  
        client.setConnectTimeout(30000);  
        try {  
            // 1、连接服务器  
            if(!client.isConnected()){  
                // 如果采用默认端口，可以使用client.connect(host)的方式直接连接FTP服务器  
                client.connect(host, port);  
                // 登录  
                client.login(username, password);  
                // 获取ftp登录应答码  
                int reply = client.getReplyCode();  
                // 验证是否登陆成功  
                if (!FTPReply.isPositiveCompletion(reply)) {  
                    logger.info("未连接到FTP，用户名或密码错误。");  
                    client.disconnect();  
                    throw new RuntimeException("未连接到FTP，用户名或密码错误。");  
                } else {  
                    logger.info("FTP连接成功。IP:"+host +"PORT:" +port);  
                }  
                // 2、设置连接属性  
                client.setControlEncoding(encoding);  
                // 设置以二进制方式传输  
                client.setFileType(FTPClient.BINARY_FILE_TYPE);    
                client.enterLocalPassiveMode();  
            }  
        } catch (SocketException e) {  
            try {  
                client.disconnect();  
            } catch (IOException e1) {  
            }  
            logger.error("连接FTP服务器失败" + e.getMessage());  
            throw new RuntimeException("连接FTP服务器失败" + e.getMessage());  
        } catch (IOException e) {  
        }  
    }  
      
  
    /** 
     * 下载文件 
     */  
    public static List<File> download(){  
          
        List<File> files = null;  
          
       connectServer();  
       DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateString = format.format(new Date());
		 File url = new File(localDir+dateString);
		 //本地创建当日数据文件夹
       if(!url.exists()){ 
       	url.mkdirs();  
       }
        InputStream is = null;  
        File downloadFile = null;  
        try {  
            // 1、设置远程FTP目录  
            client.changeWorkingDirectory(remoteDir);  
            logger.info("切换至工作目录【" + remoteDir + "】");  
            // 2、读取远程文件  
            FTPFile[] ftpFiles = client.listFiles(regEx);  
            if(ftpFiles.length==0) {  
                logger.warn("文件数为0，没有可下载的文件！");  
                return null;  
            }  
            logger.info("准备下载" + ftpFiles.length + "个文件");  
            // 3、保存文件到本地  
            for (FTPFile file : ftpFiles) {  
            	for(int i=0;i<downloadFileName.length;i++)
            	{
            		  if(!file.getName().equals(downloadFileName[i])){
                   	   continue;
                      }else{
                   	   if(files == null) files = new ArrayList<File>();  
                          is = client.retrieveFileStream(file.getName()); 
                         
                          if(is==null) throw new RuntimeException("下载失败，检查文件是否存在");  
                          logger.info(file.getName() + "下载成功");  
                          downloadFile = new File(localDir+dateString +"/"+ file.getName());  
                          FileOutputStream fos = FileUtils.openOutputStream(downloadFile);  
                          IOUtils.copy(is, fos);  
                          client.completePendingCommand();  
                          IOUtils.closeQuietly(is);  
                          IOUtils.closeQuietly(fos);  
                            
                          /* 
                          //另外一种方式，供参考 
                          OutputStream is = new FileOutputStream(localFile); 
                          ftpClient.retrieveFile(ff.getName(), is); 
                          is.close(); 
                          */  
                            
                          files.add(downloadFile);  
                      }
            	}
            }  
            logger.info("文件下载成功,下载文件路径：" + localDir);  
            return files;  
        } catch (IOException e) {  
            logger.error("下载文件失败" + e.getMessage());  
            throw new RuntimeException("下载文件失败" + e.getMessage());  
        }  
    }  
      
    /** 
     * 下载文件 
     * @param localDir 
     * @param remoteDir 
     */  
    public List<File> download(String remoteDir,String localDir){  
        this.remoteDir = remoteDir;  
        this.localDir = localDir;  
        return this.download();  
    }  
      
   
  
  
     /** 
     * 关闭连接 
     */  
    public void closeConnect() {  
        try {  
            client.disconnect();  
            logger.info(" 关闭FTP连接!!! ");  
        } catch (IOException e) {  
            logger.warn(" 关闭FTP连接失败!!! ",e);  
        }  
    }  
    public String getRemoteDir() {  
        return remoteDir;  
    }  
  
    public void setRemoteDir(String remoteDir) {  
        this.remoteDir = remoteDir;  
    }  
  
    public String getLocalPath() {  
        return localDir;  
    }  
  
    public void setLocalPath(String localPath) {  
        this.localDir = localPath;  
    }  
  
      
  /*  public static void main(String[] args)  {  
    	download();
    }*/
}  
