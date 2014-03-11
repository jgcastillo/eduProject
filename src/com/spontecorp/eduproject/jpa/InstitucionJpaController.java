/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spontecorp.eduproject.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.eduproject.entity.Zip;
import com.spontecorp.eduproject.entity.Tipo;
import com.spontecorp.eduproject.entity.Sector;
import com.spontecorp.eduproject.entity.Religion;
import com.spontecorp.eduproject.entity.Ciudad;
import com.spontecorp.eduproject.entity.Construccion;
import java.util.ArrayList;
import java.util.Collection;
import com.spontecorp.eduproject.entity.Turno;
import com.spontecorp.eduproject.entity.Servicio;
import com.spontecorp.eduproject.entity.ActividadExtra;
import com.spontecorp.eduproject.entity.Deporte;
import com.spontecorp.eduproject.entity.Nivel;
import com.spontecorp.eduproject.entity.Idioma;
import com.spontecorp.eduproject.entity.Informacion;
import com.spontecorp.eduproject.entity.Telefono;
import com.spontecorp.eduproject.entity.Email;
import com.spontecorp.eduproject.entity.Institucion;
import com.spontecorp.eduproject.entity.Reconocimiento;
import com.spontecorp.eduproject.jpa.exceptions.IllegalOrphanException;
import com.spontecorp.eduproject.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class InstitucionJpaController implements Serializable {

    public InstitucionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Institucion institucion) {
        if (institucion.getConstruccionCollection() == null) {
            institucion.setConstruccionCollection(new ArrayList<Construccion>());
        }
        if (institucion.getTurnoCollection() == null) {
            institucion.setTurnoCollection(new ArrayList<Turno>());
        }
        if (institucion.getServicioCollection() == null) {
            institucion.setServicioCollection(new ArrayList<Servicio>());
        }
        if (institucion.getActividadExtraCollection() == null) {
            institucion.setActividadExtraCollection(new ArrayList<ActividadExtra>());
        }
        if (institucion.getDeporteCollection() == null) {
            institucion.setDeporteCollection(new ArrayList<Deporte>());
        }
        if (institucion.getNivelCollection() == null) {
            institucion.setNivelCollection(new ArrayList<Nivel>());
        }
        if (institucion.getIdiomaCollection() == null) {
            institucion.setIdiomaCollection(new ArrayList<Idioma>());
        }
        if (institucion.getInformacionCollection() == null) {
            institucion.setInformacionCollection(new ArrayList<Informacion>());
        }
        if (institucion.getTelefonoCollection() == null) {
            institucion.setTelefonoCollection(new ArrayList<Telefono>());
        }
        if (institucion.getEmailCollection() == null) {
            institucion.setEmailCollection(new ArrayList<Email>());
        }
        if (institucion.getReconocimientoCollection() == null) {
            institucion.setReconocimientoCollection(new ArrayList<Reconocimiento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Zip zipId = institucion.getZipId();
            if (zipId != null) {
                zipId = em.getReference(zipId.getClass(), zipId.getId());
                institucion.setZipId(zipId);
            }
            Tipo tipoId = institucion.getTipoId();
            if (tipoId != null) {
                tipoId = em.getReference(tipoId.getClass(), tipoId.getId());
                institucion.setTipoId(tipoId);
            }
            Sector sectorId = institucion.getSectorId();
            if (sectorId != null) {
                sectorId = em.getReference(sectorId.getClass(), sectorId.getId());
                institucion.setSectorId(sectorId);
            }
            Religion religionId = institucion.getReligionId();
            if (religionId != null) {
                religionId = em.getReference(religionId.getClass(), religionId.getId());
                institucion.setReligionId(religionId);
            }
            Ciudad ciudadId = institucion.getCiudadId();
            if (ciudadId != null) {
                ciudadId = em.getReference(ciudadId.getClass(), ciudadId.getId());
                institucion.setCiudadId(ciudadId);
            }
            Collection<Construccion> attachedConstruccionCollection = new ArrayList<Construccion>();
            for (Construccion construccionCollectionConstruccionToAttach : institucion.getConstruccionCollection()) {
                construccionCollectionConstruccionToAttach = em.getReference(construccionCollectionConstruccionToAttach.getClass(), construccionCollectionConstruccionToAttach.getId());
                attachedConstruccionCollection.add(construccionCollectionConstruccionToAttach);
            }
            institucion.setConstruccionCollection(attachedConstruccionCollection);
            Collection<Turno> attachedTurnoCollection = new ArrayList<Turno>();
            for (Turno turnoCollectionTurnoToAttach : institucion.getTurnoCollection()) {
                turnoCollectionTurnoToAttach = em.getReference(turnoCollectionTurnoToAttach.getClass(), turnoCollectionTurnoToAttach.getId());
                attachedTurnoCollection.add(turnoCollectionTurnoToAttach);
            }
            institucion.setTurnoCollection(attachedTurnoCollection);
            Collection<Servicio> attachedServicioCollection = new ArrayList<Servicio>();
            for (Servicio servicioCollectionServicioToAttach : institucion.getServicioCollection()) {
                servicioCollectionServicioToAttach = em.getReference(servicioCollectionServicioToAttach.getClass(), servicioCollectionServicioToAttach.getId());
                attachedServicioCollection.add(servicioCollectionServicioToAttach);
            }
            institucion.setServicioCollection(attachedServicioCollection);
            Collection<ActividadExtra> attachedActividadExtraCollection = new ArrayList<ActividadExtra>();
            for (ActividadExtra actividadExtraCollectionActividadExtraToAttach : institucion.getActividadExtraCollection()) {
                actividadExtraCollectionActividadExtraToAttach = em.getReference(actividadExtraCollectionActividadExtraToAttach.getClass(), actividadExtraCollectionActividadExtraToAttach.getId());
                attachedActividadExtraCollection.add(actividadExtraCollectionActividadExtraToAttach);
            }
            institucion.setActividadExtraCollection(attachedActividadExtraCollection);
            Collection<Deporte> attachedDeporteCollection = new ArrayList<Deporte>();
            for (Deporte deporteCollectionDeporteToAttach : institucion.getDeporteCollection()) {
                deporteCollectionDeporteToAttach = em.getReference(deporteCollectionDeporteToAttach.getClass(), deporteCollectionDeporteToAttach.getId());
                attachedDeporteCollection.add(deporteCollectionDeporteToAttach);
            }
            institucion.setDeporteCollection(attachedDeporteCollection);
            Collection<Nivel> attachedNivelCollection = new ArrayList<Nivel>();
            for (Nivel nivelCollectionNivelToAttach : institucion.getNivelCollection()) {
                nivelCollectionNivelToAttach = em.getReference(nivelCollectionNivelToAttach.getClass(), nivelCollectionNivelToAttach.getId());
                attachedNivelCollection.add(nivelCollectionNivelToAttach);
            }
            institucion.setNivelCollection(attachedNivelCollection);
            Collection<Idioma> attachedIdiomaCollection = new ArrayList<Idioma>();
            for (Idioma idiomaCollectionIdiomaToAttach : institucion.getIdiomaCollection()) {
                idiomaCollectionIdiomaToAttach = em.getReference(idiomaCollectionIdiomaToAttach.getClass(), idiomaCollectionIdiomaToAttach.getId());
                attachedIdiomaCollection.add(idiomaCollectionIdiomaToAttach);
            }
            institucion.setIdiomaCollection(attachedIdiomaCollection);
            Collection<Informacion> attachedInformacionCollection = new ArrayList<Informacion>();
            for (Informacion informacionCollectionInformacionToAttach : institucion.getInformacionCollection()) {
                informacionCollectionInformacionToAttach = em.getReference(informacionCollectionInformacionToAttach.getClass(), informacionCollectionInformacionToAttach.getId());
                attachedInformacionCollection.add(informacionCollectionInformacionToAttach);
            }
            institucion.setInformacionCollection(attachedInformacionCollection);
            Collection<Telefono> attachedTelefonoCollection = new ArrayList<Telefono>();
            for (Telefono telefonoCollectionTelefonoToAttach : institucion.getTelefonoCollection()) {
                telefonoCollectionTelefonoToAttach = em.getReference(telefonoCollectionTelefonoToAttach.getClass(), telefonoCollectionTelefonoToAttach.getId());
                attachedTelefonoCollection.add(telefonoCollectionTelefonoToAttach);
            }
            institucion.setTelefonoCollection(attachedTelefonoCollection);
            Collection<Email> attachedEmailCollection = new ArrayList<Email>();
            for (Email emailCollectionEmailToAttach : institucion.getEmailCollection()) {
                emailCollectionEmailToAttach = em.getReference(emailCollectionEmailToAttach.getClass(), emailCollectionEmailToAttach.getId());
                attachedEmailCollection.add(emailCollectionEmailToAttach);
            }
            institucion.setEmailCollection(attachedEmailCollection);
            Collection<Reconocimiento> attachedReconocimientoCollection = new ArrayList<Reconocimiento>();
            for (Reconocimiento reconocimientoCollectionReconocimientoToAttach : institucion.getReconocimientoCollection()) {
                reconocimientoCollectionReconocimientoToAttach = em.getReference(reconocimientoCollectionReconocimientoToAttach.getClass(), reconocimientoCollectionReconocimientoToAttach.getId());
                attachedReconocimientoCollection.add(reconocimientoCollectionReconocimientoToAttach);
            }
            institucion.setReconocimientoCollection(attachedReconocimientoCollection);
            em.persist(institucion);
            if (zipId != null) {
                zipId.getInstitucionCollection().add(institucion);
                zipId = em.merge(zipId);
            }
            if (tipoId != null) {
                tipoId.getInstitucionCollection().add(institucion);
                tipoId = em.merge(tipoId);
            }
            if (sectorId != null) {
                sectorId.getInstitucionCollection().add(institucion);
                sectorId = em.merge(sectorId);
            }
            if (religionId != null) {
                religionId.getInstitucionCollection().add(institucion);
                religionId = em.merge(religionId);
            }
            if (ciudadId != null) {
                ciudadId.getInstitucionCollection().add(institucion);
                ciudadId = em.merge(ciudadId);
            }
            for (Construccion construccionCollectionConstruccion : institucion.getConstruccionCollection()) {
                construccionCollectionConstruccion.getInstitucionCollection().add(institucion);
                construccionCollectionConstruccion = em.merge(construccionCollectionConstruccion);
            }
            for (Turno turnoCollectionTurno : institucion.getTurnoCollection()) {
                turnoCollectionTurno.getInstitucionCollection().add(institucion);
                turnoCollectionTurno = em.merge(turnoCollectionTurno);
            }
            for (Servicio servicioCollectionServicio : institucion.getServicioCollection()) {
                servicioCollectionServicio.getInstitucionCollection().add(institucion);
                servicioCollectionServicio = em.merge(servicioCollectionServicio);
            }
            for (ActividadExtra actividadExtraCollectionActividadExtra : institucion.getActividadExtraCollection()) {
                actividadExtraCollectionActividadExtra.getInstitucionCollection().add(institucion);
                actividadExtraCollectionActividadExtra = em.merge(actividadExtraCollectionActividadExtra);
            }
            for (Deporte deporteCollectionDeporte : institucion.getDeporteCollection()) {
                deporteCollectionDeporte.getInstitucionCollection().add(institucion);
                deporteCollectionDeporte = em.merge(deporteCollectionDeporte);
            }
            for (Nivel nivelCollectionNivel : institucion.getNivelCollection()) {
                nivelCollectionNivel.getInstitucionCollection().add(institucion);
                nivelCollectionNivel = em.merge(nivelCollectionNivel);
            }
            for (Idioma idiomaCollectionIdioma : institucion.getIdiomaCollection()) {
                idiomaCollectionIdioma.getInstitucionCollection().add(institucion);
                idiomaCollectionIdioma = em.merge(idiomaCollectionIdioma);
            }
            for (Informacion informacionCollectionInformacion : institucion.getInformacionCollection()) {
                Institucion oldInstitutoIdOfInformacionCollectionInformacion = informacionCollectionInformacion.getInstitutoId();
                informacionCollectionInformacion.setInstitutoId(institucion);
                informacionCollectionInformacion = em.merge(informacionCollectionInformacion);
                if (oldInstitutoIdOfInformacionCollectionInformacion != null) {
                    oldInstitutoIdOfInformacionCollectionInformacion.getInformacionCollection().remove(informacionCollectionInformacion);
                    oldInstitutoIdOfInformacionCollectionInformacion = em.merge(oldInstitutoIdOfInformacionCollectionInformacion);
                }
            }
            for (Telefono telefonoCollectionTelefono : institucion.getTelefonoCollection()) {
                Institucion oldInstitutoIdOfTelefonoCollectionTelefono = telefonoCollectionTelefono.getInstitutoId();
                telefonoCollectionTelefono.setInstitutoId(institucion);
                telefonoCollectionTelefono = em.merge(telefonoCollectionTelefono);
                if (oldInstitutoIdOfTelefonoCollectionTelefono != null) {
                    oldInstitutoIdOfTelefonoCollectionTelefono.getTelefonoCollection().remove(telefonoCollectionTelefono);
                    oldInstitutoIdOfTelefonoCollectionTelefono = em.merge(oldInstitutoIdOfTelefonoCollectionTelefono);
                }
            }
            for (Email emailCollectionEmail : institucion.getEmailCollection()) {
                Institucion oldInstitutoIdOfEmailCollectionEmail = emailCollectionEmail.getInstitutoId();
                emailCollectionEmail.setInstitutoId(institucion);
                emailCollectionEmail = em.merge(emailCollectionEmail);
                if (oldInstitutoIdOfEmailCollectionEmail != null) {
                    oldInstitutoIdOfEmailCollectionEmail.getEmailCollection().remove(emailCollectionEmail);
                    oldInstitutoIdOfEmailCollectionEmail = em.merge(oldInstitutoIdOfEmailCollectionEmail);
                }
            }
            for (Reconocimiento reconocimientoCollectionReconocimiento : institucion.getReconocimientoCollection()) {
                Institucion oldInstitutoIdOfReconocimientoCollectionReconocimiento = reconocimientoCollectionReconocimiento.getInstitutoId();
                reconocimientoCollectionReconocimiento.setInstitutoId(institucion);
                reconocimientoCollectionReconocimiento = em.merge(reconocimientoCollectionReconocimiento);
                if (oldInstitutoIdOfReconocimientoCollectionReconocimiento != null) {
                    oldInstitutoIdOfReconocimientoCollectionReconocimiento.getReconocimientoCollection().remove(reconocimientoCollectionReconocimiento);
                    oldInstitutoIdOfReconocimientoCollectionReconocimiento = em.merge(oldInstitutoIdOfReconocimientoCollectionReconocimiento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Institucion institucion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Institucion persistentInstitucion = em.find(Institucion.class, institucion.getId());
            Zip zipIdOld = persistentInstitucion.getZipId();
            Zip zipIdNew = institucion.getZipId();
            Tipo tipoIdOld = persistentInstitucion.getTipoId();
            Tipo tipoIdNew = institucion.getTipoId();
            Sector sectorIdOld = persistentInstitucion.getSectorId();
            Sector sectorIdNew = institucion.getSectorId();
            Religion religionIdOld = persistentInstitucion.getReligionId();
            Religion religionIdNew = institucion.getReligionId();
            Ciudad ciudadIdOld = persistentInstitucion.getCiudadId();
            Ciudad ciudadIdNew = institucion.getCiudadId();
            Collection<Construccion> construccionCollectionOld = persistentInstitucion.getConstruccionCollection();
            Collection<Construccion> construccionCollectionNew = institucion.getConstruccionCollection();
            Collection<Turno> turnoCollectionOld = persistentInstitucion.getTurnoCollection();
            Collection<Turno> turnoCollectionNew = institucion.getTurnoCollection();
            Collection<Servicio> servicioCollectionOld = persistentInstitucion.getServicioCollection();
            Collection<Servicio> servicioCollectionNew = institucion.getServicioCollection();
            Collection<ActividadExtra> actividadExtraCollectionOld = persistentInstitucion.getActividadExtraCollection();
            Collection<ActividadExtra> actividadExtraCollectionNew = institucion.getActividadExtraCollection();
            Collection<Deporte> deporteCollectionOld = persistentInstitucion.getDeporteCollection();
            Collection<Deporte> deporteCollectionNew = institucion.getDeporteCollection();
            Collection<Nivel> nivelCollectionOld = persistentInstitucion.getNivelCollection();
            Collection<Nivel> nivelCollectionNew = institucion.getNivelCollection();
            Collection<Idioma> idiomaCollectionOld = persistentInstitucion.getIdiomaCollection();
            Collection<Idioma> idiomaCollectionNew = institucion.getIdiomaCollection();
            Collection<Informacion> informacionCollectionOld = persistentInstitucion.getInformacionCollection();
            Collection<Informacion> informacionCollectionNew = institucion.getInformacionCollection();
            Collection<Telefono> telefonoCollectionOld = persistentInstitucion.getTelefonoCollection();
            Collection<Telefono> telefonoCollectionNew = institucion.getTelefonoCollection();
            Collection<Email> emailCollectionOld = persistentInstitucion.getEmailCollection();
            Collection<Email> emailCollectionNew = institucion.getEmailCollection();
            Collection<Reconocimiento> reconocimientoCollectionOld = persistentInstitucion.getReconocimientoCollection();
            Collection<Reconocimiento> reconocimientoCollectionNew = institucion.getReconocimientoCollection();
            List<String> illegalOrphanMessages = null;
            for (Informacion informacionCollectionOldInformacion : informacionCollectionOld) {
                if (!informacionCollectionNew.contains(informacionCollectionOldInformacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Informacion " + informacionCollectionOldInformacion + " since its institutoId field is not nullable.");
                }
            }
            for (Telefono telefonoCollectionOldTelefono : telefonoCollectionOld) {
                if (!telefonoCollectionNew.contains(telefonoCollectionOldTelefono)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Telefono " + telefonoCollectionOldTelefono + " since its institutoId field is not nullable.");
                }
            }
            for (Email emailCollectionOldEmail : emailCollectionOld) {
                if (!emailCollectionNew.contains(emailCollectionOldEmail)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Email " + emailCollectionOldEmail + " since its institutoId field is not nullable.");
                }
            }
            for (Reconocimiento reconocimientoCollectionOldReconocimiento : reconocimientoCollectionOld) {
                if (!reconocimientoCollectionNew.contains(reconocimientoCollectionOldReconocimiento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reconocimiento " + reconocimientoCollectionOldReconocimiento + " since its institutoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (zipIdNew != null) {
                zipIdNew = em.getReference(zipIdNew.getClass(), zipIdNew.getId());
                institucion.setZipId(zipIdNew);
            }
            if (tipoIdNew != null) {
                tipoIdNew = em.getReference(tipoIdNew.getClass(), tipoIdNew.getId());
                institucion.setTipoId(tipoIdNew);
            }
            if (sectorIdNew != null) {
                sectorIdNew = em.getReference(sectorIdNew.getClass(), sectorIdNew.getId());
                institucion.setSectorId(sectorIdNew);
            }
            if (religionIdNew != null) {
                religionIdNew = em.getReference(religionIdNew.getClass(), religionIdNew.getId());
                institucion.setReligionId(religionIdNew);
            }
            if (ciudadIdNew != null) {
                ciudadIdNew = em.getReference(ciudadIdNew.getClass(), ciudadIdNew.getId());
                institucion.setCiudadId(ciudadIdNew);
            }
            Collection<Construccion> attachedConstruccionCollectionNew = new ArrayList<Construccion>();
            for (Construccion construccionCollectionNewConstruccionToAttach : construccionCollectionNew) {
                construccionCollectionNewConstruccionToAttach = em.getReference(construccionCollectionNewConstruccionToAttach.getClass(), construccionCollectionNewConstruccionToAttach.getId());
                attachedConstruccionCollectionNew.add(construccionCollectionNewConstruccionToAttach);
            }
            construccionCollectionNew = attachedConstruccionCollectionNew;
            institucion.setConstruccionCollection(construccionCollectionNew);
            Collection<Turno> attachedTurnoCollectionNew = new ArrayList<Turno>();
            for (Turno turnoCollectionNewTurnoToAttach : turnoCollectionNew) {
                turnoCollectionNewTurnoToAttach = em.getReference(turnoCollectionNewTurnoToAttach.getClass(), turnoCollectionNewTurnoToAttach.getId());
                attachedTurnoCollectionNew.add(turnoCollectionNewTurnoToAttach);
            }
            turnoCollectionNew = attachedTurnoCollectionNew;
            institucion.setTurnoCollection(turnoCollectionNew);
            Collection<Servicio> attachedServicioCollectionNew = new ArrayList<Servicio>();
            for (Servicio servicioCollectionNewServicioToAttach : servicioCollectionNew) {
                servicioCollectionNewServicioToAttach = em.getReference(servicioCollectionNewServicioToAttach.getClass(), servicioCollectionNewServicioToAttach.getId());
                attachedServicioCollectionNew.add(servicioCollectionNewServicioToAttach);
            }
            servicioCollectionNew = attachedServicioCollectionNew;
            institucion.setServicioCollection(servicioCollectionNew);
            Collection<ActividadExtra> attachedActividadExtraCollectionNew = new ArrayList<ActividadExtra>();
            for (ActividadExtra actividadExtraCollectionNewActividadExtraToAttach : actividadExtraCollectionNew) {
                actividadExtraCollectionNewActividadExtraToAttach = em.getReference(actividadExtraCollectionNewActividadExtraToAttach.getClass(), actividadExtraCollectionNewActividadExtraToAttach.getId());
                attachedActividadExtraCollectionNew.add(actividadExtraCollectionNewActividadExtraToAttach);
            }
            actividadExtraCollectionNew = attachedActividadExtraCollectionNew;
            institucion.setActividadExtraCollection(actividadExtraCollectionNew);
            Collection<Deporte> attachedDeporteCollectionNew = new ArrayList<Deporte>();
            for (Deporte deporteCollectionNewDeporteToAttach : deporteCollectionNew) {
                deporteCollectionNewDeporteToAttach = em.getReference(deporteCollectionNewDeporteToAttach.getClass(), deporteCollectionNewDeporteToAttach.getId());
                attachedDeporteCollectionNew.add(deporteCollectionNewDeporteToAttach);
            }
            deporteCollectionNew = attachedDeporteCollectionNew;
            institucion.setDeporteCollection(deporteCollectionNew);
            Collection<Nivel> attachedNivelCollectionNew = new ArrayList<Nivel>();
            for (Nivel nivelCollectionNewNivelToAttach : nivelCollectionNew) {
                nivelCollectionNewNivelToAttach = em.getReference(nivelCollectionNewNivelToAttach.getClass(), nivelCollectionNewNivelToAttach.getId());
                attachedNivelCollectionNew.add(nivelCollectionNewNivelToAttach);
            }
            nivelCollectionNew = attachedNivelCollectionNew;
            institucion.setNivelCollection(nivelCollectionNew);
            Collection<Idioma> attachedIdiomaCollectionNew = new ArrayList<Idioma>();
            for (Idioma idiomaCollectionNewIdiomaToAttach : idiomaCollectionNew) {
                idiomaCollectionNewIdiomaToAttach = em.getReference(idiomaCollectionNewIdiomaToAttach.getClass(), idiomaCollectionNewIdiomaToAttach.getId());
                attachedIdiomaCollectionNew.add(idiomaCollectionNewIdiomaToAttach);
            }
            idiomaCollectionNew = attachedIdiomaCollectionNew;
            institucion.setIdiomaCollection(idiomaCollectionNew);
            Collection<Informacion> attachedInformacionCollectionNew = new ArrayList<Informacion>();
            for (Informacion informacionCollectionNewInformacionToAttach : informacionCollectionNew) {
                informacionCollectionNewInformacionToAttach = em.getReference(informacionCollectionNewInformacionToAttach.getClass(), informacionCollectionNewInformacionToAttach.getId());
                attachedInformacionCollectionNew.add(informacionCollectionNewInformacionToAttach);
            }
            informacionCollectionNew = attachedInformacionCollectionNew;
            institucion.setInformacionCollection(informacionCollectionNew);
            Collection<Telefono> attachedTelefonoCollectionNew = new ArrayList<Telefono>();
            for (Telefono telefonoCollectionNewTelefonoToAttach : telefonoCollectionNew) {
                telefonoCollectionNewTelefonoToAttach = em.getReference(telefonoCollectionNewTelefonoToAttach.getClass(), telefonoCollectionNewTelefonoToAttach.getId());
                attachedTelefonoCollectionNew.add(telefonoCollectionNewTelefonoToAttach);
            }
            telefonoCollectionNew = attachedTelefonoCollectionNew;
            institucion.setTelefonoCollection(telefonoCollectionNew);
            Collection<Email> attachedEmailCollectionNew = new ArrayList<Email>();
            for (Email emailCollectionNewEmailToAttach : emailCollectionNew) {
                emailCollectionNewEmailToAttach = em.getReference(emailCollectionNewEmailToAttach.getClass(), emailCollectionNewEmailToAttach.getId());
                attachedEmailCollectionNew.add(emailCollectionNewEmailToAttach);
            }
            emailCollectionNew = attachedEmailCollectionNew;
            institucion.setEmailCollection(emailCollectionNew);
            Collection<Reconocimiento> attachedReconocimientoCollectionNew = new ArrayList<Reconocimiento>();
            for (Reconocimiento reconocimientoCollectionNewReconocimientoToAttach : reconocimientoCollectionNew) {
                reconocimientoCollectionNewReconocimientoToAttach = em.getReference(reconocimientoCollectionNewReconocimientoToAttach.getClass(), reconocimientoCollectionNewReconocimientoToAttach.getId());
                attachedReconocimientoCollectionNew.add(reconocimientoCollectionNewReconocimientoToAttach);
            }
            reconocimientoCollectionNew = attachedReconocimientoCollectionNew;
            institucion.setReconocimientoCollection(reconocimientoCollectionNew);
            institucion = em.merge(institucion);
            if (zipIdOld != null && !zipIdOld.equals(zipIdNew)) {
                zipIdOld.getInstitucionCollection().remove(institucion);
                zipIdOld = em.merge(zipIdOld);
            }
            if (zipIdNew != null && !zipIdNew.equals(zipIdOld)) {
                zipIdNew.getInstitucionCollection().add(institucion);
                zipIdNew = em.merge(zipIdNew);
            }
            if (tipoIdOld != null && !tipoIdOld.equals(tipoIdNew)) {
                tipoIdOld.getInstitucionCollection().remove(institucion);
                tipoIdOld = em.merge(tipoIdOld);
            }
            if (tipoIdNew != null && !tipoIdNew.equals(tipoIdOld)) {
                tipoIdNew.getInstitucionCollection().add(institucion);
                tipoIdNew = em.merge(tipoIdNew);
            }
            if (sectorIdOld != null && !sectorIdOld.equals(sectorIdNew)) {
                sectorIdOld.getInstitucionCollection().remove(institucion);
                sectorIdOld = em.merge(sectorIdOld);
            }
            if (sectorIdNew != null && !sectorIdNew.equals(sectorIdOld)) {
                sectorIdNew.getInstitucionCollection().add(institucion);
                sectorIdNew = em.merge(sectorIdNew);
            }
            if (religionIdOld != null && !religionIdOld.equals(religionIdNew)) {
                religionIdOld.getInstitucionCollection().remove(institucion);
                religionIdOld = em.merge(religionIdOld);
            }
            if (religionIdNew != null && !religionIdNew.equals(religionIdOld)) {
                religionIdNew.getInstitucionCollection().add(institucion);
                religionIdNew = em.merge(religionIdNew);
            }
            if (ciudadIdOld != null && !ciudadIdOld.equals(ciudadIdNew)) {
                ciudadIdOld.getInstitucionCollection().remove(institucion);
                ciudadIdOld = em.merge(ciudadIdOld);
            }
            if (ciudadIdNew != null && !ciudadIdNew.equals(ciudadIdOld)) {
                ciudadIdNew.getInstitucionCollection().add(institucion);
                ciudadIdNew = em.merge(ciudadIdNew);
            }
            for (Construccion construccionCollectionOldConstruccion : construccionCollectionOld) {
                if (!construccionCollectionNew.contains(construccionCollectionOldConstruccion)) {
                    construccionCollectionOldConstruccion.getInstitucionCollection().remove(institucion);
                    construccionCollectionOldConstruccion = em.merge(construccionCollectionOldConstruccion);
                }
            }
            for (Construccion construccionCollectionNewConstruccion : construccionCollectionNew) {
                if (!construccionCollectionOld.contains(construccionCollectionNewConstruccion)) {
                    construccionCollectionNewConstruccion.getInstitucionCollection().add(institucion);
                    construccionCollectionNewConstruccion = em.merge(construccionCollectionNewConstruccion);
                }
            }
            for (Turno turnoCollectionOldTurno : turnoCollectionOld) {
                if (!turnoCollectionNew.contains(turnoCollectionOldTurno)) {
                    turnoCollectionOldTurno.getInstitucionCollection().remove(institucion);
                    turnoCollectionOldTurno = em.merge(turnoCollectionOldTurno);
                }
            }
            for (Turno turnoCollectionNewTurno : turnoCollectionNew) {
                if (!turnoCollectionOld.contains(turnoCollectionNewTurno)) {
                    turnoCollectionNewTurno.getInstitucionCollection().add(institucion);
                    turnoCollectionNewTurno = em.merge(turnoCollectionNewTurno);
                }
            }
            for (Servicio servicioCollectionOldServicio : servicioCollectionOld) {
                if (!servicioCollectionNew.contains(servicioCollectionOldServicio)) {
                    servicioCollectionOldServicio.getInstitucionCollection().remove(institucion);
                    servicioCollectionOldServicio = em.merge(servicioCollectionOldServicio);
                }
            }
            for (Servicio servicioCollectionNewServicio : servicioCollectionNew) {
                if (!servicioCollectionOld.contains(servicioCollectionNewServicio)) {
                    servicioCollectionNewServicio.getInstitucionCollection().add(institucion);
                    servicioCollectionNewServicio = em.merge(servicioCollectionNewServicio);
                }
            }
            for (ActividadExtra actividadExtraCollectionOldActividadExtra : actividadExtraCollectionOld) {
                if (!actividadExtraCollectionNew.contains(actividadExtraCollectionOldActividadExtra)) {
                    actividadExtraCollectionOldActividadExtra.getInstitucionCollection().remove(institucion);
                    actividadExtraCollectionOldActividadExtra = em.merge(actividadExtraCollectionOldActividadExtra);
                }
            }
            for (ActividadExtra actividadExtraCollectionNewActividadExtra : actividadExtraCollectionNew) {
                if (!actividadExtraCollectionOld.contains(actividadExtraCollectionNewActividadExtra)) {
                    actividadExtraCollectionNewActividadExtra.getInstitucionCollection().add(institucion);
                    actividadExtraCollectionNewActividadExtra = em.merge(actividadExtraCollectionNewActividadExtra);
                }
            }
            for (Deporte deporteCollectionOldDeporte : deporteCollectionOld) {
                if (!deporteCollectionNew.contains(deporteCollectionOldDeporte)) {
                    deporteCollectionOldDeporte.getInstitucionCollection().remove(institucion);
                    deporteCollectionOldDeporte = em.merge(deporteCollectionOldDeporte);
                }
            }
            for (Deporte deporteCollectionNewDeporte : deporteCollectionNew) {
                if (!deporteCollectionOld.contains(deporteCollectionNewDeporte)) {
                    deporteCollectionNewDeporte.getInstitucionCollection().add(institucion);
                    deporteCollectionNewDeporte = em.merge(deporteCollectionNewDeporte);
                }
            }
            for (Nivel nivelCollectionOldNivel : nivelCollectionOld) {
                if (!nivelCollectionNew.contains(nivelCollectionOldNivel)) {
                    nivelCollectionOldNivel.getInstitucionCollection().remove(institucion);
                    nivelCollectionOldNivel = em.merge(nivelCollectionOldNivel);
                }
            }
            for (Nivel nivelCollectionNewNivel : nivelCollectionNew) {
                if (!nivelCollectionOld.contains(nivelCollectionNewNivel)) {
                    nivelCollectionNewNivel.getInstitucionCollection().add(institucion);
                    nivelCollectionNewNivel = em.merge(nivelCollectionNewNivel);
                }
            }
            for (Idioma idiomaCollectionOldIdioma : idiomaCollectionOld) {
                if (!idiomaCollectionNew.contains(idiomaCollectionOldIdioma)) {
                    idiomaCollectionOldIdioma.getInstitucionCollection().remove(institucion);
                    idiomaCollectionOldIdioma = em.merge(idiomaCollectionOldIdioma);
                }
            }
            for (Idioma idiomaCollectionNewIdioma : idiomaCollectionNew) {
                if (!idiomaCollectionOld.contains(idiomaCollectionNewIdioma)) {
                    idiomaCollectionNewIdioma.getInstitucionCollection().add(institucion);
                    idiomaCollectionNewIdioma = em.merge(idiomaCollectionNewIdioma);
                }
            }
            for (Informacion informacionCollectionNewInformacion : informacionCollectionNew) {
                if (!informacionCollectionOld.contains(informacionCollectionNewInformacion)) {
                    Institucion oldInstitutoIdOfInformacionCollectionNewInformacion = informacionCollectionNewInformacion.getInstitutoId();
                    informacionCollectionNewInformacion.setInstitutoId(institucion);
                    informacionCollectionNewInformacion = em.merge(informacionCollectionNewInformacion);
                    if (oldInstitutoIdOfInformacionCollectionNewInformacion != null && !oldInstitutoIdOfInformacionCollectionNewInformacion.equals(institucion)) {
                        oldInstitutoIdOfInformacionCollectionNewInformacion.getInformacionCollection().remove(informacionCollectionNewInformacion);
                        oldInstitutoIdOfInformacionCollectionNewInformacion = em.merge(oldInstitutoIdOfInformacionCollectionNewInformacion);
                    }
                }
            }
            for (Telefono telefonoCollectionNewTelefono : telefonoCollectionNew) {
                if (!telefonoCollectionOld.contains(telefonoCollectionNewTelefono)) {
                    Institucion oldInstitutoIdOfTelefonoCollectionNewTelefono = telefonoCollectionNewTelefono.getInstitutoId();
                    telefonoCollectionNewTelefono.setInstitutoId(institucion);
                    telefonoCollectionNewTelefono = em.merge(telefonoCollectionNewTelefono);
                    if (oldInstitutoIdOfTelefonoCollectionNewTelefono != null && !oldInstitutoIdOfTelefonoCollectionNewTelefono.equals(institucion)) {
                        oldInstitutoIdOfTelefonoCollectionNewTelefono.getTelefonoCollection().remove(telefonoCollectionNewTelefono);
                        oldInstitutoIdOfTelefonoCollectionNewTelefono = em.merge(oldInstitutoIdOfTelefonoCollectionNewTelefono);
                    }
                }
            }
            for (Email emailCollectionNewEmail : emailCollectionNew) {
                if (!emailCollectionOld.contains(emailCollectionNewEmail)) {
                    Institucion oldInstitutoIdOfEmailCollectionNewEmail = emailCollectionNewEmail.getInstitutoId();
                    emailCollectionNewEmail.setInstitutoId(institucion);
                    emailCollectionNewEmail = em.merge(emailCollectionNewEmail);
                    if (oldInstitutoIdOfEmailCollectionNewEmail != null && !oldInstitutoIdOfEmailCollectionNewEmail.equals(institucion)) {
                        oldInstitutoIdOfEmailCollectionNewEmail.getEmailCollection().remove(emailCollectionNewEmail);
                        oldInstitutoIdOfEmailCollectionNewEmail = em.merge(oldInstitutoIdOfEmailCollectionNewEmail);
                    }
                }
            }
            for (Reconocimiento reconocimientoCollectionNewReconocimiento : reconocimientoCollectionNew) {
                if (!reconocimientoCollectionOld.contains(reconocimientoCollectionNewReconocimiento)) {
                    Institucion oldInstitutoIdOfReconocimientoCollectionNewReconocimiento = reconocimientoCollectionNewReconocimiento.getInstitutoId();
                    reconocimientoCollectionNewReconocimiento.setInstitutoId(institucion);
                    reconocimientoCollectionNewReconocimiento = em.merge(reconocimientoCollectionNewReconocimiento);
                    if (oldInstitutoIdOfReconocimientoCollectionNewReconocimiento != null && !oldInstitutoIdOfReconocimientoCollectionNewReconocimiento.equals(institucion)) {
                        oldInstitutoIdOfReconocimientoCollectionNewReconocimiento.getReconocimientoCollection().remove(reconocimientoCollectionNewReconocimiento);
                        oldInstitutoIdOfReconocimientoCollectionNewReconocimiento = em.merge(oldInstitutoIdOfReconocimientoCollectionNewReconocimiento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = institucion.getId();
                if (findInstitucion(id) == null) {
                    throw new NonexistentEntityException("The institucion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Institucion institucion;
            try {
                institucion = em.getReference(Institucion.class, id);
                institucion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The institucion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Informacion> informacionCollectionOrphanCheck = institucion.getInformacionCollection();
            for (Informacion informacionCollectionOrphanCheckInformacion : informacionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Institucion (" + institucion + ") cannot be destroyed since the Informacion " + informacionCollectionOrphanCheckInformacion + " in its informacionCollection field has a non-nullable institutoId field.");
            }
            Collection<Telefono> telefonoCollectionOrphanCheck = institucion.getTelefonoCollection();
            for (Telefono telefonoCollectionOrphanCheckTelefono : telefonoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Institucion (" + institucion + ") cannot be destroyed since the Telefono " + telefonoCollectionOrphanCheckTelefono + " in its telefonoCollection field has a non-nullable institutoId field.");
            }
            Collection<Email> emailCollectionOrphanCheck = institucion.getEmailCollection();
            for (Email emailCollectionOrphanCheckEmail : emailCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Institucion (" + institucion + ") cannot be destroyed since the Email " + emailCollectionOrphanCheckEmail + " in its emailCollection field has a non-nullable institutoId field.");
            }
            Collection<Reconocimiento> reconocimientoCollectionOrphanCheck = institucion.getReconocimientoCollection();
            for (Reconocimiento reconocimientoCollectionOrphanCheckReconocimiento : reconocimientoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Institucion (" + institucion + ") cannot be destroyed since the Reconocimiento " + reconocimientoCollectionOrphanCheckReconocimiento + " in its reconocimientoCollection field has a non-nullable institutoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Zip zipId = institucion.getZipId();
            if (zipId != null) {
                zipId.getInstitucionCollection().remove(institucion);
                zipId = em.merge(zipId);
            }
            Tipo tipoId = institucion.getTipoId();
            if (tipoId != null) {
                tipoId.getInstitucionCollection().remove(institucion);
                tipoId = em.merge(tipoId);
            }
            Sector sectorId = institucion.getSectorId();
            if (sectorId != null) {
                sectorId.getInstitucionCollection().remove(institucion);
                sectorId = em.merge(sectorId);
            }
            Religion religionId = institucion.getReligionId();
            if (religionId != null) {
                religionId.getInstitucionCollection().remove(institucion);
                religionId = em.merge(religionId);
            }
            Ciudad ciudadId = institucion.getCiudadId();
            if (ciudadId != null) {
                ciudadId.getInstitucionCollection().remove(institucion);
                ciudadId = em.merge(ciudadId);
            }
            Collection<Construccion> construccionCollection = institucion.getConstruccionCollection();
            for (Construccion construccionCollectionConstruccion : construccionCollection) {
                construccionCollectionConstruccion.getInstitucionCollection().remove(institucion);
                construccionCollectionConstruccion = em.merge(construccionCollectionConstruccion);
            }
            Collection<Turno> turnoCollection = institucion.getTurnoCollection();
            for (Turno turnoCollectionTurno : turnoCollection) {
                turnoCollectionTurno.getInstitucionCollection().remove(institucion);
                turnoCollectionTurno = em.merge(turnoCollectionTurno);
            }
            Collection<Servicio> servicioCollection = institucion.getServicioCollection();
            for (Servicio servicioCollectionServicio : servicioCollection) {
                servicioCollectionServicio.getInstitucionCollection().remove(institucion);
                servicioCollectionServicio = em.merge(servicioCollectionServicio);
            }
            Collection<ActividadExtra> actividadExtraCollection = institucion.getActividadExtraCollection();
            for (ActividadExtra actividadExtraCollectionActividadExtra : actividadExtraCollection) {
                actividadExtraCollectionActividadExtra.getInstitucionCollection().remove(institucion);
                actividadExtraCollectionActividadExtra = em.merge(actividadExtraCollectionActividadExtra);
            }
            Collection<Deporte> deporteCollection = institucion.getDeporteCollection();
            for (Deporte deporteCollectionDeporte : deporteCollection) {
                deporteCollectionDeporte.getInstitucionCollection().remove(institucion);
                deporteCollectionDeporte = em.merge(deporteCollectionDeporte);
            }
            Collection<Nivel> nivelCollection = institucion.getNivelCollection();
            for (Nivel nivelCollectionNivel : nivelCollection) {
                nivelCollectionNivel.getInstitucionCollection().remove(institucion);
                nivelCollectionNivel = em.merge(nivelCollectionNivel);
            }
            Collection<Idioma> idiomaCollection = institucion.getIdiomaCollection();
            for (Idioma idiomaCollectionIdioma : idiomaCollection) {
                idiomaCollectionIdioma.getInstitucionCollection().remove(institucion);
                idiomaCollectionIdioma = em.merge(idiomaCollectionIdioma);
            }
            em.remove(institucion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Institucion> findInstitucionEntities() {
        return findInstitucionEntities(true, -1, -1);
    }

    public List<Institucion> findInstitucionEntities(int maxResults, int firstResult) {
        return findInstitucionEntities(false, maxResults, firstResult);
    }

    private List<Institucion> findInstitucionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Institucion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Institucion findInstitucion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Institucion.class, id);
        } finally {
            em.close();
        }
    }

    public int getInstitucionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Institucion> rt = cq.from(Institucion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
