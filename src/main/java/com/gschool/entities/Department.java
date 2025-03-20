package com.gschool.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "timedrops")
    private String timedrops;
    // Add OneToMany relationship for sessions
    @OneToMany(mappedBy = "dep", fetch = FetchType.LAZY)  // Lazy fetching by default
    @JsonIgnoreProperties("dep") // Prevents infinite recursion (or you can use @JsonManagedReference and @JsonBackReference)
    private List<Session> sessions;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimedrops() {
        return timedrops;
    }

    public void setTimedrops(String timedrops) {
        this.timedrops = timedrops;
    }
    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

}