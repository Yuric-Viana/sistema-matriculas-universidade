package br.com.sistemamatriculas.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Secretaria extends Usuario {

    private String setor;

    private List<Curso> cursos;
    private List<Disciplina> disciplinas;
    private List<Professor> professores;
    private List<Aluno> alunos;

    public Secretaria(
            Long id,
            String nome,
            String login,
            String senha,
            String setor) {

        super(id, nome, login, senha);

        this.setor = setor;

        this.cursos = new ArrayList<>();
        this.disciplinas = new ArrayList<>();
        this.professores = new ArrayList<>();
        this.alunos = new ArrayList<>();
    }

    public CurriculoSemestral gerarCurriculo(SemestreLetivo semestre) {

        if (semestre == null) {
            throw new IllegalArgumentException(
                    "O semestre letivo não pode ser nulo."
            );
        }

        CurriculoSemestral curriculo = new CurriculoSemestral(
                System.currentTimeMillis(),
                new Date()
        );

        for (OfertaDisciplina oferta : semestre.getOfertas()) {
            curriculo.adicionarOferta(oferta);
        }

        return curriculo;
    }

    public void gerenciarCursos(Curso curso) {

        if (curso == null) {
            throw new IllegalArgumentException(
                    "O curso não pode ser nulo."
            );
        }

        if (!cursos.contains(curso)) {
            cursos.add(curso);
        }
    }

    public void gerenciarDisciplinas(Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina não pode ser nula."
            );
        }

        if (!disciplinas.contains(disciplina)) {
            disciplinas.add(disciplina);
        }
    }

    public void gerenciarProfessores(Professor professor) {

        if (professor == null) {
            throw new IllegalArgumentException(
                    "O professor não pode ser nulo."
            );
        }

        if (!professores.contains(professor)) {
            professores.add(professor);
        }
    }

    public void gerenciarAlunos(Aluno aluno) {

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno não pode ser nulo."
            );
        }

        if (!alunos.contains(aluno)) {
            alunos.add(aluno);
        }
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public List<Professor> getProfessores() {
        return professores;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }
}