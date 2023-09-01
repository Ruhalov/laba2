import java.util.Random;

public class Operator extends Thread {
    private final Client client;

    public void run() {
        Random rand = new Random();
        System.out.println(this.getName() + " говорит c " + client.getName());
        try {
            sleep(rand.nextInt()%2000+2000); //разговор от 2 до 4 сек
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CallCentre.clientsPool.add(client);  //после разговора возвращаем клиента, чтобы он мог заново звонить
        System.out.println(currentThread().getName() + " пока " + client.getName());
    }

    Operator (Client client) {
        this.client = client;
        start();
    }
}
