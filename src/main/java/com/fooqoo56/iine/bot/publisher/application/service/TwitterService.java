package com.fooqoo56.iine.bot.publisher.application.service;

import com.fooqoo56.iine.bot.publisher.application.exception.AlreadyFavoritedTweetException;
import com.fooqoo56.iine.bot.publisher.domain.api.repository.TwitterRepository;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetResponse;
import com.fooqoo56.iine.bot.publisher.presentation.dto.mq.TweetCondition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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
    public List<TweetResponse> findTweet(final TweetCondition payload) {

        if (StringUtils.isNoneBlank(payload.getQuery())) {

            final List<TweetListResponse> tweetListResponses = new ArrayList<>();

            tweetListResponses
                    .add(twitterRepository
                            .findTweet(TweetRequest.convertPayloadToRequest(payload)));

            for (int idx = 1; idx <= 5; idx++) {
                if (StringUtils
                        .isBlank(tweetListResponses.get(idx - 1).getSearchMetaData()
                                .getNextMaxId())
                        || "0".equals(tweetListResponses.get(idx - 1).getSearchMetaData()
                        .getNextMaxId())) {
                    break;
                }

                tweetListResponses
                        .add(twitterRepository.findTweet(TweetRequest
                                .convertPayloadToRequestWithPayload(payload,
                                        tweetListResponses.get(idx - 1).getSearchMetaData()
                                                .getNextMaxId())));
            }

            return tweetListResponses.stream().map(TweetListResponse::getStatuses)
                    .flatMap(Collection::stream).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * ツイートをいいねする.
     *
     * @param tweetIds ツイートIDのリスト
     * @return ツイートレスポンス
     * @throws AlreadyFavoritedTweetException 全てのツイートがすでにいいねされたツイートだった場合の例外
     */
    public TweetResponse favoriteTweet(final List<String> tweetIds)
            throws AlreadyFavoritedTweetException {

        for (final String id : tweetIds) {
            if (StringUtils.isNoneBlank(id)) {
                try {
                    return twitterRepository.favoriteTweet(id);
                } catch (final RuntimeException exception) {
                    log.warn(exception.getMessage());
                }
            }
        }

        throw new AlreadyFavoritedTweetException();
    }
}
