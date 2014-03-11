/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spontecorp.eduproject.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "reconocimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reconocimiento.findAll", query = "SELECT r FROM Reconocimiento r"),
    @NamedQuery(name = "Reconocimiento.findById", query = "SELECT r FROM Reconocimiento r WHERE r.id = :id"),
    @NamedQuery(name = "Reconocimiento.findByNombre", query = "SELECT r FROM Reconocimiento r WHERE r.nombre = :nombre")})
public class Reconocimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "instituto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Institucion institutoId;

    public Reconocimiento() {
    }

    public Reconocimiento(Integer id) {
        this.id = id;
    }

    public Reconocimiento(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Institucion getInstitutoId() {
        return institutoId;
    }

    public void setInstitutoId(Institucion institutoId) {
        this.institutoId = institutoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reconocimiento)) {
            return false;
        }
        Reconocimiento other = (Reconocimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.eduproject.entity.Reconocimiento[ id=" + id + " ]";
    }
    
}
