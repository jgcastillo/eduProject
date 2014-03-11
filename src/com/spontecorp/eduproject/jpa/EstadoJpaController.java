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
import com.spontecorp.eduproject.entity.Pais;
import com.spontecorp.eduproject.entity.Ciudad;
import com.spontecorp.eduproject.entity.Estado;
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
public class EstadoJpaController implements Serializable {

    public EstadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estado estado) {
        if (estado.getCiudadCollection() == null) {
            estado.setCiudadCollection(new ArrayList<Ciudad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais paisId = estado.getPaisId();
            if (paisId != null) {
                paisId = em.getReference(paisId.getClass(), paisId.getId());
                estado.setPaisId(paisId);
            }
            Collection<Ciudad> attachedCiudadCollection = new ArrayList<Ciudad>();
            for (Ciudad ciudadCollectionCiudadToAttach : estado.getCiudadCollection()) {
                ciudadCollectionCiudadToAttach = em.getReference(ciudadCollectionCiudadToAttach.getClass(), ciudadCollectionCiudadToAttach.getId());
                attachedCiudadCollection.add(ciudadCollectionCiudadToAttach);
            }
            estado.setCiudadCollection(attachedCiudadCollection);
            em.persist(estado);
            if (paisId != null) {
                paisId.getEstadoCollection().add(estado);
                paisId = em.merge(paisId);
            }
            for (Ciudad ciudadCollectionCiudad : estado.getCiudadCollection()) {
                Estado oldEstadoIdOfCiudadCollectionCiudad = ciudadCollectionCiudad.getEstadoId();
                ciudadCollectionCiudad.setEstadoId(estado);
                ciudadCollectionCiudad = em.merge(ciudadCollectionCiudad);
                if (oldEstadoIdOfCiudadCollectionCiudad != null) {
                    oldEstadoIdOfCiudadCollectionCiudad.getCiudadCollection().remove(ciudadCollectionCiudad);
                    oldEstadoIdOfCiudadCollectionCiudad = em.merge(oldEstadoIdOfCiudadCollectionCiudad);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estado estado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado persistentEstado = em.find(Estado.class, estado.getId());
            Pais paisIdOld = persistentEstado.getPaisId();
            Pais paisIdNew = estado.getPaisId();
            Collection<Ciudad> ciudadCollectionOld = persistentEstado.getCiudadCollection();
            Collection<Ciudad> ciudadCollectionNew = estado.getCiudadCollection();
            List<String> illegalOrphanMessages = null;
            for (Ciudad ciudadCollectionOldCiudad : ciudadCollectionOld) {
                if (!ciudadCollectionNew.contains(ciudadCollectionOldCiudad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ciudad " + ciudadCollectionOldCiudad + " since its estadoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (paisIdNew != null) {
                paisIdNew = em.getReference(paisIdNew.getClass(), paisIdNew.getId());
                estado.setPaisId(paisIdNew);
            }
            Collection<Ciudad> attachedCiudadCollectionNew = new ArrayList<Ciudad>();
            for (Ciudad ciudadCollectionNewCiudadToAttach : ciudadCollectionNew) {
                ciudadCollectionNewCiudadToAttach = em.getReference(ciudadCollectionNewCiudadToAttach.getClass(), ciudadCollectionNewCiudadToAttach.getId());
                attachedCiudadCollectionNew.add(ciudadCollectionNewCiudadToAttach);
            }
            ciudadCollectionNew = attachedCiudadCollectionNew;
            estado.setCiudadCollection(ciudadCollectionNew);
            estado = em.merge(estado);
            if (paisIdOld != null && !paisIdOld.equals(paisIdNew)) {
                paisIdOld.getEstadoCollection().remove(estado);
                paisIdOld = em.merge(paisIdOld);
            }
            if (paisIdNew != null && !paisIdNew.equals(paisIdOld)) {
                paisIdNew.getEstadoCollection().add(estado);
                paisIdNew = em.merge(paisIdNew);
            }
            for (Ciudad ciudadCollectionNewCiudad : ciudadCollectionNew) {
                if (!ciudadCollectionOld.contains(ciudadCollectionNewCiudad)) {
                    Estado oldEstadoIdOfCiudadCollectionNewCiudad = ciudadCollectionNewCiudad.getEstadoId();
                    ciudadCollectionNewCiudad.setEstadoId(estado);
                    ciudadCollectionNewCiudad = em.merge(ciudadCollectionNewCiudad);
                    if (oldEstadoIdOfCiudadCollectionNewCiudad != null && !oldEstadoIdOfCiudadCollectionNewCiudad.equals(estado)) {
                        oldEstadoIdOfCiudadCollectionNewCiudad.getCiudadCollection().remove(ciudadCollectionNewCiudad);
                        oldEstadoIdOfCiudadCollectionNewCiudad = em.merge(oldEstadoIdOfCiudadCollectionNewCiudad);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estado.getId();
                if (findEstado(id) == null) {
                    throw new NonexistentEntityException("The estado with id " + id + " no longer exists.");
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
            Estado estado;
            try {
                estado = em.getReference(Estado.class, id);
                estado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ciudad> ciudadCollectionOrphanCheck = estado.getCiudadCollection();
            for (Ciudad ciudadCollectionOrphanCheckCiudad : ciudadCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the Ciudad " + ciudadCollectionOrphanCheckCiudad + " in its ciudadCollection field has a non-nullable estadoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pais paisId = estado.getPaisId();
            if (paisId != null) {
                paisId.getEstadoCollection().remove(estado);
                paisId = em.merge(paisId);
            }
            em.remove(estado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estado> findEstadoEntities() {
        return findEstadoEntities(true, -1, -1);
    }

    public List<Estado> findEstadoEntities(int maxResults, int firstResult) {
        return findEstadoEntities(false, maxResults, firstResult);
    }

    private List<Estado> findEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estado.class));
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

    public Estado findEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estado> rt = cq.from(Estado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
