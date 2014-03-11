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
import com.spontecorp.eduproject.entity.Institucion;
import com.spontecorp.eduproject.entity.Religion;
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
public class ReligionJpaController implements Serializable {

    public ReligionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Religion religion) {
        if (religion.getInstitucionCollection() == null) {
            religion.setInstitucionCollection(new ArrayList<Institucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Institucion> attachedInstitucionCollection = new ArrayList<Institucion>();
            for (Institucion institucionCollectionInstitucionToAttach : religion.getInstitucionCollection()) {
                institucionCollectionInstitucionToAttach = em.getReference(institucionCollectionInstitucionToAttach.getClass(), institucionCollectionInstitucionToAttach.getId());
                attachedInstitucionCollection.add(institucionCollectionInstitucionToAttach);
            }
            religion.setInstitucionCollection(attachedInstitucionCollection);
            em.persist(religion);
            for (Institucion institucionCollectionInstitucion : religion.getInstitucionCollection()) {
                Religion oldReligionIdOfInstitucionCollectionInstitucion = institucionCollectionInstitucion.getReligionId();
                institucionCollectionInstitucion.setReligionId(religion);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
                if (oldReligionIdOfInstitucionCollectionInstitucion != null) {
                    oldReligionIdOfInstitucionCollectionInstitucion.getInstitucionCollection().remove(institucionCollectionInstitucion);
                    oldReligionIdOfInstitucionCollectionInstitucion = em.merge(oldReligionIdOfInstitucionCollectionInstitucion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Religion religion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Religion persistentReligion = em.find(Religion.class, religion.getId());
            Collection<Institucion> institucionCollectionOld = persistentReligion.getInstitucionCollection();
            Collection<Institucion> institucionCollectionNew = religion.getInstitucionCollection();
            Collection<Institucion> attachedInstitucionCollectionNew = new ArrayList<Institucion>();
            for (Institucion institucionCollectionNewInstitucionToAttach : institucionCollectionNew) {
                institucionCollectionNewInstitucionToAttach = em.getReference(institucionCollectionNewInstitucionToAttach.getClass(), institucionCollectionNewInstitucionToAttach.getId());
                attachedInstitucionCollectionNew.add(institucionCollectionNewInstitucionToAttach);
            }
            institucionCollectionNew = attachedInstitucionCollectionNew;
            religion.setInstitucionCollection(institucionCollectionNew);
            religion = em.merge(religion);
            for (Institucion institucionCollectionOldInstitucion : institucionCollectionOld) {
                if (!institucionCollectionNew.contains(institucionCollectionOldInstitucion)) {
                    institucionCollectionOldInstitucion.setReligionId(null);
                    institucionCollectionOldInstitucion = em.merge(institucionCollectionOldInstitucion);
                }
            }
            for (Institucion institucionCollectionNewInstitucion : institucionCollectionNew) {
                if (!institucionCollectionOld.contains(institucionCollectionNewInstitucion)) {
                    Religion oldReligionIdOfInstitucionCollectionNewInstitucion = institucionCollectionNewInstitucion.getReligionId();
                    institucionCollectionNewInstitucion.setReligionId(religion);
                    institucionCollectionNewInstitucion = em.merge(institucionCollectionNewInstitucion);
                    if (oldReligionIdOfInstitucionCollectionNewInstitucion != null && !oldReligionIdOfInstitucionCollectionNewInstitucion.equals(religion)) {
                        oldReligionIdOfInstitucionCollectionNewInstitucion.getInstitucionCollection().remove(institucionCollectionNewInstitucion);
                        oldReligionIdOfInstitucionCollectionNewInstitucion = em.merge(oldReligionIdOfInstitucionCollectionNewInstitucion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = religion.getId();
                if (findReligion(id) == null) {
                    throw new NonexistentEntityException("The religion with id " + id + " no longer exists.");
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
            Religion religion;
            try {
                religion = em.getReference(Religion.class, id);
                religion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The religion with id " + id + " no longer exists.", enfe);
            }
            Collection<Institucion> institucionCollection = religion.getInstitucionCollection();
            for (Institucion institucionCollectionInstitucion : institucionCollection) {
                institucionCollectionInstitucion.setReligionId(null);
                institucionCollectionInstitucion = em.merge(institucionCollectionInstitucion);
            }
            em.remove(religion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Religion> findReligionEntities() {
        return findReligionEntities(true, -1, -1);
    }

    public List<Religion> findReligionEntities(int maxResults, int firstResult) {
        return findReligionEntities(false, maxResults, firstResult);
    }

    private List<Religion> findReligionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Religion.class));
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

    public Religion findReligion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Religion.class, id);
        } finally {
            em.close();
        }
    }

    public int getReligionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Religion> rt = cq.from(Religion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
