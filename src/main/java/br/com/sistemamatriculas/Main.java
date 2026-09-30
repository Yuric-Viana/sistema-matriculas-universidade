package br.com.sistemamatriculas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import br.com.sistemamatriculas.enums.TipoOpcao;
import br.com.sistemamatriculas.model.Aluno;
import br.com.sistemamatriculas.model.Curso;
import br.com.sistemamatriculas.model.Disciplina;
import br.com.sistemamatriculas.model.ItemMatricula;
import br.com.sistemamatriculas.model.MatriculaSemestral;
import br.com.sistemamatriculas.model.OfertaDisciplina;
import br.com.sistemamatriculas.model.PeriodoMatricula;
import br.com.sistemamatriculas.model.Professor;
import br.com.sistemamatriculas.model.Secretaria;
import br.com.sistemamatriculas.model.SemestreLetivo;
import br.com.sistemamatriculas.model.SistemaCobranca;
import br.com.sistemamatriculas.model.Usuario;
import br.com.sistemamatriculas.util.ConsoleUI;

public class Main {

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {

        List<Usuario> usuarios = new ArrayList<>();
        List<OfertaDisciplina> todasOfertas = new ArrayList<>();
        List<Disciplina> todasDisciplinas = new ArrayList<>();
        Curso curso = inicializarDadosDeExemplo(usuarios, todasOfertas, todasDisciplinas);

        ConsoleUI.limparTela();
        ConsoleUI.cabecalho("SISTEMA DE MATRÍCULAS — 2026/2");
        ConsoleUI.info("Usuários de teste: julia/123 (aluno), marcos/123 (professor), secretaria/123 (secretaria)");

        Usuario usuarioLogado = null;

        while (usuarioLogado == null) {
            usuarioLogado = autenticar(usuarios);
        }

        ConsoleUI.sucesso("Bem-vindo(a), " + usuarioLogado.getNome() + "!");

        if (usuarioLogado instanceof Aluno) {
            menuAluno((Aluno) usuarioLogado);
        } else if (usuarioLogado instanceof Professor) {
            menuProfessor((Professor) usuarioLogado, todasOfertas);
        } else if (usuarioLogado instanceof Secretaria) {
            menuSecretaria((Secretaria) usuarioLogado, curso, todasDisciplinas);
        }

        ConsoleUI.info("Sistema encerrado. Até logo!");
    }

    // ---------------------------------------------------------------
    // Autenticação
    // ---------------------------------------------------------------

    private static Usuario autenticar(List<Usuario> usuarios) {
        ConsoleUI.secao("Login");
        System.out.print("Login: ");
        String login = SC.nextLine().trim();
        System.out.print("Senha: ");
        String senha = SC.nextLine().trim();

        for (Usuario usuario : usuarios) {
            if (usuario.autenticar(login, senha)) {
                return usuario;
            }
        }

        ConsoleUI.erro("Login ou senha inválidos. Tente novamente.");
        return null;
    }

    // ---------------------------------------------------------------
    // Menu do Aluno
    // ---------------------------------------------------------------

    private static void menuAluno(Aluno aluno) {
        boolean continuar = true;

        while (continuar) {
            ConsoleUI.menu("Menu do Aluno", List.of(
                    "Ver disciplinas disponíveis",
                    "Matricular em disciplina",
                    "Cancelar matrícula",
                    "Ver minhas disciplinas",
                    "Sair"
            ));

            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> verDisciplinasDisponiveis(aluno);
                case 2 -> matricularEmDisciplina(aluno);
                case 3 -> cancelarMatricula(aluno);
                case 4 -> verMinhasDisciplinas(aluno);
                case 5 -> continuar = false;
                default -> ConsoleUI.erro("Opção inválida.");
            }
        }
    }

    private static void verDisciplinasDisponiveis(Aluno aluno) {
        List<OfertaDisciplina> disponiveis = aluno.consultarDisciplinasDisponiveis();

        if (disponiveis.isEmpty()) {
            ConsoleUI.aviso("Nenhuma disciplina disponível no momento.");
            return;
        }

        List<List<String>> linhas = new ArrayList<>();
        for (int i = 0; i < disponiveis.size(); i++) {
            OfertaDisciplina o = disponiveis.get(i);
            linhas.add(List.of(
                    String.valueOf(i + 1),
                    o.getDisciplina().getNome(),
                    o.getProfessor().getNome(),
                    o.totalInscritos() + "/" + o.getVagasMaximas(),
                    o.getStatus().toString()
            ));
        }

        ConsoleUI.secao("Disciplinas Disponíveis");
        ConsoleUI.tabela(List.of("#", "Disciplina", "Professor", "Vagas", "Status"), linhas);
    }

    private static void matricularEmDisciplina(Aluno aluno) {
        List<OfertaDisciplina> disponiveis = aluno.consultarDisciplinasDisponiveis();

        if (disponiveis.isEmpty()) {
            ConsoleUI.aviso("Nenhuma disciplina disponível para matrícula.");
            return;
        }

        verDisciplinasDisponiveis(aluno);
        System.out.print("Número da disciplina para matricular (0 para cancelar): ");
        int escolha = lerOpcao();

        if (escolha == 0) {
            return;
        }

        if (escolha < 1 || escolha > disponiveis.size()) {
            ConsoleUI.erro("Opção inválida.");
            return;
        }

        OfertaDisciplina oferta = disponiveis.get(escolha - 1);

        System.out.print("Tipo [1] Obrigatória  [2] Optativa: ");
        int tipoEscolhido = lerOpcao();
        TipoOpcao tipo = (tipoEscolhido == 2) ? TipoOpcao.OPTATIVA : TipoOpcao.OBRIGATORIA;

        if (aluno.getMatriculas().isEmpty()) {
            ConsoleUI.erro("Aluno não possui matrícula semestral ativa.");
            return;
        }

        MatriculaSemestral matriculaAtual = aluno.getMatriculas().get(0);

        try {
            matriculaAtual.adicionarItem(oferta, tipo);
            matriculaAtual.notificarCobranca();
            ConsoleUI.sucesso("Matrícula em \"" + oferta.getDisciplina().getNome() + "\" realizada com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ConsoleUI.erro(e.getMessage());
        }
    }

    private static void cancelarMatricula(Aluno aluno) {
        if (aluno.getMatriculas().isEmpty() || aluno.getMatriculas().get(0).getItens().isEmpty()) {
            ConsoleUI.aviso("Você não possui disciplinas matriculadas para cancelar.");
            return;
        }

        List<ItemMatricula> itens = aluno.getMatriculas().get(0).getItens();
        verMinhasDisciplinas(aluno);

        System.out.print("Número da disciplina para cancelar (0 para voltar): ");
        int escolha = lerOpcao();

        if (escolha == 0) {
            return;
        }

        if (escolha < 1 || escolha > itens.size()) {
            ConsoleUI.erro("Opção inválida.");
            return;
        }

        ItemMatricula item = itens.get(escolha - 1);

        try {
            aluno.cancelarMatricula(item);
            ConsoleUI.sucesso("Matrícula cancelada com sucesso!");
        } catch (IllegalArgumentException e) {
            ConsoleUI.erro(e.getMessage());
        }
    }

    private static void verMinhasDisciplinas(Aluno aluno) {
        if (aluno.getMatriculas().isEmpty()) {
            ConsoleUI.aviso("Você não possui matrícula semestral.");
            return;
        }

        List<ItemMatricula> itens = aluno.getMatriculas().get(0).getItens();

        if (itens.isEmpty()) {
            ConsoleUI.aviso("Você ainda não está matriculado em nenhuma disciplina.");
            return;
        }

        List<List<String>> linhas = new ArrayList<>();
        for (int i = 0; i < itens.size(); i++) {
            ItemMatricula item = itens.get(i);
            OfertaDisciplina oferta = item.getOfertaDisciplina();
            linhas.add(List.of(
                    String.valueOf(i + 1),
                    oferta.getDisciplina().getNome(),
                    item.getTipo().toString(),
                    oferta.getStatus().toString()
            ));
        }

        ConsoleUI.secao("Minhas Disciplinas");
        ConsoleUI.tabela(List.of("#", "Disciplina", "Tipo", "Status da Turma"), linhas);
    }

    // ---------------------------------------------------------------
    // Menu do Professor
    // ---------------------------------------------------------------

    private static void menuProfessor(Professor professor, List<OfertaDisciplina> todasOfertas) {
        List<OfertaDisciplina> minhasOfertas = new ArrayList<>();
        for (OfertaDisciplina o : todasOfertas) {
            if (o.getProfessor() == professor) {
                minhasOfertas.add(o);
            }
        }

        boolean continuar = true;
        while (continuar) {
            ConsoleUI.menu("Menu do Professor", List.of(
                    "Ver alunos matriculados em uma disciplina",
                    "Sair"
            ));

            int opcao = lerOpcao();

            if (opcao == 1) {
                if (minhasOfertas.isEmpty()) {
                    ConsoleUI.aviso("Você não leciona nenhuma disciplina cadastrada.");
                    continue;
                }

                List<List<String>> linhas = new ArrayList<>();
                for (int i = 0; i < minhasOfertas.size(); i++) {
                    OfertaDisciplina o = minhasOfertas.get(i);
                    linhas.add(List.of(String.valueOf(i + 1), o.getDisciplina().getNome(), o.getStatus().toString()));
                }
                ConsoleUI.secao("Minhas Disciplinas");
                ConsoleUI.tabela(List.of("#", "Disciplina", "Status"), linhas);

                System.out.print("Número da disciplina: ");
                int escolha = lerOpcao();

                if (escolha < 1 || escolha > minhasOfertas.size()) {
                    ConsoleUI.erro("Opção inválida.");
                    continue;
                }

                List<Aluno> alunos = professor.consultarAlunosMatriculados(minhasOfertas.get(escolha - 1));

                if (alunos.isEmpty()) {
                    ConsoleUI.aviso("Nenhum aluno matriculado nessa disciplina ainda.");
                    continue;
                }

                List<List<String>> linhasAlunos = new ArrayList<>();
                for (Aluno a : alunos) {
                    linhasAlunos.add(List.of(a.getNome(), a.getMatricula()));
                }
                ConsoleUI.secao("Alunos Matriculados");
                ConsoleUI.tabela(List.of("Nome", "Matrícula"), linhasAlunos);

            } else if (opcao == 2) {
                continuar = false;
            } else {
                ConsoleUI.erro("Opção inválida.");
            }
        }
    }

    // ---------------------------------------------------------------
    // Menu da Secretaria
    // ---------------------------------------------------------------

    private static void menuSecretaria(Secretaria secretaria, Curso curso, List<Disciplina> disciplinas) {
        boolean continuar = true;
        while (continuar) {
            ConsoleUI.menu("Menu da Secretaria", List.of(
                    "Ver cursos cadastrados",
                    "Ver disciplinas cadastradas",
                    "Sair"
            ));

            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> {
                    ConsoleUI.secao("Cursos");
                    ConsoleUI.tabela(
                            List.of("Código", "Nome", "Créditos"),
                            List.of(List.of(
                                    String.valueOf(curso.getCodigo()),
                                    curso.getNome(),
                                    String.valueOf(curso.getNumeroCreditos())
                            ))
                    );
                }
                case 2 -> {
                    List<List<String>> linhas = new ArrayList<>();
                    for (Disciplina d : disciplinas) {
                        linhas.add(List.of(
                                String.valueOf(d.getCodigo()),
                                d.getNome(),
                                String.valueOf(d.getCreditos())
                        ));
                    }
                    ConsoleUI.secao("Disciplinas");
                    ConsoleUI.tabela(List.of("Código", "Nome", "Créditos"), linhas);
                }
                case 3 -> {
                    continuar = false;
                }
                default -> ConsoleUI.erro("Opção inválida.");
            }
        }

        ConsoleUI.info("Cadastro completo (adicionar/editar cursos, disciplinas, professores) fica para a próxima sprint.");
    }

    // ---------------------------------------------------------------
    // Utilitários
    // ---------------------------------------------------------------

    private static int lerOpcao() {
        try {
            return Integer.parseInt(SC.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ---------------------------------------------------------------
    // Dados de exemplo (substituir por persistência em arquivo na Lab01S03)
    // ---------------------------------------------------------------

    private static Curso inicializarDadosDeExemplo(
            List<Usuario> usuarios,
            List<OfertaDisciplina> todasOfertas,
            List<Disciplina> todasDisciplinas
    ) {
        PeriodoMatricula periodo = new PeriodoMatricula(
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(30)
        );
        SemestreLetivo semestre = new SemestreLetivo(2026, 2, periodo);

        Curso curso = new Curso(1, "Engenharia de Software", 240);

        Disciplina d1 = new Disciplina(101, "Projeto de Software", 4, 60);
        Disciplina d2 = new Disciplina(102, "Banco de Dados II", 4, 60);
        Disciplina d3 = new Disciplina(103, "Inteligência Artificial", 4, 60);
        curso.adicionarDisciplina(d1);
        curso.adicionarDisciplina(d2);
        curso.adicionarDisciplina(d3);
        todasDisciplinas.add(d1);
        todasDisciplinas.add(d2);
        todasDisciplinas.add(d3);

        Professor professor = new Professor(1L, "Marcos Andrade", "marcos", "123", "REG001");

        OfertaDisciplina o1 = new OfertaDisciplina(1L, d1, professor, semestre);
        OfertaDisciplina o2 = new OfertaDisciplina(2L, d2, professor, semestre);
        OfertaDisciplina o3 = new OfertaDisciplina(3L, d3, professor, semestre);
        semestre.adicionarOferta(o1);
        semestre.adicionarOferta(o2);
        semestre.adicionarOferta(o3);
        todasOfertas.add(o1);
        todasOfertas.add(o2);
        todasOfertas.add(o3);

        Aluno aluno = new Aluno(1L, "Júlia Ventura", "julia", "123", "2026028471");
        MatriculaSemestral matriculaAluno = new MatriculaSemestral(1L, new Date());
        matriculaAluno.setSemestreLetivo(semestre);
        matriculaAluno.setSistemaCobranca(new SistemaCobranca());
        aluno.realizarMatricula(matriculaAluno);

        Secretaria secretaria = new Secretaria(1L, "Secretaria Acadêmica", "secretaria", "123", "Acadêmico");

        usuarios.add(aluno);
        usuarios.add(professor);
        usuarios.add(secretaria);

        return curso;
    }
}