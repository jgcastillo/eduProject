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
import com.spontecorp.eduproject.entity.Reconocimiento;
import com.spontecorp.eduproject.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class ReconocimientoJpaController implements Serializable {

    public ReconocimientoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reconocimiento reconocimiento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Institucion institutoId = reconocimiento.getInstitutoId();
            if (institutoId != null) {
                institutoId = em.getReference(institutoId.getClass(), institutoId.getId());
                reconocimiento.setInstitutoId(institutoId);
            }
            em.persist(reconocimiento);
            if (institutoId != null) {
                institutoId.getReconocimientoCollection().add(reconocimiento);
                institutoId = em.merge(institutoId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reconocimiento reconocimiento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reconocimiento persistentReconocimiento = em.find(Reconocimiento.class, reconocimiento.getId());
            Institucion institutoIdOld = persistentReconocimiento.getInstitutoId();
            Institucion institutoIdNew = reconocimiento.getInstitutoId();
            if (institutoIdNew != null) {
                institutoIdNew = em.getReference(institutoIdNew.getClass(), institutoIdNew.getId());
                reconocimiento.setInstitutoId(institutoIdNew);
            }
            reconocimiento = em.merge(reconocimiento);
            if (institutoIdOld != null && !institutoIdOld.equals(institutoIdNew)) {
                institutoIdOld.getReconocimientoCollection().remove(reconocimiento);
                institutoIdOld = em.merge(institutoIdOld);
            }
            if (institutoIdNew != null && !institutoIdNew.equals(institutoIdOld)) {
                institutoIdNew.getReconocimientoCollection().add(reconocimiento);
                institutoIdNew = em.merge(institutoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reconocimiento.getId();
                if (findReconocimiento(id) == null) {
                    throw new NonexistentEntityException("The reconocimiento with id " + id + " no longer exists.");
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
            Reconocimiento reconocimiento;
            try {
                reconocimiento = em.getReference(Reconocimiento.class, id);
                reconocimiento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reconocimiento with id " + id + " no longer exists.", enfe);
            }
            Institucion institutoId = reconocimiento.getInstitutoId();
            if (institutoId != null) {
                institutoId.getReconocimientoCollection().remove(reconocimiento);
                institutoId = em.merge(institutoId);
            }
            em.remove(reconocimiento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reconocimiento> findReconocimientoEntities() {
        return findReconocimientoEntities(true, -1, -1);
    }

    public List<Reconocimiento> findReconocimientoEntities(int maxResults, int firstResult) {
        return findReconocimientoEntities(false, maxResults, firstResult);
    }

    private List<Reconocimiento> findReconocimientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reconocimiento.class));
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

    public Reconocimiento findReconocimiento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reconocimiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getReconocimientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reconocimiento> rt = cq.from(Reconocimiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
