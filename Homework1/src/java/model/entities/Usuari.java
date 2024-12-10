package model.entities;

import authn.Credentials;
import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
public class Usuari implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name="Usuari_Gen", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Usuari_Gen")
    private Long id;
    @OneToOne
    private Credentials credenciales;
    @NotNull
    private String name;
    private Long edat;
    private String correu;

    public Long getEdat() {
        return edat;
    }

    public void setEdat(Long edat) {
        this.edat = edat;
    }

    public String getCorreu() {
        return correu;
    }

    public void setCorreu(String correu) {
        this.correu = correu;
    }
    
    public Usuari () {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    //@XmlTransient
    public Credentials getCredentials () {
        return credenciales;
    }
    
    public void setCredentials (Credentials credenciales) {
        this.credenciales = credenciales;
    }


}
