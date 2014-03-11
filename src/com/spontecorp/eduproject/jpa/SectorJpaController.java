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
import com.spontecorp.eduproject.entity.Ciudad;
import com.spontecorp.eduproject.entity.Institucion;
import com.spontecorp.eduproject.entity.Sector;
import com.spontecorp.eduproject.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class SectorJpaController implements Serializable {

    public SectorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sector sector) {
        if (sector.getInstitucionCollection() == null) {
            sector.setInstitucionCollection(new ArrayList<Institucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad ciudadId = sector.getCiudadId();
            if (ciudadId != null) {
                ciudadId = em.getReference(ciudadId.getClass(), ciudadId.getId());
                sector.setCiudadId(ciudadId);
            }
            Collection<Institucion> attachedInstitucionCollection = new ArrayList<Institucion>();
            for (Institucion institucionCollectionInstitucionToAttach : sector.getInstitucionCollection()) {
                institucionCollectionInstitucionToAttach = em.getReference(institucionCollectionInstitucionToAttach.getClass(), institucionCollectionInstitucionToAttach.getId());
                attachedInstitucionCollection.add(institucionCollectionInstitucionToAttach);
            }
            sector.setInstitucionCollection(attachedInstitucionCollection);
            em.persist(sector);
            if (ciudadId != null) {
                ciudadId.getSectorCollection().add(sector);
                ciudadId = em.merge(ciudadId);
            }
            for (Institucion institucionCollectionInstitucion : sector.getInstitucionCollection()) {
                Sector oldSectorIdOfInstitucionCollectionInstitucion = institucionCollectionInstitucion.getSectorId();
                institucionCollectionInstitucion.setSectorId(sector);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
                if (oldSectorIdOfInstitucionCollectionInstitucion != null) {
                    oldSectorIdOfInstitucionCollectionInstitucion.getInstitucionCollection().remove(institucionCollectionInstitucion);
                    oldSectorIdOfInstitucionCollectionInstitucion = em.merge(oldSectorIdOfInstitucionCollectionInstitucion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sector sector) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sector persistentSector = em.find(Sector.class, sector.getId());
            Ciudad ciudadIdOld = persistentSector.getCiudadId();
            Ciudad ciudadIdNew = sector.getCiudadId();
            Collection<Institucion> institucionCollectionOld = persistentSector.getInstitucionCollection();
            Collection<Institucion> institucionCollectionNew = sector.getInstitucionCollection();
            if (ciudadIdNew != null) {
                ciudadIdNew = em.getReference(ciudadIdNew.getClass(), ciudadIdNew.getId());
                sector.setCiudadId(ciudadIdNew);
            }
            Collection<Institucion> attachedInstitucionCollectionNew = new ArrayList<Institucion>();
            for (Institucion institucionCollectionNewInstitucionToAttach : institucionCollectionNew) {
                institucionCollectionNewInstitucionToAttach = em.getReference(institucionCollectionNewInstitucionToAttach.getClass(), institucionCollectionNewInstitucionToAttach.getId());
                attachedInstitucionCollectionNew.add(institucionCollectionNewInstitucionToAttach);
            }
            institucionCollectionNew = attachedInstitucionCollectionNew;
            sector.setInstitucionCollection(institucionCollectionNew);
            sector = em.merge(sector);
            if (ciudadIdOld != null && !ciudadIdOld.equals(ciudadIdNew)) {
                ciudadIdOld.getSectorCollection().remove(sector);
                ciudadIdOld = em.merge(ciudadIdOld);
            }
            if (ciudadIdNew != null && !ciudadIdNew.equals(ciudadIdOld)) {
                ciudadIdNew.getSectorCollection().add(sector);
                ciudadIdNew = em.merge(ciudadIdNew);
            }
            for (Institucion institucionCollectionOldInstitucion : institucionCollectionOld) {
                if (!institucionCollectionNew.contains(institucionCollectionOldInstitucion)) {
                    institucionCollectionOldInstitucion.setSectorId(null);
                    institucionCollectionOldInstitucion = em.merge(institucionCollectionOldInstitucion);
                }
            }
            for (Institucion institucionCollectionNewInstitucion : institucionCollectionNew) {
                if (!institucionCollectionOld.contains(institucionCollectionNewInstitucion)) {
                    Sector oldSectorIdOfInstitucionCollectionNewInstitucion = institucionCollectionNewInstitucion.getSectorId();
                    institucionCollectionNewInstitucion.setSectorId(sector);
                    institucionCollectionNewInstitucion = em.merge(institucionCollectionNewInstitucion);
                    if (oldSectorIdOfInstitucionCollectionNewInstitucion != null && !oldSectorIdOfInstitucionCollectionNewInstitucion.equals(sector)) {
                        oldSectorIdOfInstitucionCollectionNewInstitucion.getInstitucionCollection().remove(institucionCollectionNewInstitucion);
                        oldSectorIdOfInstitucionCollectionNewInstitucion = em.merge(oldSectorIdOfInstitucionCollectionNewInstitucion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sector.getId();
                if (findSector(id) == null) {
                    throw new NonexistentEntityException("The sector with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sector sector;
            try {
                sector = em.getReference(Sector.class, id);
                sector.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sector with id " + id + " no longer exists.", enfe);
            }
            Ciudad ciudadId = sector.getCiudadId();
            if (ciudadId != null) {
                ciudadId.getSectorCollection().remove(sector);
                ciudadId = em.merge(ciudadId);
            }
            Collection<Institucion> institucionCollection = sector.getInstitucionCollection();
            for (Institucion institucionCollectionInstitucion : institucionCollection) {
                institucionCollectionInstitucion.setSectorId(null);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
            }
            em.remove(sector);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sector> findSectorEntities() {
        return findSectorEntities(true, -1, -1);
    }

    public List<Sector> findSectorEntities(int maxResults, int firstResult) {
        return findSectorEntities(false, maxResults, firstResult);
    }

    private List<Sector> findSectorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sector.class));
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

    public Sector findSector(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sector.class, id);
        } finally {
            em.close();
        }
    }

    public int getSectorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sector> rt = cq.from(Sector.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
