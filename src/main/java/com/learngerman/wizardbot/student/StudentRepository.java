package com.learngerman.wizardbot.student;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class StudentRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public StudentRepository(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    public Long addStudent(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO students (d_uid, gold_balance, is_engaged, balance_defrost_date)" +
                " VALUES (:d_uid, :gold_balance, is_engaged, balance_defrost_date)";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("d_uid", student.getDiscordId())
                .addValue("gold_balance", student.getGoldBalance())
                .addValue("is_engaged", student.isEngaged())
                .addValue("balance_defrost_date", student.getBalanceDefrostDate())
        ;

        jdbc.update(sql, sqlParameterSource, keyHolder);
        return Long.valueOf((Integer)keyHolder.getKeys().get("d_uid"));
    }

    public int[] addStudents(List<Student> students) {
        String sql = "INSERT INTO students (d_uid, gold_balance, is_engaged, balance_defrost_date)" +
                " VALUES (:d_uid, :gold_balance, is_engaged, balance_defrost_date)";

        return jdbc.batchUpdate(sql, students.stream()
                .map(student -> new MapSqlParameterSource()
                        .addValue("d_uid", student.getDiscordId())
                        .addValue("gold_balance", student.getGoldBalance())
                        .addValue("is_engaged", student.isEngaged())
                        .addValue("balance_defrost_date", student.getBalanceDefrostDate())
                ).toArray(SqlParameterSource[]::new));
    }

    public int[] increaseStudentsGoldCurrencyBy(float goldAmount) {
        String sql = "UPDATE students SET gold_balance = gold_balance + :goldAmount";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("goldAmount", goldAmount);
        SqlParameterSource[] sqlParameterSourceArray = new SqlParameterSource[]{sqlParameterSource};

        return jdbc.batchUpdate(sql, sqlParameterSourceArray);
    }

    public int[] decreaseStudentsGoldCurrencyBy(float goldAmount) {
        String sql = "UPDATE students SET gold_balance = gold_balance - :goldAmount";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("goldAmount", goldAmount);
        SqlParameterSource[] sqlParameterSourceArray = new SqlParameterSource[]{sqlParameterSource};

        return jdbc.batchUpdate(sql, sqlParameterSourceArray);
    }

    public boolean studentExistsByDiscordId(Long studentDiscordId) {
        String sql = "SELECT COUNT(*) FROM students WHERE d_uid = :studentDiscordId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("studentDiscordId", studentDiscordId);
        Integer count = jdbc.queryForObject(sql, sqlParameterSource, Integer.class);

        return Objects.requireNonNull(count) > 0;
    }

    public void increaseStudentGoldCurrencyByDiscordId(float goldAmount, Long studentDiscordId) {
        if (!studentExistsByDiscordId(studentDiscordId))
            throw new RuntimeException("no student with id: " + studentDiscordId);

        String sql = "UPDATE students SET gold_balance = gold_balance + :goldAmount " +
                "WHERE d_uid = :studentDiscordId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("d_uid", studentDiscordId)
                .addValue("gold_balance", goldAmount)
        ;

        jdbc.update(sql, sqlParameterSource);
    }

    public void decreaseStudentGoldCurrencyByDiscordId(float goldAmount, Long studentDiscordId) {
        if (!studentExistsByDiscordId(studentDiscordId))
            throw new RuntimeException("no student with id: " + studentDiscordId);

        String sql = "UPDATE students SET gold_balance = gold_balance - :goldAmount " +
                "WHERE d_uid = :studentDiscordId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("d_uid", studentDiscordId)
                .addValue("gold_balance", goldAmount)
        ;

        jdbc.update(sql, sqlParameterSource);
    }

    public void setStudentEngagement(boolean isEngaged, Long studentDiscordId) {
        if (!studentExistsByDiscordId(studentDiscordId))
            throw new RuntimeException("no student with id: " + studentDiscordId);

        String sql = "UPDATE students SET is_engaged = :isEngaged" +
                "WHERE d_uid = :studentDiscordId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("d_uid", studentDiscordId)
                .addValue("gold_balance", isEngaged)
        ;

        jdbc.update(sql, sqlParameterSource);
    }

    public void freezeStudentBalanceTillDefrostDate(OffsetDateTime defrostDate, Long studentDiscordId) {
        if (!studentExistsByDiscordId(studentDiscordId))
            throw new RuntimeException("no student with id: " + studentDiscordId);

        String sql = "UPDATE students SET balance_defrost_date = :defrostDate" +
                "WHERE d_uid = :studentDiscordId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("d_uid", studentDiscordId)
                .addValue("defrostDate", defrostDate)
        ;

        jdbc.update(sql, sqlParameterSource);
    }

    public void unfreezeStudentBalanceTillDefrostDate(Long studentDiscordId) {
        if (!studentExistsByDiscordId(studentDiscordId))
            throw new RuntimeException("no student with id: " + studentDiscordId);

        String sql = "UPDATE students SET balance_defrost_date = NULL" +
                "WHERE d_uid = :studentDiscordId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("d_uid", studentDiscordId)
        ;

        jdbc.update(sql, sqlParameterSource);
    }
}
