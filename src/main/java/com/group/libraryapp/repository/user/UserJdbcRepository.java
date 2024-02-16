package com.group.libraryapp.repository.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    // 이 생성자는 스프링이 컨테이너에 빈 등록하면서 해주는거지?
    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isUserNotExist(long id) {
        String readSql = "select * from user where id = ?";
        // id == request.getId()인 user마다 0이라는 값을 반환 -> query가 list로 감싸줌
        return jdbcTemplate.query(readSql, (rs, rowNum) -> 0, id).isEmpty();
    }

    public boolean isUserNotExist(String name) {
        String readSql = "select * from user where name = ?";
        // name==name인 user마다 0이라는 값을 반환 -> query가 list로 감싸줌
        return jdbcTemplate.query(readSql, (rs, rowNum) -> 0, name).isEmpty();
    }

    public void saveUser(String name, Integer age) {
        String sql = "insert into user(name, age) values(?, ?)";
        jdbcTemplate.update(sql, name, age);
    }

//    public List<UserResponse> getUsers() {
//        String sql = "select * from user";
//        // JdbcTemplate의 쿼리가 매개변수로 받은 sql을 수행
//        // => 그 결과로 받은 유저 정보를 new RowMapper로 UserResponse타입 객체로 변환
//        return jdbcTemplate.query(sql, (rs, rowNum) -> {
//            long id = rs.getLong("id");
//            String name = rs.getString("name");
//            int age = rs.getInt("age");
//            return new UserResponse(id, name, age);
//        });
//    }

    public void updateUserName(String name, long id) {
        String sql = "update user set name = ? where id = ?";
        jdbcTemplate.update(sql, name, id);
    }

    public void deleteUser(String name) {
        String sql = "delete from user where name = ?";
        jdbcTemplate.update(sql, name);
    }
}
