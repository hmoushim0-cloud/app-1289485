package sa.factory.mizancounsellegalc.data.remote

import sa.factory.mizancounsellegalc.model.MizanCounselItem
import sa.factory.mizancounsellegalc.model.MizanCounselOrder

interface MizanCounselApiService {
    suspend fun fetchCatalog(): List<MizanCounselItem>
    suspend fun submitOrder(order: MizanCounselOrder): Boolean
}
