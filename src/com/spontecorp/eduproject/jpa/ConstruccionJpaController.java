/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spontecorp.eduproject.jpa;

import com.spontecorp.eduproject.entity.Construccion;
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
public class ConstruccionJpaController implements Serializable {

    public ConstruccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Construccion construccion) {
        if (construccion.getInstitucionCollection() == null) {
            construccion.setInstitucionCollection(new ArrayList<Institucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Institucion> attachedInstitucionCollection = new ArrayList<Institucion>();
            for (Institucion institucionCollectionInstitucionToAttach : construccion.getInstitucionCollection()) {
                institucionCollectionInstitucionToAttach = em.getReference(institucionCollectionInstitucionToAttach.getClass(), institucionCollectionInstitucionToAttach.getId());
                attachedInstitucionCollection.add(institucionCollectionInstitucionToAttach);
            }
            construccion.setInstitucionCollection(attachedInstitucionCollection);
            em.persist(construccion);
            for (Institucion institucionCollectionInstitucion : construccion.getInstitucionCollection()) {
                institucionCollectionInstitucion.getConstruccionCollection().add(construccion);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Construccion construccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Construccion persistentConstruccion = em.find(Construccion.class, construccion.getId());
            Collection<Institucion> institucionCollectionOld = persistentConstruccion.getInstitucionCollection();
            Collection<Institucion> institucionCollectionNew = construccion.getInstitucionCollection();
            Collection<Institucion> attachedInstitucionCollectionNew = new ArrayList<Institucion>();
            for (Institucion institucionCollectionNewInstitucionToAttach : institucionCollectionNew) {
                institucionCollectionNewInstitucionToAttach = em.getReference(institucionCollectionNewInstitucionToAttach.getClass(), institucionCollectionNewInstitucionToAttach.getId());
                attachedInstitucionCollectionNew.add(institucionCollectionNewInstitucionToAttach);
            }
            institucionCollectionNew = attachedInstitucionCollectionNew;
            construccion.setInstitucionCollection(institucionCollectionNew);
            construccion = em.merge(construccion);
            for (Institucion institucionCollectionOldInstitucion : institucionCollectionOld) {
                if (!institucionCollectionNew.contains(institucionCollectionOldInstitucion)) {
                    institucionCollectionOldInstitucion.getConstruccionCollection().remove(construccion);
                    institucionCollectionOldInstitucion = em.merge(institucionCollectionOldInstitucion);
                }
            }
            for (Institucion institucionCollectionNewInstitucion : institucionCollectionNew) {
                if (!institucionCollectionOld.contains(institucionCollectionNewInstitucion)) {
                    institucionCollectionNewInstitucion.getConstruccionCollection().add(construccion);
                    institucionCollectionNewInstitucion = em.merge(institucionCollectionNewInstitucion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = construccion.getId();
                if (findConstruccion(id) == null) {
                    throw new NonexistentEntityException("The construccion with id " + id + " no longer exists.");
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
            Construccion construccion;
            try {
                construccion = em.getReference(Construccion.class, id);
                construccion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The construccion with id " + id + " no longer exists.", enfe);
            }
            Collection<Institucion> institucionCollection = construccion.getInstitucionCollection();
            for (Institucion institucionCollectionInstitucion : institucionCollection) {
                institucionCollectionInstitucion.getConstruccionCollection().remove(construccion);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
            }
            em.remove(construccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Construccion> findConstruccionEntities() {
        return findConstruccionEntities(true, -1, -1);
    }

    public List<Construccion> findConstruccionEntities(int maxResults, int firstResult) {
        return findConstruccionEntities(false, maxResults, firstResult);
    }

    private List<Construccion> findConstruccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Construccion.class));
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

    public Construccion findConstruccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Construccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getConstruccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Construccion> rt = cq.from(Construccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
