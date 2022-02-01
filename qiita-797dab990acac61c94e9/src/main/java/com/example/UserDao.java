package com.example;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {

	@Select("""
				SELECT *
				FROM `user_test`
				WHERE `name` = #{name}
			""")
	public User select(String name);

	@Insert("""
				INSERT INTO `user_test`
				VALUES (#{name}, #{age})
			""")
	public boolean insert(User user);

	@Update("""
				UPDATE `user_test`
				SET `age` = #{age}
				WHERE `name` = #{name}
			""")
	public boolean update(int age);

	@Delete("""
				DELETE FROM `user_test`
				WHERE `name` = #{name}
			""")
	public boolean delete(String name);
}
