package com.cardpay.pccredit.common;

import java.awt.image.BufferedImage;    
import java.io.ByteArrayInputStream;    
import java.io.ByteArrayOutputStream;    
import java.io.File;    
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.cardpay.pccredit.intopieces.web.LocalImageForm;

import sun.misc.BASE64Decoder;    
import sun.misc.BASE64Encoder;    

public class Base64Util {
	public static List TestImageBinary(List<LocalImageForm> result) throws IOException {    
	    BASE64Encoder encoder = new sun.misc.BASE64Encoder();    
	    BASE64Decoder decoder = new sun.misc.BASE64Decoder();
	    List list=new ArrayList();
	    SFTPUtil.connect();
	    for(int i=0;i<result.size();i++){
	    }
	        File f = new File("d://1.jpg");           
	        BufferedImage bi;    
	            bi = ImageIO.read(f);    
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();    
	            ImageIO.write(bi, "jpg", baos);    
	            byte[] bytes = baos.toByteArray();    
	            byte[] bytes1 = decoder.decodeBuffer(encoder.encodeBuffer(bytes).trim());                  
	            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);    
	            BufferedImage bi1 =ImageIO.read(bais);    
	            File w2 = new File("d://2.png");//可以是jpg,png,gif格式    
	            ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动    
				return list;
	    
}}
