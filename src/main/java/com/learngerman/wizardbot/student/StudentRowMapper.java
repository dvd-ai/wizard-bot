package com.learngerman.wizardbot.student;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Student(
                rs.getLong("d_uid"),
                rs.getFloat("gold_balance"),
                rs.getBoolean("is_engaged"),
                (OffsetDateTime) rs.getObject("balance_defrost_date")
        );
    }
}
