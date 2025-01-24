package Myfood.com.Prova1AdminFilipeRodrigues;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;     // impede que o atributo fique nulo ou vazio, isso ocorre com o nome

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name="RESTAURANTE")
public class Restaurante implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "O nome do restaurante é obrigatório.")
    private String nome;

    @OneToMany(mappedBy="restaurante")
    private List<Cardapio> cardapio;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Cardapio> getCardapio() {
        return cardapio;
    }

    public void setCardapio(List<Cardapio> cardapio) {
        this.cardapio = cardapio;
    }
    
}
