package com.learngerman.wizardbot.channel;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

@Repository
public class ChannelRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public ChannelRepository(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    public void addChannel(Channel channel) {
        String sql = "INSERT INTO channels (c_id, is_for_report, is_ignored_for_currency_operations)" +
                " VALUES (:c_id, :is_for_report, :is_ignored_for_currency_operations)";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("c_id", channel.getChannelId())
                .addValue("is_for_report", channel.isForReport())
                .addValue("is_ignored_for_currency_operations", channel.isIgnoredForCurrencyOperations())
        ;

        jdbc.update(sql, sqlParameterSource);
    }

    public boolean channelExistsById(Long channelId) {
        String sql = "SELECT COUNT(*) FROM channels WHERE c_id = :channelId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("channelId", channelId);
        Integer count = jdbc.queryForObject(sql, sqlParameterSource, Integer.class);

        return Objects.requireNonNull(count) > 0;
    }

    public List<Channel> getChannelsForReport() {
        String sql = "SELECT c_id, is_for_report, is_ignored_for_currency_operations FROM channels " +
                "WHERE is_for_report = true";
        return jdbc.query(sql, new ChannelRowMapper());
    }

    public List<Channel> getIgnoredChannelsForCurrency() {
        String sql = "SELECT c_id, is_for_report, is_ignored_for_currency_operations FROM channels " +
                "WHERE is_ignored_for_currency_operations = true";
        return jdbc.query(sql, new ChannelRowMapper());
    }

    public void setChannelReport(Long channelId, boolean isForReport) {
        String sql = "UPDATE channels SET is_for_report = :is_for_report " +
                "WHERE c_id = :channelId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("is_for_report", isForReport)
                .addValue("channelId", channelId)
                ;

        jdbc.update(sql, sqlParameterSource);
    }

    public void setChannelForCurrencyOperationsIgnorance(Long channelId, boolean isIgnoredForCurrencyOperations) {
        String sql = "UPDATE channels SET is_ignored_for_currency_operations = :is_ignored_for_currency_operations " +
                "WHERE c_id = :channelId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("is_ignored_for_currency_operations", isIgnoredForCurrencyOperations)
                .addValue("channelId", channelId)
                ;

        jdbc.update(sql, sqlParameterSource);
    }

    public Channel getChannelById(Long channelId) {
        if (!channelExistsById(channelId))
            throw new RuntimeException("channel with id " + channelId + " not found.");

        String sql = "SELECT c_id, is_for_report, is_ignored_for_currency_operations FROM channels" +
                " WHERE c_id = :c_id";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("c_id", channelId);

        return jdbc.queryForObject(sql, sqlParameterSource, new ChannelRowMapper());
    }


}
