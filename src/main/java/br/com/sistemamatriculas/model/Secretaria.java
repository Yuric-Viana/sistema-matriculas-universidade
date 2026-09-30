package br.com.sistemamatriculas.model;

import java.util.Date;

public class Secretaria extends Usuario {
    private String setor;

    public Secretaria(Long id, String nome, String login, String senha, String setor) {
        super(id, nome, login, senha);
        this.setor = setor;
    }

    public CurriculoSemestral gerarCurriculo(SemestreLetivo semestre) {

        if (semestre == null) {
            throw new IllegalArgumentException(
                    "O semestre letivo não pode ser nulo.");
        }

        CurriculoSemestral curriculo = new CurriculoSemestral(
                System.currentTimeMillis(),
                new Date());

        for (OfertaDisciplina oferta : semestre.getOfertas()) {
            curriculo.adicionarOferta(oferta);
        }

        return curriculo;
    }

    public void gerenciarCursos(Curso curso) {
        // TODO: implementar
    }

    public void gerenciarDisciplinas(Disciplina disciplina) {
        // TODO: implementar
    }

    public void gerenciarProfessores(Professor professor) {
        // TODO: implementar
    }

    public void gerenciarAlunos(Aluno aluno) {
        // TODO: implementar
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

}
