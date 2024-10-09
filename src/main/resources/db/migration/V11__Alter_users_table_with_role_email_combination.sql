ALTER TABLE `users`
ADD CONSTRAINT `unique_email_role` UNIQUE (`email`, `role_id`);