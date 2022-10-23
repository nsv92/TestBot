CREATE TABLE IF NOT EXISTS bot_users
(
    chat_id      BIGINT       NOT NULL,
    username     VARCHAR(255) NOT NULL,
    zodiac       VARCHAR(255) NULL,
    subscription BIT(1)       NOT NULL,
    CONSTRAINT pk_bot_users PRIMARY KEY (chat_id)
);

ALTER TABLE bot_users
    ADD CONSTRAINT uc_bot_users_username UNIQUE (username);