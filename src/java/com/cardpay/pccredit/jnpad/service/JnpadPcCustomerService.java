package com.cardpay.pccredit.jnpad.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.cardpay.pccredit.common.SFTPUtil;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.constant.ServerSideConstant;
import com.cardpay.pccredit.intopieces.model.ImageMore;
import com.cardpay.pccredit.intopieces.model.LocalImage;
import com.cardpay.pccredit.intopieces.web.LocalImageForm;
import com.cardpay.pccredit.jnpad.dao.JnpadDailyAccountManagerDao;
import com.cardpay.pccredit.jnpad.dao.JnpadPcCustomerDao;
import com.cardpay.pccredit.jnpad.model.JNPAD_SFTPUtil;
import com.cardpay.pccredit.jnpad.model.JNPAD_UploadFileTool;
import com.cardpay.pccredit.jnpad.model.JnpadPcCustomer;
import com.cardpay.pccredit.jnpad.web.JnpadPcCustomerController;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.wicresoft.jrad.base.database.id.IDGenerator;

@Service
public class JnpadPcCustomerService {
/*	private static String host = "139.196.31.230";//测试
    private static String username="root";  
    private static String password="aDmin945";*/  
	private static String host = "10.96.1.12";//测试
    private static String username="root";  
    private static String password="qkjr123";  
    private static int port = 22;  
    public static ChannelSftp sftp = null;  
    private static String directory = "/usr/pccreditFile/";
	public static BufferedInputStream bis=null;
	public static BufferedOutputStream bos=null;
    //链接服务器		
    public static void connect() {  
        try {  
            if(sftp != null){  
                System.out.println("sftp is not null");  
            }  
            JSch jsch = new JSch();  
            jsch.getSession(username, host, port);  
            Session sshSession = jsch.getSession(username, host, port);  
            sshSession.setPassword(password);  
            Properties sshConfig = new Properties();  
            sshConfig.put("StrictHostKeyChecking", "no");  
            sshSession.setConfig(sshConfig);  
            sshSession.connect();  
            System.out.println("33333");
            Channel channel = sshSession.openChannel("sftp");  
            channel.connect();  
            sftp = (ChannelSftp) channel;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
    }
    //关闭服务器		
    public static void disconnect() {  
        if(sftp != null){  
            if(sftp.isConnected()){  
                sftp.disconnect();  
            }else if(sftp.isClosed()){  
                System.out.println("sftp is closed already");  
            }  
        }  
  
    }
    //上传到服务器上面图片
    public  static Map<String, String> uploadJn(MultipartFile oldFile,String customerId,String fileName_1) {
    	String newFileName = null;
		String fileName = null;
    	Map<String, String> map = new HashMap<String, String>();
        try {  
        	if (oldFile != null && !oldFile.isEmpty()) {
	        	//连接sftp
	        	connect();
	        	String path = Constant.FILE_PATH + customerId;
	        	try {
	    			sftp.cd(path);
				} catch (Exception e) {
					sftp.cd(Constant.FILE_PATH);
					sftp.mkdir(customerId);  
					sftp.cd(path);
				}
	    			
//	    	    fileName = oldFile.getOriginalFilename();
	    	    fileName = fileName_1;
				File tempFile = new File(path + File.separator + fileName_1);
				if (tempFile.exists()) {
					newFileName = IDGenerator.generateID() + "."+ fileName_1.split("\\.")[1];
				} else {
					newFileName = fileName_1;
				}
	    	   CommonsMultipartFile cf= (CommonsMultipartFile)oldFile;
	    	   DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
	           File file = fi.getStoreLocation();
	    	   sftp.put(new FileInputStream(file), newFileName);
	    	   disconnect();  
	           
	    	   map.put("fileName", fileName);
	   		   map.put("url", path +File.separator+ newFileName);
	   		   
        	}
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (SftpException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
          return map;
    }
	@Autowired
	private JnpadPcCustomerDao CustomerDao;
	public List<JnpadPcCustomer> selectPCustomer(@Param("userId") String userId){
		return CustomerDao.selectPCustomer(userId);
		
	}
	public int insertPCustomer(JnpadPcCustomer JnpadPcCustomer){
		return CustomerDao.insertPCustomer(JnpadPcCustomer);
	}
	public JnpadPcCustomer selectCardId(@Param("cardid") String cardid){
		return CustomerDao.selectCardId(cardid);
	}
	
	public void importpcImage(MultipartFile file, String cardid,
			String userId ,String fileName_1) {
		Map<String, String> map=null;
		if(ServerSideConstant.IS_SERVER_SIDE_TRUE.equals("0")){
			//本地测试
			map = JNPAD_UploadFileTool.scpcImage(file,cardid,fileName_1);
		}else{
			//指定服务器上传
			map =uploadJn(file, userId,fileName_1);
		}
		String fileName = map.get("fileName");
		String url = map.get("url");
		LocalImageForm localImage = new LocalImageForm();
		localImage.setId(IDGenerator.generateID());
		localImage.setUserId(userId);
		Date date=new Date();
		SimpleDateFormat sdfe=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime=sdfe.format(date);
		localImage.setCreatedTime(datetime);
		localImage.setCardid(cardid);
		if (StringUtils.trimToNull(url) != null) {
			localImage.setUri(url);
		}
		if (StringUtils.trimToNull(fileName) != null) {
			localImage.setAttachment(fileName);
		}
		CustomerDao.insertPCImage(localImage);
	}
	/**
	 * 将图片转成base64编码
	 * @param cardid
	 * @return
	 * @throws IOException
	 * @throws SftpException
	 */
	public List<LocalImageForm> selectUri(@Param("cardid") String cardid) throws IOException, SftpException{
		List<ImageMore> list=null;
		List<ImageMore>result=new ArrayList<ImageMore>();
		List<LocalImageForm>Alist=new ArrayList<LocalImageForm>();
		List<LocalImageForm> from= CustomerDao.selectUri(cardid);
		for(int i=0;i<from.size();i++){
			ImageMore more =new ImageMore();
			more.setUri(from.get(i).getUri());
			result.add(i, more);
		}
		if(ServerSideConstant.IS_SERVER_SIDE_TRUE.equals("0")){
			list=SFTPUtil.TestImageBinary1(result);
		}else{
			list=SFTPUtil.TestImageBinary(result);
		}
		for(int i=0;i<list.size();i++){
			LocalImageForm more =new LocalImageForm();
			more.setUri(list.get(i).getUri());
			Alist.add(i, more);
		}
		return Alist;
	}
	public static void ckpcImage(HttpServletResponse response,String uri,String attment) throws Exception{
		String filePath=uri;
		String fileName=attment;
			byte[] buff = new byte[2048];
			int bytesRead;
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(fileName, "UTF-8"));
				connect();
			sftp.cd(filePath.substring(0, 50));
			bis = new BufferedInputStream(sftp.get(fileName));
			 bos = new BufferedOutputStream(response.getOutputStream());
			 
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
			//disconnect();

		
	}
	/* 本地查看下载资料 */
	public static void bdpcImage(HttpServletResponse response,
			String filePath, String fileName) throws Exception {
		byte[] buff = new byte[2048];
		int bytesRead;
		System.out.println("1111111");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ java.net.URLEncoder.encode(fileName, "UTF-8"));
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				filePath));
		BufferedOutputStream bos = new BufferedOutputStream(
				response.getOutputStream());
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bos.flush();
		if (bis != null) {
			bis.close();
		}
		if (bos != null) {
			bos.close();
		}
	}
	public static void downloadImage(HttpServletResponse response,
			String filePath, String fileName) {

		try {
			byte[] buff = new byte[2048];
			int bytesRead;
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(fileName, "UTF-8"));
			
				JNPAD_SFTPUtil.connect();
			
			JNPAD_SFTPUtil.sftp.cd(filePath.substring(0, 36));
			BufferedInputStream bis = new BufferedInputStream(JNPAD_SFTPUtil.sftp.get(filePath.substring(37, filePath.length())));
			BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
			JNPAD_SFTPUtil.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 本地上传影像资料专用 */
	public static Map<String, String> scpcImage(MultipartFile file, String cardid,
			String userId ,String fileName_1) {
		String newFileName = null;
		String fileName = null;
		Map<String, String> map = new HashMap<String, String>();
		String path =Constant.FILE_PATH + cardid + File.separator;
		File tempDir = new File(path);
		if (!tempDir.isDirectory()) {
			tempDir.mkdirs();
		}
		try {
			// 取得上传文件
			if (file != null && !file.isEmpty()) {
//				fileName = file.getOriginalFilename();
				fileName = fileName_1;
				File tempFile = new File(path
						+ fileName_1);
				if (tempFile.exists()) {
					newFileName = IDGenerator.generateID() + "."
							+ fileName_1.split("\\.")[1];
				} else {
					newFileName = fileName_1;
				}
				File localFile = new File(path + newFileName);
				file.transferTo(localFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("fileName", fileName);
		map.put("url", path + newFileName);
		return map;
	}
	
	public void deleteId(@Param("id") String id){
		CustomerDao.deleteId(id);
	}
	public List<JnpadPcCustomer>findAllqyjl(){
		return CustomerDao.findAllqyjl();
	}
	public List<JnpadPcCustomer>findxzz(@Param("id") String id){
		return CustomerDao.findxzz(id);
	}
	public List<JnpadPcCustomer>findSpRy(@Param("userId") String userId){
		return CustomerDao.findSpRy(userId);
	}
	
	
	//循环多图在线预览图片
	public static void downloadjn(HttpServletResponse response,
			String filePath, String fileName) {
		BufferedInputStream bis=null;
		BufferedOutputStream bos=null;
		try {
			byte[] buff = new byte[2048];
			int bytesRead;
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(fileName, "UTF-8"));
			 if(sftp!=null&&sftp.isConnected()){  
				 System.out.println("我链接啦");
	            }else{
	            	System.out.println("11111");
	            	connect();
	            }
			sftp.cd(filePath.substring(0, 36));
		
				
				 bis = new BufferedInputStream(sftp.get(filePath.substring(37, filePath.length())));
		
			 bis = new BufferedInputStream(sftp.get(filePath.substring(37, filePath.length())));
			 bos = new BufferedOutputStream(response.getOutputStream());
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			System.out.println("111111");
			bos.flush();
			if (bis != null) {
				bis=null;
			}
			if (bos != null) {
				bos=null;
			}
			disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//单图在线预览图片
		public static void downloadjn1(HttpServletResponse response,
				String filePath, String fileName) {
			try {
				byte[] buff = new byte[2048];
				int bytesRead;
				response.setHeader("Content-Disposition", "attachment; filename="
						+ java.net.URLEncoder.encode(fileName, "UTF-8"));
				connect();
				sftp.cd(filePath.substring(0, 50));
				BufferedInputStream bis1 = new BufferedInputStream(sftp.get(filePath.substring(51, filePath.length())));//filePath.split("\\\\")[4]
				BufferedOutputStream bos1 = new BufferedOutputStream(response.getOutputStream());
				while (-1 != (bytesRead = bis1.read(buff, 0, buff.length))) {
					bos1.write(buff, 0, bytesRead);
				}
				bos1.flush();
				System.out.println("我输出了");
				if (bis1 != null) {
					bis1.close();
				}
				if (bos1 != null) {
					bos1.close();
				}
				disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void ckpcImage1(HttpServletResponse response,String id) throws Exception{
			LocalImageForm  v = CustomerDao.selectUriById(id);
			if(v!=null){
				//本地
				//this.bdpcImage(response,"D:"+v.getUri(), v.getAttachment());
				//服务器
				//downloadjn1(response,v.getUri(), v.getAttachment());
				downloadjn3333(response,v.getUri(), v.getAttachment());
			
			}}
		
		public String  kqstfp() throws SftpException{
			System.out.println("111111111111111111111111111");
			connect();
			if(sftp.isConnected()){
				sftp.cd("/usr/pccreditFile/222222222222222222");
				return "成功开启";
			}else{
				return "成功失败";
			}
			
		}
		
		static Integer i=0;
		public static void downloadjn3333(HttpServletResponse response,
				String filePath, String fileName) {
			i+=1;
			try {
				byte[] buff = new byte[2048];
				int bytesRead;
				response.setHeader("Content-Disposition", "attachment; filename="+ java.net.URLEncoder.encode(fileName, "UTF-8"));
						connect();
				sftp.cd(filePath.substring(0, 50));
				String GIF = "image/gif;charset=GB2312";// 设定输出的类型
				String JPG = "image/jpeg;charset=GB2312";
				String BMP = "image/bmp";
			    String PNG = "image/png";
				String imagePath = filePath.substring(51, filePath.length());
				OutputStream output = response.getOutputStream();// 得到输出流
				if (imagePath.toLowerCase().endsWith(".jpg"))// 使用编码处理文件流的情况：
				{
					response.setContentType(JPG);// 设定输出的类型
					// 得到图片的真实路径

					// 得到图片的文件流
					BufferedInputStream imageIn = new BufferedInputStream(sftp.get(filePath.substring(51, filePath.length())));
					// 得到输入的编码器，将文件流进行jpg格式编码
					JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
					// 得到编码后的图片对象
					BufferedImage image = decoder.decodeAsBufferedImage();
					System.out.println(image);
					// 得到输出的编码器
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
					encoder.encode(image);// 对图片进行输出编码
					imageIn.close();// 关闭文件流
				} 

				else if (imagePath.toLowerCase().endsWith(".png")
						|| imagePath.toLowerCase().endsWith(".bmp")) {
					
					BufferedImage bi = ImageIO.read(sftp.get(filePath.substring(51, filePath.length())));
					
					if(imagePath.toLowerCase().endsWith(".png")){
						response.setContentType(PNG);
						ImageIO.write(bi, "png", response.getOutputStream());
					}else{
						response.setContentType(BMP);
						ImageIO.write(bi, "bmp", response.getOutputStream());
					}
					
				}
				output.close();
				//disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    
}
