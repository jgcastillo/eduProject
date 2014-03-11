/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spontecorp.eduproject.jpa;

import com.spontecorp.eduproject.entity.Deporte;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.eduproject.entity.Institucion;
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
public class DeporteJpaController implements Serializable {

    public DeporteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Deporte deporte) {
        if (deporte.getInstitucionCollection() == null) {
            deporte.setInstitucionCollection(new ArrayList<Institucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Institucion> attachedInstitucionCollection = new ArrayList<Institucion>();
            for (Institucion institucionCollectionInstitucionToAttach : deporte.getInstitucionCollection()) {
                institucionCollectionInstitucionToAttach = em.getReference(institucionCollectionInstitucionToAttach.getClass(), institucionCollectionInstitucionToAttach.getId());
                attachedInstitucionCollection.add(institucionCollectionInstitucionToAttach);
            }
            deporte.setInstitucionCollection(attachedInstitucionCollection);
            em.persist(deporte);
            for (Institucion institucionCollectionInstitucion : deporte.getInstitucionCollection()) {
                institucionCollectionInstitucion.getDeporteCollection().add(deporte);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Deporte deporte) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Deporte persistentDeporte = em.find(Deporte.class, deporte.getId());
            Collection<Institucion> institucionCollectionOld = persistentDeporte.getInstitucionCollection();
            Collection<Institucion> institucionCollectionNew = deporte.getInstitucionCollection();
            Collection<Institucion> attachedInstitucionCollectionNew = new ArrayList<Institucion>();
            for (Institucion institucionCollectionNewInstitucionToAttach : institucionCollectionNew) {
                institucionCollectionNewInstitucionToAttach = em.getReference(institucionCollectionNewInstitucionToAttach.getClass(), institucionCollectionNewInstitucionToAttach.getId());
                attachedInstitucionCollectionNew.add(institucionCollectionNewInstitucionToAttach);
            }
            institucionCollectionNew = attachedInstitucionCollectionNew;
            deporte.setInstitucionCollection(institucionCollectionNew);
            deporte = em.merge(deporte);
            for (Institucion institucionCollectionOldInstitucion : institucionCollectionOld) {
                if (!institucionCollectionNew.contains(institucionCollectionOldInstitucion)) {
                    institucionCollectionOldInstitucion.getDeporteCollection().remove(deporte);
                    institucionCollectionOldInstitucion = em.merge(institucionCollectionOldInstitucion);
                }
            }
            for (Institucion institucionCollectionNewInstitucion : institucionCollectionNew) {
                if (!institucionCollectionOld.contains(institucionCollectionNewInstitucion)) {
                    institucionCollectionNewInstitucion.getDeporteCollection().add(deporte);
                    institucionCollectionNewInstitucion = em.merge(institucionCollectionNewInstitucion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = deporte.getId();
                if (findDeporte(id) == null) {
                    throw new NonexistentEntityException("The deporte with id " + id + " no longer exists.");
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
            Deporte deporte;
            try {
                deporte = em.getReference(Deporte.class, id);
                deporte.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The deporte with id " + id + " no longer exists.", enfe);
            }
            Collection<Institucion> institucionCollection = deporte.getInstitucionCollection();
            for (Institucion institucionCollectionInstitucion : institucionCollection) {
                institucionCollectionInstitucion.getDeporteCollection().remove(deporte);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
            }
            em.remove(deporte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Deporte> findDeporteEntities() {
        return findDeporteEntities(true, -1, -1);
    }

    public List<Deporte> findDeporteEntities(int maxResults, int firstResult) {
        return findDeporteEntities(false, maxResults, firstResult);
    }

    private List<Deporte> findDeporteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Deporte.class));
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

    public Deporte findDeporte(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Deporte.class, id);
        } finally {
            em.close();
        }
    }

    public int getDeporteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Deporte> rt = cq.from(Deporte.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
