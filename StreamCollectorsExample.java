import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Order {
  private String product;

  private double cost;

  public Order(String product, double cost) {
    this.product = product;
    this.cost = cost;
  }

  public String getProduct() {
    return product;
  }

  public double getCost() {
    return cost;
  }
}

public class StreamCollectorsExample {
  public static void main(String[] args) {
    List<Order> orders = List.of(
            new Order("Laptop", 1200.0),
            new Order("Smartphone", 800.0),
            new Order("Laptop", 1500.0),
            new Order("Tablet", 500.0),
            new Order("Smartphone", 900.0));

    //Создайте список заказов с разными продуктами и их стоимостями.
    AtomicInteger atomicInteger = new AtomicInteger(1);
    Map<Integer, Map<String, Double>> orderMap1 = orders.parallelStream()
            .collect(Collectors.toMap(num -> atomicInteger.getAndIncrement(),
                    order -> Map.of(order.getProduct(), order.getCost())));
    orderMap1.entrySet().forEach(System.out::println);
    System.out.println("________________________________________\n");

    //Группируйте заказы по продуктам.
    Map<String, Map<Order, Double>> orderMap2 = orders.parallelStream()
            .collect(Collectors.groupingBy(Order::getProduct, Collectors.toMap(order -> order, Order::getCost)));
    orderMap2.entrySet().forEach(System.out::println);
    System.out.println("________________________________________\n");

    //Для каждого продукта найдите общую стоимость всех заказов.
    Map<String, Double> orderMap3 = orders.parallelStream()
            .collect(Collectors.groupingBy(Order::getProduct, Collectors.summingDouble(Order::getCost)));
    orderMap3.entrySet().forEach(System.out::println);
    System.out.println("________________________________________\n");

    //Отсортируйте продукты по убыванию общей стоимости.
    Map<String, Double> orderMap4 = orderMap3.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, TreeMap::new));
    orderMap4.entrySet().forEach(System.out::println);
    System.out.println("________________________________________\n");

    //Выберите три самых дорогих продукта.
    Map<Order, Double> orderMap5 = orders.stream()
            .sorted(Comparator.comparingDouble(Order::getCost).reversed())
            .limit(3)
            .collect(Collectors.toMap(order -> order, Order::getCost));
    orderMap5.entrySet().forEach(order -> System.out.println(order.getKey().getProduct() + " " + order.getValue()));
    System.out.println("________________________________________\n");

    //Выведите результат: список трех самых дорогих продуктов и их общая стоимость.
    Map<Order, Double> orderMap6 = orders.stream()
            .sorted(Comparator.comparingDouble(Order::getCost))
            .limit(3)
            .collect(Collectors.toMap(order -> order, Order::getCost));
    orderMap6.entrySet().forEach(order -> System.out.println(order.getKey().getProduct() + " " + order.getValue()));
    System.out.println("Сумма: " + orderMap6.values().stream().reduce(0.0, Double::sum));
  }
}
