/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spontecorp.eduproject.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "institucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Institucion.findAll", query = "SELECT i FROM Institucion i"),
    @NamedQuery(name = "Institucion.findById", query = "SELECT i FROM Institucion i WHERE i.id = :id"),
    @NamedQuery(name = "Institucion.findByNombre", query = "SELECT i FROM Institucion i WHERE i.nombre = :nombre"),
    @NamedQuery(name = "Institucion.findByWebpage", query = "SELECT i FROM Institucion i WHERE i.webpage = :webpage"),
    @NamedQuery(name = "Institucion.findBySexo", query = "SELECT i FROM Institucion i WHERE i.sexo = :sexo"),
    @NamedQuery(name = "Institucion.findByLongitud", query = "SELECT i FROM Institucion i WHERE i.longitud = :longitud"),
    @NamedQuery(name = "Institucion.findByLatitud", query = "SELECT i FROM Institucion i WHERE i.latitud = :latitud"),
    @NamedQuery(name = "Institucion.findByInstitutocol", query = "SELECT i FROM Institucion i WHERE i.institutocol = :institutocol"),
    @NamedQuery(name = "Institucion.findByPublicoPrivado", query = "SELECT i FROM Institucion i WHERE i.publicoPrivado = :publicoPrivado"),
    @NamedQuery(name = "Institucion.findByCapacidad", query = "SELECT i FROM Institucion i WHERE i.capacidad = :capacidad"),
    @NamedQuery(name = "Institucion.findByFechaFundacion", query = "SELECT i FROM Institucion i WHERE i.fechaFundacion = :fechaFundacion"),
    @NamedQuery(name = "Institucion.findByCategoria", query = "SELECT i FROM Institucion i WHERE i.categoria = :categoria")})
public class Institucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "webpage")
    private String webpage;
    @Column(name = "sexo")
    private String sexo;
    @Lob
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "longitud")
    private String longitud;
    @Column(name = "latitud")
    private String latitud;
    @Column(name = "institutocol")
    private String institutocol;
    @Column(name = "publico_privado")
    private Integer publicoPrivado;
    @Column(name = "capacidad")
    private Integer capacidad;
    @Column(name = "fecha_fundacion")
    @Temporal(TemporalType.DATE)
    private Date fechaFundacion;
    @Column(name = "categoria")
    private Integer categoria;
    @ManyToMany(mappedBy = "institucionCollection")
    private Collection<Construccion> construccionCollection;
    @JoinTable(name = "instituto_has_turno", joinColumns = {
        @JoinColumn(name = "instituto_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "turno_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Turno> turnoCollection;
    @JoinTable(name = "instituto_has_servicio", joinColumns = {
        @JoinColumn(name = "instituto_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "servicio_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Servicio> servicioCollection;
    @ManyToMany(mappedBy = "institucionCollection")
    private Collection<ActividadExtra> actividadExtraCollection;
    @ManyToMany(mappedBy = "institucionCollection")
    private Collection<Deporte> deporteCollection;
    @JoinTable(name = "instituto_has_nivel", joinColumns = {
        @JoinColumn(name = "instituto_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "nivel_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Nivel> nivelCollection;
    @ManyToMany(mappedBy = "institucionCollection")
    private Collection<Idioma> idiomaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "institutoId")
    private Collection<Informacion> informacionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "institutoId")
    private Collection<Telefono> telefonoCollection;
    @JoinColumn(name = "zip_id", referencedColumnName = "id")
    @ManyToOne
    private Zip zipId;
    @JoinColumn(name = "tipo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tipo tipoId;
    @JoinColumn(name = "sector_id", referencedColumnName = "id")
    @ManyToOne
    private Sector sectorId;
    @JoinColumn(name = "religion_id", referencedColumnName = "id")
    @ManyToOne
    private Religion religionId;
    @JoinColumn(name = "ciudad_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Ciudad ciudadId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "institutoId")
    private Collection<Email> emailCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "institutoId")
    private Collection<Reconocimiento> reconocimientoCollection;

    public Institucion() {
    }

    public Institucion(Integer id) {
        this.id = id;
    }

    public Institucion(Integer id, String nombre) {
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

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getInstitutocol() {
        return institutocol;
    }

    public void setInstitutocol(String institutocol) {
        this.institutocol = institutocol;
    }

    public Integer getPublicoPrivado() {
        return publicoPrivado;
    }

    public void setPublicoPrivado(Integer publicoPrivado) {
        this.publicoPrivado = publicoPrivado;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Date getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(Date fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    @XmlTransient
    public Collection<Construccion> getConstruccionCollection() {
        return construccionCollection;
    }

    public void setConstruccionCollection(Collection<Construccion> construccionCollection) {
        this.construccionCollection = construccionCollection;
    }

    @XmlTransient
    public Collection<Turno> getTurnoCollection() {
        return turnoCollection;
    }

    public void setTurnoCollection(Collection<Turno> turnoCollection) {
        this.turnoCollection = turnoCollection;
    }

    @XmlTransient
    public Collection<Servicio> getServicioCollection() {
        return servicioCollection;
    }

    public void setServicioCollection(Collection<Servicio> servicioCollection) {
        this.servicioCollection = servicioCollection;
    }

    @XmlTransient
    public Collection<ActividadExtra> getActividadExtraCollection() {
        return actividadExtraCollection;
    }

    public void setActividadExtraCollection(Collection<ActividadExtra> actividadExtraCollection) {
        this.actividadExtraCollection = actividadExtraCollection;
    }

    @XmlTransient
    public Collection<Deporte> getDeporteCollection() {
        return deporteCollection;
    }

    public void setDeporteCollection(Collection<Deporte> deporteCollection) {
        this.deporteCollection = deporteCollection;
    }

    @XmlTransient
    public Collection<Nivel> getNivelCollection() {
        return nivelCollection;
    }

    public void setNivelCollection(Collection<Nivel> nivelCollection) {
        this.nivelCollection = nivelCollection;
    }

    @XmlTransient
    public Collection<Idioma> getIdiomaCollection() {
        return idiomaCollection;
    }

    public void setIdiomaCollection(Collection<Idioma> idiomaCollection) {
        this.idiomaCollection = idiomaCollection;
    }

    @XmlTransient
    public Collection<Informacion> getInformacionCollection() {
        return informacionCollection;
    }

    public void setInformacionCollection(Collection<Informacion> informacionCollection) {
        this.informacionCollection = informacionCollection;
    }

    @XmlTransient
    public Collection<Telefono> getTelefonoCollection() {
        return telefonoCollection;
    }

    public void setTelefonoCollection(Collection<Telefono> telefonoCollection) {
        this.telefonoCollection = telefonoCollection;
    }

    public Zip getZipId() {
        return zipId;
    }

    public void setZipId(Zip zipId) {
        this.zipId = zipId;
    }

    public Tipo getTipoId() {
        return tipoId;
    }

    public void setTipoId(Tipo tipoId) {
        this.tipoId = tipoId;
    }

    public Sector getSectorId() {
        return sectorId;
    }

    public void setSectorId(Sector sectorId) {
        this.sectorId = sectorId;
    }

    public Religion getReligionId() {
        return religionId;
    }

    public void setReligionId(Religion religionId) {
        this.religionId = religionId;
    }

    public Ciudad getCiudadId() {
        return ciudadId;
    }

    public void setCiudadId(Ciudad ciudadId) {
        this.ciudadId = ciudadId;
    }

    @XmlTransient
    public Collection<Email> getEmailCollection() {
        return emailCollection;
    }

    public void setEmailCollection(Collection<Email> emailCollection) {
        this.emailCollection = emailCollection;
    }

    @XmlTransient
    public Collection<Reconocimiento> getReconocimientoCollection() {
        return reconocimientoCollection;
    }

    public void setReconocimientoCollection(Collection<Reconocimiento> reconocimientoCollection) {
        this.reconocimientoCollection = reconocimientoCollection;
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
        if (!(object instanceof Institucion)) {
            return false;
        }
        Institucion other = (Institucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.eduproject.entity.Institucion[ id=" + id + " ]";
    }
    
}
