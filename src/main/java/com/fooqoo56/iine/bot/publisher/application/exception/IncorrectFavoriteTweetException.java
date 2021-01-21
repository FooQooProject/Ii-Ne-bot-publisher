package com.fooqoo56.iine.bot.publisher.application.exception;

public class IncorrectFavoriteTweetException extends TwitterException {

    private static final long serialVersionUID = -8923043163393168770L;

    /**
     * デフォルトコンストラクタ.
     */
    public IncorrectFavoriteTweetException() {

    }

    /**
     * message引数コンストラクタ.
     *
     * @param message メッセージ
     */
    public IncorrectFavoriteTweetException(final String message) {
        super(message);
    }

    /**
     * コンストラクタ.
     *
     * @param message メッセージ
     * @param cause Throwable
     */
    public IncorrectFavoriteTweetException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
