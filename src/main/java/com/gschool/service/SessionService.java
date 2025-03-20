package com.gschool.service;

import com.gschool.entities.Session;
import com.gschool.repositries.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }
    public Session getSessionById(int id) {
        return sessionRepository.findById(id).orElse(null);
    }
    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }
    public void deleteSessionById(int id) {
        sessionRepository.deleteById(id);
    }
    public Session updateSession(Integer id, Session session) {
        Session existingSession = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id "));
        existingSession.setName(session.getName());
        existingSession.setScript(session.getScript());
        existingSession.setUsername(session.getUsername());
        existingSession.setDep(session.getDep());
        existingSession.setIsActive(session.getIsActive());
        existingSession.setConfig(session.getConfig());
        return sessionRepository.save(existingSession);
    }
    public boolean existsByNameAndIdNot(String name, Integer id)  {
        return sessionRepository.existsByNameAndIdNot(name , id);
    }
    public boolean existsByName(String name)  {
        return sessionRepository.existsByName(name);
    }
    public List<Session> getSessionsByDepId(Integer id) {
        return sessionRepository.findByDepId(id);
    }


}
