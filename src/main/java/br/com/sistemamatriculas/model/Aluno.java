package br.com.sistemamatriculas.model;

import java.util.ArrayList;
import java.util.List;

public class Aluno extends Usuario {

    private String matricula;
    private List<MatriculaSemestral> matriculas;

    public Aluno(Long id, String nome, String login, String senha, String matricula) {
        super(id, nome, login, senha);
        this.matricula = matricula;
        this.matriculas = new ArrayList<>();
    }

    public List<OfertaDisciplina> consultarDisciplinasDisponiveis() {

        List<OfertaDisciplina> ofertasDisponiveis = new ArrayList<>();

        for (MatriculaSemestral matriculaSemestral : matriculas) {

            if (matriculaSemestral.getSemestreLetivo() == null) {
                continue;
            }

            for (OfertaDisciplina oferta : matriculaSemestral.getSemestreLetivo().getOfertas()) {

                if (oferta.verificarDisponibilidaDeVagas()) {
                    ofertasDisponiveis.add(oferta);
                }
            }
        }

        return ofertasDisponiveis;
    }

    public void realizarMatricula(MatriculaSemestral matricula) {

        if (matricula == null) {
            throw new IllegalArgumentException(
                    "A matrícula não pode ser nula.");
        }

        if (!matriculas.contains(matricula)) {
            matriculas.add(matricula);
        }
    }

    public void cancelarMatricula(ItemMatricula item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "O item da matrícula não pode ser nulo.");
        }

        for (MatriculaSemestral matriculaSemestral : matriculas) {

            if (matriculaSemestral.getItens().contains(item)) {
                matriculaSemestral.cancelarItem(item);
                return;
            }
        }

        throw new IllegalArgumentException(
                "Item de matrícula não encontrado.");
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public List<MatriculaSemestral> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<MatriculaSemestral> matriculas) {

        if (matriculas == null) {
            this.matriculas = new ArrayList<>();
            return;
        }

        this.matriculas = matriculas;
    }

    @Override
    public String toString() {
        return "Aluno [id=" + getId()
                + ", nome=" + getNome()
                + ", matricula=" + matricula
                + "]";
    }
}