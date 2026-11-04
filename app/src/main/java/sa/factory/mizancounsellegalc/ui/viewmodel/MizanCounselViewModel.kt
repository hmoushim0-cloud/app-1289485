package sa.factory.mizancounsellegalc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sa.factory.mizancounsellegalc.domain.usecase.*
import sa.factory.mizancounsellegalc.model.MizanCounselItem
import sa.factory.mizancounsellegalc.model.MizanCounselOrder
import sa.factory.mizancounsellegalc.model.MizanCounselTelemetry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MizanCounselUiState(
    val catalog: List<MizanCounselItem> = emptyList(),
    val filteredCatalog: List<MizanCounselItem> = emptyList(),
    val selectedCategory: String = "الكل",
    val cart: List<MizanCounselItem> = emptyList(),
    val telemetry: MizanCounselTelemetry? = null,
    val lastOrder: MizanCounselOrder? = null,
    val isCheckingOut: Boolean = false,
    val activeTab: String = "home"
)

class MizanCounselViewModel(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val processCheckoutUseCase: ProcessCheckoutUseCase,
    private val observeHardwareTelemetryUseCase: ObserveHardwareTelemetryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MizanCounselUiState())
    val uiState: StateFlow<MizanCounselUiState> = _uiState.asStateFlow()

    init {
        // Observe Catalog
        getCatalogUseCase().onEach { list ->
            _uiState.update { it.copy(catalog = list, filteredCatalog = list) }
        }.launchIn(viewModelScope)

        // Observe Cart
        manageCartUseCase.cart.onEach { cartItems ->
            _uiState.update { it.copy(cart = cartItems) }
        }.launchIn(viewModelScope)

        // Observe Telemetry
        observeHardwareTelemetryUseCase().onEach { telem ->
            _uiState.update { it.copy(telemetry = telem) }
        }.launchIn(viewModelScope)
    }

    fun selectCategory(category: String) {
        _uiState.update { state ->
            val filtered = if (category == "الكل") state.catalog else state.catalog.filter { it.category == category }
            state.copy(selectedCategory = category, filteredCatalog = filtered)
        }
    }

    fun switchTab(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun addToCart(item: MizanCounselItem) = manageCartUseCase.add(item)
    fun removeFromCart(item: MizanCounselItem) = manageCartUseCase.remove(item)

    fun checkout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true) }
            val order = processCheckoutUseCase()
            _uiState.update { it.copy(isCheckingOut = false, lastOrder = order, activeTab = "checkout") }
        }
    }
}
