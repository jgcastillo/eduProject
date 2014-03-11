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
import com.spontecorp.eduproject.entity.Zip;
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
public class ZipJpaController implements Serializable {

    public ZipJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Zip zip) {
        if (zip.getInstitucionCollection() == null) {
            zip.setInstitucionCollection(new ArrayList<Institucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad ciudadId = zip.getCiudadId();
            if (ciudadId != null) {
                ciudadId = em.getReference(ciudadId.getClass(), ciudadId.getId());
                zip.setCiudadId(ciudadId);
            }
            Collection<Institucion> attachedInstitucionCollection = new ArrayList<Institucion>();
            for (Institucion institucionCollectionInstitucionToAttach : zip.getInstitucionCollection()) {
                institucionCollectionInstitucionToAttach = em.getReference(institucionCollectionInstitucionToAttach.getClass(), institucionCollectionInstitucionToAttach.getId());
                attachedInstitucionCollection.add(institucionCollectionInstitucionToAttach);
            }
            zip.setInstitucionCollection(attachedInstitucionCollection);
            em.persist(zip);
            if (ciudadId != null) {
                ciudadId.getZipCollection().add(zip);
                ciudadId = em.merge(ciudadId);
            }
            for (Institucion institucionCollectionInstitucion : zip.getInstitucionCollection()) {
                Zip oldZipIdOfInstitucionCollectionInstitucion = institucionCollectionInstitucion.getZipId();
                institucionCollectionInstitucion.setZipId(zip);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
                if (oldZipIdOfInstitucionCollectionInstitucion != null) {
                    oldZipIdOfInstitucionCollectionInstitucion.getInstitucionCollection().remove(institucionCollectionInstitucion);
                    oldZipIdOfInstitucionCollectionInstitucion = em.merge(oldZipIdOfInstitucionCollectionInstitucion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Zip zip) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Zip persistentZip = em.find(Zip.class, zip.getId());
            Ciudad ciudadIdOld = persistentZip.getCiudadId();
            Ciudad ciudadIdNew = zip.getCiudadId();
            Collection<Institucion> institucionCollectionOld = persistentZip.getInstitucionCollection();
            Collection<Institucion> institucionCollectionNew = zip.getInstitucionCollection();
            if (ciudadIdNew != null) {
                ciudadIdNew = em.getReference(ciudadIdNew.getClass(), ciudadIdNew.getId());
                zip.setCiudadId(ciudadIdNew);
            }
            Collection<Institucion> attachedInstitucionCollectionNew = new ArrayList<Institucion>();
            for (Institucion institucionCollectionNewInstitucionToAttach : institucionCollectionNew) {
                institucionCollectionNewInstitucionToAttach = em.getReference(institucionCollectionNewInstitucionToAttach.getClass(), institucionCollectionNewInstitucionToAttach.getId());
                attachedInstitucionCollectionNew.add(institucionCollectionNewInstitucionToAttach);
            }
            institucionCollectionNew = attachedInstitucionCollectionNew;
            zip.setInstitucionCollection(institucionCollectionNew);
            zip = em.merge(zip);
            if (ciudadIdOld != null && !ciudadIdOld.equals(ciudadIdNew)) {
                ciudadIdOld.getZipCollection().remove(zip);
                ciudadIdOld = em.merge(ciudadIdOld);
            }
            if (ciudadIdNew != null && !ciudadIdNew.equals(ciudadIdOld)) {
                ciudadIdNew.getZipCollection().add(zip);
                ciudadIdNew = em.merge(ciudadIdNew);
            }
            for (Institucion institucionCollectionOldInstitucion : institucionCollectionOld) {
                if (!institucionCollectionNew.contains(institucionCollectionOldInstitucion)) {
                    institucionCollectionOldInstitucion.setZipId(null);
                    institucionCollectionOldInstitucion = em.merge(institucionCollectionOldInstitucion);
                }
            }
            for (Institucion institucionCollectionNewInstitucion : institucionCollectionNew) {
                if (!institucionCollectionOld.contains(institucionCollectionNewInstitucion)) {
                    Zip oldZipIdOfInstitucionCollectionNewInstitucion = institucionCollectionNewInstitucion.getZipId();
                    institucionCollectionNewInstitucion.setZipId(zip);
                    institucionCollectionNewInstitucion = em.merge(institucionCollectionNewInstitucion);
                    if (oldZipIdOfInstitucionCollectionNewInstitucion != null && !oldZipIdOfInstitucionCollectionNewInstitucion.equals(zip)) {
                        oldZipIdOfInstitucionCollectionNewInstitucion.getInstitucionCollection().remove(institucionCollectionNewInstitucion);
                        oldZipIdOfInstitucionCollectionNewInstitucion = em.merge(oldZipIdOfInstitucionCollectionNewInstitucion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = zip.getId();
                if (findZip(id) == null) {
                    throw new NonexistentEntityException("The zip with id " + id + " no longer exists.");
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
            Zip zip;
            try {
                zip = em.getReference(Zip.class, id);
                zip.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The zip with id " + id + " no longer exists.", enfe);
            }
            Ciudad ciudadId = zip.getCiudadId();
            if (ciudadId != null) {
                ciudadId.getZipCollection().remove(zip);
                ciudadId = em.merge(ciudadId);
            }
            Collection<Institucion> institucionCollection = zip.getInstitucionCollection();
            for (Institucion institucionCollectionInstitucion : institucionCollection) {
                institucionCollectionInstitucion.setZipId(null);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
            }
            em.remove(zip);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Zip> findZipEntities() {
        return findZipEntities(true, -1, -1);
    }

    public List<Zip> findZipEntities(int maxResults, int firstResult) {
        return findZipEntities(false, maxResults, firstResult);
    }

    private List<Zip> findZipEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Zip.class));
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

    public Zip findZip(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Zip.class, id);
        } finally {
            em.close();
        }
    }

    public int getZipCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Zip> rt = cq.from(Zip.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
