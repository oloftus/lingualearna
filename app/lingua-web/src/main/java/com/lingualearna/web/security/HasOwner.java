package com.lingualearna.web.security;

/**
 * Allows access to the owner of this object.<br/>
 * Implementors of this interface <i>must</i> be JPA entities.
 */
public interface HasOwner {

    String getOwnerUsername();
}