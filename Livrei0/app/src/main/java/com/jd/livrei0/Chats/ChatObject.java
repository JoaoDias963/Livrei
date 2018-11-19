package com.jd.livrei0.Chats;

public class ChatObject {

    private String mensagem;
    private String tituloStatus;
    private String trocaId;

    private Boolean usuarioAtualBoolean;



    public ChatObject(String mensagem, Boolean usuarioAtualBoolean, String trocaId, String tituloStatus){
        this.mensagem = mensagem;
        this.usuarioAtualBoolean = usuarioAtualBoolean;
        this.setTrocaId(trocaId);
        this.setTituloStatus(tituloStatus);



    }




    public String getMensagem() {return mensagem;}

    public String getTituloStatus() {return tituloStatus;}

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean getUsuarioAtualBoolean(){return usuarioAtualBoolean;}


    public void setUsuarioAtualBoolean(Boolean usuarioAtualBoolean) {
        this.usuarioAtualBoolean = usuarioAtualBoolean;
    }


    public String getTrocaId() {
        return trocaId;
    }

    public void setTrocaId(String trocaId) {
        this.trocaId = trocaId;
    }

    public void setTituloStatus(String tituloStatus) {
        this.tituloStatus = tituloStatus;
    }
}
