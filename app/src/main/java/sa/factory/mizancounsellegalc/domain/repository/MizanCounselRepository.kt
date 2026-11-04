package sa.factory.mizancounsellegalc.domain.repository

import sa.factory.mizancounsellegalc.model.MizanCounselItem
import sa.factory.mizancounsellegalc.model.MizanCounselOrder
import sa.factory.mizancounsellegalc.model.MizanCounselTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MizanCounselRepository {
    val catalogFlow: Flow<List<MizanCounselItem>>
    val activeCart: StateFlow<List<MizanCounselItem>>
    val telemetryFlow: Flow<MizanCounselTelemetry>
    
    fun addToCart(item: MizanCounselItem)
    fun removeFromCart(item: MizanCounselItem)
    fun clearCart()
    suspend fun checkout(paymentMethod: String): MizanCounselOrder
    suspend fun refreshCatalog()
}
