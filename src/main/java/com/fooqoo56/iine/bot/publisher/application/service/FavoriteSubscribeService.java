package com.fooqoo56.iine.bot.publisher.application.service;

import com.fooqoo56.iine.bot.publisher.application.exception.IncorrectFavoriteTweetException;
import com.fooqoo56.iine.bot.publisher.application.exception.NotFoundTweetException;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetResponse;
import com.fooqoo56.iine.bot.publisher.presentation.dto.mq.TweetCondition;
import java.util.Comparator;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteSubscribeService {

    private final TwitterService twitterService;

    /**
     * いいね作成.
     *
     * @param payload 検索条件
     * @throws NotFoundTweetException ツイートが見つからない場合の例外
     */
    public void createFavorite(final TweetCondition payload) throws NotFoundTweetException {
        final TweetListResponse response = twitterService.findTweet(payload);
        final TweetResponse tweet =
                response.getStatuses().stream().filter(res -> isValidatedTweet(res, payload))
                        .max(Comparator.comparingInt(s -> s.getUser().getFavouritesCount()))
                        .orElseThrow(NotFoundTweetException::new);

        final TweetResponse tweetResponse = twitterService.favoriteTweet(tweet.getId());

        if (!tweetResponse.getId().equals(tweet.getId())) {
            throw new IncorrectFavoriteTweetException("取得したツイートといいねしたツイートが一致しません。");
        }

    }

    /**
     * 条件に合致するツイートかどうかを判定.
     *
     * @param res       レスポンス
     * @param condition 条件
     * @return 条件に合致するツイートの場合、true
     */
    private boolean isValidatedTweet(final TweetResponse res, final TweetCondition condition) {

        if (Objects.isNull(res) || Objects.isNull(condition)) {
            return false;
        }

        if (notContainQuery(res.getText(), condition.getQuery())) {
            return false;
        }

        return (isGraterThan(res.getFavoriteCount(), condition.getFavoriteCount())
                && isGraterThan(res.getRetweetCount(), condition.getRetweetCount())
                && isGraterThan(res.getUser().getFollowersCount(), condition.getFollowersCount())
                && isGraterThan(res.getUser().getFriendsCount(), condition.getFriendsCount()))
                && (!res.getFavoriteFlag() && !res.getSensitiveFlag()
                && StringUtils.isBlank(res.getInReplyToStatusId()));
    }

    /**
     * 条件の単語がツイートに含まれるか判定する.
     *
     * @param text  ツイート
     * @param query 条件の単語
     * @return 条件の単語が含まれる場合、true
     */
    private boolean notContainQuery(final String text, final String query) {
        if (StringUtils.isBlank(text) || StringUtils.isBlank(query)) {
            return true;
        }

        return !text.contains(query);
    }

    /**
     * left > rightであるかどうか判定.
     *
     * @param left  left
     * @param right right
     * @return left > rightであれば、true
     */
    private boolean isGraterThan(final Integer left, final Integer right) {
        if (Objects.isNull(left) || Objects.isNull(right)) {
            return false;
        }

        return left > right;
    }
}
