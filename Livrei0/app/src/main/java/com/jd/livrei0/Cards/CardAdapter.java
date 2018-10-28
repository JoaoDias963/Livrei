package com.jd.livrei0.Cards;

public class CardAdapter {
    private String userId;
    private String titulo;
    private String imgUrl;


    public CardAdapter (String userId, String titulo, String imgUrl){
        this.setUserId(userId);
        this.setTitulo(titulo);
        this.setImgUrl(imgUrl);
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
