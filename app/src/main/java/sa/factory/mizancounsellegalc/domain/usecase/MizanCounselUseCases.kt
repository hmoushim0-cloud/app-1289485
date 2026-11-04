package sa.factory.mizancounsellegalc.domain.usecase

import sa.factory.mizancounsellegalc.domain.repository.MizanCounselRepository
import sa.factory.mizancounsellegalc.model.MizanCounselItem
import sa.factory.mizancounsellegalc.model.MizanCounselOrder
import sa.factory.mizancounsellegalc.model.MizanCounselTelemetry
import kotlinx.coroutines.flow.Flow

class GetCatalogUseCase(private val repository: MizanCounselRepository) {
    operator fun invoke(): Flow<List<MizanCounselItem>> = repository.catalogFlow
}

class ManageCartUseCase(private val repository: MizanCounselRepository) {
    val cart = repository.activeCart
    fun add(item: MizanCounselItem) = repository.addToCart(item)
    fun remove(item: MizanCounselItem) = repository.removeFromCart(item)
    fun clear() = repository.clearCart()
}

class ProcessCheckoutUseCase(private val repository: MizanCounselRepository) {
    suspend operator fun invoke(paymentMethod: String = "Mada Contactless EMV"): MizanCounselOrder =
        repository.checkout(paymentMethod)
}

class ObserveHardwareTelemetryUseCase(private val repository: MizanCounselRepository) {
    operator fun invoke(): Flow<MizanCounselTelemetry> = repository.telemetryFlow
}
