package Myfood.com.Prova1AdminFilipeRodrigues;

import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;      // gera os valores da chave principal
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;     // impede que o atributo fique nulo ou vazio, isso ocorre com o nome
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name="ITEM_CARDAPIO")
public class Cardapio implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank                           //só serve para strings
    private String nome;
    private String descricao;
    @NotNull(message = "O preço não pode ser nulo") //quando se trata de numeros deve usar NotNull
    private double preco;
    //private int id_restaurante;

    @JoinColumn(name="id_restaurante", nullable = false)
    @ManyToOne
    private Restaurante restaurante;


    public Cardapio(){}

    public void setId(int id){this.id = id;}

    public int getId(){return id;}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    //public int getId_restaurante() {return id_restaurante;}



    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }
}
