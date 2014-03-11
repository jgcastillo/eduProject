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
import com.spontecorp.eduproject.entity.Estado;
import com.spontecorp.eduproject.entity.Pais;
import com.spontecorp.eduproject.jpa.exceptions.IllegalOrphanException;
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
public class PaisJpaController implements Serializable {

    public PaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pais pais) {
        if (pais.getEstadoCollection() == null) {
            pais.setEstadoCollection(new ArrayList<Estado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Estado> attachedEstadoCollection = new ArrayList<Estado>();
            for (Estado estadoCollectionEstadoToAttach : pais.getEstadoCollection()) {
                estadoCollectionEstadoToAttach = em.getReference(estadoCollectionEstadoToAttach.getClass(), estadoCollectionEstadoToAttach.getId());
                attachedEstadoCollection.add(estadoCollectionEstadoToAttach);
            }
            pais.setEstadoCollection(attachedEstadoCollection);
            em.persist(pais);
            for (Estado estadoCollectionEstado : pais.getEstadoCollection()) {
                Pais oldPaisIdOfEstadoCollectionEstado = estadoCollectionEstado.getPaisId();
                estadoCollectionEstado.setPaisId(pais);
                estadoCollectionEstado = em.merge(estadoCollectionEstado);
                if (oldPaisIdOfEstadoCollectionEstado != null) {
                    oldPaisIdOfEstadoCollectionEstado.getEstadoCollection().remove(estadoCollectionEstado);
                    oldPaisIdOfEstadoCollectionEstado = em.merge(oldPaisIdOfEstadoCollectionEstado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pais pais) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais persistentPais = em.find(Pais.class, pais.getId());
            Collection<Estado> estadoCollectionOld = persistentPais.getEstadoCollection();
            Collection<Estado> estadoCollectionNew = pais.getEstadoCollection();
            List<String> illegalOrphanMessages = null;
            for (Estado estadoCollectionOldEstado : estadoCollectionOld) {
                if (!estadoCollectionNew.contains(estadoCollectionOldEstado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Estado " + estadoCollectionOldEstado + " since its paisId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Estado> attachedEstadoCollectionNew = new ArrayList<Estado>();
            for (Estado estadoCollectionNewEstadoToAttach : estadoCollectionNew) {
                estadoCollectionNewEstadoToAttach = em.getReference(estadoCollectionNewEstadoToAttach.getClass(), estadoCollectionNewEstadoToAttach.getId());
                attachedEstadoCollectionNew.add(estadoCollectionNewEstadoToAttach);
            }
            estadoCollectionNew = attachedEstadoCollectionNew;
            pais.setEstadoCollection(estadoCollectionNew);
            pais = em.merge(pais);
            for (Estado estadoCollectionNewEstado : estadoCollectionNew) {
                if (!estadoCollectionOld.contains(estadoCollectionNewEstado)) {
                    Pais oldPaisIdOfEstadoCollectionNewEstado = estadoCollectionNewEstado.getPaisId();
                    estadoCollectionNewEstado.setPaisId(pais);
                    estadoCollectionNewEstado = em.merge(estadoCollectionNewEstado);
                    if (oldPaisIdOfEstadoCollectionNewEstado != null && !oldPaisIdOfEstadoCollectionNewEstado.equals(pais)) {
                        oldPaisIdOfEstadoCollectionNewEstado.getEstadoCollection().remove(estadoCollectionNewEstado);
                        oldPaisIdOfEstadoCollectionNewEstado = em.merge(oldPaisIdOfEstadoCollectionNewEstado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pais.getId();
                if (findPais(id) == null) {
                    throw new NonexistentEntityException("The pais with id " + id + " no longer exists.");
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
            Pais pais;
            try {
                pais = em.getReference(Pais.class, id);
                pais.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pais with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Estado> estadoCollectionOrphanCheck = pais.getEstadoCollection();
            for (Estado estadoCollectionOrphanCheckEstado : estadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pais (" + pais + ") cannot be destroyed since the Estado " + estadoCollectionOrphanCheckEstado + " in its estadoCollection field has a non-nullable paisId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pais> findPaisEntities() {
        return findPaisEntities(true, -1, -1);
    }

    public List<Pais> findPaisEntities(int maxResults, int firstResult) {
        return findPaisEntities(false, maxResults, firstResult);
    }

    private List<Pais> findPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pais.class));
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

    public Pais findPais(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pais> rt = cq.from(Pais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
