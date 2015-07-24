package com.api_github_users.communicator;

public class CommunicatorFactory {
	public static BackendCommunicator createBackendCommunicator() {
		return new BackendCommunicatorStub();
	}
}