/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spontecorp.eduproject.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "actividad_extra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActividadExtra.findAll", query = "SELECT a FROM ActividadExtra a"),
    @NamedQuery(name = "ActividadExtra.findById", query = "SELECT a FROM ActividadExtra a WHERE a.id = :id"),
    @NamedQuery(name = "ActividadExtra.findByNombre", query = "SELECT a FROM ActividadExtra a WHERE a.nombre = :nombre")})
public class ActividadExtra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @JoinTable(name = "instituto_has_actividad_extra", joinColumns = {
        @JoinColumn(name = "actividad_extra_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "instituto_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Institucion> institucionCollection;

    public ActividadExtra() {
    }

    public ActividadExtra(Integer id) {
        this.id = id;
    }

    public ActividadExtra(Integer id, String nombre) {
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

    @XmlTransient
    public Collection<Institucion> getInstitucionCollection() {
        return institucionCollection;
    }

    public void setInstitucionCollection(Collection<Institucion> institucionCollection) {
        this.institucionCollection = institucionCollection;
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
        if (!(object instanceof ActividadExtra)) {
            return false;
        }
        ActividadExtra other = (ActividadExtra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.eduproject.entity.ActividadExtra[ id=" + id + " ]";
    }
    
}
