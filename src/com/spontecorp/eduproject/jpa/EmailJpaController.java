/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spontecorp.eduproject.jpa;

import com.spontecorp.eduproject.entity.Email;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.eduproject.entity.Institucion;
import com.spontecorp.eduproject.jpa.exceptions.NonexistentEntityException;
import com.spontecorp.eduproject.jpa.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class EmailJpaController implements Serializable {

    public EmailJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Email email) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Institucion institutoId = email.getInstitutoId();
            if (institutoId != null) {
                institutoId = em.getReference(institutoId.getClass(), institutoId.getId());
                email.setInstitutoId(institutoId);
            }
            em.persist(email);
            if (institutoId != null) {
                institutoId.getEmailCollection().add(email);
                institutoId = em.merge(institutoId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmail(email.getId()) != null) {
                throw new PreexistingEntityException("Email " + email + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Email email) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Email persistentEmail = em.find(Email.class, email.getId());
            Institucion institutoIdOld = persistentEmail.getInstitutoId();
            Institucion institutoIdNew = email.getInstitutoId();
            if (institutoIdNew != null) {
                institutoIdNew = em.getReference(institutoIdNew.getClass(), institutoIdNew.getId());
                email.setInstitutoId(institutoIdNew);
            }
            email = em.merge(email);
            if (institutoIdOld != null && !institutoIdOld.equals(institutoIdNew)) {
                institutoIdOld.getEmailCollection().remove(email);
                institutoIdOld = em.merge(institutoIdOld);
            }
            if (institutoIdNew != null && !institutoIdNew.equals(institutoIdOld)) {
                institutoIdNew.getEmailCollection().add(email);
                institutoIdNew = em.merge(institutoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = email.getId();
                if (findEmail(id) == null) {
                    throw new NonexistentEntityException("The email with id " + id + " no longer exists.");
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
            Email email;
            try {
                email = em.getReference(Email.class, id);
                email.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The email with id " + id + " no longer exists.", enfe);
            }
            Institucion institutoId = email.getInstitutoId();
            if (institutoId != null) {
                institutoId.getEmailCollection().remove(email);
                institutoId = em.merge(institutoId);
            }
            em.remove(email);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Email> findEmailEntities() {
        return findEmailEntities(true, -1, -1);
    }

    public List<Email> findEmailEntities(int maxResults, int firstResult) {
        return findEmailEntities(false, maxResults, firstResult);
    }

    private List<Email> findEmailEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Email.class));
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

    public Email findEmail(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Email.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmailCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Email> rt = cq.from(Email.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
