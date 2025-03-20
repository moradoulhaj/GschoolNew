package com.gschool.controller.web;

import com.gschool.entities.Session;
import com.gschool.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    private final SessionService sessionService;
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }
    @GetMapping("/getAll")
    public ResponseEntity <List<Session>> getSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<Object> getSessionById(@PathVariable int id) {
        Session session = sessionService.getSessionById(id);
        if (session == null) {
            Map <String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Session not found");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(session, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestBody Session session) {
        // check if a session with same name already exists
        if (sessionService.existsByName(session.getName())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Session with the same name already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        Session newSession = sessionService.saveSession(session);
        return new ResponseEntity<>(newSession, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSession(@RequestBody Session session, @PathVariable int id) {
        Session currentSession = sessionService.getSessionById(id);
        //Check if te sessions exists
        if (currentSession == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Session not found.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        // Check if a session with the same name already exists
        } else if (sessionService.existsByNameAndIdNot(session.getName(), id)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Session with the same name already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        //Save the sessions
        currentSession.setName(session.getName());
        currentSession.setDep(session.getDep());
        currentSession.setConfig(session.getConfig());
        currentSession.setUsername(session.getUsername());
        currentSession.setScript(session.getScript());
        currentSession.setIsActive(session.getIsActive());
        sessionService.saveSession(currentSession);
        return new ResponseEntity<>(currentSession, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSession(@PathVariable int id) {
        Session currentSession = sessionService.getSessionById(id);
        if (currentSession == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found.");
        }
        sessionService.deleteSessionById(id);
    }
    @GetMapping("/getByDepId/{depId}")
    public ResponseEntity<List<Session>> getSessionsByDepId(@PathVariable int depId) {
        List<Session> sessions = sessionService.getSessionsByDepId(depId);
        return new ResponseEntity<>(sessions, HttpStatus.OK);

    }
    @PutMapping("/updateMultipleStatus")
    public ResponseEntity<?> updateMultipleSessionStatus(@RequestBody List<Map<String, Object>> sessionsStatus) {
        // Iterate through the list of sessions
        for (Map<String, Object> sessionStatus : sessionsStatus) {
            Integer id = (Integer) sessionStatus.get("id");
            Boolean status = (Boolean) sessionStatus.get("status");

            // Check if both ID and status are provided
            if (id == null || status == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Each session must have both 'id' and 'status'.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }

            // Get session by ID
            Session currentSession = sessionService.getSessionById(id);
            if (currentSession == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Session not found.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }

            // Update the session status
            currentSession.setIsActive(status);
            sessionService.saveSession(currentSession);
        }
        Map <String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Modification done successfully.");

        return  new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateSessionStatus(@PathVariable int id, @RequestBody Map<String, Boolean> statusRequest) {
        // Get the status from the request body
        Boolean status = statusRequest.get("status");

        // If status is null, return bad request
        if (status == null) {
            return ResponseEntity.badRequest().body("Status must be provided.");
        }

        // Fetch the session by id
        Session currentSession = sessionService.getSessionById(id);
        if (currentSession == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found.");
        }

        // Update session status
        currentSession.setIsActive(status);
        sessionService.saveSession(currentSession);

        return new ResponseEntity<>(currentSession, HttpStatus.OK);
    }


}
