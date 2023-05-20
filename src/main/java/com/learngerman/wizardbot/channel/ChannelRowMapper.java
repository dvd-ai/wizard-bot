package com.learngerman.wizardbot.channel;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ChannelRowMapper implements RowMapper<Channel> {
    @Override
    public Channel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Channel(
                rs.getLong("c_id"),
                rs.getBoolean("is_for_report"),
                rs.getBoolean("is_ignored_for_currency_operations")
        );
    }
}
