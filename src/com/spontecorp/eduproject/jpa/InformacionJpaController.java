/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spontecorp.eduproject.jpa;

import com.spontecorp.eduproject.entity.Informacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.eduproject.entity.Institucion;
import com.spontecorp.eduproject.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class InformacionJpaController implements Serializable {

    public InformacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Informacion informacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Institucion institutoId = informacion.getInstitutoId();
            if (institutoId != null) {
                institutoId = em.getReference(institutoId.getClass(), institutoId.getId());
                informacion.setInstitutoId(institutoId);
            }
            em.persist(informacion);
            if (institutoId != null) {
                institutoId.getInformacionCollection().add(informacion);
                institutoId = em.merge(institutoId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Informacion informacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Informacion persistentInformacion = em.find(Informacion.class, informacion.getId());
            Institucion institutoIdOld = persistentInformacion.getInstitutoId();
            Institucion institutoIdNew = informacion.getInstitutoId();
            if (institutoIdNew != null) {
                institutoIdNew = em.getReference(institutoIdNew.getClass(), institutoIdNew.getId());
                informacion.setInstitutoId(institutoIdNew);
            }
            informacion = em.merge(informacion);
            if (institutoIdOld != null && !institutoIdOld.equals(institutoIdNew)) {
                institutoIdOld.getInformacionCollection().remove(informacion);
                institutoIdOld = em.merge(institutoIdOld);
            }
            if (institutoIdNew != null && !institutoIdNew.equals(institutoIdOld)) {
                institutoIdNew.getInformacionCollection().add(informacion);
                institutoIdNew = em.merge(institutoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = informacion.getId();
                if (findInformacion(id) == null) {
                    throw new NonexistentEntityException("The informacion with id " + id + " no longer exists.");
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
            Informacion informacion;
            try {
                informacion = em.getReference(Informacion.class, id);
                informacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The informacion with id " + id + " no longer exists.", enfe);
            }
            Institucion institutoId = informacion.getInstitutoId();
            if (institutoId != null) {
                institutoId.getInformacionCollection().remove(informacion);
                institutoId = em.merge(institutoId);
            }
            em.remove(informacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Informacion> findInformacionEntities() {
        return findInformacionEntities(true, -1, -1);
    }

    public List<Informacion> findInformacionEntities(int maxResults, int firstResult) {
        return findInformacionEntities(false, maxResults, firstResult);
    }

    private List<Informacion> findInformacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Informacion.class));
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

    public Informacion findInformacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Informacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getInformacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Informacion> rt = cq.from(Informacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
