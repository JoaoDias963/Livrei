package com.jd.livrei0.Trocas;

public class TrocasObject {

    private String userId;
    private String titulo;
    private String imgUrlLivro;
    private String imgUrlPerfil;
    private String nome;
    private String status;
    private String idTroca;


    public TrocasObject(String userId, String nome, String imgUrlPerfil, String imgUrlLivro, String titulo, String status, String idTroca){
        this.setUserId(userId);
        this.setNome(nome);
        this.setImgUrlPerfil(imgUrlPerfil);
        this.setImgUrlLivro(imgUrlLivro);
        this.setTitulo(titulo);
        this.setStatus(status);
        this.setIdTroca(idTroca);

    }

    public String setIdTroca(String idTroca) {
        return this.idTroca = idTroca;
    }
    public String getIdTroca() { return idTroca;}
    public String getUserId() {
        return userId;
    }
    public String setUserId(String userId){ return this.userId = userId; }
    public String getNome() {
        return nome;
    }
    public String setNome(String nome){ return this.nome = nome; }
    public String getImgUrlPerfil() {
        return imgUrlPerfil;
    }
    public String setImgUrlPerfil(String imgUrlPerfil){ return this.imgUrlPerfil = imgUrlPerfil; }
    public String getimgUrlLivro() {
        return imgUrlLivro;
    }
    public String setImgUrlLivro(String imgUrlLivro){ return this.imgUrlLivro = imgUrlLivro; }
    public String getTitulo() {
        return titulo;
    }
    public String setTitulo(String titulo){ return this.userId = titulo; }
    public String getStatus() {
        return status;
    }
    public String setStatus(String status){ return this.status = status; }


}
