/*
* Trabalho de AEDs desenvolvido por Izabela Cecilia(780998) e Vinicius Tavares(784759)
* Esse trabalho visa fazer um tipo de controladoria de produtos.
*/
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;//bibliotecas usadas para verificar se o arquivo ja existe
import java.nio.file.Path;
import java.nio.file.Paths;

public class Trabalho {

    /*
     * Verifica se o arquivo com o nome X existe, caso ele exista ele retorna false.
     */
    private static boolean arquivoExiste(String nomeArquivo) {
        Path end = Paths.get(nomeArquivo); // converte o nome de arquivo em um endereco
        if (Files.exists(end)) {// se o arquivo for encontrado no endereco
            return true;
        }
       // se o arquivo nao foi encontrado
        return false;
    }

    /*
     * modulo que cria relatorios das vendas, recendo como parametro a matriz de
     * produtos já modificada com os valores de vendas feitas.
     * enquanto o while der true ele vai criando nomes diferentes para o relatorio,
     * preferi isso para ter novos relatorios atualizados sem ficar apagando e
     * escrevendo no mesmo
     */
    public static void criaRelatorioVendas(int[][] produtos) {

        // em cada linha desse vetor vai ficar o lucro de um produto, sendo a linha 5 o
        // lucro total

        int[] lucro = new int[5];

        calculaLucro(produtos, lucro);
        String nomeArquivo = "RelatorioVendas.txt";
        int i = 1;
        while (arquivoExiste(nomeArquivo)) {
            nomeArquivo = "Relatorio Vendas n" + i + ".txt";
            System.out.println(nomeArquivo);
            i++;
        }
        try (BufferedWriter arquivoRelatorio = new BufferedWriter(new FileWriter(new File(nomeArquivo)))) {
            for (int p = 0; p < 4; p++) {
                for (int j = 0; j < 5; j++) {

                    arquivoRelatorio.write(produtos[p][j] + ";");
                }

                arquivoRelatorio.write("Lucro no produto: " + lucro[p]);
                arquivoRelatorio.newLine();
            }
            arquivoRelatorio.write("Lucro total: " + lucro[4]);
            arquivoRelatorio.flush();
        } catch (IOException e) {
            System.err.println(nomeArquivo);
        }
    }

    public static void calculaLucro(int[][] produtos, int[] Lucro) {
        int lMascara;
        // lucro por mascara
        for (int ln = 0; ln < 4; ln++) {
            lMascara = produtos[ln][4] - produtos[ln][3];
            Lucro[ln] = lMascara * (produtos[ln][2]);
        }
        // resetando
        lMascara = 0;
        // lucro total
        for (int i = 0; i < 4; i++) {
            lMascara += Lucro[i];
        }
        Lucro[4] = lMascara;
    }

    // verifica quantas mascaras o usuario quer comprar
    public static int desejaComprar(Scanner ent) {
        int qntCompra;
        System.out.println("Quantos você deseja comprar?");
        qntCompra = ent.nextInt();
        return qntCompra;
    }

    /*
     * modulo que verifica se tem no estoque a quantidade que a pessoa deseja
     * comprar de determinado produto, se tiver ele retorna true, caso não tenha ele
     * retorna false.
     */
    public static boolean verificaEstoque(int qntCompraN, int tipoProduto, int[][] produtos) {
        boolean temEstoque = false;
        if (produtos[tipoProduto][1] >= qntCompraN) {
            temEstoque = true;
            return temEstoque;
        }
        System.out.println("Fora de estoque");
        return temEstoque;
    }
    //Realiza a subtração e adição de produtos na matriz
    public static void manipulaProdutos(int[][] produtos, int opt, int qntCompra) {
        produtos[opt][1] -= qntCompra;
        produtos[opt][2] += qntCompra;
    }

    public static void menu(Scanner ent, int[][] produtos) {

        // variavel para quantidade que a pessoa deseja comprar
        int qntCompra, opt;
        System.out.println("======================Menu===========================");

        System.out.println(
                "0-Mascara Infantil Lisa\n1-Mascara Infantil Estampada\n2-Mascara Adulta Lisa\n3-Mascara Adulta Estampada\n4-Gerar Relatório de vendas");
        opt = ent.nextInt();

        /*
         * Se o valor retorna for true ele vai na matriz na linha onde se encontra o
         * produto entra na coluna que está o estoque, subtrai o valor a ser comprado
         * e depois vai no local onde está os produtos a serem vendidos e adciona o
         * valor a ser comprado, no vetor total de vendas, ele está colocando na posição
         * dos produtos
         * o total de vendas feitas, posteriormente será usado para gerar o relatorio de
         * vendas.
         */
        if (opt == 0 || opt == 1 || opt == 2 || opt == 3) {
            qntCompra = desejaComprar(ent);
            if (verificaEstoque(qntCompra, opt, produtos)) {
                manipulaProdutos(produtos, opt, qntCompra);
            }
        } else if (opt == 4) {
            criaRelatorioVendas(produtos);
            System.out.println("Relatório de Vendas gerado");
        }
        else {
        System.out.println("Nenhum produto selecionado");
        }

    }

    public static void main(String[] args) {
        
        Scanner ent = new Scanner(System.in);
        int[][] produtos = new int[4][5];
        String linha = null;
        String[] data;
        int quantidadeP, tipo, vendas, custo, pvenda;
        int control = 1;

        StringBuilder sb = new StringBuilder("s");
        char resposta = sb.charAt(0);
        String diretorio = "C:\\Users\\sucas\\Desktop\\trabalho\\baseProdutos.txt";
        try {
            File arqProdutos = new File(diretorio);
            Scanner entArqProdutos = new Scanner(arqProdutos);
            while (entArqProdutos.hasNextLine()) {
                linha = entArqProdutos.nextLine();
                // separa os ; que estão na linha e guarda em um vetor as posições separadas.
                data = linha.split(";");
                /*
                 * /*
                 * Os comandos abaixo salvam os tipos na matriz.
                 */
                tipo = Integer.parseInt(data[0]);
                quantidadeP = Integer.parseInt(data[1]);
                vendas = Integer.parseInt(data[2]);
                custo = Integer.parseInt(data[3]);
                pvenda = Integer.parseInt(data[4]);
                produtos[tipo][0] = tipo;
                produtos[tipo][1] = quantidadeP;
                produtos[tipo][2] = vendas;
                produtos[tipo][3] = custo;
                produtos[tipo][4] = pvenda;

                // a quantidade de produto é a unica coisa que no programa em si vai alterar,
                // logo, vamos guarda-la em um vetor para acesso posterior.
            }
            entArqProdutos.close();
        } catch (IOException r) {
            System.out.println("Falha na leitura");
        }
        /*
         * verifica se usuario quer continuar no programa
         */

        while (resposta == 's') {
            menu(ent, produtos);
            System.out.println("Você deseja continuar\n(S)Sim --- (N)Nao");
            ent.nextLine();
            sb.append(ent.nextLine().toLowerCase());
            resposta = sb.charAt(control);
            control++;
        }
        criaRelatorioVendas(produtos);


        ent.close();
    }

}