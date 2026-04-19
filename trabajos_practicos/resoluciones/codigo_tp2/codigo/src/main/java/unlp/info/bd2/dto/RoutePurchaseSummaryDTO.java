package unlp.info.bd2.dto;

public class RoutePurchaseSummaryDTO {

    private final String routeName;
    private final long purchaseCount;
    private final double averagePurchasePrice;

    public RoutePurchaseSummaryDTO(String routeName, long purchaseCount, double averagePurchasePrice) {
        this.routeName = routeName;
        this.purchaseCount = purchaseCount;
        this.averagePurchasePrice = averagePurchasePrice;
    }

    public String getRouteName() {
        return routeName;
    }

    public long getPurchaseCount() {
        return purchaseCount;
    }

    public double getAveragePurchasePrice() {
        return averagePurchasePrice;
    }
}