package com.lingualearna.web.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.lingualearna.web.notes.Note;

@Component
public class GenericDao<T> {

	@PersistenceContext
	private EntityManager em;
	private Class<T> entityType;

	public void setEntityType(Class<T> type) {

		this.entityType = type;
	}

	public void persist(T obj) {

		em.persist(obj);
		em.flush();
	}

	public T findNoLock(int id) {

		return em.find(entityType, id);
	}

	public Note merge(Note note) {

		return em.merge(note);
	}

	public void delete(int noteId) {

		em.remove(findNoLock(noteId));
	}
}
