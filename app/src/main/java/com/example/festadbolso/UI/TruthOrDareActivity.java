package com.example.festadbolso.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.festadbolso.R;
import java.util.ArrayList;
import java.util.Random;

public class TruthOrDareActivity extends AppCompatActivity {

    private Button truthButton, dareButton;
    private ArrayList<String> truths, dares;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truth_or_dare);

        truthButton = findViewById(R.id.truthButton);
        dareButton = findViewById(R.id.dareButton);

        random = new Random();
        populateLists();

        // Set initial texts
        truthButton.setText("Carregue para uma Verdade");
        dareButton.setText("Carregue para uma Consequência");

        // Set click listeners to get random truths and dares
        truthButton.setOnClickListener(v -> truthButton.setText(getRandomTruth()));
        dareButton.setOnClickListener(v -> dareButton.setText(getRandomDare()));
    }

    private void populateLists() {
        truths = new ArrayList<>();
        dares = new ArrayList<>();

        // Add some truths
// Adicionando perguntas de "verdade"
        truths.add("Qual é o teu maior arrependimento?");
        truths.add("Quem foi a última pessoa que pesquisaste nas redes sociais?");
        truths.add("Conta-nos todos os detalhes do teu primeiro beijo.");
        truths.add("Qual é o teu maior medo?");
        truths.add("Já colaste em algum teste?");
        truths.add("Já roubaste alguma coisa?");
        truths.add("Qual é o teu prazer culposo em filmes?");
        truths.add("Qual foi o teu encontro mais embaraçoso?");
        truths.add("Se tivesses três desejos, quais seriam?");
        truths.add("Quantas selfies tiras por dia?");
        truths.add("Acreditas em alguma superstição?");
        truths.add("Já mentiste sobre a tua idade?");
        truths.add("Qual é a tua habilidade secreta?");
        truths.add("Quem foi a tua primeira paixão por uma celebridade?");
        truths.add("Onde te vês daqui a cinco anos?");
        truths.add("Qual é a tua linguagem do amor?");
        truths.add("Quem é 'aquele(a) que escapou'?");
        truths.add("Qual foi o lugar mais estranho onde já foste à casa de banho?");
        truths.add("Já desmaiaste de tanto beber?");
        truths.add("Quais são as cinco coisas que levarias para uma ilha deserta?");
        truths.add("De quem estás a fim?");
        truths.add("Qual foi o momento mais constrangedor da tua vida?");
        truths.add("Qual foi a coisa mais estúpida que já fizeste na vida?");
        truths.add("Quem levarias para uma ilha deserta?");
        truths.add("Já estiveste interessado(a) em alguém que está neste grupo?");
        truths.add("Qual é o teu maior sonho?");
        truths.add("Qual é o teu maior segredo?");
        truths.add("Qual é a tua maior fraqueza?");
        truths.add("Qual é a tua maior qualidade?");
        truths.add("Qual é a tua maior vergonha?");
        truths.add("Qual é a tua maior conquista?");
        truths.add("Qual é a tua maior decepção?");
        truths.add("Qual é a tua maior frustração?");
        truths.add("Qual é a tua memória de infância favorita?");
        truths.add("Já traíste alguém?");
        truths.add("Qual é a tua comida favorita?");
        truths.add("Qual é o teu filme favorito?");
        truths.add("Qual é a tua música favorita?");
        truths.add("Qual é o teu livro favorito?");
        truths.add("Qual é o teu lugar favorito no mundo?");
        truths.add("Qual é o teu animal favorito?");
        truths.add("Qual é a tua cor favorita?");
        truths.add("Qual é o teu hobby favorito?");
        truths.add("Qual é o teu desporto favorito?");
        truths.add("Qual é o teu jogo favorito?");
        truths.add("Qual é a tua série de TV favorita?");
        truths.add("Qual é o teu ator/atriz favorito(a)?");
        truths.add("Qual é o teu cantor(a) favorito(a)?");
        truths.add("Qual é a tua banda favorita?");
        truths.add("Qual é o teu prato favorito?");
        truths.add("Qual é a tua bebida favorita?");
        truths.add("Qual é o teu restaurante favorito?");
        truths.add("Qual é o teu destino de férias favorito?");
        truths.add("Qual é o teu cheiro favorito?");
        truths.add("Qual é o teu sabor favorito?");
        truths.add("Qual é a tua estação do ano favorita?");
        truths.add("Qual é o teu feriado favorito?");
        truths.add("Qual é o teu super-herói favorito?");
        truths.add("Qual é o teu vilão favorito?");
        truths.add("Qual é o teu personagem de desenho animado favorito?");
        truths.add("Qual é o teu personagem de filme favorito?");
        truths.add("Qual é o teu personagem de livro favorito?");
        truths.add("Qual é o teu personagem de série de TV favorito?");
        truths.add("Qual é o teu personagem de videojogo favorito?");
        truths.add("Qual é o teu autor(a) favorito(a)?");
        truths.add("Qual é o teu poeta(a) favorito(a)?");
        truths.add("Qual é o teu artista plástico favorito?");
        truths.add("Qual é o teu cientista favorito?");
        truths.add("Qual é o teu herói da vida real?");
        truths.add("Qual é o teu lema de vida?");
        truths.add("Qual é o teu maior medo?");
        truths.add("Qual é o teu maior sonho?");
        truths.add("Qual é o teu maior segredo?");
        truths.add("Qual é a tua maior fraqueza?");
        truths.add("Qual é a tua maior qualidade?");
        truths.add("Qual é a tua maior vergonha?");
        truths.add("Qual é a tua maior conquista?");
        truths.add("Qual é a tua maior decepção?");
        truths.add("Qual é a tua maior frustração?");
        truths.add("Qual é a tua memória de infância favorita?");
        truths.add("Já traíste alguém?");
        truths.add("Qual é a tua comida favorita?");
        truths.add("Qual é o teu filme favorito?");
        truths.add("Qual é a tua música favorita?");
        truths.add("Qual é o teu livro favorito?");
        truths.add("Qual é o teu lugar favorito no mundo?");
        truths.add("Qual é o teu animal favorito?");
        truths.add("Qual é a tua cor favorita?");
        truths.add("Qual é o teu hobby favorito?");
        truths.add("Qual é o teu desporto favorito?");
        truths.add("Qual é o teu jogo favorito?");
        truths.add("Qual é a tua série de TV favorita?");
        truths.add("Qual é o teu ator/atriz favorito(a)?");
        truths.add("Qual é o teu cantor(a) favorito(a)?");
        truths.add("Qual é a tua banda favorita?");
        truths.add("Qual é o teu prato favorito?");
        truths.add("Qual é a tua bebida favorita?");
        truths.add("Qual é o teu restaurante favorito?");
        truths.add("Qual é o teu destino de férias favorito?");
        truths.add("Qual é o teu cheiro favorito?");
        truths.add("Qual é o teu sabor favorito?");
        truths.add("Qual é a tua estação do ano favorita?");
        truths.add("Qual é o teu feriado favorito?");
        truths.add("Qual é o teu super-herói favorito?");
        truths.add("Qual é o teu vilão favorito?");
        truths.add("Qual é o teu personagem de desenho animado favorito?");
        truths.add("Qual é o teu personagem de filme favorito?");
        truths.add("Qual é o teu personagem de livro favorito?");
        truths.add("Qual é o teu personagem de série de TV favorito?");
        truths.add("Qual é o teu personagem de videojogo favorito?");
        truths.add("Qual é o teu autor(a) favorito(a)?");
        truths.add("Qual é o teu poeta(a) favorito(a)?");
        truths.add("Qual é o teu artista plástico favorito?");
        truths.add("Qual é o teu cientista favorito?");
        truths.add("Qual é o teu herói da vida real?");
        truths.add("Qual é o teu lema de vida?");


        // Add some dares
// Adicionando desafios
        dares.add("Dança como um robô durante um minuto.");
        dares.add("Imita um professor da escola.");
        dares.add("Diz o alfabeto ao contrário.");
        dares.add("Faz 10 flexões.");
        dares.add("Canta uma música infantil como se estivesses num concerto de ópera.");
        dares.add("Faz uma careta e mantém durante 30 segundos.");
        dares.add("Faz o moonwalk como o Michael Jackson.");
        dares.add("Dá uma volta completa sobre ti mesmo(a) cinco vezes e depois tenta caminhar em linha reta.");
        dares.add("Diz um trava-línguas três vezes sem errar.");
        dares.add("Conta uma piada muito má.");
        dares.add("Dança sem música durante 1 minuto.");
        dares.add("Publica uma mensagem embaraçosa numa rede social.");
        dares.add("Envia um emoji aleatório para a última pessoa com quem falaste no telemóvel.");
        dares.add("Faz um discurso emocionante sobre uma colher.");
        dares.add("Tira uma selfie engraçada e envia para o grupo.");
        dares.add("Anda de gatas pela sala.");
        dares.add("Tenta equilibrar um objeto na cabeça por 30 segundos.");
        dares.add("Lambe o teu cotovelo (ou pelo menos tenta).");
        dares.add("Dança como um frango durante 1 minuto.");
        dares.add("Imita um personagem de desenho animado até alguém adivinhar quem é.");
        dares.add("Finge que és um pirata durante os próximos 5 minutos.");
        dares.add("Faz uma declaração de amor para um objeto próximo.");
        dares.add("Sai à rua e grita ‘Sou incrível!’.");
        dares.add("Fecha os olhos e deixa outra pessoa desenhar algo no teu rosto.");
        dares.add("Tenta dizer três palavras começando com ‘Z’ em menos de 10 segundos.");
        dares.add("Deixa outra pessoa escolher uma nova foto de perfil para ti.");
        dares.add("Escreve com a mão não dominante durante um minuto.");
        dares.add("Conta uma história engraçada da tua infância.");
        dares.add("Faz uma pose de super-herói durante dois minutos.");
        dares.add("Come uma colher de mostarda ou ketchup.");
        dares.add("Equilibra uma colher no nariz por 30 segundos.");
        dares.add("Imita o teu animal favorito.");
        dares.add("Diz algo romântico para a primeira pessoa que olhares nos olhos.");
        dares.add("Corre pela casa como um avião durante 1 minuto.");
        dares.add("Deixa alguém enviar uma mensagem de voz no teu telefone.");
        dares.add("Liga para um número aleatório e canta ‘Parabéns a você’.");
        dares.add("Faz um penteado engraçado e mantém durante o resto do jogo.");
        dares.add("Tira os sapatos e faz um vídeo a dançar de meias.");
        dares.add("Dá um nome e personalidade a um objeto próximo.");
        dares.add("Tenta tocar bateria na mesa sem rir durante 30 segundos.");
        dares.add("Faz uma imitação do teu amigo mais próximo.");
        dares.add("Conta até 20 de olhos fechados sem perder a conta.");
        dares.add("Faz um elogio sincero a cada jogador.");
        dares.add("Escreve uma palavra na testa com caneta.");
        dares.add("Come algo sem usar as mãos.");
        dares.add("Abraça uma almofada como se fosse um amigo perdido.");
        dares.add("Lambe o cotovelo de alguém (ou tenta).");
        dares.add("Coloca uma meia nas mãos e finge que é um fantoche.");
        dares.add("Finge que és um apresentador de televisão.");
        dares.add("Corre no lugar como se estivesses a fugir de um monstro.");
        dares.add("Faz uma dança da vitória depois de cada turno.");
        dares.add("Coloca um cubo de gelo no teu bolso e mantém até derreter.");
        dares.add("Coloca um copo de água na cabeça e tenta caminhar sem derramar.");
        dares.add("Coloca um sapato na cabeça e tenta equilibrá-lo.");
        dares.add("Desenha um bigode na tua cara com um marcador lavável.");
        dares.add("Bebe um copo de água de olhos fechados sem derramar.");
        dares.add("Imita um robô durante o próximo minuto.");
        dares.add("Desenha um animal no braço de alguém com uma caneta.");
        dares.add("Joga pedra, papel ou tesoura. Se perderes, tens que dançar.");
        dares.add("Tira uma foto fazendo uma pose super dramática.");
        dares.add("Conta um segredo engraçado sobre ti.");
        dares.add("Sussurra tudo o que disseres durante os próximos três turnos.");
        dares.add("Pede uma música e canta como se estivesses num concurso de talentos.");
        dares.add("Senta-te ao contrário na cadeira pelo resto do jogo.");
        dares.add("Cria e apresenta uma pequena coreografia de dança.");
        dares.add("Canta tudo o que disseres durante os próximos cinco minutos.");
        dares.add("Faz uma pose para foto como se fosses uma estrela de cinema.");
        dares.add("Faz 10 agachamentos enquanto dizes algo engraçado.");
        dares.add("Faz um gesto engraçado sempre que alguém disser ‘sim’.");
        dares.add("Põe óculos escuros e age como um agente secreto.");
        dares.add("Dá um nome à tua mão esquerda e fala com ela como se fosse uma pessoa.");
        dares.add("Diz algo muito formal de forma exageradamente dramática.");
        dares.add("Ajoelha-te e declara amor eterno à primeira pessoa que olhares.");

    }

    private String getRandomTruth() {
        return truths.get(random.nextInt(truths.size()));
    }

    private String getRandomDare() {
        return dares.get(random.nextInt(dares.size()));
    }
}
