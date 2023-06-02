package com.learngerman.wizardbot.student;

import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Date date = rs.getDate("balance_defrost_date");
        LocalDate localDate = null;

        if (date != null)
            localDate = date.toLocalDate();

        return new Student(
                rs.getLong("d_uid"),
                rs.getFloat("gold_balance"),
                rs.getBoolean("is_engaged"),
                localDate,
                rs.getBoolean("is_in_guild"));
    }
}
