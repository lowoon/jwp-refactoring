package kitchenpos.menu.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.price.domain.Price;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @DisplayName("정상적인 요청에 created 상태로 응답하는지 확인한다.")
    @Test
    void createTest() throws Exception {
        final String name = "메뉴";
        final MenuProduct menuProduct = createMenuProduct(null, 1L, 1L, 10);
        final MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 10L);
        final Menu savedMenu = createMenu(1L, name, 1000, 1L, Collections.singletonList(menuProduct));
        final MenuCreateRequest menuWithoutId = new MenuCreateRequest(name, new Price(BigDecimal.valueOf(1000)), 1L,
            Collections.singletonList(menuProductRequest));
        final MenuResponse menuResponse = MenuResponse.from(savedMenu);

        given(menuService.create(any(Menu.class))).willReturn(savedMenu);

        mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(menuWithoutId))
        )
            .andExpect(status().isCreated())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(menuResponse)))
            .andExpect(header().exists("Location"));
    }

    @DisplayName("정상적인 메뉴 리스트 요청에 ok상태로 응답하는지 확인한다.")
    @Test
    void listTest() throws Exception {
        final MenuProduct menuProduct = createMenuProduct(null, 1L, 1L, 10);
        final Menu first = createMenu(1L, "후라이드", 0, 1L, Collections.singletonList(menuProduct));
        final Menu second = createMenu(2L, "양념", 0, 1L, Collections.singletonList(menuProduct));
        final List<Menu> menus = Arrays.asList(first, second);
        final List<MenuResponse> menuResponses = menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());

        given(menuService.list()).willReturn(menus);

        mockMvc.perform(get("/api/menus"))
            .andExpect(status().isOk())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(menuResponses)));
    }
}
