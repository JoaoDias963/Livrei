package com.jd.livrei0.Chats;

public class ChatObject {

    private String mensagem;
    private Boolean usuarioAtualBoolean;


    public ChatObject(String mensagem, Boolean usuarioAtualBoolean){
        this.mensagem = mensagem;
        this.usuarioAtualBoolean = usuarioAtualBoolean;


    }




    public String getMensagem() {return mensagem;}

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean getUsuarioAtualBoolean(){return usuarioAtualBoolean;}

    public void setUsuarioAtualBoolean(Boolean usuarioAtualBoolean) {
        this.usuarioAtualBoolean = usuarioAtualBoolean;
    }
}
