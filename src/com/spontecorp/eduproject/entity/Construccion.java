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
@Table(name = "construccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Construccion.findAll", query = "SELECT c FROM Construccion c"),
    @NamedQuery(name = "Construccion.findById", query = "SELECT c FROM Construccion c WHERE c.id = :id"),
    @NamedQuery(name = "Construccion.findByConstruccion", query = "SELECT c FROM Construccion c WHERE c.construccion = :construccion")})
public class Construccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "construccion")
    private String construccion;
    @JoinTable(name = "instituto_has_construccion", joinColumns = {
        @JoinColumn(name = "construccion_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "instituto_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Institucion> institucionCollection;

    public Construccion() {
    }

    public Construccion(Integer id) {
        this.id = id;
    }

    public Construccion(Integer id, String construccion) {
        this.id = id;
        this.construccion = construccion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConstruccion() {
        return construccion;
    }

    public void setConstruccion(String construccion) {
        this.construccion = construccion;
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
        if (!(object instanceof Construccion)) {
            return false;
        }
        Construccion other = (Construccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.eduproject.entity.Construccion[ id=" + id + " ]";
    }
    
}
