/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import authn.Credentials;
import authn.Secured;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.entities.Article;
import model.entities.Usuari;

@Stateless
@Path("/customer")
public class UsuariService extends AbstractFacade<Usuari> {
    
    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    public UsuariService() {
        super(Usuari.class);
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getCustomers () {
        List<Usuari> usuaris = em.createQuery("SELECT e FROM Usuari e").getResultList();
        
        List<Map<String, Object>> llistaCustomer = new ArrayList<>();
        for (Usuari usuari : usuaris) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", usuari.getId());
            userMap.put("name", usuari.getName());
            userMap.put("edat", usuari.getEdat());
            userMap.put("correu", usuari.getCorreu());
            
            List<Article> llistaArticlesUsuari = em.createQuery("SELECT e FROM Article e WHERE e.autor.name = :nom").setParameter("nom", usuari.getName()).getResultList();
            if (!llistaArticlesUsuari.isEmpty()) {
                Map<String, String> links = new HashMap<>();
                Article ultimArticle = llistaArticlesUsuari.get(llistaArticlesUsuari.size()-1);
                links.put("article", "/article/" + ultimArticle.getId());
                userMap.put("links", links);
            }
            llistaCustomer.add(userMap);
        }
        return Response.ok(llistaCustomer).build();
    }
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getCustomersId (@PathParam("id") Long id) {
        Map<String, Object> dadesUsuari = new HashMap<>();
        try {
            Usuari usuari = (Usuari) em.createQuery("SELECT e FROM Usuari e WHERE :id = e.id").setParameter("id", id).getSingleResult();
            dadesUsuari.put("id", usuari.getId());
            dadesUsuari.put("name", usuari.getName());
            dadesUsuari.put("edat", usuari.getEdat());
            dadesUsuari.put("correu", usuari.getCorreu());
        } catch (NoResultException e) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(dadesUsuari).build();
    }
    
    
    @PUT
    @Secured
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response modifyCustomer (@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathParam("id") Long id, Usuari nouUsuari) {
        if (!validarUsuario(authorizationHeader)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No estás validado.").build();
        }
        
        Long idUsuari = (Long) em.createQuery("SELECT u.id FROM Usuari u WHERE u.credenciales.username = :user").setParameter("user", validarAutor(authorizationHeader)).getSingleResult();
        
        if (idUsuari.equals(id)) {
            try {
                Usuari usuari = (Usuari) em.createQuery("SELECT e FROM Usuari e WHERE :id = e.id").setParameter("id", id).getSingleResult();
                usuari.setName(nouUsuari.getName());
                usuari.setCorreu(nouUsuari.getCorreu());
                usuari.setEdat(nouUsuari.getEdat());
                em.merge(usuari);
                return Response.ok().build();
            } catch (NoResultException e) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No estás autorizado.").build();
        }
    }
    
    public boolean validarUsuario(String authorizationHeader) {
       boolean validat = false; 
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String base64Credentials = authorizationHeader.substring(6); // "Basic " = Longitud 6
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));

            String[] parts = credentials.split(":");
            String username = parts[0];
            String password = parts[1];

            Credentials cred = null;
            try {
                TypedQuery<Credentials> query = em.createNamedQuery("Credentials.findUser", Credentials.class);
                query.setParameter("username", username);
                cred = query.getSingleResult();
            } catch (NoResultException e) {
                validat = false;
            }
            
            if (cred != null && cred.getPassword().equals(password)) {
                validat = true;
            }
        }
        return validat;
    }
    
    public String validarAutor (String authorizationHeader) {
        String username = "";
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String base64Credentials = authorizationHeader.substring(6); // "Basic " = Longitud 6
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));

            String[] parts = credentials.split(":");
            username = parts[0];
        }
        return username;
    }

 
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
