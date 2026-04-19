package unlp.info.bd2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import unlp.info.bd2.repositories.*;
import unlp.info.bd2.services.*;

@Configuration
@ComponentScan("unlp.info.bd2")
public class AppConfig {

    @Bean
    @Primary
    public ToursService createService(
        PurchaseRepository purchaseRepository,
        ReviewRepository reviewRepository,
        RouteRepository routeRepository,
        ServiceRepository serviceRepository,
        SupplierRepository supplierRepository,
        UserRepository userRepository,
        StopRepository stopRepository
    ) {
        return new ToursServiceImpl(
            purchaseRepository,
            reviewRepository,
            routeRepository,
            serviceRepository,
            supplierRepository,
            userRepository,
            stopRepository
        );
    }
}
