package negocio.entidades.pagamento;

public class Pix implements MetodoPagamento {

    @Override
    public String pagar(double valor) {
       return "Gerando QR Code para pagamento de R$" + valor + " via PIX...";
    }
}
