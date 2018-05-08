package com.jeno.fantasyleague.resources;

public class NoActiveResourcesFoundException extends RuntimeException {

	public NoActiveResourcesFoundException() {
		super("Could not find the active resources, consider using ActiveResources.set() to set the resources");
	}
}
