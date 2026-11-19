import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class Projeto3Agrojava {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        double[] chuvas = new double[7];
        String[] dias = {
                "Segunda-feira", "Terça-feira", "Quarta-feira",
                "Quinta-feira", "Sexta-feira", "Sábado", "Domingo"
        };
        double[][] umidade = new double[4][4];

        boolean dadosCadastrados = false;
        int opcao;

        do {
            System.out.println("AGROJAVA - Sistema Integrado de Agronegócio");
            System.out.println();
            System.out.print("Seja bem-vindo(a) ao AGROJAVA, leia as opções e em seguida digite qual deseja para continuar: \n");
            System.out.println("Digite 1 - Para cadastrar dados (Chuvas e Umidade)");
            System.out.println("Digite 2 - Para exibir mapa do campo e relatório de chuvas");
            System.out.println("Digite 3 - Para relatório de alertas de irrigação");
            System.out.println("Digite 4 - Para registrar semana agrícola"); // RF04
            System.out.println("Digite 5 - Para consultar histórico de chuvas"); // RF05
            System.out.println("Digite 6 - Para identificar semana mais chuvosa"); // RF06
            System.out.println("Digite 7 - Para dashboard climático"); // RF07
            System.out.println("Digite 8 - Para gerar relatório de irrigação (SmartFarm)"); // RF10
            System.out.println("Digite 9 - Para relatório gerencial da fazenda"); // RF11
            System.out.println("Digite 10 - Para exportar relatórios em arquivo"); // RF12
            System.out.println("Digite 11 - Para sair");
            System.out.println("Escolha uma opcao: ");

            opcao = entrada.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("CADASTRO DE PLUVIOSIDADE (7 dias)");
                    for (int i = 0; i < 7; i++) {
                        System.out.print("Volume de chuva em " + dias[i] + " (mm): ");
                        chuvas[i] = entrada.nextDouble();
                    }
                    System.out.println();
                    System.out.println("CADASTRO DE UMIDADE DOS TALHÕES");
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            System.out.print("Informe a umidade do talhão [" + (i + 1) + "][" + (j + 1) + "] (%): ");
                            umidade[i][j] = entrada.nextDouble();
                        }
                    }

                    // RF08 + RF09 - Salvar Mapa de Umidade com Classificação em CSV
                    try (BufferedWriter writerMapa = new BufferedWriter(new FileWriter("mapa_umidade.csv"))) {
                        writerMapa.write("talhao,umidade,status");
                        writerMapa.newLine();
                        for (int i = 0; i < 4; i++) {
                            char letraLinha = (char) ('A' + i);
                            for (int j = 0; j < 4; j++) {
                                String talhao = "" + letraLinha + (j + 1);
                                double valor = umidade[i][j];
                                String status;
                                if (valor < 20.0) {
                                    status = "Crítico";
                                } else if (valor <= 30.0) {
                                    status = "Atenção";
                                } else {
                                    status = "Adequado";
                                }
                                writerMapa.write(String.format(Locale.US, "%s,%.0f,%s", talhao, valor, status));
                                writerMapa.newLine();
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao salvar o mapa de umidade CSV: " + e.getMessage());
                    }

                    dadosCadastrados = true;
                    System.out.println("Dados cadastrados com sucesso!");
                    System.out.println();
                    break;

                case 2:
                    if (!dadosCadastrados) {
                        System.out.println("Cadastre os dados primeiro selecionando a opção 1.");
                        break;
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

                    System.out.println("RELATÓRIO DE PLUVIOSIDADE");
                    System.out.printf("Média semanal de precipitação: %.2f mm\n", mediaChuva);
                    System.out.println("Dia com maior índice de chuva: " + dias[diaMaiorChuva] + " (" + maiorChuva + " mm)");

                    System.out.println("MAPA DE UMIDADE DO CAMPO");
                    System.out.println("            Col 1 Col 2 Col 3 Col 4");
                    for (int i = 0; i < 4; i++) {
                        System.out.print("Talhão Linha " + (i + 1) + "");
                        for (int j = 0; j < 4; j++) {
                            System.out.printf(" %.1f", umidade[i][j]);
                        }
                        System.out.println();
                    }
                    break;

                case 3:
                    if (!dadosCadastrados) {
                        System.out.println("Cadastre os dados primeiro selecionando a opção 1.");
                        break;
                    }

                    System.out.println("RELATÓRIO DE ALERTAS DE IRRIGAÇÃO");
                    boolean alertaEncontrado = false;

                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (umidade[i][j] < 30.0) {
                                System.out.printf("ALERTA: O talhão [%d][%d] está com %.1f%% de umidade e necessita de irrigação imediata!\n",
                                        (i + 1), (j + 1), umidade[i][j]);
                                alertaEncontrado = true;
                            }
                        }
                    }

                    if (!alertaEncontrado) {
                        System.out.println("Todos os talhões estão com umidade adequada (>= 30%). Nenhuma irrigação necessária no momento.");
                    }
                    break;

                case 4:
                    entrada.nextLine();
                    System.out.println("REGISTRAR SEMANA AGRÍCOLA"); // RF04
                    System.out.print("Informe a data da semana (ex: 19/11/2026): ");
                    String dataSemana = entrada.nextLine();

                    double[] chuvasSemana = new double[7];
                    double totalSemana = 0;

                    for (int i = 0; i < 7; i++) {
                        System.out.print(" Chuva de " + dias[i] + " (mm): ");
                        chuvasSemana[i] = entrada.nextDouble();
                        totalSemana += chuvasSemana[i];
                    }

                    double mediaSemana = totalSemana / 7.0;

                    File arquivo = new File("semanas_agricolas.csv");
                    boolean arquivoNovo = !arquivo.exists() || arquivo.length() == 0;

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
                        if (arquivoNovo) {
                            writer.write("data,seg,ter,qua,qui,sex,sab,dom,media,total");
                            writer.newLine();
                        }

                        String registro = String.format(Locale.US, "%s,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.1f,%.0f",
                                dataSemana,
                                chuvasSemana[0], chuvasSemana[1], chuvasSemana[2], chuvasSemana[3],
                                chuvasSemana[4], chuvasSemana[5], chuvasSemana[6],
                                mediaSemana, totalSemana);

                        writer.write(registro);
                        writer.newLine();
                        System.out.println("Semana agrícola registrada com sucesso no arquivo CSV!");
                    } catch (IOException e) {
                        System.out.println("Erro ao gravar o arquivo CSV: " + e.getMessage());
                    }
                    System.out.println();
                    break;

                case 5:
                    System.out.println("HISTÓRICO DE CHUVAS"); // RF05
                    File arquivoHistorico = new File("semanas_agricolas.csv");

                    if (!arquivoHistorico.exists()) {
                        System.out.println("Nenhum registro encontrado. Cadastre uma semana na opção 4 primeiro.");
                        System.out.println();
                        break;
                    }

                    try (BufferedReader reader = new BufferedReader(new FileReader(arquivoHistorico))) {
                        String linha = reader.readLine();
                        boolean possuiRegistros = false;

                        while ((linha = reader.readLine()) != null) {
                            if (linha.trim().isEmpty()) {
                                continue;
                            }
                            String[] colunas = linha.split(",");
                            String data = colunas[0];
                            double media = Double.parseDouble(colunas[8]);
                            double total = Double.parseDouble(colunas[9]);

                            System.out.println("Semana: " + data);
                            System.out.printf(Locale.US, "Média: %.0f mm\n", media);
                            System.out.printf(Locale.US, "Total: %.0f mm\n", total);
                            System.out.println("-------------------------");
                            possuiRegistros = true;
                        }

                        if (!possuiRegistros) {
                            System.out.println("O arquivo está vazio. Nenhum registro salvo.");
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao ler o arquivo CSV: " + e.getMessage());
                    }
                    System.out.println();
                    break;

                case 6:
                    System.out.println("IDENTIFICAR SEMANA MAIS CHUVOSA"); // RF06
                    File arquivoMaisChuvosa = new File("semanas_agricolas.csv");

                    if (!arquivoMaisChuvosa.exists()) {
                        System.out.println("Nenhum registro encontrado. Cadastre uma semana na opção 4 primeiro.");
                        System.out.println();
                        break;
                    }

                    try (BufferedReader reader = new BufferedReader(new FileReader(arquivoMaisChuvosa))) {
                        String linha = reader.readLine();
                        String semanaMaisChuvosa = "";
                        double maiorTotal = -1;

                        while ((linha = reader.readLine()) != null) {
                            if (linha.trim().isEmpty()) {
                                continue;
                            }
                            String[] colunas = linha.split(",");
                            String data = colunas[0];
                            double total = Double.parseDouble(colunas[9]);

                            if (total > maiorTotal) {
                                maiorTotal = total;
                                semanaMaisChuvosa = data;
                            }
                        }

                        if (maiorTotal >= 0) {
                            System.out.println("Semana mais chuvosa: " + semanaMaisChuvosa);
                            System.out.printf(Locale.US, "Total: %.0f mm\n", maiorTotal);
                        } else {
                            System.out.println("Nenhum registro válido encontrado no arquivo.");
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao analisar o arquivo CSV: " + e.getMessage());
                    }
                    System.out.println();
                    break;

                case 7:
                    File arquivoDashboard = new File("semanas_agricolas.csv"); // RF07

                    if (!arquivoDashboard.exists()) {
                        System.out.println("Nenhum registro encontrado. Cadastre uma semana na opção 4 primeiro.");
                        System.out.println();
                        break;
                    }

                    try (BufferedReader reader = new BufferedReader(new FileReader(arquivoDashboard))) {
                        String linha = reader.readLine();
                        int qtdSemanas = 0;
                        double volumeAcumulado = 0;
                        double maiorChuvaDash = Double.NEGATIVE_INFINITY;
                        double menorChuvaDash = Double.POSITIVE_INFINITY;

                        while ((linha = reader.readLine()) != null) {
                            if (linha.trim().isEmpty()) {
                                continue;
                            }
                            String[] colunas = linha.split(",");
                            double total = Double.parseDouble(colunas[9]);

                            qtdSemanas++;
                            volumeAcumulado += total;

                            if (total > maiorChuvaDash) {
                                maiorChuvaDash = total;
                            }
                            if (total < menorChuvaDash) {
                                menorChuvaDash = total;
                            }
                        }

                        if (qtdSemanas > 0) {
                            double mediaGeral = volumeAcumulado / qtdSemanas;
                            System.out.println("===== DASHBOARD =====");
                            System.out.println("Semanas cadastradas: " + qtdSemanas);
                            System.out.printf(Locale.US, "Volume acumulado: %.0f mm\n", volumeAcumulado);
                            System.out.printf(Locale.US, "Maior chuva: %.0f mm\n", maiorChuvaDash);
                            System.out.printf(Locale.US, "Menor chuva: %.0f mm\n", menorChuvaDash);
                            System.out.printf(Locale.US, "Média geral: %.1f mm\n", mediaGeral);
                        } else {
                            System.out.println("Nenhum registro válido encontrado no arquivo.");
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao ler o arquivo CSV: " + e.getMessage());
                    }
                    System.out.println();
                    break;

                case 8:
                    File arquivoAlertas = new File("mapa_umidade.csv"); // RF10

                    if (!arquivoAlertas.exists()) {
                        System.out.println("Nenhum mapa de umidade encontrado. Cadastre os dados na opção 1 primeiro.");
                        System.out.println();
                        break;
                    }

                    try (BufferedReader reader = new BufferedReader(new FileReader(arquivoAlertas))) {
                        String linha = reader.readLine();
                        int totalAlertas = 0;

                        System.out.println("===== ALERTAS =====");
                        while ((linha = reader.readLine()) != null) {
                            if (linha.trim().isEmpty()) {
                                continue;
                            }
                            String[] colunas = linha.split(",");
                            String talhao = colunas[0];
                            String status = colunas[2];

                            if (status.equalsIgnoreCase("Crítico") || status.equalsIgnoreCase("Atenção")) {
                                System.out.println(talhao + " - " + status);
                                totalAlertas++;
                            }
                        }
                        System.out.println("Total de alertas: " + totalAlertas);
                    } catch (IOException e) {
                        System.out.println("Erro ao ler o mapa de umidade CSV: " + e.getMessage());
                    }
                    System.out.println();
                    break;

                case 9:
                    File arquivoGerencial = new File("mapa_umidade.csv"); // RF11

                    if (!arquivoGerencial.exists()) {
                        System.out.println("Nenhum mapa de umidade encontrado. Cadastre os dados na opção 1 primeiro.");
                        System.out.println();
                        break;
                    }

                    try (BufferedReader reader = new BufferedReader(new FileReader(arquivoGerencial))) {
                        String linha = reader.readLine();
                        int totalTalhoes = 0;
                        double somaUmidade = 0;
                        int criticos = 0;
                        int adequados = 0;
                        int precisamIrrigacao = 0;
                        double menorUmidade = Double.POSITIVE_INFINITY;
                        double maiorUmidade = Double.NEGATIVE_INFINITY;
                        String talhaoMaisSeco = "";
                        String talhaoMaisUmido = "";

                        while ((linha = reader.readLine()) != null) {
                            if (linha.trim().isEmpty()) {
                                continue;
                            }
                            String[] colunas = linha.split(",");
                            String talhao = colunas[0];
                            double valor = Double.parseDouble(colunas[1]);
                            String status = colunas[2];

                            totalTalhoes++;
                            somaUmidade += valor;

                            if (status.equalsIgnoreCase("Crítico")) {
                                criticos++;
                                precisamIrrigacao++;
                            } else if (status.equalsIgnoreCase("Atenção")) {
                                precisamIrrigacao++;
                            } else if (status.equalsIgnoreCase("Adequado")) {
                                adequados++;
                            }

                            if (valor < menorUmidade) {
                                menorUmidade = valor;
                                talhaoMaisSeco = talhao;
                            }
                            if (valor > maiorUmidade) {
                                maiorUmidade = valor;
                                talhaoMaisUmido = talhao;
                            }
                        }

                        if (totalTalhoes > 0) {
                            double mediaGeral = somaUmidade / totalTalhoes;
                            double percentualIrrigacao = ((double) precisamIrrigacao / totalTalhoes) * 100.0;

                            System.out.println("===== RELATORIO DA FAZENDA =====");
                            System.out.printf(Locale.US, "Media Geral: %.0f%%\n", mediaGeral);
                            System.out.println("Talhoes Criticos: " + criticos);
                            System.out.println("Talhoes Adequados: " + adequados);
                            System.out.printf(Locale.US, "Necessidade de Irrigacao: %.0f%%\n", percentualIrrigacao);
                            System.out.printf(Locale.US, "Talhao mais seco: %s - %.0f%%\n", talhaoMaisSeco, menorUmidade);
                            System.out.printf(Locale.US, "Talhao mais umido: %s - %.0f%%\n", talhaoMaisUmido, maiorUmidade);
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao gerar relatório gerencial: " + e.getMessage());
                    }
                    System.out.println();
                    break;

                case 10:
                    File arquivoExportar = new File("mapa_umidade.csv"); // RF12

                    if (!arquivoExportar.exists()) {
                        System.out.println("Nenhum mapa de umidade encontrado. Cadastre os dados na opção 1 primeiro.");
                        System.out.println();
                        break;
                    }

                    try (BufferedReader reader = new BufferedReader(new FileReader(arquivoExportar))) {
                        String linha = reader.readLine();
                        int totalTalhoes = 0;
                        double somaUmidade = 0;
                        int criticos = 0;
                        int adequados = 0;

                        while ((linha = reader.readLine()) != null) {
                            if (linha.trim().isEmpty()) {
                                continue;
                            }
                            String[] colunas = linha.split(",");
                            double valor = Double.parseDouble(colunas[1]);
                            String status = colunas[2];

                            totalTalhoes++;
                            somaUmidade += valor;

                            if (status.equalsIgnoreCase("Crítico")) {
                                criticos++;
                            } else if (status.equalsIgnoreCase("Adequado")) {
                                adequados++;
                            }
                        }

                        if (totalTalhoes > 0) {
                            double mediaGeral = somaUmidade / totalTalhoes;
                            String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("relatorio_fazenda.txt"))) {
                                writer.write("===== RELATORIO DA FAZENDA =====");
                                writer.newLine();
                                writer.write("Data: " + dataAtual);
                                writer.newLine();
                                writer.write(String.format(Locale.US, "Media Geral: %.0f%%", mediaGeral));
                                writer.newLine();
                                writer.write("Talhoes Criticos: " + criticos);
                                writer.newLine();
                                writer.write("Talhoes Adequados: " + adequados);
                                writer.newLine();
                            }
                            System.out.println("Relatório exportado com sucesso para relatorio_fazenda.txt!");
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao exportar relatório: " + e.getMessage());
                    }
                    System.out.println();
                    break;

                case 11:
                    System.out.println("Encerrando o sistema.");
                    break;

                default:
                    System.out.println("Opção inválida! Escolha um número entre 1 e 11.");
            }
        } while (opcao != 11);

        entrada.close();
    }
}