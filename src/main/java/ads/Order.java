package ads;

public class Order {
    public int orderId;
    public int orderValue;
    public int deliveryTime;
    public int currentSystemTime;
    public int driverStartTime;

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderValue=" + orderValue +
                ", deliveryTime=" + deliveryTime +
                ", priority= "+calculatePriority()+
                '}';
    }

    public Order(int orderId, int currentSystemTime, int orderValue, int deliveryTime) {
        this.orderId = orderId;
        this.orderValue = orderValue;
        this.currentSystemTime = currentSystemTime;
        this.deliveryTime =  deliveryTime;

    }

    public double calculatePriority(){
        double valueWeight = 0.3;
        double timeWeight = 0.7;
        double normalizedWeight = orderValue / 50.0;
        return valueWeight * normalizedWeight - timeWeight * currentSystemTime;
    }

}
