package model.entities;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Entity
@XmlRootElement
public class Article implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name="Article_Gen", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Article_Gen")
    private Long id;
    private String imatge;
    @NotNull
    @ManyToOne
    private Usuari autor;
    @NotNull
    private String titol;
    private String resum;
    private String dataPublicacio;
    private int visualitzacions;
    private boolean privat = false;
    @ManyToMany
    private List<Topic> topicos = new ArrayList<>();
    
    public Article() {}

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public List<Topic> getTopicos() {
        return topicos;
    }

    public void setTopicos(List<Topic> topicos) {
        this.topicos = topicos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImatge() {
        return imatge;
    }

    public void setImatge(String imatge) {
        this.imatge = imatge;
    }

    public Usuari getAutor() {
        return autor;
    }

    public void setAutor(Usuari autor) {
        this.autor = autor;
    }

    public String getResum() {
        return resum;
    }

    public void setResum(String resum) {
        this.resum = resum;
    }

    public String getDataPublicacio() {
        return dataPublicacio;
    }

    public void setDataPublicacio(String dataPublicacio) {
        this.dataPublicacio = dataPublicacio;
    }
    
    public int getVisualitzacions() {
        return visualitzacions;
    }

    public void setVisualitzacions(int visualitzacions) {
        this.visualitzacions = visualitzacions;
    }
    
    public void visualitzar () {
        visualitzacions++;
    }

    public boolean isPrivat() {
        return privat;
    }

    public void setPrivat(boolean privat) {
        this.privat = privat;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals (Object object) {
        if (!(object instanceof Article)) {
            return false;
        }
        Article other = (Article) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
}
