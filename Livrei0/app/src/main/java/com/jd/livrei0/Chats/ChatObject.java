package com.jd.livrei0.Chats;

public class ChatObject {

    private String mensagem;
    private String trocaId;

    private Boolean usuarioAtualBoolean;



    public ChatObject(String mensagem, Boolean usuarioAtualBoolean, String trocaId){
        this.mensagem = mensagem;
        this.usuarioAtualBoolean = usuarioAtualBoolean;
        this.setTrocaId(trocaId);



    }




    public String getMensagem() {return mensagem;}

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
}
