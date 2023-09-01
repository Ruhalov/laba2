import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class CallCentre {
    private final int MAX_OPERATORS;     //количетво операторов(потоков) в колл-центре
    public static ArrayList<Client> clientsPool;    //Список клиентв которые могут позвоить в колл-центр
    private final ArrayDeque<Client> clientDeque = new ArrayDeque<>();   //Очередь звонков к операторам

    CallCentre(int MAX_OPERATORS, ArrayList<Client> clientsPool) {
        this.MAX_OPERATORS = MAX_OPERATORS;
        CallCentre.clientsPool = clientsPool;
    }

    public Client randomCall() {
        //симуляция звонков
        Random rand = new Random();
        Client randClient = clientsPool.get(Math.abs(rand.nextInt()%clientsPool.size()));
        //получаем случайный индекс клиента, который позвонит из списка
        System.out.println(randClient.getName() + " звонит!");
        clientsPool.remove(randClient);
        //удаляем клиента из списка на время разговора, чтобы он не мог позвонить ещё раз
        return randClient;
    }

    public void dispatchCall() throws InterruptedException {
        System.out.println("callcentre started");
        ArrayList <Operator> operators = new ArrayList<>(MAX_OPERATORS); // Список операторов

        final int WORKING_TIME = 15;  //сколько секунд будет работать колл-центр
        final long START_WORKING_TIME = new Date().getTime();  //время начала работы

        Random rand = new Random();
        while(new Date().getTime() < START_WORKING_TIME + WORKING_TIME *1000) {
            //работаем не до последнего клиента, а заданное время
            //если разговор вышел за рамки абочего дня, оператор не бросит трубку, а договорит, но нового клиента уже не возьмет
            if (!clientsPool.isEmpty() && rand.nextDouble() < 0.33) {
                // если список клиентов не пустой то с вероятносттю 0.33
                //берем перемещаем случайного клиента в очередь к операторам
                Client randClient = randomCall();
                clientDeque.addLast(randClient);

                for (int i = 0; i < MAX_OPERATORS; i++) {
                    //перебераем операторов
                    if (operators.size() < MAX_OPERATORS) {
                        //если список операторов меньше максимальновозмжно работающих операторов то
                        operators.add(new Operator(clientDeque.poll()));
                        //добавляем нового оператора и передаем ему первого в очереди
                        break;
                    } else if (operators.get(i).getState() == Thread.State.TERMINATED) {
                        //если оператор(поток) отработал удаляем его и добавляем новый передавая ему последнего клиента
                        operators.remove(i);
                        operators.add(new Operator(clientDeque.poll()));
                    }
                }
            }
            Thread.sleep(500); // проверяем звонки раз в пол секунды
        }
        System.out.println("callCenter is closed");
    }

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Client> clients = new ArrayList<>();
        clients.add(new Client("petro"));
        clients.add(new Client("ivan"));
        clients.add(new Client("ira"));
        clients.add(new Client("olha"));

        CallCentre cc = new CallCentre(2, clients);
        cc.dispatchCall();
    }
}
