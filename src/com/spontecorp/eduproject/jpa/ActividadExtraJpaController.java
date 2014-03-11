/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spontecorp.eduproject.jpa;

import com.spontecorp.eduproject.entity.ActividadExtra;
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
public class ActividadExtraJpaController implements Serializable {

    public ActividadExtraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ActividadExtra actividadExtra) {
        if (actividadExtra.getInstitucionCollection() == null) {
            actividadExtra.setInstitucionCollection(new ArrayList<Institucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Institucion> attachedInstitucionCollection = new ArrayList<Institucion>();
            for (Institucion institucionCollectionInstitucionToAttach : actividadExtra.getInstitucionCollection()) {
                institucionCollectionInstitucionToAttach = em.getReference(institucionCollectionInstitucionToAttach.getClass(), institucionCollectionInstitucionToAttach.getId());
                attachedInstitucionCollection.add(institucionCollectionInstitucionToAttach);
            }
            actividadExtra.setInstitucionCollection(attachedInstitucionCollection);
            em.persist(actividadExtra);
            for (Institucion institucionCollectionInstitucion : actividadExtra.getInstitucionCollection()) {
                institucionCollectionInstitucion.getActividadExtraCollection().add(actividadExtra);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ActividadExtra actividadExtra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ActividadExtra persistentActividadExtra = em.find(ActividadExtra.class, actividadExtra.getId());
            Collection<Institucion> institucionCollectionOld = persistentActividadExtra.getInstitucionCollection();
            Collection<Institucion> institucionCollectionNew = actividadExtra.getInstitucionCollection();
            Collection<Institucion> attachedInstitucionCollectionNew = new ArrayList<Institucion>();
            for (Institucion institucionCollectionNewInstitucionToAttach : institucionCollectionNew) {
                institucionCollectionNewInstitucionToAttach = em.getReference(institucionCollectionNewInstitucionToAttach.getClass(), institucionCollectionNewInstitucionToAttach.getId());
                attachedInstitucionCollectionNew.add(institucionCollectionNewInstitucionToAttach);
            }
            institucionCollectionNew = attachedInstitucionCollectionNew;
            actividadExtra.setInstitucionCollection(institucionCollectionNew);
            actividadExtra = em.merge(actividadExtra);
            for (Institucion institucionCollectionOldInstitucion : institucionCollectionOld) {
                if (!institucionCollectionNew.contains(institucionCollectionOldInstitucion)) {
                    institucionCollectionOldInstitucion.getActividadExtraCollection().remove(actividadExtra);
                    institucionCollectionOldInstitucion = em.merge(institucionCollectionOldInstitucion);
                }
            }
            for (Institucion institucionCollectionNewInstitucion : institucionCollectionNew) {
                if (!institucionCollectionOld.contains(institucionCollectionNewInstitucion)) {
                    institucionCollectionNewInstitucion.getActividadExtraCollection().add(actividadExtra);
                    institucionCollectionNewInstitucion = em.merge(institucionCollectionNewInstitucion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = actividadExtra.getId();
                if (findActividadExtra(id) == null) {
                    throw new NonexistentEntityException("The actividadExtra with id " + id + " no longer exists.");
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
            ActividadExtra actividadExtra;
            try {
                actividadExtra = em.getReference(ActividadExtra.class, id);
                actividadExtra.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actividadExtra with id " + id + " no longer exists.", enfe);
            }
            Collection<Institucion> institucionCollection = actividadExtra.getInstitucionCollection();
            for (Institucion institucionCollectionInstitucion : institucionCollection) {
                institucionCollectionInstitucion.getActividadExtraCollection().remove(actividadExtra);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
            }
            em.remove(actividadExtra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ActividadExtra> findActividadExtraEntities() {
        return findActividadExtraEntities(true, -1, -1);
    }

    public List<ActividadExtra> findActividadExtraEntities(int maxResults, int firstResult) {
        return findActividadExtraEntities(false, maxResults, firstResult);
    }

    private List<ActividadExtra> findActividadExtraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ActividadExtra.class));
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

    public ActividadExtra findActividadExtra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ActividadExtra.class, id);
        } finally {
            em.close();
        }
    }

    public int getActividadExtraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ActividadExtra> rt = cq.from(ActividadExtra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
