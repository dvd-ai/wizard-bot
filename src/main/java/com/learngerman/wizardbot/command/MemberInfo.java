package com.learngerman.wizardbot.command;

public class MemberInfo {
    private String username;
    private String discriminator;

    private String avatar;

    public MemberInfo() {
    }

    public MemberInfo(String username, String discriminator, String avatar) {
        this.username = username;
        this.discriminator = discriminator;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
