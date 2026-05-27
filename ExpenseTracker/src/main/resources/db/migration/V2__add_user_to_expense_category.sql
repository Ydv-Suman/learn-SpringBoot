ALTER TABLE `expense_category`
    ADD COLUMN `USER_ID` BIGINT NOT NULL,
    ADD CONSTRAINT `FK_expense_category_user`
        FOREIGN KEY (`USER_ID`)
        REFERENCES `users` (`ID`)
        ON DELETE CASCADE,
    ADD UNIQUE KEY `UK_expense_category_user_name` (`USER_ID`, `CATEGORY_NAME`);
