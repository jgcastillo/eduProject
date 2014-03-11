/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spontecorp.eduproject.jpa;

import com.spontecorp.eduproject.entity.Ciudad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.eduproject.entity.Estado;
import com.spontecorp.eduproject.entity.Sector;
import java.util.ArrayList;
import java.util.Collection;
import com.spontecorp.eduproject.entity.Zip;
import com.spontecorp.eduproject.entity.Institucion;
import com.spontecorp.eduproject.jpa.exceptions.IllegalOrphanException;
import com.spontecorp.eduproject.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class CiudadJpaController implements Serializable {

    public CiudadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ciudad ciudad) {
        if (ciudad.getSectorCollection() == null) {
            ciudad.setSectorCollection(new ArrayList<Sector>());
        }
        if (ciudad.getZipCollection() == null) {
            ciudad.setZipCollection(new ArrayList<Zip>());
        }
        if (ciudad.getInstitucionCollection() == null) {
            ciudad.setInstitucionCollection(new ArrayList<Institucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado estadoId = ciudad.getEstadoId();
            if (estadoId != null) {
                estadoId = em.getReference(estadoId.getClass(), estadoId.getId());
                ciudad.setEstadoId(estadoId);
            }
            Collection<Sector> attachedSectorCollection = new ArrayList<Sector>();
            for (Sector sectorCollectionSectorToAttach : ciudad.getSectorCollection()) {
                sectorCollectionSectorToAttach = em.getReference(sectorCollectionSectorToAttach.getClass(), sectorCollectionSectorToAttach.getId());
                attachedSectorCollection.add(sectorCollectionSectorToAttach);
            }
            ciudad.setSectorCollection(attachedSectorCollection);
            Collection<Zip> attachedZipCollection = new ArrayList<Zip>();
            for (Zip zipCollectionZipToAttach : ciudad.getZipCollection()) {
                zipCollectionZipToAttach = em.getReference(zipCollectionZipToAttach.getClass(), zipCollectionZipToAttach.getId());
                attachedZipCollection.add(zipCollectionZipToAttach);
            }
            ciudad.setZipCollection(attachedZipCollection);
            Collection<Institucion> attachedInstitucionCollection = new ArrayList<Institucion>();
            for (Institucion institucionCollectionInstitucionToAttach : ciudad.getInstitucionCollection()) {
                institucionCollectionInstitucionToAttach = em.getReference(institucionCollectionInstitucionToAttach.getClass(), institucionCollectionInstitucionToAttach.getId());
                attachedInstitucionCollection.add(institucionCollectionInstitucionToAttach);
            }
            ciudad.setInstitucionCollection(attachedInstitucionCollection);
            em.persist(ciudad);
            if (estadoId != null) {
                estadoId.getCiudadCollection().add(ciudad);
                estadoId = em.merge(estadoId);
            }
            for (Sector sectorCollectionSector : ciudad.getSectorCollection()) {
                Ciudad oldCiudadIdOfSectorCollectionSector = sectorCollectionSector.getCiudadId();
                sectorCollectionSector.setCiudadId(ciudad);
                sectorCollectionSector = em.merge(sectorCollectionSector);
                if (oldCiudadIdOfSectorCollectionSector != null) {
                    oldCiudadIdOfSectorCollectionSector.getSectorCollection().remove(sectorCollectionSector);
                    oldCiudadIdOfSectorCollectionSector = em.merge(oldCiudadIdOfSectorCollectionSector);
                }
            }
            for (Zip zipCollectionZip : ciudad.getZipCollection()) {
                Ciudad oldCiudadIdOfZipCollectionZip = zipCollectionZip.getCiudadId();
                zipCollectionZip.setCiudadId(ciudad);
                zipCollectionZip = em.merge(zipCollectionZip);
                if (oldCiudadIdOfZipCollectionZip != null) {
                    oldCiudadIdOfZipCollectionZip.getZipCollection().remove(zipCollectionZip);
                    oldCiudadIdOfZipCollectionZip = em.merge(oldCiudadIdOfZipCollectionZip);
                }
            }
            for (Institucion institucionCollectionInstitucion : ciudad.getInstitucionCollection()) {
                Ciudad oldCiudadIdOfInstitucionCollectionInstitucion = institucionCollectionInstitucion.getCiudadId();
                institucionCollectionInstitucion.setCiudadId(ciudad);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
                if (oldCiudadIdOfInstitucionCollectionInstitucion != null) {
                    oldCiudadIdOfInstitucionCollectionInstitucion.getInstitucionCollection().remove(institucionCollectionInstitucion);
                    oldCiudadIdOfInstitucionCollectionInstitucion = em.merge(oldCiudadIdOfInstitucionCollectionInstitucion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciudad ciudad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad persistentCiudad = em.find(Ciudad.class, ciudad.getId());
            Estado estadoIdOld = persistentCiudad.getEstadoId();
            Estado estadoIdNew = ciudad.getEstadoId();
            Collection<Sector> sectorCollectionOld = persistentCiudad.getSectorCollection();
            Collection<Sector> sectorCollectionNew = ciudad.getSectorCollection();
            Collection<Zip> zipCollectionOld = persistentCiudad.getZipCollection();
            Collection<Zip> zipCollectionNew = ciudad.getZipCollection();
            Collection<Institucion> institucionCollectionOld = persistentCiudad.getInstitucionCollection();
            Collection<Institucion> institucionCollectionNew = ciudad.getInstitucionCollection();
            List<String> illegalOrphanMessages = null;
            for (Sector sectorCollectionOldSector : sectorCollectionOld) {
                if (!sectorCollectionNew.contains(sectorCollectionOldSector)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sector " + sectorCollectionOldSector + " since its ciudadId field is not nullable.");
                }
            }
            for (Institucion institucionCollectionOldInstitucion : institucionCollectionOld) {
                if (!institucionCollectionNew.contains(institucionCollectionOldInstitucion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Institucion " + institucionCollectionOldInstitucion + " since its ciudadId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (estadoIdNew != null) {
                estadoIdNew = em.getReference(estadoIdNew.getClass(), estadoIdNew.getId());
                ciudad.setEstadoId(estadoIdNew);
            }
            Collection<Sector> attachedSectorCollectionNew = new ArrayList<Sector>();
            for (Sector sectorCollectionNewSectorToAttach : sectorCollectionNew) {
                sectorCollectionNewSectorToAttach = em.getReference(sectorCollectionNewSectorToAttach.getClass(), sectorCollectionNewSectorToAttach.getId());
                attachedSectorCollectionNew.add(sectorCollectionNewSectorToAttach);
            }
            sectorCollectionNew = attachedSectorCollectionNew;
            ciudad.setSectorCollection(sectorCollectionNew);
            Collection<Zip> attachedZipCollectionNew = new ArrayList<Zip>();
            for (Zip zipCollectionNewZipToAttach : zipCollectionNew) {
                zipCollectionNewZipToAttach = em.getReference(zipCollectionNewZipToAttach.getClass(), zipCollectionNewZipToAttach.getId());
                attachedZipCollectionNew.add(zipCollectionNewZipToAttach);
            }
            zipCollectionNew = attachedZipCollectionNew;
            ciudad.setZipCollection(zipCollectionNew);
            Collection<Institucion> attachedInstitucionCollectionNew = new ArrayList<Institucion>();
            for (Institucion institucionCollectionNewInstitucionToAttach : institucionCollectionNew) {
                institucionCollectionNewInstitucionToAttach = em.getReference(institucionCollectionNewInstitucionToAttach.getClass(), institucionCollectionNewInstitucionToAttach.getId());
                attachedInstitucionCollectionNew.add(institucionCollectionNewInstitucionToAttach);
            }
            institucionCollectionNew = attachedInstitucionCollectionNew;
            ciudad.setInstitucionCollection(institucionCollectionNew);
            ciudad = em.merge(ciudad);
            if (estadoIdOld != null && !estadoIdOld.equals(estadoIdNew)) {
                estadoIdOld.getCiudadCollection().remove(ciudad);
                estadoIdOld = em.merge(estadoIdOld);
            }
            if (estadoIdNew != null && !estadoIdNew.equals(estadoIdOld)) {
                estadoIdNew.getCiudadCollection().add(ciudad);
                estadoIdNew = em.merge(estadoIdNew);
            }
            for (Sector sectorCollectionNewSector : sectorCollectionNew) {
                if (!sectorCollectionOld.contains(sectorCollectionNewSector)) {
                    Ciudad oldCiudadIdOfSectorCollectionNewSector = sectorCollectionNewSector.getCiudadId();
                    sectorCollectionNewSector.setCiudadId(ciudad);
                    sectorCollectionNewSector = em.merge(sectorCollectionNewSector);
                    if (oldCiudadIdOfSectorCollectionNewSector != null && !oldCiudadIdOfSectorCollectionNewSector.equals(ciudad)) {
                        oldCiudadIdOfSectorCollectionNewSector.getSectorCollection().remove(sectorCollectionNewSector);
                        oldCiudadIdOfSectorCollectionNewSector = em.merge(oldCiudadIdOfSectorCollectionNewSector);
                    }
                }
            }
            for (Zip zipCollectionOldZip : zipCollectionOld) {
                if (!zipCollectionNew.contains(zipCollectionOldZip)) {
                    zipCollectionOldZip.setCiudadId(null);
                    zipCollectionOldZip = em.merge(zipCollectionOldZip);
                }
            }
            for (Zip zipCollectionNewZip : zipCollectionNew) {
                if (!zipCollectionOld.contains(zipCollectionNewZip)) {
                    Ciudad oldCiudadIdOfZipCollectionNewZip = zipCollectionNewZip.getCiudadId();
                    zipCollectionNewZip.setCiudadId(ciudad);
                    zipCollectionNewZip = em.merge(zipCollectionNewZip);
                    if (oldCiudadIdOfZipCollectionNewZip != null && !oldCiudadIdOfZipCollectionNewZip.equals(ciudad)) {
                        oldCiudadIdOfZipCollectionNewZip.getZipCollection().remove(zipCollectionNewZip);
                        oldCiudadIdOfZipCollectionNewZip = em.merge(oldCiudadIdOfZipCollectionNewZip);
                    }
                }
            }
            for (Institucion institucionCollectionNewInstitucion : institucionCollectionNew) {
                if (!institucionCollectionOld.contains(institucionCollectionNewInstitucion)) {
                    Ciudad oldCiudadIdOfInstitucionCollectionNewInstitucion = institucionCollectionNewInstitucion.getCiudadId();
                    institucionCollectionNewInstitucion.setCiudadId(ciudad);
                    institucionCollectionNewInstitucion = em.merge(institucionCollectionNewInstitucion);
                    if (oldCiudadIdOfInstitucionCollectionNewInstitucion != null && !oldCiudadIdOfInstitucionCollectionNewInstitucion.equals(ciudad)) {
                        oldCiudadIdOfInstitucionCollectionNewInstitucion.getInstitucionCollection().remove(institucionCollectionNewInstitucion);
                        oldCiudadIdOfInstitucionCollectionNewInstitucion = em.merge(oldCiudadIdOfInstitucionCollectionNewInstitucion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ciudad.getId();
                if (findCiudad(id) == null) {
                    throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.");
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
            Ciudad ciudad;
            try {
                ciudad = em.getReference(Ciudad.class, id);
                ciudad.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Sector> sectorCollectionOrphanCheck = ciudad.getSectorCollection();
            for (Sector sectorCollectionOrphanCheckSector : sectorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ciudad (" + ciudad + ") cannot be destroyed since the Sector " + sectorCollectionOrphanCheckSector + " in its sectorCollection field has a non-nullable ciudadId field.");
            }
            Collection<Institucion> institucionCollectionOrphanCheck = ciudad.getInstitucionCollection();
            for (Institucion institucionCollectionOrphanCheckInstitucion : institucionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ciudad (" + ciudad + ") cannot be destroyed since the Institucion " + institucionCollectionOrphanCheckInstitucion + " in its institucionCollection field has a non-nullable ciudadId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estado estadoId = ciudad.getEstadoId();
            if (estadoId != null) {
                estadoId.getCiudadCollection().remove(ciudad);
                estadoId = em.merge(estadoId);
            }
            Collection<Zip> zipCollection = ciudad.getZipCollection();
            for (Zip zipCollectionZip : zipCollection) {
                zipCollectionZip.setCiudadId(null);
                zipCollectionZip = em.merge(zipCollectionZip);
            }
            em.remove(ciudad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ciudad> findCiudadEntities() {
        return findCiudadEntities(true, -1, -1);
    }

    public List<Ciudad> findCiudadEntities(int maxResults, int firstResult) {
        return findCiudadEntities(false, maxResults, firstResult);
    }

    private List<Ciudad> findCiudadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudad.class));
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

    public Ciudad findCiudad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudad.class, id);
        } finally {
            em.close();
        }
    }

    public int getCiudadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ciudad> rt = cq.from(Ciudad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
