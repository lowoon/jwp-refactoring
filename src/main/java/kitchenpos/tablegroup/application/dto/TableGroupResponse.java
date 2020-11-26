package kitchenpos.tablegroup.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TableGroupResponse {
    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    public static TableGroupResponse from(TableGroup tableGroup) {
        List<OrderTableResponse> orderTableResponses = tableGroup.getOrderTables()
            .getOrderTables()
            .stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponses);
    }
}
