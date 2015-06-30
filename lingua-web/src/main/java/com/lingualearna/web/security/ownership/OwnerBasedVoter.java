package com.lingualearna.web.security.ownership;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.lingualearna.web.shared.components.AbstractDao;

@Component
public class OwnerBasedVoter implements AccessDecisionVoter<MethodInvocation> {

    private static final int OWNED_OBJECT_OR_ID_POSITION = 0;

    @Autowired
    private AbstractDao genericDao;

    private HasOwner getOwnedObject(MethodInvocation methodInvocation, Integer ownedObjectId) {

        HasOwner ownedObject = null;

        for (Annotation annotation : methodInvocation.getMethod().getAnnotations()) {
            if (annotation instanceof OwnedObjectType) {

                OwnedObjectType securedTypeAnnotation = (OwnedObjectType) annotation;
                Class<?> securedType = securedTypeAnnotation.value();

                if (HasOwner.class.isAssignableFrom(securedType)) {
                    ownedObject = getEntity(ownedObjectId, securedType);
                    break;
                }
            }
        }

        return ownedObject;
    }

    private HasOwner getEntity(Integer objectId, Class<?> objectType) {

        return (HasOwner) genericDao.find(objectType, objectId);
    }

    private int isOwner(HasOwner ownedObject, UserDetails userDetails) {

        if (ownedObject == null ||
                (ownedObject != null && userDetails.getUsername().equals(ownedObject.getOwnerUsername()))) {
            return ACCESS_GRANTED;
        }

        return ACCESS_DENIED;
    }

    /**
     * Still have to lookup object by ID because it's probably just a shell...
     */
    private int isOwnerOfObject(Object ownedObjectObj, UserDetails userDetails) {

        HasOwner ownedObject = (HasOwner) ownedObjectObj;
        ownedObject = getEntity(ownedObject.getId(), ownedObject.getClass());
        return isOwner(ownedObject, userDetails);
    }

    private int isOwnerOfObjectId(Object ownedObjectIdObj, UserDetails userDetails, MethodInvocation methodInvocation) {

        Integer ownedObjectId = (Integer) ownedObjectIdObj;
        HasOwner ownedObject = getOwnedObject(methodInvocation, ownedObjectId);
        return isOwner(ownedObject, userDetails);
    }

    private int isOwnerOfObjectOrId(UserDetails userDetails, MethodInvocation methodInvocation) {

        Object ownedObjectOrId = methodInvocation.getArguments()[OWNED_OBJECT_OR_ID_POSITION];

        if (ownedObjectOrId instanceof HasOwner) {
            return isOwnerOfObject(ownedObjectOrId, userDetails);
        }
        else if (ownedObjectOrId instanceof Integer) {
            return isOwnerOfObjectId(ownedObjectOrId, userDetails, methodInvocation);
        }

        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return MethodInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {

        return attribute.getAttribute().equals(SecuredConfigAttributes.ALLOW_OWNER);
    }

    @Override
    public int vote(Authentication authentication, MethodInvocation methodInvocation,
            Collection<ConfigAttribute> attributes) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        for (ConfigAttribute attribute : attributes) {
            if (supports(attribute)) {
                return isOwnerOfObjectOrId(userDetails, methodInvocation);
            }
        }

        return ACCESS_DENIED;
    }
}
