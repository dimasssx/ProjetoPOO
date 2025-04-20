package negocio.entidades.pagamento;

public class CartaoDebito implements MetodoPagamento {
    private String numero;
    private String nomeTitular;

    public CartaoDebito(String numero, String nomeTitular) {
        this.numero = numero;
        this.nomeTitular = nomeTitular;
    }

    @Override
    public String pagar(double valor) {
        return "Processando pagamento de R$" + valor + " com Cartão de Débito...";
    }
}
