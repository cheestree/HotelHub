package org.cheese.hotelhubserver.repository.jdbi.hotel

import org.cheese.hotelhubserver.domain.hotel.Hotel
import org.cheese.hotelhubserver.http.model.hotel.HotelOutputModel
import org.cheese.hotelhubserver.repository.interfaces.hotel.HotelRepository
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo

class JdbiHotelRepository(
    private val handle: Handle,
) : HotelRepository {
    override fun createHotel(
        name: String,
        address: String,
        stars: Int,
        latitude: Double,
        longitude: Double,
        features: List<Int>?,
    ): Boolean {
        return handle.createUpdate(
            """
                insert into hotelhub.hotel (name, address, stars, latitude, longitude) values (:name, :address, :stars, :latitude, :longitude);
            """,
        )
            .bind("name", name)
            .bind("address", address)
            .bind("stars", stars)
            .bind("latitude", latitude)
            .bind("longitude", longitude)
            .execute() == 1
    }

    override fun getHotel(id: Int): Hotel {
        return handle.createQuery("""select * from hotelhub.hotel where id = :id""")
            .bind("id", id)
            .mapTo<Hotel>()
            .single()
    }

    override fun getHotels(
        stars: Int?,
        features: List<String>?,
    ): List<HotelOutputModel> {
        return handle.createQuery(
            """select * from hotelhub.hotel""",
        )
            .mapTo<HotelOutputModel>()
            .list()
    }

    override fun hotelExists(id: Int): Boolean {
        return handle.createQuery(
            """
                select * from hotelhub.hotel where id = :id
            """,
        )
            .bind("id", id)
            .mapTo<Hotel>()
            .singleOrNull() != null
    }

    override fun getFeatures(): List<String> {
        return handle.createQuery(
            """select feature from hotelhub.features""",
        )
            .mapTo<String>()
            .list()
    }
}
