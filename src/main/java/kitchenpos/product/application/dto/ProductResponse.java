package kitchenpos.product.application.dto;

import java.beans.ConstructorProperties;

import kitchenpos.price.domain.Price;
import kitchenpos.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties({"id", "name", "price"}))
@Getter
public class ProductResponse {
    private final Long id;
    private final String name;
    private final Price price;

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
