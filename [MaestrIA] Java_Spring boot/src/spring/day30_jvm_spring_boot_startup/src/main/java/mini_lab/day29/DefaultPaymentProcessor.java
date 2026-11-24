package mini_lab.day29;

class DefaultPaymentProcessor implements PaymentProcessor {

    @Override
    public String process(Long paymentId) {
        System.out.println("Processing payment id=" + paymentId );
        return "APPROVED";
    }
}