package com.learngerman.wizardbot.student;

import java.time.LocalDate;
import java.util.Objects;

public class Student {
    private Long discordId;
    private float goldBalance;
    private boolean isEngaged;
    private LocalDate balanceDefrostDate;

    private boolean isInGuild;

    public Student(Long discordId, float goldBalance, boolean isEngaged, LocalDate balanceDefrostDate, boolean isInGuild) {
        this.discordId = discordId;
        this.goldBalance = goldBalance;
        this.isEngaged = isEngaged;
        this.balanceDefrostDate = balanceDefrostDate;
        this.isInGuild = isInGuild;
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

    public LocalDate getBalanceDefrostDate() {
        return balanceDefrostDate;
    }

    public void setBalanceDefrostDate(LocalDate balanceDefrostDate) {
        this.balanceDefrostDate = balanceDefrostDate;
    }

    public boolean isInGuild() {
        return isInGuild;
    }

    public void setInGuild(boolean inGuild) {
        isInGuild = inGuild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;
        return Objects.equals(discordId, student.getDiscordId());
    }

    @Override
    public int hashCode() {
        return discordId.hashCode();
    }
}
