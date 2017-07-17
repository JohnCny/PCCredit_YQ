package com.cardpay.pccredit.jnpad.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.intopieces.model.LocalImage;
import com.cardpay.pccredit.intopieces.web.LocalImageForm;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface JnpadImageBrowseDao {

	public List<LocalImageForm> findLocalImage(@Param("customerId") String id,@Param("productId") String productId,@Param("phone_type") String phone_type );

	//删除图片
	public void deleteImage(@Param("id") String imageId);

	void addImageByPType(LocalImage I);
	//照片分类查询
		public List<LocalImageForm> findLocalImageByType(@Param("customerId") String id,
				@Param("productId") String productId,@Param("phone_type") String phone_type);
	
}
