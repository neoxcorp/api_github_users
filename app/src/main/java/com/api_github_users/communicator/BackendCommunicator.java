package com.api_github_users.communicator;

import com.api_github_users.list_ui.User;

import java.util.ArrayList;

public interface BackendCommunicator {
	ArrayList<User> postGetData(int sinceId) throws InterruptedException;
}