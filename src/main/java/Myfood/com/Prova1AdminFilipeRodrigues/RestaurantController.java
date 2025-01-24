package Myfood.com.Prova1AdminFilipeRodrigues;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class RestaurantController {
    //vamos ter que colocar a session para relacionar com o outro projeto? ACHO QUE NÃO

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    CardapioRepository cardapioRepository;      //pesei em criar uma classe separada só para controlar cardapio, mas poderia dar problema.

    @GetMapping(value={"/listar-restaurante"})              //lista os restaurantes existentes
    public String mostrarListaDeRestaurantes(Model model){
        model.addAttribute("restaurantes", restaurantRepository.findAll());  //variavel enviada, repositorio com informações
        return "listar-restaurante"; //chama a pagina
    }

    @GetMapping("/novo-restaurante")
    public String MostrarFormNovoRestaurante(Restaurante restaurante){
        return "novo-restaurante";              //chama a pagina novo restaurante
    }

    @GetMapping(value={"/index", "/"})       //vai para pagina principal
    public String mostrarPaginaPrincipal(){
        return "index";
    }

    @PostMapping("/adicionar-restaurante")       //adiciona o novo restaurante
    public String adicionarRestaurante(@Valid Restaurante restaurante, BindingResult result){
        if(result.hasErrors()){
            return "/novo-restaurante";
        }
        restaurantRepository.save(restaurante);
        return "redirect:/listar-restaurante";
    }



    @GetMapping("/editar-restaurante/{id}")
    public String mostrarFormatualizarRestaurante(@PathVariable("id") int id, Model model){
        Restaurante restaurante = restaurantRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("O id do restaurante é invalido:" + id));
        model.addAttribute("restaurante", restaurante);   //coloca o conteudo de restaurant dentro do atributo restaurante
        return "atualizar-restaurante";
    }






    @PostMapping("/atualizar-restaurante/{id}")
    public String atualizarRestaurante(@PathVariable("id") int id, @Valid Restaurante restaurante,
                                       BindingResult result, Model model, HttpServletRequest request){

        if(result.hasErrors()) {
            restaurante.setId(id);
            return "atualizar-restaurante";
        }

        restaurantRepository.save(restaurante);
        return "redirect:/listar-restaurante";
    }






    @GetMapping("/remover-restaurante/{id}")
    public String removerRestaurante(@PathVariable("id") int id){
        Restaurante restaurante = restaurantRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + id));

        //excluir todos os cardapios deste restaurante
        for(Cardapio cardapio : restaurante.getCardapio()){
            cardapioRepository.delete(cardapio);
        }



        restaurantRepository.delete(restaurante);
        return "redirect:/listar-restaurante";
    }



    @GetMapping("/listar-cardapio/{id}")
    public String listarCardapio(@PathVariable("id") int id, Model model){
        Restaurante restaurante = restaurantRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + id));//encontra o restaurante de quem pediu para mostrar o cardapio

        model.addAttribute("cardapios",restaurante.getCardapio()); //coloco todos os cardapios desse restaurante no model para enviar para a view. (não sei se funciona);
        model.addAttribute("restaurante",restaurante);             // envie também o restaurante para saber de quem é o cardapio
        return"listar-cardapio";    //chama a pagina, então não tem /
    }



    @GetMapping("/novo-cardapio/{id}")
    public String mostrarFormNovoCardapio(Cardapio cardapio,@PathVariable("id") int id, Model model){
        Restaurante restaurante = restaurantRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("o ID do restaurante é invalido:" + id));
        model.addAttribute("restaurante",restaurante);
        return "novo-cardapio";
    }

    @PostMapping("/adicionar-cardapio/{id}")        //o ID é do restaurante, não do cardapio
    public String adicionarCardapio(@PathVariable("id") int id,@Valid Cardapio cardapio,
                                    BindingResult result, Model model, HttpServletRequest request){

        if(result.hasErrors()){
            return "/novo-cardapio/"+id;
        }

        Restaurante restaurante = restaurantRepository.findById(id).orElseThrow(() -> new IllegalIdentifierException("O id do restaurante é invalido:"+id));


        List<Cardapio> aux = restaurante.getCardapio();
        if(aux == null){
            aux = new ArrayList<Cardapio>();
        }
        aux.add(cardapio);
        restaurante.setCardapio(aux);


        /*if(restaurante.getCardapio()==null){
            List<Cardapio> aux = new ArrayList<Cardapio>();
            restaurante.setCardapio(aux);
        }

        List<Cardapio> aux = restaurante.getCardapio();
        aux.add(cardapio);
        restaurante.setCardapio(aux);*/

        cardapio.setRestaurante(restaurante);
        cardapioRepository.save(cardapio);  //aqui ele deu o id, o objeto cardapio ja tem id também.
        restaurantRepository.save(restaurante);     // para ter certeza que esta salvando restaurante

        //int id2 = cardapio.getRestaurante().getId();  // teste para saber se esta salvando

        return "redirect:/listar-cardapio/"+id;
    }


    @GetMapping("/editar-cardapio/{id}")    //vem antes do atualizar, ele chama o form para fazer a edição.
    public String mostrarFormParaEditarCardapio(@PathVariable("id") int id, Model model){       // Como vai passar um item para a view, precisa do model
        Cardapio cardapio = cardapioRepository.findById(id).orElseThrow(() -> new IllegalIdentifierException("O id do produto é invalido:"+id));
        model.addAttribute("cardapio", cardapio);

        return "atualizar-cardapio";
    }


    @PostMapping("/atualizar-cardapio/{id}")
    public String atualizarCardapio(@PathVariable("id") int id, @Valid Cardapio cardapio,BindingResult result,Model model, HttpServletRequest request){ //result para verificar se tem erro, @valid?

        if(result.hasErrors()){
            cardapio.setId(id);
            return "atualizar-cardapio";
        }

        Cardapio cardapioAux = cardapioRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("id do carcapio é invalida: "+id));
        Restaurante restaurante = cardapioAux.getRestaurante();

        cardapio.setRestaurante(cardapioAux.getRestaurante());      //cardapio vindo do formulario esta sem restaurante, então foi preciso repor
        cardapioRepository.save(cardapio);      //salva as alterações feitas no cardapio

        return "redirect:/listar-cardapio/"+restaurante.getId();
    }


    @GetMapping("/remover-cardapio/{id}")
    public String removeRestaurante(@PathVariable("id") int id){
        Cardapio cardapio = cardapioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id do cardapio é invalido: "+id));

        Restaurante restaurante = cardapio.getRestaurante();
        List<Cardapio> cardapios = restaurante.getCardapio();
        cardapios.remove(cardapio);

        cardapioRepository.delete(cardapio);

        return "redirect:/listar-cardapio/"+restaurante.getId();
    }





}
