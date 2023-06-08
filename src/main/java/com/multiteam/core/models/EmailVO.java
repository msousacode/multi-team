package com.multiteam.core.models;

import com.multiteam.modules.user.User;
import com.sendgrid.helpers.mail.objects.Content;

public record EmailVO(
        String email,
        String subject,
        Content content
) {

    public static EmailVO buildEmail(User user, String subject, Content content) {
        return new EmailVO(user.getEmail(), subject, content);
    }
}