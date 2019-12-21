package com.jeno.fantasyleague.security;

import com.jeno.fantasyleague.backend.model.User;

@FunctionalInterface
public interface CurrentUser {

	User getUser();
}
