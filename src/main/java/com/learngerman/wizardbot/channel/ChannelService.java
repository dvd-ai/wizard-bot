package com.learngerman.wizardbot.channel;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public void setChannelForReports(Channel channel) {
        if (channelRepository.channelExistsById(channel.getChannelId())) {
            channelRepository.setChannelReport(channel.getChannelId(), channel.isForReport());
        } else channelRepository.addChannel(channel);
    }

    public void setChannelForCurrencyOperationsIgnorance(Channel channel) {
        if (channelRepository.channelExistsById(channel.getChannelId())) {
            channelRepository.setChannelForCurrencyOperationsIgnorance(
                    channel.getChannelId(),
                    channel.isIgnoredForCurrencyOperations()
            );
        } else channelRepository.addChannel(channel);
    }

    public Channel getChannelById(Long channelId) {
        return channelRepository.getChannelById(channelId);
    }

    public List<Channel> getChannelsForReport() {
        return channelRepository.getChannelsForReport();
    }

    public List<Channel> getIgnoredChannelsForCurrency() {
        return channelRepository.getIgnoredChannelsForCurrency();
    }
}
