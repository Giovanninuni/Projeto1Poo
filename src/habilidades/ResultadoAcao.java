package habilidades;

public class ResultadoAcao {
	private final boolean sucesso; // final aplicada a um atributo significa que o valor dele só pode ser atribuído uma única vez
    private final String mensagem;

    public ResultadoAcao(boolean sucesso, String mensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }
}
