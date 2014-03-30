package com.lingualearna.web.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.lingualearna.web.notes.Note;

@Component
public class NotesDao {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void testInsert(Note note) {

		em.persist(note);
		em.flush();
	}
}
