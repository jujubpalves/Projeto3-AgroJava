import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;

public class Projeto3Agrojava {

    static Scanner entrada = new Scanner(System.in);
    static String arquivo = "historico_chuvas.csv";

    static double[] chuvas = new double[7];

    static String[] dias = {
            "Segunda-feira", "Terça-feira", "Quarta-feira",
            "Quinta-feira", "Sexta-feira", "Sábado", "Domingo"
    };

    static double[][] umidade = new double[4][4];

    static boolean dadosCadastrados = false;
    static String dataSemana;

    public static void main(String[] args) {

        entrada.useLocale(Locale.US);

        int opcao;

        do {
            System.out.println();
            System.out.println("==========================================");
            System.out.println("       AGROJAVA - AGROJAVA ANALYTICS");
            System.out.println("==========================================");
            System.out.println("1 - Cadastrar dados (Chuvas e Umidade)");
            System.out.println("2 - Exibir mapa do campo e relatório de chuvas");
            System.out.println("3 - Relatório de alertas de irrigação");
            System.out.println("4 - Consultar histórico de chuvas");
            System.out.println("5 - Identificar semana mais chuvosa");
            System.out.println("6 - Dashboard climático");
            System.out.println("7 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = entrada.nextInt();

            switch (opcao) {

                case 1:
                    cadastrarDados();
                    break;

                case 2:
                    exibirRelatorio();
                    break;

                case 3:
                    exibirAlertas();
                    break;

                case 4:
                    consultarHistorico();
                    break;

                case 5:
                    semanaMaisChuvosa();
                    break;

                case 6:
                    dashboardClimatico();
                    break;

                case 7:
                    System.out.println("Encerrando o sistema. Até logo!");
                    break;

                default:
                    System.out.println("Opção inválida! Escolha um número entre 1 e 7.");
            }

        } while (opcao != 7);

        entrada.close();
    }

    // RF04 - Cadastrar dados e registrar semana no CSV
    public static void cadastrarDados() {

        entrada.nextLine();

        System.out.println();
        System.out.println("========== CADASTRO DE PLUVIOSIDADE ==========");

        System.out.print("Informe a data da semana (dd/MM/yyyy): ");
        dataSemana = entrada.nextLine();

        double somaChuva = 0;

        for (int i = 0; i < 7; i++) {

            System.out.print("Volume de chuva em " + dias[i] + " (mm): ");

            chuvas[i] = entrada.nextDouble();

            somaChuva += chuvas[i];
        }

        double mediaChuva = somaChuva / 7;

        System.out.println();
        System.out.println("========== CADASTRO DE UMIDADE ==========");

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {

                System.out.print("Informe a umidade do talhão ["
                        + (i + 1) + "][" + (j + 1) + "] (%): ");

                umidade[i][j] = entrada.nextDouble();
            }
        }

        dadosCadastrados = true;

        salvarCSV(mediaChuva, somaChuva);

        System.out.println();
        System.out.println("Dados cadastrados com sucesso!");
        System.out.println("Média semanal: " + mediaChuva + " mm");
        System.out.println("Total de precipitação: " + somaChuva + " mm");
    }

    // RF04 - Salvar os dados no arquivo CSV
    public static void salvarCSV(double media, double total) {

        File arquivoCSV = new File(arquivo);

        try {

            FileWriter escritor = new FileWriter(arquivoCSV, true);

            if (arquivoCSV.length() == 0) {
                escritor.write("data,seg,ter,qua,qui,sex,sab,dom,media,total\n");
            }

            escritor.write(dataSemana);

            for (int i = 0; i < 7; i++) {
                escritor.write("," + chuvas[i]);
            }

            escritor.write("," + media + "," + total + "\n");

            escritor.close();

            System.out.println("Registro salvo no histórico de chuvas!");

        } catch (IOException e) {

            System.out.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    // RF05 - Consultar histórico de chuvas
    public static void consultarHistorico() {

        System.out.println();
        System.out.println("========== HISTÓRICO DE CHUVAS ==========");

        try {

            BufferedReader leitor = new BufferedReader(
                    new FileReader(arquivo));

            String linha;

            leitor.readLine(); // Ignora o cabeçalho

            boolean encontrouRegistro = false;

            while ((linha = leitor.readLine()) != null) {

                String[] dados = linha.split(",");

                if (dados.length >= 10) {

                    encontrouRegistro = true;

                    System.out.println();
                    System.out.println("Semana: " + dados[0]);
                    System.out.println("Média: " + dados[8] + " mm");
                    System.out.println("Total: " + dados[9] + " mm");
                    System.out.println("------------------------------------------");
                }
            }

            leitor.close();

            if (!encontrouRegistro) {
                System.out.println("Nenhum registro encontrado.");
            }

        } catch (IOException e) {

            System.out.println("Não foi possível acessar o histórico.");
        }
    }

    // RF06 - Identificar semana mais chuvosa
    public static void semanaMaisChuvosa() {

        System.out.println();
        System.out.println("========== SEMANA MAIS CHUVOSA ==========");

        try {

            BufferedReader leitor = new BufferedReader(
                    new FileReader(arquivo));

            String linha;

            leitor.readLine();

            double maiorTotal = -1;
            String dataMaior = "";
            boolean encontrouRegistro = false;

            while ((linha = leitor.readLine()) != null) {

                String[] dados = linha.split(",");

                if (dados.length >= 10) {

                    double total = Double.parseDouble(dados[9]);

                    if (total > maiorTotal) {

                        maiorTotal = total;
                        dataMaior = dados[0];
                    }

                    encontrouRegistro = true;
                }
            }

            leitor.close();

            if (encontrouRegistro) {

                System.out.println("Semana mais chuvosa: " + dataMaior);
                System.out.println("Total de precipitação: "
                        + maiorTotal + " mm");

            } else {

                System.out.println("Nenhuma semana cadastrada.");
            }

        } catch (IOException e) {

            System.out.println("Não foi possível acessar o histórico.");
        }
    }

    // RF07 - Dashboard climático
    public static void dashboardClimatico() {

        System.out.println();
        System.out.println("========== DASHBOARD CLIMÁTICO ==========");

        try {

            BufferedReader leitor = new BufferedReader(
                    new FileReader(arquivo));

            String linha;

            leitor.readLine();

            int quantidadeSemanas = 0;

            double volumeAcumulado = 0;
            double maiorChuva = -1;
            double menorChuva = Double.MAX_VALUE;

            String dataMaior = "";
            String dataMenor = "";

            while ((linha = leitor.readLine()) != null) {

                String[] dados = linha.split(",");

                if (dados.length >= 10) {

                    double total = Double.parseDouble(dados[9]);

                    quantidadeSemanas++;

                    volumeAcumulado += total;

                    if (total > maiorChuva) {

                        maiorChuva = total;
                        dataMaior = dados[0];
                    }

                    if (total < menorChuva) {

                        menorChuva = total;
                        dataMenor = dados[0];
                    }
                }
            }

            leitor.close();

            if (quantidadeSemanas > 0) {

                double mediaGeral = volumeAcumulado / quantidadeSemanas;

                System.out.println("Semanas cadastradas: " + quantidadeSemanas);

                System.out.println("Volume acumulado: "
                        + volumeAcumulado + " mm");

                System.out.println();
                System.out.println("SEMANA MAIS CHUVOSA:");
                System.out.println("Data: " + dataMaior);
                System.out.println("Total: " + maiorChuva + " mm");

                System.out.println();
                System.out.println("SEMANA MAIS SECA:");
                System.out.println("Data: " + dataMenor);
                System.out.println("Total: " + menorChuva + " mm");

                System.out.println();
                System.out.println("Média geral das semanas: "
                        + mediaGeral + " mm");

            } else {

                System.out.println("Nenhuma semana cadastrada.");
            }

        } catch (IOException e) {

            System.out.println("Não foi possível acessar o histórico.");
        }
    }

    // Exibir relatório semanal e mapa de umidade
    public static void exibirRelatorio() {

        if (!dadosCadastrados) {

            System.out.println("Cadastre os dados primeiro selecionando a opção 1.");
            return;
        }

        double somaChuva = 0;
        double maiorChuva = chuvas[0];
        int diaMaiorChuva = 0;

        for (int i = 0; i < 7; i++) {

            somaChuva += chuvas[i];

            if (chuvas[i] > maiorChuva) {

                maiorChuva = chuvas[i];
                diaMaiorChuva = i;
            }
        }

        double mediaChuva = somaChuva / 7;

        System.out.println();
        System.out.println("========== RELATÓRIO DE PLUVIOSIDADE ==========");

        System.out.println("Média semanal de precipitação: "
                + mediaChuva + " mm");

        System.out.println("Total de precipitação: "
                + somaChuva + " mm");

        System.out.println("Dia com maior índice de chuva: "
                + dias[diaMaiorChuva] + " (" + maiorChuva + " mm)");

        System.out.println();
        System.out.println("========== MAPA DE UMIDADE DO CAMPO ==========");

        System.out.println("            Col 1   Col 2   Col 3   Col 4");

        for (int i = 0; i < 4; i++) {

            System.out.print("Talhão " + (i + 1) + "   ");

            for (int j = 0; j < 4; j++) {

                System.out.print(umidade[i][j] + "    ");
            }

            System.out.println();
        }
    }

    // Exibir alertas de irrigação
    public static void exibirAlertas() {

        if (!dadosCadastrados) {

            System.out.println("Cadastre os dados primeiro selecionando a opção 1.");
            return;
        }

        System.out.println();
        System.out.println("========== RELATÓRIO DE ALERTAS ==========");

        boolean alertaEncontrado = false;

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {

                if (umidade[i][j] < 30.0) {

                    System.out.println("ALERTA: O talhão ["
                            + (i + 1) + "][" + (j + 1)
                            + "] está com " + umidade[i][j]
                            + "% de umidade e necessita de irrigação imediata!");

                    alertaEncontrado = true;
                }
            }
        }

        if (!alertaEncontrado) {

            System.out.println("Todos os talhões estão com umidade adequada.");
            System.out.println("Nenhuma irrigação necessária no momento.");
        }
    }
}