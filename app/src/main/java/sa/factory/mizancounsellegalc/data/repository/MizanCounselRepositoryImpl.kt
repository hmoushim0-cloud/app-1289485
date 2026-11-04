package sa.factory.mizancounsellegalc.data.repository

import sa.factory.mizancounsellegalc.domain.repository.MizanCounselRepository
import sa.factory.mizancounsellegalc.hardware.HardwareSensorDriver
import sa.factory.mizancounsellegalc.model.MizanCounselItem
import sa.factory.mizancounsellegalc.model.MizanCounselOrder
import sa.factory.mizancounsellegalc.model.MizanCounselTelemetry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MizanCounselRepositoryImpl(
    private val sensorDriver: HardwareSensorDriver
) : MizanCounselRepository {

    private val _catalog = MutableStateFlow<List<MizanCounselItem>>(listOf(
        MizanCounselItem("1", "الباقة التنفيذية المعتمدة", "تكامل مباشر مع FIPS 140-2 Cryptographic HSM Security Token", 180.0, "معتمد", "الخدمات الرئيسية"),
        MizanCounselItem("2", "خدمة المعالجة الفورية", "أداء أصيل بسرعة 60 إطار في الثانية", 95.0, "فوري", "المراقبة الحية"),
        MizanCounselItem("3", "باقة الصيانة والدعم السحابي", "تشفير كامل للبيانات المحلية ومزامنة آمنة", 250.0, "شامل", "التقارير الضريبية")
    ))
    override val catalogFlow: Flow<List<MizanCounselItem>> = _catalog.asStateFlow()

    private val _cart = MutableStateFlow<List<MizanCounselItem>>(emptyList())
    override val activeCart: StateFlow<List<MizanCounselItem>> = _cart.asStateFlow()

    override val telemetryFlow: Flow<MizanCounselTelemetry> = sensorDriver.observePeripheralStream()
        .map { frame ->
            MizanCounselTelemetry(
                peripheralName = frame.peripheralName,
                status = frame.connectionState,
                liveReading = frame.livePayload,
                batteryPct = 96,
                signalDbm = -54
            )
        }

    override fun addToCart(item: MizanCounselItem) {
        val current = _cart.value.toMutableList()
        current.add(item)
        _cart.value = current
    }

    override fun removeFromCart(item: MizanCounselItem) {
        val current = _cart.value.toMutableList()
        current.remove(item)
        _cart.value = current
    }

    override fun clearCart() {
        _cart.value = emptyList()
    }

    override suspend fun checkout(paymentMethod: String): MizanCounselOrder {
        val currentItems = _cart.value.toList()
        val subtotal = currentItems.sumOf { it.priceSar }
        val vat = subtotal * 0.15
        val total = subtotal + vat
        val order = MizanCounselOrder(
            orderId = "ORD-${System.currentTimeMillis() % 100000}",
            items = currentItems,
            subtotalSar = subtotal,
            vatSar = vat,
            totalSar = total,
            paymentMethod = paymentMethod
        )
        clearCart()
        return order
    }

    override suspend fun refreshCatalog() {
        // Offline-first sync logic
    }
}
