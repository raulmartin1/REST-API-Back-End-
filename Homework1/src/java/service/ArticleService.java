package service;

import authn.Credentials;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.entities.Article;
import authn.Secured;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import model.entities.Topic;
import model.entities.Usuari;

@Stateless
@Path("/article")
public class ArticleService extends AbstractFacade<Article> {

    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    public ArticleService() {
        super(Article.class);
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getArticles(
        @QueryParam("topic") List<String> topics,
        @QueryParam("author") String author
        ) {

         String queryStr = "SELECT DISTINCT a FROM Article a LEFT JOIN a.topicos t WHERE 1=1 ";

            if (topics != null && !topics.isEmpty()) {
                if (topics.size() == 1) {
                    queryStr += "AND t.name = :topic1 ";
                } else if (topics.size() == 2) {
                    queryStr += "AND (t.name = :topic1 OR t.name = :topic2) ";
                }
            }
            
            if (author != null && !author.isEmpty()) {
                queryStr += "AND a.autor.name = :author ";
            }
            queryStr += "ORDER BY COALESCE(a.visualitzacions, 0) DESC";
            
            Query query = em.createQuery(queryStr);
            if (topics != null && !topics.isEmpty()) {
                if (topics.size() == 1) {
                    query.setParameter("topic1", topics.get(0));
                } else if (topics.size() == 2) {
                    query.setParameter("topic1", topics.get(0));
                    query.setParameter("topic2", topics.get(1));
                }
            }
            if (author != null && !author.isEmpty()) {
                query.setParameter("author", author);
            }

            List<Article> results = query.getResultList();
            
            List<Map<String, Object>> articles = new ArrayList<>();
            for (Article article : results) {
                Map<String, Object> dadesArticle = new HashMap<>();
                    Map<String, Object> autor = new HashMap<>();
                    autor.put("correu", article.getAutor().getCorreu());
                    autor.put("edat", article.getAutor().getEdat());
                    autor.put("id", article.getAutor().getId());
                    autor.put("name", article.getAutor().getName());
                    
                dadesArticle.put("autor", autor);
                dadesArticle.put("id", article.getId());
                dadesArticle.put("imatge", article.getImatge());
                dadesArticle.put("privat", article.isPrivat());
                dadesArticle.put("resum", article.getResum());
                dadesArticle.put("topicos", article.getTopicos());
                dadesArticle.put("visualitzacions", article.getVisualitzacions());
                dadesArticle.put("dataPublicacio", article.getDataPublicacio());
                dadesArticle.put("titol", article.getTitol());
                
                articles.add(dadesArticle);
            }
            
            if (!results.isEmpty()) {
                return Response.ok(articles).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("No existe ningun articulo con esas condiciones.").build();
            } 
    }
    
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getArticleId (@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathParam("id") Long id) {
        Article article;
        
        try {
            if (validarUsuario(authorizationHeader)) {
                article = (Article) em.createQuery("SELECT e FROM Article e WHERE e.id = :id").setParameter("id", id).getSingleResult();
            } else {
                article = (Article) em.createQuery("SELECT e FROM Article e WHERE e.id = :id AND e.privat = FALSE").setParameter("id", id).getSingleResult();
                if (article == null) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
            }
        } catch (NoResultException e) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        
        article.visualitzar();
        em.merge(article);
        
        Map<String, Object> dadesArticle = new HashMap<>();
                    Map<String, Object> autor = new HashMap<>();
                    autor.put("correu", article.getAutor().getCorreu());
                    autor.put("edat", article.getAutor().getEdat());
                    autor.put("id", article.getAutor().getId());
                    autor.put("name", article.getAutor().getName());
                    
                dadesArticle.put("autor", autor);
                dadesArticle.put("id", article.getId());
                dadesArticle.put("imatge", article.getImatge());
                dadesArticle.put("privat", article.isPrivat());
                dadesArticle.put("resum", article.getResum());
                dadesArticle.put("topicos", article.getTopicos());
                dadesArticle.put("visualitzacions", article.getVisualitzacions());
                dadesArticle.put("dataPublicacio", article.getDataPublicacio());
                dadesArticle.put("titol", article.getTitol());
                
                
        return Response.ok(dadesArticle).build();
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
    
    @DELETE
    @Secured
    @Path("{id}")
    public Response deleteArticle (@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathParam("id") Long id) {
        Article articulo = null;
        try {
            articulo =  (Article) em.createQuery("SELECT e FROM Article e WHERE e.id = :id").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NO_CONTENT).entity("No existe este id.").build();
        }
        
        String autor = articulo.getAutor().getCredentials().getUsername();
        if (validarAutor(authorizationHeader).equals(autor)) {
            em.remove(articulo);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No eres el autor.").build();
        }
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
    
    @POST
    @Secured
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createArticle(@HeaderParam("Authorization") String authorizationHeader, Article article) {
        if (!validarUsuario(authorizationHeader)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No estás validado.").build();
        }
        
        List<String> topics = em.createQuery("SELECT a.name FROM Topic a").getResultList();
        
        if (article.getTopicos().size() != 2) {
            return Response.status(Response.Status.BAD_REQUEST).entity("El articulo debe tener 2 topicos").build();
        }
        
        Topic topic1 = article.getTopicos().get(0);
        Topic topic2 = article.getTopicos().get(1);
        
        if (topics.contains(topic1.getName()) && topics.contains(topic2.getName())) {
            article.setDataPublicacio(LocalDate.now().toString());
            
            String username = validarAutor(authorizationHeader);
            Usuari user = (Usuari) em.createQuery("SELECT u FROM Usuari u LEFT JOIN Credentials c WHERE u.credenciales.id = c.id AND c.username = :username").setParameter("username", username).getSingleResult();
            article.setAutor(user);
            
            em.persist(article);
            return Response.status(201).entity(article.getId()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Topico no encontrado.").build();
        }
    }
 
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
