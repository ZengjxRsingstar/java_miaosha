package com.zengjx.miaosha.dao;

import com.zengjx.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.*;


@Mapper
public interface MiaoshaUserDao {
	
	@Select("select * from miaosha_user where id = #{id}")
	public MiaoshaUser getById(@Param("id") long id);
	@Update("update miaosha_user set  password  =#{password} where  id=#{id} ")
	void update( MiaoshaUser  toBeUpdate);

}
