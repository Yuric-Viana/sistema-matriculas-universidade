package br.com.sistemamatriculas.util;

import java.util.List;

/**
 * Utilitário para deixar a interface de linha de comando mais visual,
 * usando cores ANSI e caracteres de desenho de caixa (box-drawing).
 *
 * Funciona em terminais modernos (VS Code, iTerm, Terminal do Mac,
 * Windows Terminal, etc). Se aparecer "lixo" tipo [0m no lugar das
 * cores, o terminal usado não suporta ANSI — nesse caso é só chamar
 * ConsoleUI.desativarCores() no início do Main.
 */
public class ConsoleUI {

    private static boolean coresAtivas = true;

    // --- Cores ---
    private static final String RESET   = "\u001B[0m";
    private static final String BOLD    = "\u001B[1m";
    private static final String CIANO   = "\u001B[36m";
    private static final String VERDE   = "\u001B[32m";
    private static final String AMARELO = "\u001B[33m";
    private static final String VERMELHO = "\u001B[31m";
    private static final String CINZA   = "\u001B[90m";
    private static final String AZUL    = "\u001B[34m";

    public static void desativarCores() {
        coresAtivas = false;
    }

    private static String cor(String codigoCor, String texto) {
        if (!coresAtivas) return texto;
        return codigoCor + texto + RESET;
    }

    /** Limpa a tela (funciona na maioria dos terminais). */
    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /** Cabeçalho grande com borda dupla, para telas principais. */
    public static void cabecalho(String titulo) {
        int largura = titulo.length() + 4;
        String topo = "╔" + "═".repeat(largura) + "╗";
        String meio = "║  " + titulo + "  ║";
        String base = "╚" + "═".repeat(largura) + "╝";

        System.out.println(cor(CIANO + BOLD, topo));
        System.out.println(cor(CIANO + BOLD, meio));
        System.out.println(cor(CIANO + BOLD, base));
    }

    /** Subtítulo simples, com linha embaixo. */
    public static void secao(String titulo) {
        System.out.println();
        System.out.println(cor(BOLD, titulo));
        System.out.println(cor(CINZA, "─".repeat(titulo.length())));
    }

    public static void sucesso(String mensagem) {
        System.out.println(cor(VERDE, "✔ " + mensagem));
    }

    public static void erro(String mensagem) {
        System.out.println(cor(VERMELHO, "✘ " + mensagem));
    }

    public static void aviso(String mensagem) {
        System.out.println(cor(AMARELO, "⚠ " + mensagem));
    }

    public static void info(String mensagem) {
        System.out.println(cor(AZUL, "ℹ " + mensagem));
    }

    /** Imprime um menu numerado a partir de uma lista de opções. */
    public static void menu(String titulo, List<String> opcoes) {
        secao(titulo);
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.println(cor(BOLD, " [" + (i + 1) + "] ") + opcoes.get(i));
        }
        System.out.println();
        System.out.print(cor(CINZA, "Escolha uma opção: "));
    }

    /**
     * Imprime uma tabela simples alinhada, dado o cabeçalho e as linhas.
     * Exemplo:
     *   ConsoleUI.tabela(
     *       List.of("Código", "Disciplina", "Vagas"),
     *       List.of(
     *           List.of("PSW01", "Projeto de Software", "38/60"),
     *           List.of("BD02",  "Banco de Dados II",   "52/60")
     *       )
     *   );
     */
    public static void tabela(List<String> colunas, List<List<String>> linhas) {
        int[] largura = new int[colunas.size()];
        for (int i = 0; i < colunas.size(); i++) {
            largura[i] = colunas.get(i).length();
            for (List<String> linha : linhas) {
                largura[i] = Math.max(largura[i], linha.get(i).length());
            }
        }

        imprimirBordaTabela(largura, "┌", "┬", "┐");
        imprimirLinhaTabela(colunas, largura, true);
        imprimirBordaTabela(largura, "├", "┼", "┤");
        for (List<String> linha : linhas) {
            imprimirLinhaTabela(linha, largura, false);
        }
        imprimirBordaTabela(largura, "└", "┴", "┘");
    }

    private static void imprimirBordaTabela(int[] largura, String esq, String meio, String dir) {
        StringBuilder sb = new StringBuilder(esq);
        for (int i = 0; i < largura.length; i++) {
            sb.append("─".repeat(largura[i] + 2));
            sb.append(i == largura.length - 1 ? dir : meio);
        }
        System.out.println(cor(CINZA, sb.toString()));
    }

    private static void imprimirLinhaTabela(List<String> valores, int[] largura, boolean cabecalho) {
        StringBuilder sb = new StringBuilder("│");
        for (int i = 0; i < valores.size(); i++) {
            String valor = String.format(" %-" + largura[i] + "s ", valores.get(i));
            sb.append(cabecalho ? cor(BOLD, valor) : valor);
            sb.append("│");
        }
        System.out.println(sb.toString());
    }
}
