package com.gschool.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "filiere")
public class Filiere {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;

    @ColumnDefault("0")
    @Column(name = "nbr_etudiant")
    private Integer nbrEtudiant;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getNbrEtudiant() {
        return nbrEtudiant;
    }

    public void setNbrEtudiant(Integer nbrEtudiant) {
        this.nbrEtudiant = nbrEtudiant;
    }

}