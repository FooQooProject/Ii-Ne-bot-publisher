package com.fooqoo56.iine.bot.publisher.presentation.subscriber;

import com.fooqoo56.iine.bot.publisher.application.service.FavoriteSubscribeService;
import com.fooqoo56.iine.bot.publisher.presentation.dto.mq.TweetCondition;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;

@Slf4j
@Configuration
public class FavoriteSubscriber {

    private final String ERROR_CHANNEL = "favorite.fav-group.errors";

    /**
     * いいねを実行する.
     *
     * @return Consumer
     */
    @Bean
    public Consumer<TweetCondition> favorite(
            final FavoriteSubscribeService favoriteSubscribeService) {
        return favoriteSubscribeService::createFavorite;
    }

    /**
     * エラーハンドリング.
     *
     * @param errorMessage エラーメッセージ
     */
    @ServiceActivator(inputChannel = ERROR_CHANNEL)
    public void handleError(final ErrorMessage errorMessage) {
        // Getting exception objects
        final Throwable errorMessagePayload = errorMessage.getPayload();
        log.error("exception occurred - {}", errorMessagePayload.getMessage());

        // Get message body
        final Message<?> originalMessage = errorMessage.getOriginalMessage();

        if (originalMessage != null) {
            log.error("Message Body: {}", new String((byte[]) originalMessage.getPayload()));
        } else {
            log.error("The message body is empty");
        }
    }

}
