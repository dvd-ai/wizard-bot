package com.learngerman.wizardbot.student;

import java.time.OffsetDateTime;
import java.util.Objects;

public class Student {
    private Long discordId;
    private float goldBalance;
    private boolean isEngaged;
    private OffsetDateTime balanceDefrostDate;

    public Student(Long discordId, float goldBalance, boolean isEngaged, OffsetDateTime balanceDefrostDate) {
        this.discordId = discordId;
        this.goldBalance = goldBalance;
        this.isEngaged = isEngaged;
        this.balanceDefrostDate = balanceDefrostDate;
    }

    public Long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(Long discordId) {
        this.discordId = discordId;
    }

    public float getGoldBalance() {
        return goldBalance;
    }

    public void setGoldBalance(float goldBalance) {
        this.goldBalance = goldBalance;
    }

    public boolean isEngaged() {
        return isEngaged;
    }

    public void setEngaged(boolean engaged) {
        isEngaged = engaged;
    }

    public OffsetDateTime getBalanceDefrostDate() {
        return balanceDefrostDate;
    }

    public void setBalanceDefrostDate(OffsetDateTime balanceDefrostDate) {
        this.balanceDefrostDate = balanceDefrostDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (Float.compare(student.goldBalance, goldBalance) != 0) return false;
        if (isEngaged != student.isEngaged) return false;
        if (!discordId.equals(student.discordId)) return false;
        return Objects.equals(balanceDefrostDate, student.balanceDefrostDate);
    }

    @Override
    public int hashCode() {
        int result = discordId.hashCode();
        result = 31 * result + (goldBalance != +0.0f ? Float.floatToIntBits(goldBalance) : 0);
        result = 31 * result + (isEngaged ? 1 : 0);
        result = 31 * result + (balanceDefrostDate != null ? balanceDefrostDate.hashCode() : 0);
        return result;
    }
}
