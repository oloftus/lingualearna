package com.lingualearna.web.security.ownership;

/**
 * Allows access to the owner of this object.<br/>
 * Implementors of this interface <i>must</i> be JPA entities to allow
 * OwnerBasedVoter to lookup by ID,
 */
public interface HasOwner {

    String getOwnerUsername();

    int getId();
}
