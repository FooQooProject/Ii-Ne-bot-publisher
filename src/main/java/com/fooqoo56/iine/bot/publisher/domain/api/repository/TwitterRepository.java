package com.fooqoo56.iine.bot.publisher.domain.api.repository;

import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetResponse;

public interface TwitterRepository {

    /**
     * フォロワー取得.
     *
     * @param request TweetRequest
     * @return TwitterFollowerResponse
     */
    TweetListResponse findTweet(final TweetRequest request);

    /**
     * ツイートのいいね.
     *
     * @param id ツイートID
     * @return TweetResponse
     */
    TweetResponse favoriteTweet(final String id);
}
