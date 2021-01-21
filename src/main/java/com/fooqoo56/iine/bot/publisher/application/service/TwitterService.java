package com.fooqoo56.iine.bot.publisher.application.service;

import com.fooqoo56.iine.bot.publisher.domain.api.repository.TwitterRepository;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetResponse;
import com.fooqoo56.iine.bot.publisher.presentation.dto.mq.TweetCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwitterService {

    private final TwitterRepository twitterRepository;

    /**
     * ツイートを検索する.
     *
     * @param payload 検索条件
     * @return ツイートレスポンス
     */
    public TweetListResponse findTweet(final TweetCondition payload) {

        if (StringUtils.isNoneBlank(payload.getQuery())) {
            final TweetRequest request = TweetRequest.convertPayloadToRequest(payload);
            return twitterRepository.findTweet(request);
        }
        return new TweetListResponse();
    }

    /**
     * ツイートをいいねする.
     *
     * @param id ツイートID
     * @return ツイートレスポンス
     */
    public TweetResponse favoriteTweet(final String id) {

        if (StringUtils.isNoneBlank(id)) {
            return twitterRepository.favoriteTweet(id);
        }
        return new TweetResponse();
    }
}
