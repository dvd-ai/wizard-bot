package com.learngerman.wizardbot.student;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Repository
public class StudentRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public StudentRepository(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    public void addStudent(Student student) {
        if (studentExistsByDiscordId(student.getDiscordId())) {
            setStudentPresenceInGuild(student.getDiscordId(), true);
            return;
        }

        String sql = "INSERT INTO students (d_uid, gold_balance, is_engaged, balance_defrost_date, is_in_guild)" +
                " VALUES (:d_uid, :gold_balance, :is_engaged, :balance_defrost_date, :is_in_guild)";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("d_uid", student.getDiscordId())
                .addValue("gold_balance", student.getGoldBalance())
                .addValue("is_engaged", student.isEngaged())
                .addValue("balance_defrost_date", student.getBalanceDefrostDate())
                .addValue("is_in_guild", student.isInGuild());

        jdbc.update(sql, sqlParameterSource);
    }

    public int[] addStudents(List<Student> students) {
        String sql = "INSERT INTO students (d_uid, gold_balance, is_engaged, balance_defrost_date)" +
                " VALUES (:d_uid, :gold_balance, :is_engaged, :balance_defrost_date, :is_in_guild)";

        return jdbc.batchUpdate(sql, students.stream()
                .map(student -> new MapSqlParameterSource()
                        .addValue("d_uid", student.getDiscordId())
                        .addValue("gold_balance", student.getGoldBalance())
                        .addValue("is_engaged", student.isEngaged())
                        .addValue("balance_defrost_date", student.getBalanceDefrostDate())
                        .addValue("is_in_guild", student.isInGuild())
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

    public int[] decreaseUnfreezedStudentsGoldCurrencyBy(float goldAmount) {
        String sql = "UPDATE students SET gold_balance = gold_balance - :goldAmount" +
                " WHERE balance_defrost_date IS NULL";

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
        if (!studentIsPresentInGuild(studentDiscordId))
            throw new RuntimeException("student with id: " + studentDiscordId + " not present in the guild");

        String sql = "UPDATE students SET gold_balance = gold_balance + :goldAmount " +
                "WHERE d_uid = :studentDiscordId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("studentDiscordId", studentDiscordId)
                .addValue("goldAmount", goldAmount);

        jdbc.update(sql, sqlParameterSource);
    }

    public void decreaseStudentGoldCurrencyByDiscordId(float goldAmount, Long studentDiscordId) {
        if (!studentIsPresentInGuild(studentDiscordId))
            throw new RuntimeException("student with id: " + studentDiscordId + " not present in the guild");

        String sql = "UPDATE students SET gold_balance = gold_balance - :goldAmount " +
                "WHERE d_uid = :studentDiscordId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("studentDiscordId", studentDiscordId)
                .addValue("goldAmount", goldAmount);

        jdbc.update(sql, sqlParameterSource);
    }

    public void setStudentEngagement(boolean isEngaged, Long studentDiscordId) {
        if (!studentIsPresentInGuild(studentDiscordId))
            throw new RuntimeException("student with id: " + studentDiscordId + " not present in the guild");

        String sql = "UPDATE students SET is_engaged = :isEngaged" +
                "WHERE d_uid = :studentDiscordId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("d_uid", studentDiscordId)
                .addValue("gold_balance", isEngaged);

        jdbc.update(sql, sqlParameterSource);
    }

    public void freezeStudentBalanceTillDefrostDate(LocalDate defrostDate, Long studentDiscordId) {
        if (!studentIsPresentInGuild(studentDiscordId))
            throw new RuntimeException("student with id: " + studentDiscordId + " not present in the guild");

        String sql = "UPDATE students SET balance_defrost_date = :defrostDate" +
                " WHERE d_uid = :d_uid";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("d_uid", studentDiscordId)
                .addValue("defrostDate", defrostDate);

        jdbc.update(sql, sqlParameterSource);
    }

    public void unfreezeStudentBalance(Long studentDiscordId) {
        if (!studentIsPresentInGuild(studentDiscordId))
            throw new RuntimeException("student with id: " + studentDiscordId + " not present in the guild");

        String sql = "UPDATE students SET balance_defrost_date IS NULL" +
                " WHERE d_uid = :d_uid";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("d_uid", studentDiscordId);

        jdbc.update(sql, sqlParameterSource);
    }

    public void updateFreezeStatus() {
        String sql = "UPDATE students " +
                "    SET balance_defrost_date = NULL " +
                "    WHERE balance_defrost_date = CURRENT_DATE AT TIME ZONE 'Europe/Berlin'";
        jdbc.update(sql, new HashMap<>());
    }

    public float getStudentGoldCurrency(Long studentDiscordId) {
        if (!studentIsPresentInGuild(studentDiscordId))
            throw new RuntimeException("student with id: " + studentDiscordId + " not present in the guild");

        String sql = "SELECT d_uid, gold_balance, is_engaged, balance_defrost_date, is_in_guild" +
                "  FROM students WHERE d_uid = :studentDiscordId";


        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("studentDiscordId", studentDiscordId);

        Student student = jdbc.queryForObject(sql, sqlParameterSource, new StudentRowMapper());
        return student.getGoldBalance();
    }

    public List<Long> getStudentIdsWithNoCurrency(int page, int size) {
        String sql =
                "SELECT d_uid FROM students " +
                        "WHERE gold_balance <= 0 AND balance_defrost_date IS NULL AND is_in_guild = true " +
                        "ORDER BY d_uid " +
                        "LIMIT :size " +
                        "OFFSET :offset ";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("size", size)
                .addValue("offset", size * page);

        return jdbc.query(sql, sqlParameterSource, (rs, rowNum) -> rs.getLong("d_uid"));
    }

    public List<Student> getStudents(int page, int size) {
        String sql =
                "SELECT d_uid, gold_balance, is_engaged, balance_defrost_date, is_in_guild FROM students " +
                        "WHERE is_in_guild = true " +
                        "ORDER BY gold_balance DESC " +
                        "LIMIT :size " +
                        "OFFSET :offset ";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("size", size)
                .addValue("offset", size * page);

        return jdbc.query(sql, sqlParameterSource, new StudentRowMapper());
    }

    public Student getStudent(Long studentId) {
        if (!studentIsPresentInGuild(studentId))
            throw new RuntimeException("student with id: " + studentId + " not present in the guild");

        String sql = "SELECT d_uid, gold_balance, is_engaged, balance_defrost_date, is_in_guild" +
                "  FROM students WHERE d_uid = :studentId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("studentId", studentId);

        return jdbc.queryForObject(sql, sqlParameterSource, new StudentRowMapper());
    }

    public LocalDate getStudentDefrostDate(Long studentDiscordId) {
        if (!studentIsPresentInGuild(studentDiscordId))
            throw new RuntimeException("student with id: " + studentDiscordId + " not present in the guild");

        String sql = "SELECT d_uid, gold_balance, is_engaged, balance_defrost_date, is_in_guild" +
                "  FROM students WHERE d_uid = :studentDiscordId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("studentDiscordId", studentDiscordId);

        Student student = jdbc.queryForObject(sql, sqlParameterSource, new StudentRowMapper());
        return student.getBalanceDefrostDate();
    }

    public Integer calculateStudentsWithZeroOrLessCurrency() {
        String sql = "SELECT count(*) " +
                "  FROM students " +
                "WHERE balance_defrost_date IS NULL AND gold_balance <= 0 AND is_in_guild = true";

        return jdbc.queryForObject(sql, new HashMap<>(), Integer.class);
    }

    public Integer calculateStudents() {
        String sql = "SELECT count(*) " +
                "  FROM students " +
                "WHERE is_in_guild = true";

        return jdbc.queryForObject(sql, new HashMap<>(), Integer.class);
    }

    public void setStudentPresenceInGuild(Long studentId, boolean isPresent) {
        if (!studentExistsByDiscordId(studentId))
            throw new RuntimeException("no student with id: " + studentId);

        String sql = "UPDATE students " +
                "    SET is_in_guild = :isPresent " +
                "    WHERE d_uid = :studentId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("isPresent", isPresent)
                .addValue("studentId", studentId);

        jdbc.update(sql, sqlParameterSource);
    }

    public boolean studentIsPresentInGuild(Long studentId) {
        if (!studentExistsByDiscordId(studentId))
            throw new RuntimeException("no student with id: " + studentId);

        String sql = "SELECT COUNT(*) FROM students" +
                " WHERE d_uid = :studentId AND is_in_guild = true";


        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("studentId", studentId);

        Integer count = jdbc.queryForObject(sql, sqlParameterSource, Integer.class);

        return Objects.requireNonNull(count) > 0;
    }


}
