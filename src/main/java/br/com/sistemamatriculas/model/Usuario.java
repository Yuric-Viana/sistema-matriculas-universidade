package br.com.sistemamatriculas.model;

public abstract class Usuario {

    private Long id;
    private String nome;
    private String login;
    private String senha;

    public Usuario(
            Long id,
            String nome,
            String login,
            String senha) {

        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public boolean autenticar(String senha) {
        return this.senha.equals(senha);
    }

    public void realizarLogin(String login, String senha) {
        boolean userAuthenticated = this.login.equals(login) && autenticar(senha);

        if (userAuthenticated) {
            System.out.println("Usuário logado!");
        } else {
            System.out.println("Login e/ou senha inválidos.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}