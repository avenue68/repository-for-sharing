package com.example;

import static org.assertj.core.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@MybatisTest
@Sql("classpath:META-INF/mysql/init.sql")
class UserDaoTest {

	@Autowired
	UserDao dao;

	JdbcTemplate jdbcTemplate;

	@Autowired
	void setJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Test
	void testSelect() {
		var existingUser = new User("sample_user", 30);

		assertThat(dao.select("_user")).isNull();
		assertThat(dao.select("sample_user")).isEqualTo(existingUser);
	}

	@Test
	void testDelete() {
		assertThat(dao.delete("_user")).isFalse();
		assertThat(dao.delete("sample_user")).isTrue();

		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "user_test")).isEqualTo(0);
	}

}
